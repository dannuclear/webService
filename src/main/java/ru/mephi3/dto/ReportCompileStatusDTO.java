package ru.mephi3.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReportCompileStatusDTO {
    private String message;
}
