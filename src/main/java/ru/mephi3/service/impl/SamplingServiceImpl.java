package ru.mephi3.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import ru.mephi3.domain.Sampling;
import ru.mephi3.reposotory.SamplingRepository;
import ru.mephi3.service.SamplingService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SamplingServiceImpl implements SamplingService {

    private final SamplingRepository samplingRepository;

    @Override
    @PreAuthorize("hasAuthority('SAMPLING_EDIT') or hasAnyRole('ADMIN')")
    public Sampling save(Sampling sample) {
        Sampling res = samplingRepository.save(sample);
        return res;
    }

    @Override
    @PreAuthorize("hasAuthority('SAMPLING_EDIT') or hasAnyRole('ADMIN')")
    public Sampling create(String number) {
        return Sampling.builder().number(number).build();
    }

    @Override
    @PreAuthorize("hasAuthority('SAMPLING_READ') or hasRole('ADMIN')")
    public Optional<Sampling> findById(Integer id) {
        Optional<Sampling> optSampling = samplingRepository.findById(id);
        return optSampling;
    }

    @Override
    @PreAuthorize("hasAuthority('SAMPLING_READ') or hasAnyRole('ADMIN')")
    public Page<Sampling> findAll(Pageable pageable) {
        return samplingRepository.findAll(pageable);
    }

    @Override
    @PreAuthorize("hasAuthority('SAMPLING_EDIT') or hasAnyRole('ADMIN')")
    public void delete(Sampling sample) {
        samplingRepository.delete(sample);
    }

    @Override
    @PreAuthorize("hasAuthority('SAMPLING_READ') or hasAnyRole('ADMIN')")
    public Page<Sampling> findByString(String value, Pageable pageable) {
        return samplingRepository.findAll(pageable);
    }
}
