package ru.mephi3.rest.hateoas;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import ru.mephi3.domain.DocumentType;
import ru.mephi3.domain.Equipment;
import ru.mephi3.rest.controller.DocumentRestController;
import ru.mephi3.rest.controller.EquipmentRestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class EquipmentModelAssembler implements RepresentationModelAssembler<Equipment, EntityModel<Equipment>> {

    @Override
    public EntityModel<Equipment> toModel(Equipment equipment) {
        Link selfLink = linkTo(methodOn(DocumentRestController.class).findOne(equipment.getId())).withSelfRel();//
//				.andAffordance(afford(methodOn(DocumentRestController.class).delete(document.getId()))) //
//				.andAffordance(afford(methodOn(DocumentRestController.class).update(document.getId(), null)));
        Link documentsLink = linkTo(methodOn(EquipmentRestController.class).findAll(null, null)).withRel("properties");
        return EntityModel.of(equipment, selfLink, documentsLink);

//		linkTo(methodOn(PropertyRestController.class).delete(property.getId())).withRel("delete")
    }
}
