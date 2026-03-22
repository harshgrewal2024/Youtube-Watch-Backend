package com.watchparty.model;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Room {

    private String roomId;
    private Map<String, Participant> participants = new ConcurrentHashMap<>();

    private String videoId = "dQw4w9WgXcQ";
    private double currentTime = 0;
    private boolean isPlaying = false;

    // Getters & Setters
    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public Map<String, Participant> getParticipants() {
        return participants;
    }

    public void setParticipants(Map<String, Participant> participants) {
        this.participants = participants;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public double getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(double currentTime) {
        this.currentTime = currentTime;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }
}