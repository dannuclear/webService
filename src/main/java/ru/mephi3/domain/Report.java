package ru.mephi3.domain;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.*;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Blob;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "REPORT")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class Report {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "NAME")
    private String name;
    @Column(name = "file_name")
    private String fileName;
    @Type(type = "jsonb")
    @Column(name = "parameters")
    private Map<String, Object> params;

    @Transient
    private boolean existsSource;
    @Transient
    private boolean existsCompiled;
}
