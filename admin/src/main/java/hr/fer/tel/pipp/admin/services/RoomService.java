package hr.fer.tel.pipp.admin.services;

import hr.fer.tel.pipp.admin.dto.RoomDTO;
import hr.fer.tel.pipp.admin.model.Resource;
import hr.fer.tel.pipp.admin.model.Room;
import hr.fer.tel.pipp.admin.model.User;
import hr.fer.tel.pipp.admin.repositories.ResourceRepository;

import hr.fer.tel.pipp.admin.repositories.RoomRepository;
import hr.fer.tel.pipp.admin.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoomService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoomRepository roomRepository;

    public void deleteRoom(String roomName) throws IllegalArgumentException {
        Boolean exist=roomRepository.existsById(roomName);

        if(!exist) throw new IllegalArgumentException("ne postoji soba s imenom: "+roomName);

        Room room = roomRepository.getOne(roomName);
        for(User user: userRepository.findAll()){
            if(user.getRooms().contains(room)) {
                user.getRooms().remove(room);
                userRepository.save(user);
            }
        }


        roomRepository.delete(room);
    }

    public List<String> getAllRooms(){
        return roomRepository.findAll().stream().map(r -> r.getRoomName()).collect(Collectors.toList());

    }


    public List<String> getRoomResources(String roomName) throws  IllegalArgumentException{
        Boolean exists=roomRepository.existsById(roomName);
        if(!exists) throw new IllegalArgumentException("Ne postoji ime sobe: "+ roomName);
        return roomRepository.getOne(roomName).getResources().stream().map(r->r.getResourceName()).collect(Collectors.toList());

    }

    public void addRoom(String roomName) throws IllegalArgumentException{
        Boolean exists=roomRepository.existsById(roomName);
        if(exists) throw new IllegalArgumentException("Vec postoji ime sobe: "+ roomName);
        Room newRoom= new Room();
        newRoom.setRoomName(roomName);
        roomRepository.save(newRoom);

    }

    public List<RoomDTO> getAllRoomsResources() {
        return roomRepository.findAll().stream().map(r->new RoomDTO(r)).collect(Collectors.toList());
    }

}
