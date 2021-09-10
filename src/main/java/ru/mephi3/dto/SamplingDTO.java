package ru.mephi3.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import ru.mephi3.domain.Sampling;
import ru.mephi3.domain.SamplingSample;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class SamplingDTO {
    private Integer id;
    private String number;
    private LocalDate createDate;
    private String base;
    private LocalDateTime deliveryTimestamp;
    private String deliveryCondition;
    @JsonIgnore
    private List<SamplingSample> samples;

    public static SamplingDTO fromDomain(Sampling domain) {
        return SamplingDTO.builder()//
                .id(domain.getId())//
                .number(domain.getNumber())//
                .createDate(domain.getCreateDate())//
                .base(domain.getBase())//
                .deliveryTimestamp(domain.getDeliveryTimestamp())//
                .deliveryCondition(domain.getDeliveryCondition())//
                .samples(domain.getSamples())//
                .build();
    }

    public Sampling toDomain() {
        return Sampling.builder()//
                .number(this.number)//
                .createDate(this.createDate)//
                .base(this.base)//
                .deliveryTimestamp(this.deliveryTimestamp)//
                .deliveryCondition(this.deliveryCondition)//
                .build();
    }

    public static SamplingDTO createDefault() {
        return SamplingDTO.builder().build();
    }
}
