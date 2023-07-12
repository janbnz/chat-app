package de.janbnz.chat.chat;

import de.janbnz.chat.ChatServer;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class ChatRegistry {

    private final ChatServer server;

    public ChatRegistry(ChatServer server) {
        this.server = server;
    }

    public String createChat(String chatName, String... chatMembers) {
        final String chatId = UUID.randomUUID().toString();
        this.server.getDatabase().executeUpdate("INSERT INTO chats(chat_id, chat_name) VALUES ('" + chatId + "', '" + chatName + "');").join();
        Arrays.stream(chatMembers).forEach(member -> this.addMember(chatId, member));
        return chatId;
    }

    public void addMember(String chatId, String userId) {
        this.server.getDatabase().executeUpdate("INSERT INTO chat_members(chat_id, user_id) VALUES ('" + chatId + "', '" + userId + ");").join();
    }

    public void removeMember(String chatId, String userId) {
        this.server.getDatabase().executeUpdate("DELETE FROM chat_members WHERE chat_id='" + chatId + "' AND user_id='" + userId + "'").join();
    }

    public CompletableFuture<Boolean> isMember(String chatId, String userId) {
        return this.server.getDatabase().executeQuery("SELECT COUNT(*) FROM chat_members WHERE chat_id = ? AND user_id = ?", chatId, userId).thenApplyAsync(set -> {
            try (set) {
                return set.getInt(1) > 0;
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;
        });
    }

    public CompletableFuture<List<Chat>> getChats(String userId) {
        return this.server.getDatabase().executeQuery("SELECT chat_id FROM chat_members WHERE user_id = ?", userId).thenApplyAsync(set -> {
            try (set) {
                List<Chat> chats = new ArrayList<>();
                while (set.next()) {
                    this.getChatById(set.getString(1)).thenAccept(chats::add);
                }
                return chats;
            } catch (SQLException ex) {
                ex.printStackTrace();
                return new ArrayList<>();
            }
        });
    }

    public CompletableFuture<List<String>> getMembers(String chatId) {
        return this.server.getDatabase().executeQuery("SELECT * FROM chat_members WHERE chat_id = ?", chatId).thenApplyAsync(set -> {
            try (set) {
                List<String> members = new ArrayList<>();
                while (set.next()) {
                    members.add(set.getString("user_id"));
                }
                return members;
            } catch (SQLException ex) {
                ex.printStackTrace();
                return new ArrayList<>();
            }
        });
    }

    public CompletableFuture<List<JSONObject>> getMessages(String chatId) {
        return this.server.getDatabase().executeQuery("SELECT * FROM chat_messages WHERE chat_id = ?", chatId).thenApplyAsync(set -> {
            try (set) {
                List<JSONObject> messages = new ArrayList<>();
                while (set.next()) {
                    messages.add(new JSONObject()
                            .put("message_id", set.getString("message_id")).put("chat_id", chatId)
                            .put("message", set.getString("message")).put("user_id", set.getString("user_id"))
                            .put("sent_at", set.getLong("sent_at")));
                }
                return messages;
            } catch (SQLException ex) {
                ex.printStackTrace();
                return new ArrayList<>();
            }
        });
    }

    public CompletableFuture<Chat> getChatById(String chatId) {
        return this.server.getDatabase().executeQuery("SELECT * FROM chats WHERE chat_id = ?", chatId).thenComposeAsync(resultSet -> {
            try {
                if (resultSet == null || !resultSet.next()) {
                    return CompletableFuture.completedFuture(null);
                }

                return getMembers(chatId).thenApplyAsync(members -> {
                    try {
                        return new Chat(chatId, resultSet.getString("chat_name"), members);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        return null;
                    }
                });
            } catch (SQLException ex) {
                ex.printStackTrace();
                return CompletableFuture.completedFuture(null);
            }
        });
    }

    public void storeMessage(String chatId, String message, String userId, long sentAt) {
        final String messageId = UUID.randomUUID().toString();
        this.server.getDatabase().executeUpdate("INSERT INTO chat_messages(message_id, chat_id, message, user_id, sent_at) VALUES ('" + messageId + "', '" + chatId + "', '" + message + "', '" + userId + "', '" + sentAt + "');").join();
    }
}