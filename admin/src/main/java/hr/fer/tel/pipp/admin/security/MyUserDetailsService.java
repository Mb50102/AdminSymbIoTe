package hr.fer.tel.pipp.admin.security;

import hr.fer.tel.pipp.admin.model.User;
import hr.fer.tel.pipp.admin.repositories.UserRepository;
import hr.fer.tel.pipp.admin.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

@Service
public class MyUserDetailsService implements UserDetailsService {
	
	@Autowired
	private UserService userService;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		try {
			User user = userService.getUserByEmail(email);
			return new MyUserDetails(user);
		} catch (EntityNotFoundException e) {
			throw new UsernameNotFoundException("User with email '" + email + "' not found.");
		}
	}
}
