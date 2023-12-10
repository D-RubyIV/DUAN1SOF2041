package com.raven.model;

public class ModelMessage1 {

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ModelMessage1(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public ModelMessage1() {
    }

    private boolean success;
    private String message;

    @Override
    public String toString() {
        return "ModelMessage{" + "success=" + success + ", message=" + message + '}';
    }
}
