package ru.mephi3.rest.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.mephi3.domain.DocumentType;
import ru.mephi3.rest.hateoas.DocumentTypeModelAssembler;
import ru.mephi3.service.DocumentTypeService;
import ru.mephi3.service.exception.DocumentTypeNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/documentTypes")
@RequiredArgsConstructor
public class DocumentTypeRestController {

    private final DocumentTypeService documentTypeService;
    private final PagedResourcesAssembler<DocumentType> pagedResourcesAssembler;
    private final DocumentTypeModelAssembler documentTypeModelAssembler;

    @GetMapping
    public HttpEntity<PagedModel<EntityModel<DocumentType>>> findAll(Pageable pageable, @RequestParam(required = false) String search) {
        Page<DocumentType> documentTypePage = null;
        if (StringUtils.hasText(search))
            documentTypePage = documentTypeService.findByString(search, pageable);
        else
            documentTypePage = documentTypeService.findAll(pageable);
        Link selfLink = Link.of(ServletUriComponentsBuilder.fromCurrentRequest().build().toUriString());
        return ResponseEntity.ok(pagedResourcesAssembler.toModel(documentTypePage, documentTypeModelAssembler, selfLink));
    }

    @GetMapping(path = "{id:\\d+}")
    public HttpEntity<?> findOne(@PathVariable Integer id) {
        return documentTypeService.findById(id).map(property -> ResponseEntity.ok(documentTypeModelAssembler.toModel(property))).orElseThrow(() -> new DocumentTypeNotFoundException(id));
    }

    @GetMapping(path = "{ids:(?>\\d+,)+\\d+}")
    public HttpEntity<?> findByIds(@PathVariable List<Integer> ids) {
        return ResponseEntity.ok(documentTypeService.findByIds(ids).stream().map(property -> documentTypeModelAssembler.toModel(property)).collect(Collectors.toList()));
    }
}