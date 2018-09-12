package RoomReservationSystem.api;

import RoomReservationSystem.dto.SemesterDTO;
import static RoomReservationSystem.dto.SemesterDTO.toSemesterDTO;
import static RoomReservationSystem.dto.SemesterDTO.toSemesterDTOList;
import RoomReservationSystem.exception.SemesterAlredyExistsException;
import RoomReservationSystem.exception.SemesterNotExistsException;
import RoomReservationSystem.model.Semester;
import static RoomReservationSystem.model.Semester.toSemester;
import RoomReservationSystem.service.SemesterService;
import static RoomReservationSystem.util.ExceptionUtils.handleException;
import static RoomReservationSystem.util.ValidationUtils.concatErrors;
import RoomReservationSystem.validation.SemesterValidator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * A szemeszterekhez tartozó végpontokat tartalmazó osztály
 *
 * @author Tomecz Patrik
 */
@RestController
@RequestMapping(value = "/api/semester")
public class SemesterApiController {

    @Autowired
    private SemesterService semesterService;

    @Autowired
    private SemesterValidator semesterValidator;

    /**
     * A függvény ami visszaadja egy listában az összes adatbázisban található
     * szemesztert
     *
     * @return A szemeszterek egy listában
     */
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping
    public ResponseEntity getAll() {
        return ResponseEntity.ok(toSemesterDTOList(semesterService.getAll()));
    }

    /**
     * A függvény ami visszaadja egy listában az szemeszterek neveit
     *
     * @return A szemeszterek nevei egy listában
     */
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/getSemesterNames")
    public ResponseEntity getSemesterNames() {
        return ResponseEntity.ok(semesterService.getNames());
    }

    /**
     * A függvény ami létrehozza a megfelelő szemesztert
     *
     * @param semesterDTO A szemeszter
     * @param bindingResult
     * @return A megfelelő válasz entitás
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/createSemester")
    public ResponseEntity createSemester(@RequestBody SemesterDTO semesterDTO, BindingResult bindingResult) {
        semesterValidator.validate(semesterDTO, bindingResult);
        if (!bindingResult.hasErrors()) {
            try {
                Semester saved = semesterService.save(toSemester(semesterDTO));
                return ResponseEntity.status(HttpStatus.CREATED).body(toSemesterDTO(saved));
            } catch (SemesterAlredyExistsException | NullPointerException ex) {
                return handleException(ex);
            }

        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(concatErrors(bindingResult));
        }
    }

    /**
     * A függvény ami törli az adott névhez tartozó szemesztert
     *
     * @param name A név
     * @return A megfelelő válasz entitás
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/deleteByName")
    public ResponseEntity deleteByName(@RequestParam String name) {
        try {
            semesterService.deleteByName(name);
            return new ResponseEntity(HttpStatus.OK);
        } catch (SemesterNotExistsException | NullPointerException ex) {
            return handleException(ex);
        } 
    }
}
