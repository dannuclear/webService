package ru.mephi3.webService;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import ru.mephi3.domain.ObjectClass;
import ru.mephi3.domain.ObjectClassProperty;
import ru.mephi3.domain.Property;
import ru.mephi3.main.App;
import ru.mephi3.reposotory.ObjectClassRepository;
import ru.mephi3.service.SampleService;

@SpringBootTest
@ContextConfiguration(classes = App.class)
public class SampleTest {

	@Autowired
	private SampleService sampleService;
	@Autowired
	private ObjectClassRepository objectClassRepository;

	@Test
	@Transactional
	public void testAdd() {
		try {
			ObjectClass oc = objectClassRepository.findById(1).orElse(null);
			oc.getProperties().add(ObjectClassProperty.builder().property(Property.builder().id(1).build()).build());
			objectClassRepository.save(oc);
			
			oc = objectClassRepository.findById(oc.getId()).orElse(null);
			ObjectClassProperty ocp = oc.getProperties().stream().findFirst().orElse(null);
//			oc.setProperties(new ArrayList<>(oc.getProperties()));
			oc.getProperties().remove(ocp);
			objectClassRepository.saveAndFlush(oc);
			oc = objectClassRepository.findById(oc.getId()).orElse(null);
		} catch (Exception e) {
			e.printStackTrace();
		}

		assertEquals("123", "123");
	}
}