package com.rallies.business.services.impl;

import com.rallies.dataaccess.api.RallyRepository;
import models.Rally;

import java.util.Collection;

public class RallyService {
    private RallyRepository rallyRepository;

    public RallyService(RallyRepository rallyRepository) {
        this.rallyRepository = rallyRepository;
    }

    public Rally addNewRally(int engineCapacity) {
        Rally rally = new Rally(engineCapacity);
        return rallyRepository.save(rally);
    }

    public Collection<? extends Rally> findAllRallies() {
        return rallyRepository.findAll();
    }
}
