package de.janbnz.chat.rest;

import io.javalin.http.HttpStatus;

public class HttpResponse {

    private final HttpStatus status;
    private final String response;

    public HttpResponse(HttpStatus status, String response) {
        this.status = status;
        this.response = response;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getResponse() {
        return response;
    }
}