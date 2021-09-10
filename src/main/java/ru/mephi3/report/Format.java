package ru.mephi3.report;

import lombok.Getter;

@Getter
public enum Format {
    HTML("Новая страница"),
    PDF("PDF Документ"),
    TXT("Чистый текст"),
    XLSX("Excel (.xlsx)"),
    XLS("Excel (.xls)"),
    DOC("Word (.doc)"),
    DOCX("Word (.docx)"),
    RTF("RTF (.rtf)"),
    CSV("CSV (.csv)");

    private String label;

    Format(String label) {
        this.label = label;
    }
}
