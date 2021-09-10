package ru.mephi3.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

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
@Table(name = "SAMPLING_SAMPLE")
public class SamplingSample {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "ID_SAMPLE", referencedColumnName = "ID")
    @JsonIgnore
    private Sample sample;

    @ManyToOne
    @JoinColumn(name = "ID_SAMPLING", referencedColumnName = "ID")
    @JsonIgnore
    private Sampling sampling;
}
