package RoomReservationSystem.model.reservation;

import RoomReservationSystem.model.Classroom;
import RoomReservationSystem.model.Semester;
import RoomReservationSystem.model.Status;
import RoomReservationSystem.model.Subject;
import RoomReservationSystem.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * A tanórákra vonatkozó foglalásokhoz tartozó ősosztály
 * @author Tomecz Patrik
 */
@Data
@EqualsAndHashCode(callSuper=true)
@Entity
@DiscriminatorValue("CLASS")
public class ClassReservation extends Reservation {
    @JsonIgnore
    @JoinColumn(name = "SUBJECT", referencedColumnName = "ID", nullable = true)
    @ManyToOne(optional = true)
    private Subject subject;    /*A tantárgy*/
    
    @JsonIgnore
    @JoinColumn(name = "SEMESTER", referencedColumnName = "ID", nullable = true)
    @ManyToOne(optional = true)
    private Semester semester;    /*A szemeszter*/    
    
    protected ClassReservation(            
            User user,
            Subject subject,
            Classroom classroom,
            Status status,
            Semester semester,
            List<ReservationDate> dateList,
            String note) {
        super(user, classroom, status, dateList, note);
        this.subject = subject;
        this.semester = semester;
    }
    
    protected ClassReservation() {
        super();
    }
    
    public static ClassReservation toClassReservation(
           User user,
            Subject subject,
            Classroom classroom,
            Status status,
            Semester semester,
            List<ReservationDate> dateList,
            String note
    ) {
        return new ClassReservation(user, subject, classroom, status, semester, dateList, note);
    }
}
