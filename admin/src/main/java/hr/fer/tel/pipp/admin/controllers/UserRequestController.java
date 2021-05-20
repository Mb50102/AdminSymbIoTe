package hr.fer.tel.pipp.admin.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import eu.h2020.symbiote.security.commons.Token;
import eu.h2020.symbiote.security.commons.exceptions.custom.AAMException;
import eu.h2020.symbiote.security.commons.exceptions.custom.SecurityHandlerException;
import eu.h2020.symbiote.security.commons.exceptions.custom.ValidationException;
import hr.fer.tel.pipp.admin.dto.UserRequestDTO;
import hr.fer.tel.pipp.admin.model.User;
import hr.fer.tel.pipp.admin.model.UserRequest;
import hr.fer.tel.pipp.admin.services.SymbIoTeService;
import hr.fer.tel.pipp.admin.services.UserRequestService;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/register")
public class UserRequestController {

    @Autowired
    private UserRequestService userRequestService;

    @Autowired
    private SymbIoTeService symbIoTeService;

    @PostMapping("")
    public ResponseEntity<String> createUserRequest(@RequestBody UserRequestDTO userRequestDTO) {
        userRequestService.saveUserRequest(userRequestDTO);
        return ResponseEntity.ok("Vaš zahtjev je spremljen");
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/requests")
    public List<UserRequestDTO> getAllRequests() {
        return userRequestService.getAll();
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/requests")
    public ResponseEntity<String> approve(@PathParam("email") String email) throws SecurityHandlerException, AAMException {
        userRequestService.approveRegistration(email, true);
        return ResponseEntity.ok("Korisnik je registriran");
    }

    @Secured("ROLE_ADMIN")
    @DeleteMapping("/requests")
    public ResponseEntity<String> reject(@PathParam("email") String email) throws SecurityHandlerException, AAMException {
        userRequestService.approveRegistration(email, false);
        return ResponseEntity.ok("Korisnik je odbijen.");
    }

    @GetMapping("/Login")
    public void getToken() throws SecurityHandlerException, ValidationException, NoSuchAlgorithmException, JsonProcessingException {
        symbIoTeService.HomeToken();
    }
}
