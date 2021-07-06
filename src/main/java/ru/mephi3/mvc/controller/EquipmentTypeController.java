package ru.mephi3.mvc.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;
import ru.mephi3.domain.EquipmentType;
import ru.mephi3.dto.EquipmentTypeDTO;
import ru.mephi3.mvc.utils.ModelUtils;
import ru.mephi3.service.EquipmentTypeService;
import ru.mephi3.service.exception.EquipmentTypeNotFoundException;
import ru.mephi3.web.method.support.DataTablesRequest;
import ru.mephi3.web.method.support.DataTablesResponse;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequestMapping("/private/equipmentTypes")
@RequiredArgsConstructor
@SessionAttributes({"equipmentType"})
public class EquipmentTypeController {
    public static final String editView = "/private/equipmentType";
    public static final String allView = "/private/equipmentTypes";
    public static final String redirectEditView = "redirect:" + editView;
    public static final String redirectAllView = "redirect:" + allView;

    private final EquipmentTypeService equipmentTypeService;

    @PostMapping("/dataTable")
    public ResponseEntity<DataTablesResponse<EquipmentType>> findPageable(@RequestBody DataTablesRequest dataTablesRequest) {
        Page<EquipmentType> equipmentTypesPage = equipmentTypeService.findByFilter(dataTablesRequest.getSearch().getValue(), dataTablesRequest.getPageRequest());
        return ResponseEntity.ok(DataTablesResponse.of(dataTablesRequest.getDraw(), equipmentTypesPage));
    }

    @GetMapping
    public String findAll(Principal principal, Authentication authentication, SessionStatus sessionStatus) {
        sessionStatus.setComplete();
        return allView;
    }

    @GetMapping("{equipmentTypeId}")
    public String edit(@PathVariable("equipmentTypeId") Integer id, Model model) {
        EquipmentTypeDTO equipmentType = equipmentTypeService.findById(id).map(doc -> {
            return EquipmentTypeDTO.fromDomain(doc);
        }).orElseThrow(() -> new EquipmentTypeNotFoundException(id));
        ModelUtils.addIfNotExist(model, "equipmentType", equipmentType);
        model.addAttribute("readOnly", false);
        return editView;
    }

    @GetMapping("/new")
    public String add(Model model) {
        ModelUtils.addIfNotExist(model, "equipmentType", EquipmentTypeDTO.createDefault());
        return editView;
    }

    @GetMapping("{equipmentTypeId}/delete")
    public ModelAndView delete(@PathVariable("equipmentTypeId") Integer id, ModelAndView mv) {
        equipmentTypeService.findById(id).map(equipmentType -> {
            equipmentTypeService.delete(equipmentType);
            return equipmentType;
        }).orElseThrow(() -> new EquipmentTypeNotFoundException(id));

        mv.setStatus(HttpStatus.NO_CONTENT);
        mv.setViewName(redirectAllView);
        return mv;
    }

    @PostMapping
    public String save(@ModelAttribute("equipmentType") @Valid EquipmentTypeDTO equipmentTypeDTO, BindingResult bindingResult, SessionStatus sessionStatus) {
        if (bindingResult.hasErrors()) {
            return editView;
        }

        EquipmentType equipmentType = equipmentTypeDTO.toDomain();
        if (equipmentType.getId() != null) {
            equipmentTypeService.findById(equipmentType.getId()).map(dbDoc -> {
                return equipmentTypeService.save(equipmentType);
            }).orElseThrow(() -> new EquipmentTypeNotFoundException(equipmentType.getId()));
        } else {
            equipmentTypeService.save(equipmentType);
        }
        sessionStatus.setComplete();
        return redirectAllView;
    }
}