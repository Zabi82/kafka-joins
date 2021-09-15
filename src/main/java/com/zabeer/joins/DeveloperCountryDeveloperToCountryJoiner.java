package com.zabeer.joins;

import com.zabeer.joins.model.Country;
import com.zabeer.joins.model.DeveloperCountryJoinResult;
import org.apache.kafka.streams.kstream.ValueJoiner;

public class DeveloperCountryDeveloperToCountryJoiner implements ValueJoiner<DeveloperCountryJoinResult, Country, DeveloperCountryJoinResult> {
    public DeveloperCountryJoinResult apply(DeveloperCountryJoinResult developerCountryJoinResult, Country country) {
        return DeveloperCountryJoinResult.newBuilder()
                .setDeveloperId(developerCountryJoinResult.getDeveloperId())
                .setDeveloperName(developerCountryJoinResult.getDeveloperName())
                .setSkill(developerCountryJoinResult.getSkill())
                .setExpLevel(developerCountryJoinResult.getExpLevel())
                .setCountryId(developerCountryJoinResult.getCountryId())
                .setRelation(developerCountryJoinResult.getRelation())
                .setCountryCode(country.getCode())
                .setCountryName(country.getDescription())
                .build();
    }
}
