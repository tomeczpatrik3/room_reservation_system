package RoomReservationSystem.model;

import RoomReservationSystem.model.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.sql.Timestamp;

/**
 * A foglalásokhoz tartozó dátumokat tartalmazó osztály
 *
 * @author Tomecz Patrik
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "RESERVATION_DATES")
public class ReservationDate extends BaseEntity {

    /*A foglalás státusza*/
    @JsonIgnore
    @JoinColumn(name = "RESERVATION", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Reservation reservation;

    /*A foglalás kezdete (dátum)*/
    @Basic(optional = false)
    @NotNull
    @Column(name = "START")
    private Timestamp start;

    /*A foglalás vége (dátum)*/
    @Basic(optional = false)
    @NotNull
    @Column(name = "END")
    private Timestamp end;
}
