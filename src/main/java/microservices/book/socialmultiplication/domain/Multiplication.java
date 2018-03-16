package microservices.book.socialmultiplication.domain;

import lombok.*;

import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Entity;

import java.io.Serializable;

@RequiredArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "MULTIPLICATION")
public final class Multiplication  implements Serializable {
    @Id
    @GeneratedValue
    @Column(name = "MULTIPLICATION_ID")
    private Long id;
    private final int factorA;
    private final int factorB;

    //Empty constructor for JSON (de)serialization
    public Multiplication() {
        this.factorA = 0;
        this.factorB = 0;
    }
}
