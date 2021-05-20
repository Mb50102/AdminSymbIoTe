package hr.fer.tel.pipp.admin.services;

import eu.h2020.symbiote.security.commons.enums.ManagementStatus;
import eu.h2020.symbiote.security.commons.exceptions.custom.AAMException;
import eu.h2020.symbiote.security.commons.exceptions.custom.SecurityHandlerException;
import hr.fer.tel.pipp.admin.model.Role;
import hr.fer.tel.pipp.admin.model.Room;
import hr.fer.tel.pipp.admin.model.User;
import hr.fer.tel.pipp.admin.model.UserRequest;
import hr.fer.tel.pipp.admin.repositories.RoomRepository;
import hr.fer.tel.pipp.admin.repositories.UserRepository;
import hr.fer.tel.pipp.admin.security.WebSecurityConfigurer;
import hr.fer.tel.pipp.admin.dto.UserRequestDTO;
import io.jsonwebtoken.lang.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private SymbIoTeService symbIoTeService;


    @Autowired
    private WebSecurityConfigurer webSecurityConfigurer;

    public Function<UserRequestDTO, User> mapper = urDto -> {
        User ur = new User();

        ur.setRole(Role.ROLE_USER);
        ur.setEmail(urDto.getEmail());
        ur.setUsername(urDto.getUsername());
        ur.setName(urDto.getName());
        ur.setSurname(urDto.getSurname());
        ur.setComment(urDto.getComment());
        ur.setPassword(urDto.getPassword());
        ur.setRooms(urDto.getRooms().stream()
                .map(roomName -> roomRepository.getOne(roomName)).collect(Collectors.toList()));

        return ur;
    };

    public void saveUser(UserRequestDTO userDTO) {
        String passwordHash = webSecurityConfigurer.getPasswordEncoder().encode(userDTO.getPassword());
        userDTO.setPassword(passwordHash);
        User user=mapper.apply(userDTO);
        userRepository.save(user);
    }

    public List<UserRequestDTO> getAll() {
        return userRepository.findAll().stream().map(ur -> new UserRequestDTO(ur)).collect(Collectors.toList());
    }

    public void deleteUser(String email) throws SecurityHandlerException, AAMException {
        boolean exists = userRepository.existsById(email);
        if (!exists) throw new IllegalArgumentException("korisnik s email-om ne postoji:" + email);

        User user = userRepository.getOne(email);

        ManagementStatus status = symbIoTeService.deleteUser(user);
        if (status != ManagementStatus.OK) throw new AAMException(status.name());
        userRepository.delete(user);
    }

    public void updateUser(UserRequestDTO userDTO, boolean safe) throws SecurityHandlerException, AAMException {
        validate(userDTO);


        User user=mapper.apply(userDTO);
        String email=user.getEmail();
        User oldUser = getUserByEmail(email);
        if(!safe){
            user.setComment(oldUser.getComment());
            user.setRooms(oldUser.getRooms());
        }

        ManagementStatus status = symbIoTeService.updateUser(oldUser, user);
        if (status != ManagementStatus.OK) throw new AAMException(status.name());
        else {
                userRepository.deleteById(email);
                saveUser(userDTO);
        }
    }


    public User getUserByEmail(String email) { return userRepository.getOne(email); }

    public String setAdmin(String adminEmail, String email, boolean toAdmin) {

        if (!toAdmin && adminEmail.equals(email)) {
            throw new IllegalArgumentException("You cannot revoke the administrator role from Yourself");
        }

        User user = userRepository.getOne(email);

        if (toAdmin)
            user.setRole(Role.ROLE_ADMIN);
        else
            user.setRole(Role.ROLE_USER);

        userRepository.save(user);
        return user.getEmail();
    }

    private void validate(UserRequestDTO userRequest) {
        Assert.notNull(userRequest.getEmail(), "email ne smije biti null");
        Assert.notNull(userRequest.getUsername(), "username ne smije biti null");
        Assert.notNull(userRequest.getName(), "ime ne smije biti null");
        Assert.notNull(userRequest.getSurname(), "prezime ne smije biti null");
        Assert.notNull(userRequest.getPassword(), "lozinka ne smije biti null");

        List<String> rooms=userRequest.getRooms();
        if(rooms!=null){
            for(String roomName:rooms){
                Boolean exists=roomRepository.existsById(roomName);
                if(!exists) throw new IllegalArgumentException("ne postoji soba: "+roomName);
            }

        }




        String email = userRequest.getEmail();
        boolean exists = userRepository.existsById(email);
        if (!exists) throw new IllegalArgumentException("korisnik s email-om ne postoji:" + email);

    }

}
