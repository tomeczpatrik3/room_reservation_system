package RoomReservationSystem.api.reservation;

import static RoomReservationSystem.config.ErrorMessageConstants.RESERVATION_NOT_EXISTS;
import static RoomReservationSystem.config.ErrorMessageConstants.STATUS_NOT_EXISTS;
import static RoomReservationSystem.config.ErrorMessageConstants.concatErrors;
import RoomReservationSystem.dto.reservation.ClassReservationDTO;
import static RoomReservationSystem.dto.reservation.ClassReservationDTO.toClassReservationDTO;
import static RoomReservationSystem.dto.reservation.ClassReservationDTO.toClassReservationDTOList;
import RoomReservationSystem.exception.BuildingNotExistsException;
import RoomReservationSystem.exception.ClassReservationNotExistsException;
import RoomReservationSystem.exception.ClassroomNotExistsException;
import RoomReservationSystem.exception.SemesterNotExistsException;
import RoomReservationSystem.exception.StatusNotExistsException;
import RoomReservationSystem.exception.SubjectNotExistsException;
import RoomReservationSystem.exception.UserNotExistsException;
import RoomReservationSystem.model.reservation.ClassReservation;
import RoomReservationSystem.service.StatusService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import RoomReservationSystem.service.reservation.ClassReservationService;
import static RoomReservationSystem.util.ExceptionUtils.handleException;
import RoomReservationSystem.validation.BaseReservationValidator;
import RoomReservationSystem.validation.ClassReservationValidator;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A tanórákra vonatkozó foglalásokhoz tartozó végpontokat tartalmazó osztály
 *
 * @author Tomecz Patrik
 */
@RestController
@RequestMapping(value = "/api/simpleReservation")
public class ClassReservationApiController extends ReservationApiController {

    @Autowired
    ClassReservationService classService;

    @Autowired
    StatusService statusService;

    @Autowired
    BaseReservationValidator baseRValidator;

    @Autowired
    ClassReservationValidator classRValidator;

    /**
     * A függvény ami visszaadja az elfogadott foglalásokat
     *
     * @return A megfelelő foglalások egy listában
     */
    @GetMapping
    @Override
    public List<ClassReservationDTO> getAccepted() {
        try {
            return toClassReservationDTOList(classService.findByStatus("ACCEPTED"));
        } catch (StatusNotExistsException | NullPointerException ex) {
            Logger.getLogger(ClassReservationApiController.class.getName()).log(Level.WARNING, ex.getMessage(), ex);
            return Collections.EMPTY_LIST;
        }

    }

    /**
     * A függvény ami visszaadja az adott felhasználóhoz tartozó foglalásokat
     *
     * @param username A felhasználónév
     * @return A megfelelő foglalások egy listában
     */
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/findByUsername/{username}")
    @Override
    public List<ClassReservationDTO> findByUsername(@PathVariable String username) {
        try {
            return toClassReservationDTOList(classService.findByUsername(username));
        } catch (UserNotExistsException | NullPointerException ex) {
            Logger.getLogger(ClassReservationApiController.class.getName()).log(Level.WARNING, ex.getMessage(), ex);
            return Collections.EMPTY_LIST;
        }
    }

    /**
     * A függvény ami visszaadja az adott státusszal rendelkező foglalásokat
     *
     * @param status A státusz
     * @return A megfelelő válasz entitás
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/findByStatus/{status}")
    @Override
    public ResponseEntity findByStatus(@PathVariable String status) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(toClassReservationDTOList(classService.findByStatus(status)));
        } catch (StatusNotExistsException | NullPointerException ex) {
            return handleException(ex);
        }
    }

    /**
     * A függvény ami visszaadja az adott terem kiválasztott időpontjára
     * vonatkozó foglalásokat
     *
     * @param building Az épület
     * @param classroom A terem
     * @param date A dátum
     * @return A megfelelő foglalások egy listában
     */
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/findByClassroomAndDate")
    @Override
    public List<ClassReservationDTO> findByClassroomAndDate(
            @RequestParam("building") String building,
            @RequestParam("classroom") String classroom,
            @RequestParam("date") String date
    ) {
//	return toReservationDTOList(
//                eventService.findByClassroomAndDate(
//                        building,
//                        classroom,
//                        getDate(date)
//                ));
        return null;
    }

    /**
     * A függvény ami beállítja egy adott foglalást státuszát a paraméterben
     * átadott értékre
     *
     * @param id A foglalás azonosítója
     * @param status A státusz
     * @return A megfelelő válasz entitás
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/setStatus")
    @Override
    public ResponseEntity setStatus(@RequestParam("id") int id, @RequestParam("status") String status) {
        try {
            return ResponseEntity.ok(toClassReservationDTO(classService.setStatus(id, status)));
        } catch (StatusNotExistsException | ClassReservationNotExistsException | NullPointerException ex) {
            return handleException(ex);
        }
    }

    /**
     * A függvény ami létrehozza a megfelelő tanórára vonatkozó foglalást
     *
     * @param classReservationDTO A foglalás
     * @param bindingResult
     * @return A megfelelő válasz entitás
     */
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PostMapping("/createReservation")
    public ResponseEntity createReservation(@RequestBody ClassReservationDTO classReservationDTO, BindingResult bindingResult) {
        baseRValidator.validate(classReservationDTO, bindingResult);
        classRValidator.validate(classReservationDTO, bindingResult);
        if (!bindingResult.hasErrors()) {
            try {
                ClassReservation saved = classService.save(classReservationDTO);
                return ResponseEntity.status(HttpStatus.CREATED).body(toClassReservationDTO(saved));
            } catch (UserNotExistsException | SubjectNotExistsException | ClassroomNotExistsException | StatusNotExistsException | SemesterNotExistsException | BuildingNotExistsException ex) {
                return handleException(ex);
            } 
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(concatErrors(bindingResult));
        }
    }

}
