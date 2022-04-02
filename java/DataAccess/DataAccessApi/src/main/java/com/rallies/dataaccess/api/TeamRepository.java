package com.rallies.dataaccess.api;

import com.rallies.domain.models.Team;

import java.util.Optional;

public interface TeamRepository extends Repository<Long, Team> {
    Optional<Team> getTeamByName(String teamName);
}
