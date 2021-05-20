package hr.fer.tel.pipp.admin.model;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    private String email;

    @Column(unique = true)
    private String username;

    @Column
    @NotNull
    private String name;

    @Column
    @NotNull
    private String surname;

    /** Ovo je hashirana (kriptirana) lozinka, nije plain text */
    @Column
    @NotNull
    private String password;

    @Column(length = 255)
    private String comment;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Role role;

    @ManyToMany
    private List<Room> rooms;

    public User() { }

    public User(UserRequest userRequest) {
        this.email = userRequest.getEmail();
        this.username = userRequest.getUsername();
        this.name = userRequest.getName();
        this.surname = userRequest.getSurname();
        this.comment = userRequest.getComment();
        this.role = userRequest.getRole();
        this.rooms = userRequest.getRooms();
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

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getSurname() { return surname; }

    public void setSurname(String surname) { this.surname = surname; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    public String getComment() { return comment; }

    public void setComment(String comment) { this.comment = comment; }

    public Role getRole() { return role; }

    public void setRole(Role role) { this.role = role; }

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }
}
