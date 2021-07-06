package ru.mephi3.rest.hateoas;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.afford;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import ru.mephi3.domain.Property;
import ru.mephi3.rest.controller.PropertyRestController;

@Component
public class PropertyModelAssembler implements RepresentationModelAssembler<Property, EntityModel<Property>> {

	@Override
	public EntityModel<Property> toModel(Property property) {
		Link selfLink = linkTo(methodOn(PropertyRestController.class).findOne(property.getId())).withSelfRel()//
				.andAffordance(afford(methodOn(PropertyRestController.class).delete(property.getId()))) //
				.andAffordance(afford(methodOn(PropertyRestController.class).update(property.getId(), null)));
		Link propertysLink = linkTo(methodOn(PropertyRestController.class).findAll(null, null)).withRel("properties");
		return EntityModel.of(property, selfLink, propertysLink);

//		linkTo(methodOn(PropertyRestController.class).delete(property.getId())).withRel("delete")
	}
}
