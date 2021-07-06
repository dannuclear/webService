package ru.mephi3.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ru.mephi3.domain.Sample;

public interface SampleService {

	Sample create(String code);

	Sample findBySamplename(String samplename);

	Sample save(Sample sample);

	Optional<Sample> findById(Integer id);

	Page<Sample> findAll(Pageable pageable);

	Page<Sample> findByString(String value, Pageable pageable);

	void delete(Sample sample);
}
