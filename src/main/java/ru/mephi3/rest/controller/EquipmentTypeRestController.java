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
import ru.mephi3.domain.EquipmentType;
import ru.mephi3.rest.hateoas.EquipmentTypeModelAssembler;
import ru.mephi3.service.EquipmentTypeService;
import ru.mephi3.service.exception.PropertyNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/equipmentTypes")
@RequiredArgsConstructor
public class EquipmentTypeRestController {

	private final EquipmentTypeService equipmentTypeService;
	private final PagedResourcesAssembler<EquipmentType> pagedResourcesAssembler;
	private final EquipmentTypeModelAssembler equipmentTypeModelAssembler;

	@GetMapping
	public HttpEntity<PagedModel<EntityModel<EquipmentType>>> findAll(Pageable pageable, @RequestParam(required = false) String search) {
		Page<EquipmentType> equipmentTypePage = null;
		if (StringUtils.hasText(search))
			equipmentTypePage = equipmentTypeService.findByFilter(search, pageable);
		else
			equipmentTypePage = equipmentTypeService.findAll(pageable);
		Link selfLink = Link.of(ServletUriComponentsBuilder.fromCurrentRequest().build().toUriString()).withSelfRel();
		return ResponseEntity.ok(pagedResourcesAssembler.toModel(equipmentTypePage, equipmentTypeModelAssembler, selfLink));
	}

	@GetMapping(path = "{id:\\d+}")
	public HttpEntity<?> findOne(@PathVariable Integer id) {
		return equipmentTypeService.findById(id).map(property -> ResponseEntity.ok(equipmentTypeModelAssembler.toModel(property))).orElseThrow(() -> new PropertyNotFoundException(id));
	}

	@GetMapping(path = "{ids:(?>\\d+,)+\\d+}")
	public HttpEntity<?> findByIds(@PathVariable List<Integer> ids) {
		return ResponseEntity.ok(equipmentTypeService.findByIds(ids).stream().map(property -> equipmentTypeModelAssembler.toModel(property)).collect(Collectors.toList()));
	}
}