package hr.fer.tel.pipp.admin.controllers;

import eu.h2020.symbiote.security.commons.exceptions.custom.AAMException;
import eu.h2020.symbiote.security.commons.exceptions.custom.SecurityHandlerException;
import hr.fer.tel.pipp.admin.dto.RoomDTO;
import hr.fer.tel.pipp.admin.services.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@RequestMapping("/room")
public class RoomController {

    @Autowired
    private RoomService roomService;

    @DeleteMapping("/deleteRoom")
    @Secured("ROLE_ADMIN")
    //paziti s ovim, ako se izbriše soba izbrišu se i svi njezini resursi
    public ResponseEntity<String> deleteRoom(@PathParam("roomName") String roomName) throws IllegalArgumentException {
        roomService.deleteRoom(roomName);
        return ResponseEntity.ok("soba"+ roomName +"je izbrisana");
    }

    @GetMapping("/rooms")
    public List<String> getAll() {return roomService.getAllRooms();}

    @GetMapping("/resources")
    @Secured({"ROLE_ADMIN","ROLE_USER"})
    public List<String> getResources(@PathParam("roomName") String roomName) throws IllegalArgumentException{
         return roomService.getRoomResources(roomName);
    }

    @PostMapping("/newRoom")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<String> addRoom(@PathParam("roomName") String roomName) throws IllegalArgumentException {
        roomService.addRoom(roomName);
        return ResponseEntity.ok("dodana je soba:"+ roomName);
    }

    @GetMapping("/allResources")
    @Secured({"ROLE_ADMIN","ROLE_USER"})
    public List<RoomDTO> getAllResources() {return roomService.getAllRoomsResources();}
}
