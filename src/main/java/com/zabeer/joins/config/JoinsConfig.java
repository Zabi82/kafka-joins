package com.zabeer.joins.config;

import com.zabeer.joins.model.Country;
import com.zabeer.joins.model.Developer;
import com.zabeer.joins.model.DeveloperCountry;
import com.zabeer.joins.model.DeveloperCountryJoinResult;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import org.apache.kafka.common.serialization.Serde;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class JoinsConfig {


    @Bean
    public Serde<Country> avroCountryInSerde() {
        final SpecificAvroSerde<Country> avroInSerde = new SpecificAvroSerde<>();
        Map<String, Object> serdeProperties = new HashMap<>();
        return avroInSerde;
    }

    @Bean
    public Serde<Developer> avroDeveloperInSerde() {
        final SpecificAvroSerde<Developer> avroInSerde = new SpecificAvroSerde<>();
        Map<String, Object> serdeProperties = new HashMap<>();
        return avroInSerde;
    }

    @Bean
    public Serde<DeveloperCountry> avroDeveloperCountryInSerde() {
        final SpecificAvroSerde<DeveloperCountry> avroInSerde = new SpecificAvroSerde<>();
        Map<String, Object> serdeProperties = new HashMap<>();
        return avroInSerde;
    }

    @Bean
    public Serde<DeveloperCountryJoinResult> avroDeveloperCountryJoinResultInSerde() {
        final SpecificAvroSerde<DeveloperCountryJoinResult> avroInSerde = new SpecificAvroSerde<>();
        Map<String, Object> serdeProperties = new HashMap<>();
        return avroInSerde;
    }
}
