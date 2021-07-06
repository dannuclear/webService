package ru.mephi3.rest.controller;

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

import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.mephi3.domain.Document;
import ru.mephi3.domain.Property;
import ru.mephi3.rest.hateoas.DocumentModelAssembler;
import ru.mephi3.rest.hateoas.PropertyModelAssembler;
import ru.mephi3.service.DocumentService;
import ru.mephi3.service.exception.PropertyNotFoundException;
import ru.mephi3.web.method.support.DataTablesRequest;
import ru.mephi3.web.method.support.DataTablesResponse;

@RestController
@RequestMapping("/api/v1/documents")
@RequiredArgsConstructor
public class DocumentRestController {

	private final DocumentService documentService;
	private final PagedResourcesAssembler<Document> pagedResourcesAssembler;
	private final DocumentModelAssembler documentModelAssembler;

	@GetMapping
	public HttpEntity<PagedModel<EntityModel<Document>>> findAll(Pageable pageable, @RequestParam(required = false) String search) {
		Page<Document> documentPage = null;
		if (StringUtils.hasText(search))
			documentPage = documentService.findByFilter(search, pageable);
		else
			documentPage = documentService.findAll(pageable);
		Link selfLink = Link.of(ServletUriComponentsBuilder.fromCurrentRequest().build().toUriString());
		return ResponseEntity.ok(pagedResourcesAssembler.toModel(documentPage, documentModelAssembler, selfLink));
	}

	@GetMapping(path = "{id}")
	public HttpEntity<?> findOne(@PathVariable Integer id) {
		return documentService.findById(id).map(property -> ResponseEntity.ok(documentModelAssembler.toModel(property))).orElseThrow(() -> new PropertyNotFoundException(id));
	}

}