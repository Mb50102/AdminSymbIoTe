package hr.fer.tel.pipp.admin.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "resources")
public class Resource {

    @Id
    private String resourceName;

    @Column(unique = true)
    @NotNull
    private String internalId;

    @Column(unique = true)
    private String routingKey;

    @ManyToOne()
    private Room room;

    public Resource(){ }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resource) {
        this.resourceName = resource;
    }

    public String getInternalId() {
        return internalId;
    }

    public void setInternalId(String internalId) {
        this.internalId = internalId;
    }

    public String getRoutingKey() {
        return routingKey;
    }

    public void setRoutingKey(String routingKey) {
        this.routingKey = routingKey;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }
}