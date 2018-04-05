package RoomReservationSystem.api;

import RoomReservationSystem.dto.ReservationDTO;
import RoomReservationSystem.model.Reservation;
import RoomReservationSystem.service.ReservationService;
import static RoomReservationSystem.config.ValidationErrorMessageConstants.concatErrors;
import static RoomReservationSystem.dto.ReservationDTO.toReservationDTO;
import static RoomReservationSystem.dto.ReservationDTO.toReservationDTOList;
import static RoomReservationSystem.config.ValidationErrorMessageConstants.RESERVATION_NOT_EXISTS;
import RoomReservationSystem.dto.EventReservationDTO;
import RoomReservationSystem.dto.SemesterReservationDTO;
import RoomReservationSystem.dto.SimpleReservationDTO;
import static RoomReservationSystem.util.DateUtils.getDate;
import RoomReservationSystem.validation.BaseReservationValidator;
import RoomReservationSystem.validation.EventReservationValidator;
import RoomReservationSystem.validation.SemesterReservationValidator;
import RoomReservationSystem.validation.SimpleReservationValidator;

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

@RestController
@RequestMapping(value="/api/reservation")
public class ReservationApiController {
    
    @Autowired
    ReservationService reservationService;
    
    /*Validátorok*/
    @Autowired
    BaseReservationValidator baseReservationValidator;
    
    @Autowired
    SimpleReservationValidator simpleReservationValidator;
    
    @Autowired
    SemesterReservationValidator semesterReservationValidator;
    
    @Autowired
    EventReservationValidator eventReservationValidator;
    
    @GetMapping
    public List<ReservationDTO> getAccepted(){
        return toReservationDTOList(reservationService.findByStatus("ACCEPTED"));
    }
    
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/findByUsername/{username}")
    public List<ReservationDTO> findByName(@PathVariable String username){
	return toReservationDTOList(reservationService.findByUsername(username));
    }
    
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PostMapping("/createSimpleReservation")
    public ResponseEntity createSimpleReservation(@RequestBody SimpleReservationDTO simpleReservationDTO, BindingResult bindingResult) {
        baseReservationValidator.validate(simpleReservationDTO, bindingResult);
        simpleReservationValidator.validate(simpleReservationDTO, bindingResult);
        if (!bindingResult.hasErrors()) {
            Reservation saved = reservationService.save(simpleReservationDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(toReservationDTO(saved));           
        }
        else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(concatErrors(bindingResult));
        }        
    }
    
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PostMapping("/createSemesterReservation")
    public ResponseEntity createSemesterReservation(@RequestBody SemesterReservationDTO semesterReservationDTO, BindingResult bindingResult) {
        baseReservationValidator.validate(semesterReservationDTO, bindingResult);
        semesterReservationValidator.validate(semesterReservationDTO, bindingResult);
        if (!bindingResult.hasErrors()) {
            Reservation saved = reservationService.save(semesterReservationDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(toReservationDTO(saved));           
        }
        else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(concatErrors(bindingResult));
        }        
    }
    
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PostMapping("/createEventReservation")
    public ResponseEntity createEventReservation(@RequestBody EventReservationDTO eventReservationDTO, BindingResult bindingResult) {
        baseReservationValidator.validate(eventReservationDTO, bindingResult);
        eventReservationValidator.validate(eventReservationDTO, bindingResult);
        if (!bindingResult.hasErrors()) {
            Reservation saved = reservationService.save(eventReservationDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(toReservationDTO(saved));           
        }
        else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(concatErrors(bindingResult));
        } 
    }
    
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/findByStatus/{status}")
    public List<ReservationDTO> findByStatus(@PathVariable String status){
	return toReservationDTOList(reservationService.findByStatus(status));
    } 
    
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/findByClassroomAndDate")
    public List<ReservationDTO> findByClassroomAndDate(
            @RequestParam("building") String building,
            @RequestParam("classroom") String classroom,
            @RequestParam("date") String date
    ){
	return toReservationDTOList(
                reservationService.findByClassroomAndDate(
                        building,
                        classroom,
                        getDate(date)
                ));
    }
    
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/setStatus")
    public ResponseEntity setStatus(@RequestParam("id") int id, @RequestParam("status") String status){
	if (reservationService.findById(id) != null)
            return ResponseEntity.ok(toReservationDTO(reservationService.setStatus(id, status)));
        else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(RESERVATION_NOT_EXISTS);
    } 
}
