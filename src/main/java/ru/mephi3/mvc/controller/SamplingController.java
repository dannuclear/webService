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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;
import ru.mephi3.domain.Sampling;
import ru.mephi3.dto.SampleDTO;
import ru.mephi3.dto.SamplingDTO;
import ru.mephi3.service.SamplingService;
import ru.mephi3.service.exception.SamplingNotFoundException;
import ru.mephi3.web.method.support.DataTablesRequest;
import ru.mephi3.web.method.support.DataTablesResponse;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequestMapping("/private/samplings")
@RequiredArgsConstructor
@SessionAttributes({"sampling"})
@Log4j2
public class SamplingController {
    public static final String EDIT_VIEW_NAME = "/private/sampling/edit";
    public static final String ALL_VIEW_NAME = "/private/sampling/all";
    public static final String REDIRECT_ALL_URL = "redirect:/private/samplings";

    private final SamplingService samplingService;

    @PostMapping("/dataTable")
    public ResponseEntity<DataTablesResponse<Sampling>> findPageable(@RequestBody DataTablesRequest dataTablesRequest, SessionStatus sessionStatus) {
        sessionStatus.setComplete();
        Page<Sampling> samplingPage = samplingService.findByString(dataTablesRequest.getSearch().getValue(), dataTablesRequest.getPageRequest());
        return ResponseEntity.ok(DataTablesResponse.of(dataTablesRequest.getDraw(), samplingPage));
    }

    @GetMapping
    public String findAll(Principal principal, Authentication authentication, SessionStatus sessionStatus) {
        sessionStatus.setComplete();
        return ALL_VIEW_NAME;
    }

    @GetMapping({"{samplingId}", "new"})
    @Transactional(readOnly = true)
    public String edit(@PathVariable(value = "samplingId", required = false) Integer id, Model model, @ModelAttribute(value = "sample") SampleDTO sampleDTO) {
        if (!model.containsAttribute("sampling")) {
            SamplingDTO sampling = null;
            if (id != null) {
                sampling = samplingService.findById(id).map(eq -> {
                    Hibernate.initialize(eq.getSamples());
                    SamplingDTO samplingDTO = SamplingDTO.fromDomain(eq);
                    return samplingDTO;
                }).orElseThrow(() -> new SamplingNotFoundException(id));
            } else
                sampling = SamplingDTO.createDefault();
            model.addAttribute("sampling", sampling);
        }
        model.addAttribute("readOnly", false);
        return EDIT_VIEW_NAME;
    }

    @GetMapping("/{samplingId}/delete")
    public ModelAndView delete(@PathVariable("samplingId") Integer samplingId, ModelAndView mv) {
        samplingService.findById(samplingId).map(sampling -> {
            samplingService.delete(sampling);
            return sampling;
        }).orElseThrow(() -> new SamplingNotFoundException(samplingId));

        mv.setStatus(HttpStatus.NO_CONTENT);
        mv.setViewName("redirect:" + ALL_VIEW_NAME);
        return mv;
    }

    @PostMapping({"{samplingId}", "new"})
    public String save(@PathVariable(required = false) Integer samplingId, @ModelAttribute("sampling") @Valid SamplingDTO samplingDTO, BindingResult bindingResult, SessionStatus sessionStatus, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return EDIT_VIEW_NAME;
        }
        if (samplingId != null) {
            samplingService.findById(samplingId).map(sampling -> {
                Sampling samplingToSave = samplingDTO.toDomain();
                samplingToSave.setId(sampling.getId());
                return samplingService.save(samplingToSave);
            }).orElseThrow(() -> new SamplingNotFoundException(samplingId));
        } else {
            samplingService.save(samplingDTO.toDomain());
        }
        sessionStatus.setComplete();

        String referer = request.getHeader("Referer");
        return REDIRECT_ALL_URL;
    }
}