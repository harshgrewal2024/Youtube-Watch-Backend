package com.watchparty.controller;

import com.watchparty.model.*;
import com.watchparty.service.RoomService;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class RoomWebSocketController {

    private final RoomService roomService;
    private final SimpMessagingTemplate messagingTemplate;

    public RoomWebSocketController(RoomService roomService,
                                   SimpMessagingTemplate messagingTemplate) {
        this.roomService = roomService;
        this.messagingTemplate = messagingTemplate;
    }

    // COMMON VALIDATION
    private boolean canControl(Room room, String userId) {
        if (room == null) return false;

        Participant user = room.getParticipants().get(userId);

        return user != null &&
                (user.getRole() == Role.HOST || user.getRole() == Role.MODERATOR);
    }

    // ============================
    // JOIN ROOM
    // ============================
    @MessageMapping("/join")
    public void joinRoom(@Payload JoinRequest request) {

        Room room = roomService.createOrJoinRoom(
                request.getRoomId(),
                request.getUserId(),
                request.getUsername()
        );

        messagingTemplate.convertAndSend(
                "/topic/room/" + request.getRoomId(),
                room
        );
    }

    // ============================
    // PLAY
    // ============================
    @MessageMapping("/play/{roomId}")
    public void play(
            @DestinationVariable String roomId,
            @Header("userId") String userId,
            @Payload PlayRequest request
    ) {
        Room room = roomService.getRoom(roomId);

        if (!canControl(room, userId)) return;

        room.setPlaying(true);
        room.setCurrentTime(request.getTime());

        messagingTemplate.convertAndSend("/topic/room/" + roomId, room);
    }

    // ============================
    // PAUSE
    // ============================
    @MessageMapping("/pause/{roomId}")
    public void pause(
            @DestinationVariable String roomId,
            @Header("userId") String userId,
            @Payload PlayRequest request
    ) {
        Room room = roomService.getRoom(roomId);

        if (!canControl(room, userId)) return;

        room.setPlaying(false);
        room.setCurrentTime(request.getTime());

        messagingTemplate.convertAndSend("/topic/room/" + roomId, room);
    }

    // ============================
    // SEEK 
    // ============================
    @MessageMapping("/seek/{roomId}")
    public void seek(
            @DestinationVariable String roomId,
            @Header("userId") String userId,
            @Payload PlayRequest request
    ) {
        Room room = roomService.getRoom(roomId);

        if (!canControl(room, userId)) return;

        room.setCurrentTime(request.getTime());

        messagingTemplate.convertAndSend("/topic/room/" + roomId, room);
    }

    // ============================
    // CHANGE VIDEO
    // ============================
    @MessageMapping("/changeVideo/{roomId}")
    public void changeVideo(
            @DestinationVariable String roomId,
            @Header("userId") String userId,
            @Payload VideoChangeRequest request
    ) {
        Room room = roomService.getRoom(roomId);

        if (!canControl(room, userId)) return;

        room.setVideoId(request.getVideoId().trim());
        room.setCurrentTime(0);
        room.setPlaying(false);

        messagingTemplate.convertAndSend("/topic/room/" + roomId, room);
    }

    // ============================
    // ASSIGN ROLE (HOST ONLY)
    // ============================
    @MessageMapping("/assignRole/{roomId}")
    public void assignRole(
            @DestinationVariable String roomId,
            @Header("userId") String userId,
            @Payload AssignRoleRequest request
    ) {
        Room room = roomService.getRoom(roomId);

        Participant sender = room.getParticipants().get(userId);

        if (sender == null || sender.getRole() != Role.HOST) return;

        Participant target = room.getParticipants().get(request.getUserId());

        if (target != null) {
            target.setRole(request.getRole());
        }

        messagingTemplate.convertAndSend("/topic/room/" + roomId, room);
    }

    // ============================
    // REMOVE USER (HOST ONLY)
    // ============================
    @MessageMapping("/kick/{roomId}")
    public void removeUser(
            @DestinationVariable String roomId,
            @Header("userId") String hostId,
            @Payload RemoveRequest request
    ) {
        Room room = roomService.getRoom(roomId);

        if (room == null) return;

        Participant host = room.getParticipants().get(hostId);

        if (host == null ||
           (host.getRole() != Role.HOST && host.getRole() != Role.MODERATOR)) return;

        Participant target = room.getParticipants().get(request.getUserId());

        if (target == null || target.getRole() == Role.HOST) return;

        room.getParticipants().remove(request.getUserId());

        messagingTemplate.convertAndSend("/topic/room/" + roomId, room);
    }

    // ============================
    // LEAVE
    // ============================
    @MessageMapping("/leave")
    public void leaveRoom(
            @Header("userId") String userId,
            @Payload LeaveRequest request
    ) {
        roomService.removeUser(request.getRoomId(), userId);

        messagingTemplate.convertAndSend(
                "/topic/room/" + request.getRoomId(),
                roomService.getRoom(request.getRoomId())
        );
    }
}