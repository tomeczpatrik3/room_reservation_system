package RoomReservationSystem.service.reservation;

import RoomReservationSystem.dto.reservation.ClassReservationDTO;
import RoomReservationSystem.exception.BuildingNotExistsException;
import RoomReservationSystem.exception.ClassReservationNotExistsException;
import RoomReservationSystem.exception.ClassroomNotExistsException;
import RoomReservationSystem.exception.SemesterNotExistsException;
import RoomReservationSystem.exception.StatusNotExistsException;
import RoomReservationSystem.exception.SubjectNotExistsException;
import RoomReservationSystem.exception.UserNotExistsException;
import RoomReservationSystem.model.Classroom;
import RoomReservationSystem.model.Subject;
import RoomReservationSystem.model.reservation.ClassReservation;
import java.util.List;

/**
 * A tanórákra vonatkozó foglalásokkal kapcsolatos műveletekért felelős interfész
 * Részletes információ a függvényekről a megválósításnál
 * @author SimpleReservationomecz Patrik
 */
public interface ClassReservationService {
    ClassReservation save(ClassReservationDTO reservation) throws UserNotExistsException, SubjectNotExistsException, ClassroomNotExistsException, StatusNotExistsException, SemesterNotExistsException, BuildingNotExistsException;
    void delete(ClassReservation reservation);
    ClassReservation findById(int id) throws ClassReservationNotExistsException;
    ClassReservation setStatus(int id, String status) throws StatusNotExistsException, ClassReservationNotExistsException;
    List<ClassReservation> getAll();
    List<ClassReservation> findByUsername(String username) throws UserNotExistsException;
    List<ClassReservation> findByStatus(String statusName) throws StatusNotExistsException;
    List<ClassReservation> findByClassroom(Classroom classroom) throws ClassroomNotExistsException;
    List<ClassReservation> findBySubject(Subject subject) throws SubjectNotExistsException;
    //List<SimpleReservation> findByDate(Date date);
    //List<SimpleReservation> findByClassroomAndDate(String building, String classroom, Date date);
}
