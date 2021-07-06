package ru.mephi3.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.hateoas.server.core.Relation;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "EQUIPMENT_VERIFICATION")
public class EquipmentVerification {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "NUM")
    @NotEmpty
    private String num;
    @Column(name = "CREATE_DATE")
    @NotNull
    private LocalDate createDate;
    @ManyToOne
    @JoinColumn(name = "ID_EQUIPMENT", referencedColumnName = "ID")
    @JsonIgnore
    private Equipment equipment;

    @Override
    public String toString() {
        return String.format("%s номер: %s", createDate, num);
    }
}
