package ru.mephi3.mvc.controller;

import java.security.Principal;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.ModelAndView;
import ru.mephi3.domain.Document;
import ru.mephi3.domain.Sample;
import ru.mephi3.dto.DocumentDTO;
import ru.mephi3.mvc.utils.ModelUtils;
import ru.mephi3.service.DocumentService;
import ru.mephi3.service.exception.DocumentNotFoundException;
import ru.mephi3.service.exception.SampleNotFoundException;
import ru.mephi3.web.method.support.DataTablesRequest;
import ru.mephi3.web.method.support.DataTablesResponse;

@Controller
@RequestMapping("/private/documents")
@RequiredArgsConstructor
@SessionAttributes({"document"})
public class DocumentController {
    public static final String editView = "/private/document";
    public static final String allView = "/private/documents";
    public static final String redirectEditView = "redirect:" + editView;
    public static final String redirectAllView = "redirect:" + allView;

    private final DocumentService documentService;

    @PostMapping("/dataTable")
    public ResponseEntity<DataTablesResponse<Document>> findPageable(@RequestBody DataTablesRequest dataTablesRequest) {
        Page<Document> documentsPage = documentService.findByFilter(dataTablesRequest.getSearch().getValue(), dataTablesRequest.getPageRequest());
        return ResponseEntity.ok(DataTablesResponse.of(dataTablesRequest.getDraw(), documentsPage));
    }

    @GetMapping
    public String findAll(Principal principal, Authentication authentication, SessionStatus sessionStatus) {
        sessionStatus.setComplete();
        return allView;
    }

    @GetMapping("{documentId}")
    public String edit(@PathVariable("documentId") Integer id, Model model) {
        DocumentDTO document = documentService.findById(id).map(doc -> {
            return DocumentDTO.fromDomain(doc);
        }).orElseThrow(() -> new DocumentNotFoundException(id));
        ModelUtils.addIfNotExist(model, "document", document);
        model.addAttribute("readOnly", false);
        return editView;
    }

    @GetMapping("/new")
    public String add(Model model) {
        ModelUtils.addIfNotExist(model, "document", DocumentDTO.createDefault());
        return editView;
    }

    @GetMapping("{documentId}/delete")
    public ModelAndView delete(@PathVariable("documentId") Integer id, ModelAndView mv) {
        documentService.findById(id).map(document -> {
            documentService.delete(document);
            return document;
        }).orElseThrow(() -> new DocumentNotFoundException(id));

        mv.setStatus(HttpStatus.NO_CONTENT);
        mv.setViewName(redirectAllView);
        return mv;
    }

    @PostMapping
    public String save(@ModelAttribute("document") @Valid DocumentDTO documentDTO, BindingResult bindingResult, SessionStatus sessionStatus) {
        if (bindingResult.hasErrors()) {
            return editView;
        }

        Document document = documentDTO.toDomain();
        if (document.getId() != null) {
            documentService.findById(document.getId()).map(dbDoc -> {
                return documentService.save(document);
            }).orElseThrow(() -> new DocumentNotFoundException(document.getId()));
        } else {
            documentService.save(document);
        }
        sessionStatus.setComplete();
        return redirectAllView;
    }
}