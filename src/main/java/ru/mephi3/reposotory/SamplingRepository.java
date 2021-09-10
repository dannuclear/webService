package ru.mephi3.reposotory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import ru.mephi3.domain.Sample;
import ru.mephi3.domain.Sampling;


@Transactional(readOnly = true)
public interface SamplingRepository extends JpaRepository<Sampling, Integer> {
//    Page<Sample> findByNum(String string, Pageable pageable);
}
