package com.jmel.cryonotes;

import com.jmel.cryonotes.models.Sample;
import com.jmel.cryonotes.models.data.SampleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SampleService {
    @Autowired
    private SampleRepository sampleRepository;

    public List<Sample> getSamplesMatching(String keyword) {
        // TODO make keyword nonnullable
        return sampleRepository.search(keyword);
    }
}
