package ru.mephi3.mvc.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.web.format.WebConversionService;
import org.springframework.core.convert.ConversionService;
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
import ru.mephi3.domain.Document;
import ru.mephi3.domain.DocumentType;
import ru.mephi3.dto.DocumentDTO;
import ru.mephi3.dto.DocumentTypeDTO;
import ru.mephi3.mvc.utils.ModelUtils;
import ru.mephi3.service.DocumentService;
import ru.mephi3.service.DocumentTypeService;
import ru.mephi3.service.exception.DocumentNotFoundException;
import ru.mephi3.service.exception.DocumentTypeNotFoundException;
import ru.mephi3.web.method.support.DataTablesRequest;
import ru.mephi3.web.method.support.DataTablesResponse;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequestMapping("/private/documentTypes")
@RequiredArgsConstructor
@SessionAttributes({"documentType"})
@Log4j2
public class DocumentTypeController {
    public static final String editView = "/private/documentType";
    public static final String allView = "/private/documentTypes";
    public static final String redirectEditView = "redirect:" + editView;
    public static final String redirectAllView = "redirect:" + allView;

    private final DocumentTypeService documentTypeService;

    @PostMapping("/dataTable")
    public ResponseEntity<DataTablesResponse<DocumentType>> findPageable(@RequestBody DataTablesRequest dataTablesRequest) {
        log.debug("Извлечение данных для DataTables");
        Page<DocumentType> documentTypesPage = documentTypeService.findByString(dataTablesRequest.getSearch().getValue(), dataTablesRequest.getPageRequest());
        return ResponseEntity.ok(DataTablesResponse.of(dataTablesRequest.getDraw(), documentTypesPage));
    }

    @GetMapping
    public String findAll(Principal principal, Authentication authentication, SessionStatus sessionStatus) {
        sessionStatus.setComplete();
        return allView;
    }

    @GetMapping("{documentTypeId}")
    public String edit(@PathVariable("documentTypeId") Integer id, Model model) {
        DocumentTypeDTO documentType = documentTypeService.findById(id).map(doc -> {
            return DocumentTypeDTO.fromDomain(doc);
        }).orElseThrow(() -> new DocumentTypeNotFoundException(id));
        ModelUtils.addIfNotExist(model, "documentType", documentType);
        return editView;
    }

    @GetMapping("/new")
    public String add(Model model) {
        ModelUtils.addIfNotExist(model, "documentType", DocumentTypeDTO.createDefault());
        return editView;
    }

    @GetMapping("{documentTypeId}/delete")
    public ModelAndView delete(@PathVariable("documentTypeId") Integer id, ModelAndView mv) {
        documentTypeService.findById(id).map(documentType -> {
            documentTypeService.delete(documentType);
            return documentType;
        }).orElseThrow(() -> new DocumentTypeNotFoundException(id));

        mv.setStatus(HttpStatus.NO_CONTENT);
        mv.setViewName(redirectAllView);
        return mv;
    }

    @PostMapping
    public String save(@ModelAttribute("documentType") @Valid DocumentTypeDTO documentTypeDTO, BindingResult bindingResult, SessionStatus sessionStatus) {
        if (bindingResult.hasErrors()) {
            return editView;
        }

        DocumentType documentType = documentTypeDTO.toDomain();
        if (documentType.getId() != null) {
            documentTypeService.findById(documentType.getId()).map(dbDoc -> {
                return documentTypeService.save(documentType);
            }).orElseThrow(() -> new DocumentTypeNotFoundException(documentType.getId()));
        } else {
            documentTypeService.save(documentType);
        }
        sessionStatus.setComplete();
        return redirectAllView;
    }
}