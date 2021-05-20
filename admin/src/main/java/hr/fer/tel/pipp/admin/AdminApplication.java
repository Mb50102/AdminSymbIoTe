package hr.fer.tel.pipp.admin;

import hr.fer.tel.pipp.admin.dto.UserRequestDTO;
import hr.fer.tel.pipp.admin.model.Role;
import hr.fer.tel.pipp.admin.model.User;
import hr.fer.tel.pipp.admin.model.UserRequest;
import hr.fer.tel.pipp.admin.repositories.UserRepository;
import hr.fer.tel.pipp.admin.security.WebSecurityConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AdminApplication implements CommandLineRunner {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private WebSecurityConfigurer webSecurityConfigurer;

	public static void main(String[] args) {
		SpringApplication.run(AdminApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		if (userRepository.count() == 0) {
			// init default admin user
			User u = new User();
			u.setEmail("admin@fer.hr");
			u.setPassword(webSecurityConfigurer.getPasswordEncoder().encode("admin"));
			u.setName("Admin");
			u.setSurname("Admin");
			u.setRole(Role.ROLE_ADMIN);
			u.setUsername("admin");
			userRepository.save(u);
		}
		// tu se moze isprobat dio koda koji se izvodi pri pookretanju aplikacije
	}
}
