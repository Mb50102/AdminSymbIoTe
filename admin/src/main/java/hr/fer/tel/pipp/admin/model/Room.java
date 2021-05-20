package hr.fer.tel.pipp.admin.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import java.util.List;

@Entity
@Table(name = "rooms")
public class Room {

    @Id
    private String roomName;

    @OneToMany(orphanRemoval = true, mappedBy = "room")
    private List<Resource> resources;

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public List<Resource> getResources() {
        return resources;
    }

    public void setResources(List<Resource> resources) {
        this.resources = resources;
    }

    public void addResource(Resource resource){this.resources.add(resource);}

    public void deleteResource(Resource resource) { this.resources.remove(resource); }

}
