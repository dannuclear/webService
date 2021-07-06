package ru.mephi3.mvc.controller;

import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
import ru.mephi3.domain.*;
import ru.mephi3.dto.ObjectClassPropertyDTO;
import ru.mephi3.service.DocumentTypeService;
import ru.mephi3.service.ObjectClassPropertyService;
import ru.mephi3.service.ObjectClassService;
import ru.mephi3.service.exception.ObjectClassPropertyNotFoundException;
import ru.mephi3.web.method.support.DataTablesRequest;
import ru.mephi3.web.method.support.DataTablesResponse;

import javax.validation.Valid;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping({"/private/objectClassProperties", "/private/objectClasses/{objectClassId}/properties"})
@RequiredArgsConstructor
@SessionAttributes({"objectClassProperty", "documentTypes"})
public class ObjectClassPropertyController {
    public static final String EDIT_VIEW = "/private/objectClassProperty";
    public static final String ALL_VIEW = "/private/objectClasses";

    private final ObjectClassService objectClassService;
    private final DocumentTypeService documentTypeService;
    private final ObjectClassPropertyService objectClassPropertyService;

    @PostMapping(path = "/dataTable")
    public ResponseEntity<DataTablesResponse<ObjectClassProperty>> findPropertiesByObjectClass(@RequestBody DataTablesRequest dataTablesRequest, @PathVariable("objectClassId") ObjectClass objectClass) {
        Page<ObjectClassProperty> result = objectClassPropertyService.findByObjectClassAndFilterString(objectClass, dataTablesRequest.getSearch().getValue(), dataTablesRequest.getPageRequest());
        return ResponseEntity.ok(DataTablesResponse.of(dataTablesRequest.getDraw(), result));
    }

    @GetMapping
    public String findAll(Principal principal, Authentication authentication, SessionStatus sessionStatus) {
        sessionStatus.setComplete();
        return ALL_VIEW;
    }

    @GetMapping("{id}")
    @Transactional(readOnly = true)
    public String edit(@PathVariable("id") Integer id, Model model) {
        // Если страница обновляется то не заполнять модель, использовать сессию
        if (model.containsAttribute("objectClassProperty"))
            return EDIT_VIEW;

        ObjectClassPropertyDTO objectClassProperty = objectClassPropertyService.findById(id).map(ocp -> {
            Hibernate.initialize(ocp.getDocuments());
            return ObjectClassPropertyDTO.fromDomain(ocp);
        }).orElseThrow(() -> new ObjectClassPropertyNotFoundException((id)));

        objectClassProperty.setObjectClassTree(objectClassService.getObjectClassTreeString(objectClassProperty.getObjectClassId()));

        model.addAttribute("objectClassProperty", objectClassProperty);
        addDocumentTypesAttribute(model);

        return EDIT_VIEW;
    }

    @GetMapping("/new")
    public String add(@RequestParam(required = false) Integer objectClassId, Model model) {
        ObjectClassPropertyDTO objectClassProperty = ObjectClassPropertyDTO.createDefault();
        if (objectClassId != null) {
            objectClassProperty.setObjectClassTree(objectClassService.getObjectClassTreeString(objectClassId));
            objectClassProperty.setObjectClassId(objectClassId);
        }
        model.addAttribute("objectClassProperty", objectClassProperty);
        addDocumentTypesAttribute(model);

        return EDIT_VIEW;
    }

    @PostMapping("delete/{id}")
    public ModelAndView delete(@PathVariable("id") Integer id, ModelAndView mv) {
        objectClassPropertyService.findById(id).map(ocp -> {
            objectClassPropertyService.delete(ocp);
            return ocp;
        }).orElseThrow(() -> new ObjectClassPropertyNotFoundException(id));
        mv.setStatus(HttpStatus.NO_CONTENT);
        mv.setViewName("redirect:" + ALL_VIEW);
        return mv;
    }

    @PostMapping
    @Transactional
    public ModelAndView save(@ModelAttribute("objectClassProperty") @Valid ObjectClassPropertyDTO objectClassPropertyDTO, BindingResult bindingResult, SessionStatus sessionStatus, ModelAndView mv) {
        if (bindingResult.hasErrors()) {
            mv.setStatus(HttpStatus.BAD_REQUEST);
            mv.setViewName(EDIT_VIEW);
            return mv;
        }
        if (objectClassPropertyDTO.getId() == null) {
            objectClassPropertyService.save(objectClassPropertyDTO.toDomain());
        } else {
            objectClassPropertyService.findById(objectClassPropertyDTO.getId()).map(ocp -> {
                ObjectClassProperty objectClassProperty = objectClassPropertyDTO.toDomain();
                Hibernate.initialize(ocp.getDocuments());
                return objectClassPropertyService.save(objectClassProperty);
            }).orElseThrow(() -> new ObjectClassPropertyNotFoundException(objectClassPropertyDTO.getId()));
        }
        sessionStatus.setComplete();
        mv.setViewName("redirect:" + ALL_VIEW);
        return mv;
    }

    private void addDocumentTypesAttribute(Model model) {
        model.addAttribute("documentTypes", documentTypeService.findAll(PageRequest.of(0, 99999)).getContent());
    }

    @PostMapping(path = "{propertyId}/documentsByType/{type}/delete/{idx}")
    public String deleteDocument(@PathVariable("idx") Integer documentIdx, @PathVariable("type") Integer documentTypeId, @ModelAttribute("objectClassProperty") ObjectClassPropertyDTO objectClassProperty, Model model) {
        DocumentType dt = DocumentType.builder().id(documentTypeId).build();
        objectClassProperty.getDocumentsByType().get(dt).remove(documentIdx.intValue());
        return EDIT_VIEW;
    }

    @PostMapping(path = "{propertyId}/documentsByType/{type}/add")
    public String addDocument(@PathVariable("type") Integer documentTypeId, @ModelAttribute("objectClassProperty") ObjectClassPropertyDTO objectClassProperty,  Model model) {
        DocumentType dt = DocumentType.builder().id(documentTypeId).build();
        objectClassProperty.getDocumentsByType().get(dt).add(ObjectClassPropertyDocument.builder().build());
        model.addAttribute("objectClassPropertyDocument", ObjectClassPropertyDocument.builder().build());
        return "private/objectClassPropertyDocument";
    }
}