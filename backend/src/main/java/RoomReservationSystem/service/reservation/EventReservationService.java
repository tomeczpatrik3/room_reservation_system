package RoomReservationSystem.service.reservation;

import RoomReservationSystem.dto.reservation.EventReservationDTO;
import RoomReservationSystem.exception.BuildingNotExistsException;
import RoomReservationSystem.exception.ClassroomNotExistsException;
import RoomReservationSystem.exception.EventReservationNotExistsException;
import RoomReservationSystem.exception.SemesterNotExistsException;
import RoomReservationSystem.exception.StatusNotExistsException;
import RoomReservationSystem.exception.UserNotExistsException;
import RoomReservationSystem.model.reservation.EventReservation;
import java.util.List;

/**
 * Az eseményre vonatkozó foglalásokkal kapcsolatos műveletekért felelős
 * interfész Részletes információ a függvényekről a megválósításnál
 *
 * @author Tomecz Patrik
 */
public interface EventReservationService {

    EventReservation save(EventReservationDTO reservation) throws UserNotExistsException, ClassroomNotExistsException, StatusNotExistsException, SemesterNotExistsException, BuildingNotExistsException;

    EventReservation findById(int id) throws EventReservationNotExistsException;

    EventReservation setStatus(int id, String status) throws StatusNotExistsException, EventReservationNotExistsException;

    EventReservation findByName(String name) throws EventReservationNotExistsException;

    List<EventReservation> getAll();

    List<EventReservation> findByUsername(String username) throws UserNotExistsException;

    List<EventReservation> findByStatus(String statusName) throws StatusNotExistsException;

    List<EventReservation> findByBuildingAndClassroom(String building, String classroom) throws ClassroomNotExistsException, BuildingNotExistsException;

    boolean existsById(int id);

    void deleteByUsername(String username) throws UserNotExistsException;

    void deleteByBuildingAndClassroom(String building, String classroom) throws ClassroomNotExistsException, BuildingNotExistsException;

    void deleteByStatus(String status) throws StatusNotExistsException;

    void deleteByName(String name) throws EventReservationNotExistsException;
}
