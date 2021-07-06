package ru.mephi3.rest.hateoas;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.afford;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import ru.mephi3.domain.Sample;
import ru.mephi3.rest.controller.SampleRestController;

@Component
public class SampleModelAssembler implements RepresentationModelAssembler<Sample, EntityModel<Sample>> {

	@Override
	public EntityModel<Sample> toModel(Sample sample) {
		Link selfLink = linkTo(methodOn(SampleRestController.class).findOne(sample.getId())).withSelfRel()//
				.andAffordance(afford(methodOn(SampleRestController.class).delete(sample.getId()))) //
				.andAffordance(afford(methodOn(SampleRestController.class).update(sample.getId(), null)));
		Link samplesLink = linkTo(methodOn(SampleRestController.class).findAll(null)).withRel("samples");
		return EntityModel.of(sample, selfLink, samplesLink);

//		linkTo(methodOn(SampleRestController.class).delete(sample.getId())).withRel("delete")
	}
}
