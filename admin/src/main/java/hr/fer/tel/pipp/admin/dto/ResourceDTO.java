package hr.fer.tel.pipp.admin.dto;

import hr.fer.tel.pipp.admin.model.Resource;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

public class ResourceDTO {

    private String resourceName;
    private String internalId;
    private String routingKey;
    private String roomName;

    public ResourceDTO(){ }

    public ResourceDTO(Resource resource){
        this.internalId = resource.getInternalId();
        this.resourceName = resource.getResourceName();
        this.routingKey = resource.getRoutingKey();

        this.roomName = resource.getRoom().getRoomName();
    }
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

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

}
