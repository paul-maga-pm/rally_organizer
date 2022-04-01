package com.rallies.dataaccess.repository.api;

import models.Team;

import java.util.Optional;

public interface TeamRepository extends Repository<Long, Team> {
    Optional<Team> findTeamByName(String teamName);
}
