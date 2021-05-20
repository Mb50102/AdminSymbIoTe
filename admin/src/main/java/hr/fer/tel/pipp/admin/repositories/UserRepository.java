package hr.fer.tel.pipp.admin.repositories;

import hr.fer.tel.pipp.admin.model.User;
import org.springframework.data.jpa.repository.JpaRepository;


@org.springframework.stereotype.Repository
public interface UserRepository extends JpaRepository<User, String>{
}

