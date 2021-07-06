package ru.mephi3.rest.hateoas;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import ru.mephi3.domain.EquipmentType;
import ru.mephi3.rest.controller.DocumentRestController;
import ru.mephi3.rest.controller.EquipmentTypeRestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class EquipmentTypeModelAssembler implements RepresentationModelAssembler<EquipmentType, EntityModel<EquipmentType>> {

    @Override
    public EntityModel<EquipmentType> toModel(EquipmentType equipmentType) {
        Link selfLink = linkTo(methodOn(DocumentRestController.class).findOne(equipmentType.getId())).withSelfRel();//
//				.andAffordance(afford(methodOn(DocumentRestController.class).delete(equipmentType.getId()))) //
//				.andAffordance(afford(methodOn(DocumentRestController.class).update(equipmentType.getId(), null)));
        Link equipmentTypesLink = linkTo(methodOn(EquipmentTypeRestController.class).findAll(null, null)).withRel("properties");
        return EntityModel.of(equipmentType, selfLink, equipmentTypesLink);

//		linkTo(methodOn(PropertyRestController.class).delete(property.getId())).withRel("delete")
    }
}
