package hr.fer.tel.pipp.admin.repositories;

import hr.fer.tel.pipp.admin.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, String> {
}
