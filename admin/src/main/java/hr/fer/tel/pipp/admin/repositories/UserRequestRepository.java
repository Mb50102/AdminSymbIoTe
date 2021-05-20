package hr.fer.tel.pipp.admin.repositories;

import hr.fer.tel.pipp.admin.model.Resource;
import hr.fer.tel.pipp.admin.model.UserRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRequestRepository extends JpaRepository<UserRequest, String> {
}
