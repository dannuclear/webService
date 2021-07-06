package ru.mephi3.rest.hateoas;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import ru.mephi3.domain.Document;
import ru.mephi3.domain.DocumentType;
import ru.mephi3.rest.controller.DocumentRestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class DocumentTypeModelAssembler implements RepresentationModelAssembler<DocumentType, EntityModel<DocumentType>> {

	@Override
	public EntityModel<DocumentType> toModel(DocumentType documentType) {
		Link selfLink = linkTo(methodOn(DocumentRestController.class).findOne(documentType.getId())).withSelfRel();//
//				.andAffordance(afford(methodOn(DocumentRestController.class).delete(document.getId()))) //
//				.andAffordance(afford(methodOn(DocumentRestController.class).update(document.getId(), null)));
		Link documentsLink = linkTo(methodOn(DocumentRestController.class).findAll(null, null)).withRel("properties");
		return EntityModel.of(documentType, selfLink, documentsLink);

//		linkTo(methodOn(PropertyRestController.class).delete(property.getId())).withRel("delete")
	}
}
