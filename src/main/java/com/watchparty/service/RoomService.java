package com.watchparty.service;

import com.watchparty.model.*;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RoomService {

	private Map<String, Room> rooms = new ConcurrentHashMap<>();

	public Room createOrJoinRoom(String roomId, String userId, String username) {

	    Room room = rooms.get(roomId);

	    if (room == null) {
	        room = new Room();
	        room.setRoomId(roomId);
	        
	        room.setVideoId("8hly31xKli0");
	        room.setParticipants(new ConcurrentHashMap<>());
	        room.setPlaying(false);
	        room.setCurrentTime(0);
	        rooms.put(roomId, room);
	    }

	    Role role;

	    if (room.getParticipants().size() == 0) {
	        role = Role.HOST;
	    } else {
	        role = Role.PARTICIPANT;
	    }

	    Participant participant = new Participant(userId, username, role);

	    room.getParticipants().put(userId, participant);

	    return room;
	}

    public Room getRoom(String roomId) {
        return rooms.get(roomId);
    }

    public void removeUser(String roomId, String userId) {
        Room room = rooms.get(roomId);
        if (room != null) {
            room.getParticipants().remove(userId);
        }
    }
}