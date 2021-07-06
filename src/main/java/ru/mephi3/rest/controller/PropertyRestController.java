package ru.mephi3.rest.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import lombok.RequiredArgsConstructor;
import ru.mephi3.domain.Property;
import ru.mephi3.rest.hateoas.PropertyModelAssembler;
import ru.mephi3.service.PropertyService;
import ru.mephi3.service.exception.PropertyNotFoundException;
import ru.mephi3.web.method.support.DataTablesRequest;
import ru.mephi3.web.method.support.DataTablesResponse;

@RestController
@RequestMapping("/api/v1/properties")
@RequiredArgsConstructor
public class PropertyRestController {

	private final PropertyService propertyService;
	private final PagedResourcesAssembler<Property> pagedResourcesAssembler;
	private final PropertyModelAssembler propertyModelAssembler;

	@PostMapping
	public ResponseEntity<DataTablesResponse<Property>> forDataTables(@RequestBody DataTablesRequest dataTablesRequest) {
		Page<Property> propertysPage = propertyService.findAll(dataTablesRequest.getPageRequest());
		return ResponseEntity.ok(DataTablesResponse.of(dataTablesRequest.getDraw(), propertysPage));
	}

	@GetMapping
	public HttpEntity<PagedModel<EntityModel<Property>>> findAll(Pageable pageable, @RequestParam(required = false) String search) {
		Page<Property> propertyPage = null;
		if (StringUtils.hasText(search))
			propertyPage = propertyService.findByFilter(search, pageable);
		else
			propertyPage = propertyService.findAll(pageable);
		Link selfLink = Link.of(ServletUriComponentsBuilder.fromCurrentRequest().build().toUriString());
		return ResponseEntity.ok(pagedResourcesAssembler.toModel(propertyPage, propertyModelAssembler, selfLink));
	}

	@GetMapping(path = "{id}")
	public HttpEntity<?> findOne(@PathVariable Integer id) {
		return propertyService.findById(id).map(property -> ResponseEntity.ok(propertyModelAssembler.toModel(property))).orElseThrow(() -> new PropertyNotFoundException(id));
	}

	@DeleteMapping("{id}")
	public HttpEntity<?> delete(@PathVariable Integer id) {
		Property property = propertyService.findById(id).orElseThrow(() -> new PropertyNotFoundException(id));
		propertyService.delete(property);

//		  return ResponseEntity //
//			      .status(HttpStatus.METHOD_NOT_ALLOWED) //
//			      .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE) //
//			      .body(Problem.create() //
//			          .withTitle("Method not allowed") //
//			          .withDetail("You can't complete an order that is in the " + order.getStatus() + " status"));
//		  
		return ResponseEntity.noContent().build();
	}

	@PutMapping("{id}")
	public HttpEntity<?> update(@PathVariable Integer id, @RequestBody Property newProperty) {
		return propertyService.findById(id).map(property -> {
			newProperty.setId(property.getId());
			return propertyService.save(newProperty);
		}).map(property -> ResponseEntity.ok(propertyModelAssembler.toModel(property))).orElseThrow(() -> new PropertyNotFoundException(id));
	}
}