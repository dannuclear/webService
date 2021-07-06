package ru.mephi3.rest.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import lombok.RequiredArgsConstructor;
import ru.mephi3.domain.Sample;
import ru.mephi3.rest.hateoas.SampleModelAssembler;
import ru.mephi3.service.SampleService;
import ru.mephi3.service.exception.SampleNotFoundException;
import ru.mephi3.web.method.support.DataTablesRequest;
import ru.mephi3.web.method.support.DataTablesResponse;

@RestController
@RequestMapping("/api/v1/samples")
@RequiredArgsConstructor
public class SampleRestController {

	private final SampleService sampleService;
	private final PagedResourcesAssembler<Sample> pagedResourcesAssembler;
	private final SampleModelAssembler sampleModelAssembler;

	@PostMapping
	public ResponseEntity<DataTablesResponse<Sample>> findPageable(@RequestBody DataTablesRequest dataTablesRequest) {
		Page<Sample> samplesPage = sampleService.findAll(dataTablesRequest.getPageRequest());
		return ResponseEntity.ok(DataTablesResponse.of(dataTablesRequest.getDraw(), samplesPage));
	}

	@GetMapping
	public HttpEntity<PagedModel<EntityModel<Sample>>> findAll(Pageable pageable) {
		Page<Sample> samplePage = sampleService.findAll(pageable);
		Link selfLink = Link.of(ServletUriComponentsBuilder.fromCurrentRequest().build().toUriString());
		return ResponseEntity.ok(pagedResourcesAssembler.toModel(samplePage, sampleModelAssembler, selfLink));
	}

	@GetMapping(path = "{id}")
	public HttpEntity<?> findOne(@PathVariable Integer id) {
		return sampleService.findById(id).map(sample -> ResponseEntity.ok(sampleModelAssembler.toModel(sample))).orElseThrow(() -> new SampleNotFoundException(id));
	}

	@DeleteMapping("{id}")
	public HttpEntity<?> delete(@PathVariable Integer id) {
		Sample sample = sampleService.findById(id).orElseThrow(() -> new SampleNotFoundException(id));
		sampleService.delete(sample);

//		  return ResponseEntity //
//			      .status(HttpStatus.METHOD_NOT_ALLOWED) //
//			      .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE) //
//			      .body(Problem.create() //
//			          .withTitle("Method not allowed") //
//			          .withDetail("You can't complete an order that is in the " + order.getStatus() + " status"));
//		  
		return ResponseEntity.ok().build();
	}

	@PutMapping("{id}")
	public HttpEntity<?> update(@PathVariable Integer id, @RequestBody Sample newSample) {
		return sampleService.findById(id).map(sample -> {
			newSample.setId(sample.getId());
			return sampleService.save(newSample);
		}).map(sample -> ResponseEntity.ok(sampleModelAssembler.toModel(sample))).orElseThrow(() -> new SampleNotFoundException(id));
	}
}