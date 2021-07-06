package ru.mephi3.rest.controller;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ru.mephi3.domain.ObjectClass;
import ru.mephi3.domain.ObjectClassProperty;
import ru.mephi3.dto.JSTreeNode;
import ru.mephi3.service.ObjectClassPropertyService;
import ru.mephi3.service.ObjectClassService;
import ru.mephi3.web.method.support.DataTablesRequest;
import ru.mephi3.web.method.support.DataTablesResponse;

@RestController
@RequestMapping("/api/v1/objectClasses")
@RequiredArgsConstructor
public class ObjectClassRestController {
	private final ObjectClassService objectClassService;
	private final ObjectClassPropertyService objectClassPropertyService;

	@GetMapping(path = "{objectClassId}/properties/{propertyId}")
	public ResponseEntity<ObjectClassProperty> findPropertyById(@PathVariable("objectClassId") ObjectClass objectClass, @PathVariable("propertyId") Integer propertyId) {
		ObjectClassProperty objectClassProperty = objectClassPropertyService.findById(propertyId).orElse(null);
		return ResponseEntity.ok(objectClassProperty);
	}
}