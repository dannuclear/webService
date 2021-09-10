package ru.mephi3.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.OrderBy;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "SAMPLING")
public class Sampling {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "NUM")
    private String number;
    @Column(name = "CREATE_DATE")
    private LocalDate createDate;
    @Column(name = "BASE")
    private String base;
    @Column(name = "DELIVERY_TS")
    private LocalDateTime deliveryTimestamp;
    @Column(name = "DELIVERY_CONDITION")
    private String deliveryCondition;

    @OneToMany(mappedBy = "sampling")
    @OrderBy(clause = "id ASC")
    @JsonIgnore
    private List<SamplingSample> samples;
}
