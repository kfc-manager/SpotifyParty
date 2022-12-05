package com.kalle.spotifyparty.spotifyapi;

public class ApiException extends Exception {

    private int status;

    public ApiException(String message) {
        super(message);
    }

    public ApiException(int status, String message) {
        super(message);
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

}
