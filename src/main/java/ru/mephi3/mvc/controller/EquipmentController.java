package ru.mephi3.mvc.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;
import ru.mephi3.domain.Equipment;
import ru.mephi3.domain.EquipmentMaintenance;
import ru.mephi3.domain.EquipmentVerification;
import ru.mephi3.dto.EquipmentDTO;
import ru.mephi3.service.EquipmentService;
import ru.mephi3.service.exception.EquipmentNotFoundException;
import ru.mephi3.web.method.support.DataTablesRequest;
import ru.mephi3.web.method.support.DataTablesResponse;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/private/equipments")
@RequiredArgsConstructor
@SessionAttributes({"equipment", "equipmentVerification", "equipmentMaintenance"})
@Log4j2
public class EquipmentController {
    public static final String EDIT_VIEW = "/private/equipment/edit";
    public static final String ALL_VIEW = "/private/equipment/all";
    public static final String REDIRECT_ALL_URL = "redirect:/private/equipments";
    public static final String REDIRECT_EDIT_URL = "redirect:/private/equipments/{equipmentId}";
    public static final String REDIRECT_NEW_URL = "redirect:/private/equipments/new";


    private final EquipmentService equipmentService;

    @PostMapping("/dataTable")
    public ResponseEntity<DataTablesResponse<Equipment>> findPageable(@RequestBody DataTablesRequest dataTablesRequest, SessionStatus sessionStatus) {
        sessionStatus.setComplete();
        Page<Equipment> equipmentsPage = equipmentService.findByFilter(dataTablesRequest.getSearch().getValue(), dataTablesRequest.getPageRequest());
        return ResponseEntity.ok(DataTablesResponse.of(dataTablesRequest.getDraw(), equipmentsPage));
    }

    @GetMapping
    public String findAll(Principal principal, Authentication authentication, SessionStatus sessionStatus) {
        sessionStatus.setComplete();
        return ALL_VIEW;
    }

    @GetMapping({"{equipmentId}", "new"})
    @Transactional(readOnly = true)
    public String edit(@PathVariable(value = "equipmentId", required = false) Integer id, Model model) {
        if (!model.containsAttribute("equipment")) {
            EquipmentDTO equipment = null;
            if (id != null) {
                equipment = equipmentService.findById(id).map(eq -> {
                    Hibernate.initialize(eq.getMaintenances());
                    Hibernate.initialize(eq.getVerifications());
                    EquipmentDTO equipmentDTO = EquipmentDTO.fromDomain(eq);
                    updateCurrentVerification(equipmentDTO);
                    updateCurrentMaintenance(equipmentDTO);
                    return equipmentDTO;
                }).orElseThrow(() -> new EquipmentNotFoundException(id));
            } else
                equipment = EquipmentDTO.createDefault();
            model.addAttribute("equipment", equipment);
            model.addAttribute("equipmentVerification", EquipmentVerification.builder().build());
            model.addAttribute("equipmentMaintenance", EquipmentMaintenance.builder().build());
        }
        model.addAttribute("readOnly", false);
        return EDIT_VIEW;
    }

    @GetMapping("{equipmentId}/delete")
    public ModelAndView delete(@PathVariable("equipmentId") Integer id, ModelAndView mv) {
        equipmentService.findById(id).map(equipment -> {
            equipmentService.delete(equipment);
            return equipment;
        }).orElseThrow(() -> new EquipmentNotFoundException(id));

        mv.setStatus(HttpStatus.NO_CONTENT);
        mv.setViewName(REDIRECT_ALL_URL);
        return mv;
    }

    @PostMapping({"{equipmentId}", "new"})
    @Transactional
    public String save(@ModelAttribute("equipment") @Valid EquipmentDTO equipmentDTO, BindingResult bindingResult, SessionStatus sessionStatus) {
        if (bindingResult.hasErrors()) {
            return EDIT_VIEW;
        }

        Equipment equipment = equipmentDTO.toDomain();
        if (equipment.getId() != null) {
            equipmentService.findById(equipment.getId()).map(dbDoc -> {
                Hibernate.initialize(dbDoc.getVerifications());
                Hibernate.initialize(dbDoc.getMaintenances());
                return equipmentService.save(equipment);
            }).orElseThrow(() -> new EquipmentNotFoundException(equipment.getId()));
        } else {
            equipmentService.save(equipment);
        }
        sessionStatus.setComplete();
        return "redirect:/private/equipments";
    }

    @PostMapping(value = {"{equipmentId}", "new"}, params = {"addVerification"})
    public String addVerification(@PathVariable(value = "equipmentId", required = false) Integer id, @ModelAttribute("equipment") EquipmentDTO equipmentDTO, @ModelAttribute("equipmentVerification") @Valid EquipmentVerification equipmentVerification, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return EDIT_VIEW;
        }
        equipmentDTO.getVerifications().add(equipmentVerification);
        updateCurrentVerification(equipmentDTO);
        model.addAttribute("equipmentVerification", EquipmentVerification.builder().build());
        return "redirect:#";
    }

    @PostMapping(value = {"{equipmentId}", "new"}, params = {"deleteVerificationIdx"})
    public String deleteVerification(@ModelAttribute("equipment") EquipmentDTO equipmentDTO, @PathVariable(value = "equipmentId", required = false) Integer equipmentId, @RequestParam("tab") String tab, @RequestParam("deleteVerificationIdx") Integer idx) {
        equipmentDTO.getVerifications().remove(idx.intValue());
        updateCurrentVerification(equipmentDTO);
        return "redirect:#";
    }

    @PostMapping(value = {"{equipmentId}", "new"}, params = {"addMaintenance"})
    public String addMaintenance(@PathVariable(value = "equipmentId", required = false) Integer id, @ModelAttribute("equipment") EquipmentDTO equipmentDTO, @ModelAttribute("equipmentMaintenance") @Valid EquipmentMaintenance equipmentMaintenance, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return EDIT_VIEW;
        }
        equipmentDTO.getMaintenances().add(equipmentMaintenance);
        updateCurrentMaintenance(equipmentDTO);
        model.addAttribute("equipmentMaintenance", EquipmentMaintenance.builder().build());
        return "redirect:#";
    }

    @PostMapping(value = {"{equipmentId}", "new"}, params = {"deleteMaintenanceIdx"})
    public String deleteMaintenance(@ModelAttribute("equipment") EquipmentDTO equipmentDTO, @PathVariable(value = "equipmentId", required = false) Integer equipmentId, @RequestParam("tab") String tab, @RequestParam("deleteMaintenanceIdx") Integer idx) {
        equipmentDTO.getMaintenances().remove(idx.intValue());
        updateCurrentMaintenance(equipmentDTO);
        return "redirect:#";
    }

    private void updateCurrentVerification(EquipmentDTO equipmentDTO) {
        List<EquipmentVerification> vrfs = equipmentDTO.getVerifications();
        if (vrfs == null || vrfs.size() == 0) {
            equipmentDTO.setVerification(null);
            return;
        }
        Collections.sort(vrfs, (e1, e2) -> e2.getCreateDate().compareTo(e1.getCreateDate()));
        equipmentDTO.setVerification(vrfs.get(0));
    }

    private void updateCurrentMaintenance(EquipmentDTO equipmentDTO) {
        List<EquipmentMaintenance> mnts = equipmentDTO.getMaintenances();
        if (mnts == null || mnts.size() == 0) {
            equipmentDTO.setMaintenance(null);
            return;
        }
        Collections.sort(mnts, (e1, e2) -> e1.getCreateDate().compareTo(e2.getCreateDate()));
        equipmentDTO.setMaintenance(mnts.get(0));
    }
}