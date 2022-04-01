package com.rallies.business.impl;

import com.rallies.dataaccess.api.RallyRepository;
import com.rallies.domain.models.Rally;

import java.util.Collection;

public class RallyService {
    private RallyRepository rallyRepository;

    public RallyService(RallyRepository rallyRepository) {
        this.rallyRepository = rallyRepository;
    }

    public Rally addRally(int engineCapacity) {
        Rally rally = new Rally(engineCapacity);
        return rallyRepository.save(rally);
    }

    public Collection<? extends Rally> getAllRallies() {
        return rallyRepository.findAll();
    }
}
