package com.kalle.spotifyparty.transcripts;

public class ErrorBody {

    private ApiError error;

    public ApiError getError() {
        return error;
    }

    public void setError(ApiError error) {
        this.error = error;
    }
}
