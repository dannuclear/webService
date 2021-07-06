package ru.mephi3.rest.hateoas;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import ru.mephi3.domain.Document;
import ru.mephi3.domain.Property;
import ru.mephi3.rest.controller.DocumentRestController;
import ru.mephi3.rest.controller.PropertyRestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class DocumentModelAssembler implements RepresentationModelAssembler<Document, EntityModel<Document>> {

	@Override
	public EntityModel<Document> toModel(Document document) {
		Link selfLink = linkTo(methodOn(DocumentRestController.class).findOne(document.getId())).withSelfRel();//
//				.andAffordance(afford(methodOn(DocumentRestController.class).delete(document.getId()))) //
//				.andAffordance(afford(methodOn(DocumentRestController.class).update(document.getId(), null)));
		Link documentsLink = linkTo(methodOn(DocumentRestController.class).findAll(null, null)).withRel("properties");
		return EntityModel.of(document, selfLink, documentsLink);

//		linkTo(methodOn(PropertyRestController.class).delete(property.getId())).withRel("delete")
	}
}
