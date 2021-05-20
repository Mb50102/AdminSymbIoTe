package hr.fer.tel.pipp.admin.dto;

import hr.fer.tel.pipp.admin.model.Room;

import java.util.List;
import java.util.stream.Collectors;

public class RoomDTO {

    private String roomName;
    private List<String> resource;

    public RoomDTO(){

    }

    public RoomDTO(Room room) {
        this.roomName = room.getRoomName();
        this.resource = room.getResources().stream().map(r->r.getResourceName()).collect(Collectors.toList());
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public List<String> getResource() {
        return resource;
    }

    public void setResource(List<String> resource) {
        this.resource = resource;
    }
}
