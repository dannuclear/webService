package ru.mephi3.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.mephi3.domain.Sampling;

import java.util.Optional;

public interface SamplingService {

	Sampling create(String number);

	Sampling save(Sampling sample);

	Optional<Sampling> findById(Integer id);

	Page<Sampling> findAll(Pageable pageable);

	Page<Sampling> findByString(String value, Pageable pageable);

	void delete(Sampling sample);
}
