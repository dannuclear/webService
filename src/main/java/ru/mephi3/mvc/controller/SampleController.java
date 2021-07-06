package ru.mephi3.mvc.controller;

import java.security.Principal;

import javax.validation.Valid;

import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.ModelAndView;
import ru.mephi3.domain.Sample;
import ru.mephi3.dto.SampleDTO;
import ru.mephi3.service.SampleService;
import ru.mephi3.service.exception.SampleNotFoundException;

@Controller
@RequestMapping("/private/samples")
@RequiredArgsConstructor
//@SessionAttributes({ "sample", "permissions", "roles" })
@Log4j2
public class SampleController {
    public static final String editPage = "/private/sample";
    public static final String allPage = "/private/samples";

    private final SampleService sampleService;

    @GetMapping
    public String findAll(Principal principal, Authentication authentication, SessionStatus sessionStatus) {
        sessionStatus.setComplete();
        return allPage;
    }

    @GetMapping("/{sampleId}")
    public String edit(@PathVariable("sampleId") Integer sampleId, Model model) {
        log.debug("edit sample with id {}", sampleId);
        SampleDTO sampleDTO = sampleService.findById(sampleId).map(sml -> {
            return SampleDTO.fromDomain(sml);
        }).orElseThrow(() -> new SampleNotFoundException(sampleId));

        model.addAttribute("sample", sampleDTO);
        return editPage;
    }

    @GetMapping("/new")
    public String create(Model model) {
        model.addAttribute("sample", SampleDTO.createDefault());

        return editPage;
    }

    @GetMapping("/delete/{sampleId}")
    public ModelAndView delete(@PathVariable("sampleId") Integer sampleId, ModelAndView mv) {
        sampleService.findById(sampleId).map(sample -> {
            sampleService.delete(sample);
            return sample;
        }).orElseThrow(() -> new SampleNotFoundException(sampleId));

        mv.setStatus(HttpStatus.NO_CONTENT);
        mv.setViewName("redirect:" + allPage);
        return mv;
    }

    @PostMapping({"{sampleId}", ""})
    public String save(@PathVariable(required = false) Integer sampleId, @ModelAttribute("sample") @Valid SampleDTO sampleDTO, BindingResult bindingResult, SessionStatus sessionStatus) {
        if (bindingResult.hasErrors()) {
            return editPage;
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
        return "redirect:" + allPage;
    }
}