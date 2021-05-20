package hr.fer.tel.pipp.admin.services;

import eu.h2020.symbiote.security.commons.enums.ManagementStatus;
import eu.h2020.symbiote.security.commons.exceptions.custom.AAMException;
import eu.h2020.symbiote.security.commons.exceptions.custom.SecurityHandlerException;
import hr.fer.tel.pipp.admin.dto.UserRequestDTO;
import hr.fer.tel.pipp.admin.model.*;
import hr.fer.tel.pipp.admin.repositories.ResourceRepository;
import hr.fer.tel.pipp.admin.repositories.RoomRepository;
import hr.fer.tel.pipp.admin.repositories.UserRepository;
import hr.fer.tel.pipp.admin.repositories.UserRequestRepository;
import hr.fer.tel.pipp.admin.security.WebSecurityConfigurer;
import io.jsonwebtoken.lang.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class UserRequestService {

    @Autowired
    private UserRequestRepository userRequestRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private SymbIoTeService symbIoTeService;

    @Autowired
    private WebSecurityConfigurer webSecurityConfigurer;

    public Function<UserRequestDTO, UserRequest> mapper = urDto -> {
        UserRequest ur = new UserRequest();

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

    public void saveUserRequest(UserRequestDTO userRequestDTO) {
        filterOldRequests();
        UserRequest userRequest = mapper.apply(userRequestDTO);
        validate(userRequest);
        userRequestRepository.save(userRequest);
    }

    public List<UserRequestDTO> getAll() {
        filterOldRequests();
        return userRequestRepository.findAll().stream().map(ur -> new UserRequestDTO(ur)).collect(Collectors.toList());
    }

    public void approveRegistration(String email, boolean approve) throws SecurityHandlerException, AAMException {
        UserRequest userRequest = userRequestRepository.getOne(email);
        List<Room> rooms = userRequest.getRooms();
        userRequest.getRooms().forEach(r -> System.out.println(r.getRoomName()));
        // sigurno brisemo request, a ako je approve-an ga dodamo ko user-a
        if (approve) {
            User user = new User(userRequest);
            String passwordHash = webSecurityConfigurer.getPasswordEncoder().encode(userRequest.getPassword());
            user.setPassword(passwordHash);
            ManagementStatus status = symbIoTeService.registerUser(userRequest);
            if (status == ManagementStatus.ERROR) throw new AAMException("nešto je pošlo po zlu");
            if (status == ManagementStatus.USERNAME_EXISTS) throw new AAMException("korisnik već postoji");
            userRepository.save(user);
        }
        userRequestRepository.delete(userRequest);
    }

    private void filterOldRequests() {
        List<UserRequest> requests = userRequestRepository.findAll();
        LocalDateTime yesterday = LocalDateTime.now().minusHours(24);

        List<UserRequest> toDelete = requests.stream().filter(ur -> ur.getRequestTime().isBefore(yesterday)).collect(Collectors.toList());
        userRequestRepository.deleteAll(toDelete);
    }

    private void validate(UserRequest userRequest) {
        Assert.notNull(userRequest.getEmail(), "email ne smije biti null");
        Assert.notNull(userRequest.getName(), "ime ne smije biti null");
        Assert.notNull(userRequest.getSurname(), "prezime ne smije biti null");
        Assert.notNull(userRequest.getPassword(), "lozinka ne smije biti null");
        Assert.notNull(userRequest.getRole(), "uloga ne smije biti null");

        String email = userRequest.getEmail();
        boolean exists = userRepository.existsById(email) || userRequestRepository.existsById(email);
        if (exists) throw new IllegalArgumentException("korisnik s email-om '" + email + "' već postoji");
    }
}
