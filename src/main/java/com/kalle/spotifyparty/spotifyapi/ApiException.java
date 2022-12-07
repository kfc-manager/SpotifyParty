package com.kalle.spotifyparty.spotifyapi;

import com.kalle.spotifyparty.transcripts.ApiError;
import com.kalle.spotifyparty.transcripts.ErrorBody;

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

    public ErrorBody getErrorBody() {
        ErrorBody errorBody = new ErrorBody();
        ApiError error = new ApiError();
        error.setMessage(getMessage());
        if (getStatus() == 0) {
            error.setStatus(500);
        } else {
            error.setStatus(getStatus());
        }
        errorBody.setError(error);
        return errorBody;
    }

}
