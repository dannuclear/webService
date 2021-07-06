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
import ru.mephi3.domain.Equipment;
import ru.mephi3.rest.hateoas.EquipmentModelAssembler;
import ru.mephi3.service.EquipmentService;
import ru.mephi3.service.exception.PropertyNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/equipments")
@RequiredArgsConstructor
public class EquipmentRestController {

	private final EquipmentService equipmentService;
	private final PagedResourcesAssembler<Equipment> pagedResourcesAssembler;
	private final EquipmentModelAssembler equipmentModelAssembler;

	@GetMapping
	public HttpEntity<PagedModel<EntityModel<Equipment>>> findAll(Pageable pageable, @RequestParam(required = false) String search) {
		Page<Equipment> equipmentPage = null;
		if (StringUtils.hasText(search))
			equipmentPage = equipmentService.findByFilter(search, pageable);
		else
			equipmentPage = equipmentService.findAll(pageable);
		Link selfLink = Link.of(ServletUriComponentsBuilder.fromCurrentRequest().build().toUriString()).withSelfRel();
		return ResponseEntity.ok(pagedResourcesAssembler.toModel(equipmentPage, equipmentModelAssembler, selfLink));
	}

	@GetMapping(path = "{id:\\d+}")
	public HttpEntity<?> findOne(@PathVariable Integer id) {
		return equipmentService.findById(id).map(property -> ResponseEntity.ok(equipmentModelAssembler.toModel(property))).orElseThrow(() -> new PropertyNotFoundException(id));
	}

	@GetMapping(path = "{ids:(?>\\d+,)+\\d+}")
	public HttpEntity<?> findByIds(@PathVariable List<Integer> ids) {
		return ResponseEntity.ok(equipmentService.findByIds(ids).stream().map(property -> equipmentModelAssembler.toModel(property)).collect(Collectors.toList()));
	}
}