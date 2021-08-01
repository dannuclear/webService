package ru.mephi3.mvc.controller;

import java.security.Principal;

import javax.validation.Valid;

import lombok.extern.log4j.Log4j2;
import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.ModelAndView;
import ru.mephi3.domain.Equipment;
import ru.mephi3.domain.EquipmentMaintenance;
import ru.mephi3.domain.EquipmentVerification;
import ru.mephi3.domain.Sample;
import ru.mephi3.dto.EquipmentDTO;
import ru.mephi3.dto.SampleDTO;
import ru.mephi3.service.SampleService;
import ru.mephi3.service.exception.EquipmentNotFoundException;
import ru.mephi3.service.exception.SampleNotFoundException;
import ru.mephi3.web.method.support.DataTablesRequest;
import ru.mephi3.web.method.support.DataTablesResponse;

@Controller
@RequestMapping("/private/samples")
@RequiredArgsConstructor
@SessionAttributes({ "sample" })
@Log4j2
public class SampleController {
    public static final String EDIT_VIEW_NAME = "/private/sample/edit";
    public static final String ALL_VIEW_NAME = "/private/sample/all";
    public static final String REDIRECT_ALL_URL = "redirect:/private/samples";

    private final SampleService sampleService;

    @PostMapping("/dataTable")
    public ResponseEntity<DataTablesResponse<Sample>> findPageable(@RequestBody DataTablesRequest dataTablesRequest, SessionStatus sessionStatus) {
        sessionStatus.setComplete();
        Page<Sample> samplePage = sampleService.findByString(dataTablesRequest.getSearch().getValue(), dataTablesRequest.getPageRequest());
        return ResponseEntity.ok(DataTablesResponse.of(dataTablesRequest.getDraw(), samplePage));
    }

    @GetMapping
    public String findAll(Principal principal, Authentication authentication, SessionStatus sessionStatus) {
        sessionStatus.setComplete();
        return ALL_VIEW_NAME;
    }

    @GetMapping({"{sampleId}", "new"})
    @Transactional(readOnly = true)
    public String edit(@PathVariable(value = "sampleId", required = false) Integer id, Model model) {
        if (!model.containsAttribute("sample")) {
            SampleDTO sample = null;
            if (id != null) {
                sample = sampleService.findById(id).map(eq -> {
                    SampleDTO sampleDTO = SampleDTO.fromDomain(eq);
                    return sampleDTO;
                }).orElseThrow(() -> new SampleNotFoundException(id));
            } else
                sample = SampleDTO.createDefault();
            model.addAttribute("sample", sample);
        }
        model.addAttribute("readOnly", false);
        return EDIT_VIEW_NAME;
    }

    @GetMapping("/{sampleId}/delete")
    public ModelAndView delete(@PathVariable("sampleId") Integer sampleId, ModelAndView mv) {
        sampleService.findById(sampleId).map(sample -> {
            sampleService.delete(sample);
            return sample;
        }).orElseThrow(() -> new SampleNotFoundException(sampleId));

        mv.setStatus(HttpStatus.NO_CONTENT);
        mv.setViewName("redirect:" + ALL_VIEW_NAME);
        return mv;
    }

    @PostMapping({"{sampleId}", "new"})
    public String save(@PathVariable(required = false) Integer sampleId, @ModelAttribute("sample") @Valid SampleDTO sampleDTO, BindingResult bindingResult, SessionStatus sessionStatus) {
        if (bindingResult.hasErrors()) {
            return EDIT_VIEW_NAME;
        }
        if (sampleId != null) {
            sampleService.findById(sampleId).map(sample -> {
                Sample sampleToSave = sampleDTO.toDomain();
                sampleToSave.setId(sample.getId());
                return sampleService.save(sampleToSave);
            }).orElseThrow(() -> new SampleNotFoundException(sampleId));
        } else {
            sampleService.save(sampleDTO.toDomain());
        }
        sessionStatus.setComplete();
        return REDIRECT_ALL_URL;
    }
}