package service;

import interfaces.RallyRepository;
import models.Rally;

public class RallyService {
    private RallyRepository rallyRepository;

    public RallyService(RallyRepository rallyRepository) {
        this.rallyRepository = rallyRepository;
    }

    public Rally addNewRally(int engineCapacity) {
        Rally rally = new Rally(engineCapacity);
        return rallyRepository.save(rally);
    }

    public Iterable<Rally> findAllRallies() {
        return rallyRepository.findAll();
    }
}
