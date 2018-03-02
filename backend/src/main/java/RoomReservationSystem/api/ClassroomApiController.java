package RoomReservationSystem.api;

import RoomReservationSystem.model.Classroom;
import RoomReservationSystem.service.impl.ClassroomServiceImpl;
import RoomReservationSystem.validation.ClassroomValidator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/api/classroom")
public class ClassroomApiController {
    
    @Autowired
    ClassroomServiceImpl classroomService;
    
    @Autowired
    ClassroomValidator classroomValidator;
    
    @GetMapping
    public Iterable<Classroom> getAll(){
        return classroomService.findAll();
    }
    
    @GetMapping("/getNames")
    public List<String> getNames(){
        return classroomService.getNames();
    }  
    
    @GetMapping("/findByName/{name}")
    public Classroom findByName(@PathVariable String name){
	return classroomService.findByName(name);
    }
    
    @GetMapping("/findByHasPC/{hasPC}")
    public List<Classroom> findByHasPC(@PathVariable boolean hasPC){
	return classroomService.findByHasPc(hasPC);
    }
    
    @GetMapping("/findByHasProjector/{hasProjector}")
    public List<Classroom> findByHasProjector(@PathVariable boolean hasProjector){
	return classroomService.findByHasProjector(hasProjector);
    }
    
    @GetMapping("/findByChairsLessThan/{chair}")
    public List<Classroom> findByChairsLessThan(@PathVariable int chairs){
	return classroomService.findByChairsLessThan(chairs);
    }
    
    @GetMapping("/findByChairsGreaterThan/{chair}")
    public List<Classroom> findByChairsGreaterThan(@PathVariable int chairs){
	return classroomService.findByChairsGreaterThan(chairs);
    }
    
    @GetMapping("/findByChairsBetween")
    public List<Classroom> findByChairsBetween(@PathVariable int from, @PathVariable int to){
	return classroomService.findByChairsBetween(from, to);
    }
    
    @PostMapping("/deleteByRoomName/{name}")
    public ResponseEntity<Classroom> deleteByRoomName(@PathVariable String name){
	classroomService.deleteByName(name);
        return new ResponseEntity(HttpStatus.OK);
    }
    
    @PostMapping("/createClassroom")
    public ResponseEntity<Classroom> createClassroom(@RequestBody Classroom cr, BindingResult bindingResult) {
        classroomValidator.validate(cr, bindingResult);
        if ( !bindingResult.hasErrors() ) {
            Classroom saved = classroomService.save(cr);
            return ResponseEntity.ok(saved);             
        }
        else {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
    }
    
    @PostMapping("/updateClassroom")
    public ResponseEntity<Classroom> updateClassroom(@RequestBody Classroom cr, BindingResult bindingResult) {
        classroomValidator.validate(cr, bindingResult);
        if (!bindingResult.hasErrors()) {
            classroomService.delete( classroomService.findByName(cr.getName()));
            Classroom saved = classroomService.save(cr);
            return ResponseEntity.ok(saved);          
        }
        else {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
    }    
}


