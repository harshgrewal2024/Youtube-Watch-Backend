package com.watchparty.model;

public class PlayRequest {

	private String userId;
    private double time;

    public String getUserId() { return userId; }
    public double getTime() { return time; }

    public void setUserId(String userId) { this.userId = userId; }
    public void setTime(double time) { this.time = time; }
}
