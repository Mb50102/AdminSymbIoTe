package hr.fer.tel.pipp.admin.dto;

import hr.fer.tel.pipp.admin.model.User;
import hr.fer.tel.pipp.admin.model.UserRequest;

import java.util.List;
import java.util.stream.Collectors;

public class UserRequestDTO {

    private String email;
    private String username;
    private String name;
    private String surname;
    private String comment;
    private String password;
    private List<String> rooms;

    public UserRequestDTO() {
    }

    /**
     * Postavlja u sebe sve članske varijable primljenog <code>userRequest</code>-a osim <code>password</code>-a
     * @param userRequest
     */
    public UserRequestDTO(UserRequest userRequest) {
        this.email = userRequest.getEmail();
        this.username = userRequest.getUsername();
        this.name = userRequest.getName();
        this.surname = userRequest.getSurname();
        this.comment = userRequest.getComment();
        this.rooms = userRequest.getRooms().stream()
                .map(r -> r.getRoomName()).collect(Collectors.toList());
    }

    /**
     * Postavlja u sebe sve članske varijable primljenog <code>user</code>-a osim <code>password</code>-a
     * @param user
     */
    public UserRequestDTO(User user) {
        this.email = user.getEmail();
        this.username = user.getUsername();
        this.name = user.getName();
        this.surname = user.getSurname();
        this.comment = user.getComment();
        this.rooms = user.getRooms().stream()
                .map(r -> r.getRoomName()).collect(Collectors.toList());
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getRooms() {
        return rooms;
    }

    public void setRooms(List<String> rooms) {
        this.rooms = rooms;
    }
}
