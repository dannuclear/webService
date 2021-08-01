package ru.mephi3.domain;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "REPORT")
public class Report {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "NAME")
    private String name;
    @Column(name = "data")
    private byte[] data;
    @Column(name = "source")
    private byte[] source;
}
