package ru.mephi3.webService.formatter;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.Formatter;
import ru.mephi3.domain.EquipmentType;
import ru.mephi3.domain.ObjectClass;
import ru.mephi3.dto.JSTreeNode;
import ru.mephi3.service.EquipmentTypeService;
import ru.mephi3.service.exception.EquipmentTypeNotFoundException;

import java.text.ParseException;
import java.util.Locale;

@RequiredArgsConstructor
public class EquipmentTypeFormatter implements Formatter<EquipmentType> {
    private final EquipmentTypeService equipmentTypeService;

    @Override
    public EquipmentType parse(String text, Locale locale) throws ParseException {
        final Integer id = Integer.valueOf(text);
        return equipmentTypeService.findById(id).orElseThrow(() -> new EquipmentTypeNotFoundException(id));
    }

    @Override
    public String print(EquipmentType object, Locale locale) {
        return object != null ? object.getId().toString() : "";
    }
}
