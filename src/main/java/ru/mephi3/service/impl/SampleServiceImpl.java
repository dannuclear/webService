package ru.mephi3.service.impl;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.mephi3.domain.Sample;
import ru.mephi3.reposotory.SampleRepository;
import ru.mephi3.service.SampleService;

@Service
@RequiredArgsConstructor
public class SampleServiceImpl implements SampleService {

    private final SampleRepository sampleRepository;

    @Override
    @PreAuthorize("hasAuthority('SAMPLE_EDIT') or hasAnyRole('ADMIN')")
    public Sample save(Sample sample) {
        Sample res = sampleRepository.save(sample);
        return res;
    }

    @Override
    @PreAuthorize("hasAuthority('SAMPLE_EDIT') or hasAnyRole('ADMIN')")
    public Sample create(String code) {
        Sample sample = new Sample();
        return sample;
    }

    @Override
    @PreAuthorize("hasAuthority('SAMPLE_READ') or hasRole('ADMIN')")
    public Optional<Sample> findById(Integer id) {
        Optional<Sample> optSample = sampleRepository.findById(id);
        return optSample;
    }

    @Override
    @PreAuthorize("hasAuthority('SAMPLE_READ') or hasAnyRole('ADMIN')")
    public Page<Sample> findAll(Pageable pageable) {
        return sampleRepository.findAll(pageable);
    }

    @Override
    @PreAuthorize("hasAuthority('SAMPLE_EDIT') or hasAnyRole('ADMIN')")
    public void delete(Sample sample) {
        sampleRepository.delete(sample);
    }

    @Override
    @PreAuthorize("hasAuthority('SAMPLE_READ') or hasAnyRole('ADMIN')")
    public Page<Sample> findByString(String value, Pageable pageable) {
        return sampleRepository.findByCodeContainsIgnoreCase(value, pageable);
    }

    @Override
    public Sample findBySamplename(String samplename) {
        return null;
    }
}
