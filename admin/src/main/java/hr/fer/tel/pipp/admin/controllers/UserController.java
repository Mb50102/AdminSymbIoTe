package hr.fer.tel.pipp.admin.controllers;


import eu.h2020.symbiote.security.commons.exceptions.custom.AAMException;
import eu.h2020.symbiote.security.commons.exceptions.custom.SecurityHandlerException;
import eu.h2020.symbiote.security.commons.exceptions.custom.ValidationException;
import hr.fer.tel.pipp.admin.dto.UserRequestDTO;
import hr.fer.tel.pipp.admin.security.AuthenticationRequest;
import hr.fer.tel.pipp.admin.security.AuthenticationResponse;
import hr.fer.tel.pipp.admin.security.JwtUtil;
import hr.fer.tel.pipp.admin.security.MyUserDetailsService;
import hr.fer.tel.pipp.admin.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws AuthenticationException, SecurityHandlerException, ValidationException {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword()));

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getEmail());

        final String jwt = jwtUtil.generateToken(userDetails);

        @SuppressWarnings("unchecked")
        String role = ((List<GrantedAuthority>) userDetails.getAuthorities()).get(0).toString();
        return ResponseEntity.ok(new AuthenticationResponse(jwt, role));
    }


    @DeleteMapping("")
    @Secured({"ROLE_ADMIN","ROLE_USER"})
    public ResponseEntity<String> deleteUser(@PathParam("email") String email) throws SecurityHandlerException, AAMException {
        userService.deleteUser(email);
        return ResponseEntity.ok("Korisnik '" + email + "' je izbrisan.");
    }
    @PutMapping("")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<String> updateUserAdmin(@RequestBody UserRequestDTO user) throws SecurityHandlerException, AAMException{
        userService.updateUser(user, true);
        return ResponseEntity.ok("korisnik je ažuriran");
    }

    @GetMapping("/users")
    @Secured("ROLE_ADMIN")
    public List<UserRequestDTO> getAll() {
        return userService.getAll();
    }

    @PutMapping("/update")
    @Secured("ROLE_USER")
    public ResponseEntity<String> updateUser(@RequestBody UserRequestDTO user) throws SecurityHandlerException, AAMException{
        userService.updateUser(user, false);
        return ResponseEntity.ok("korisnik je ažuriran");
    }

    @PutMapping("/grant_admin")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<String> grantAdmin(@PathParam("email") String email, Principal p) {
        String grantedEmail = userService.setAdmin(p.getName(), email, true);
        return ResponseEntity.ok("Korisniku s email-om '" + grantedEmail + "' je dodijeljena uloga administratora");
    }

    @PutMapping("/revoke_admin")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<String> revokeAdmin(@PathParam("email") String email, Principal p) {
        String revokedEmail = userService.setAdmin(p.getName(), email, false);
        return ResponseEntity.ok("Korisniku s email-om '" + revokedEmail + "' je oduzeta uloga administratora");
    }
}
