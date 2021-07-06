package ru.mephi3.reposotory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import ru.mephi3.domain.Sample;

@Transactional(readOnly = true)
public interface SampleRepository extends JpaRepository<Sample, Integer> {

}
