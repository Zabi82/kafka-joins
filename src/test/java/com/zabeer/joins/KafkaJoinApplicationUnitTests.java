package com.zabeer.joins;

import com.zabeer.joins.model.Country;
import com.zabeer.joins.model.Developer;
import com.zabeer.joins.model.DeveloperCountry;
import com.zabeer.joins.model.DeveloperCountryJoinResult;
import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.TopologyTestDriver;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Produced;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Properties;
import java.util.function.BiFunction;

import static com.zabeer.joins.TopicConstants.*;
import static org.assertj.core.api.Assertions.assertThat;


/**
 * Unit testing using Topology Test Driver
 */
public class KafkaJoinApplicationUnitTests {


    @Test
    public void testDeveloperCountryToDeveloperJoin() {

        final StreamsBuilder streamsBuilder = new StreamsBuilder();

        Properties properties = new Properties();
        Map<String, String> propMap = Map.of(
                StreamsConfig.APPLICATION_ID_CONFIG, "join-test",
                StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "dummy:9092",
                AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, "mock://8081"
        );
        properties.putAll(propMap);

        Serde<String> stringSerde = Serdes.String();
        Serde<Developer> developerSerde = new SpecificAvroSerde();
        developerSerde.configure(propMap, false);
        Serde<DeveloperCountry> developerCountrySerde = new SpecificAvroSerde<>();
        developerCountrySerde.configure(propMap, false);
        Serde<DeveloperCountryJoinResult> developerCountryJoinResultSerde = new SpecificAvroSerde<>();
        developerCountryJoinResultSerde.configure(propMap, false);


        final KTable<String, DeveloperCountry> devCountryTable = streamsBuilder.table(DEVELOPER_COUNTRY_TOPIC, Consumed.with(stringSerde, developerCountrySerde));
        final KTable<String, Developer> devTable = streamsBuilder.table(DEVELOPER_TOPIC, Consumed.with(stringSerde, developerSerde));
        final BiFunction<KTable<String, DeveloperCountry>, KTable<String, Developer>, KStream<String, DeveloperCountryJoinResult>>  developerCountryToDeveloperJoin = new KafkaJoinsApplication().developerCountryToDeveloperJoin();

        KStream<String, DeveloperCountryJoinResult> resultStream = developerCountryToDeveloperJoin.apply(devCountryTable, devTable);

        resultStream.to(DEVELOPER_COUNTRY_TO_DEVELOPER_TOPIC, Produced.with(stringSerde, developerCountryJoinResultSerde));


        try (TopologyTestDriver testDriver = new TopologyTestDriver(streamsBuilder.build(), properties)) {
            var devCountryInputTopic =
                    testDriver.createInputTopic(DEVELOPER_COUNTRY_TOPIC, stringSerde.serializer(), developerCountrySerde.serializer());

            var devInputTopic =
                    testDriver.createInputTopic(DEVELOPER_TOPIC, stringSerde.serializer(), developerSerde.serializer());

            var devCountryToDevOutputTopic = testDriver.createOutputTopic(DEVELOPER_COUNTRY_TO_DEVELOPER_TOPIC, stringSerde.deserializer(), developerCountryJoinResultSerde.deserializer());

            DeveloperCountry devCountryObj = getDeveloperCountry();
            devCountryInputTopic.pipeInput(devCountryObj.getDeveloperId().toString(), devCountryObj);


            Developer devObj = getDeveloper();
            devInputTopic.pipeInput(devObj.getId().toString(), devObj);


            KeyValue<String, DeveloperCountryJoinResult> outputRecord = devCountryToDevOutputTopic.readKeyValue();
            assertThat(outputRecord.key).isEqualTo(devObj.getId().toString());
            assertThat(outputRecord.value.getCountryId().toString()).isEqualTo(devCountryObj.getCountryId().toString());


        }
    }

    @Test
    public void testDeveloperCountryDeveloperToCountryJoin() {

        final StreamsBuilder streamsBuilder = new StreamsBuilder();

        Properties properties = new Properties();
        Map<String, String> propMap = Map.of(
                StreamsConfig.APPLICATION_ID_CONFIG, "join-test",
                StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "dummy:9092",
                AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, "mock://8081"
        );
        properties.putAll(propMap);

        Serde<String> stringSerde = Serdes.String();
        Serde<DeveloperCountryJoinResult> developerCountryJoinResultSerde = new SpecificAvroSerde<>();
        developerCountryJoinResultSerde.configure(propMap, false);
        Serde<Country> countrySerde = new SpecificAvroSerde<>();
        countrySerde.configure(propMap, false);

        final KTable<String, DeveloperCountryJoinResult> devCountryToDevJoinResultTable = streamsBuilder.table(DEVELOPER_COUNTRY_TO_DEVELOPER_TOPIC, Consumed.with(stringSerde, developerCountryJoinResultSerde));
        final KTable<String, Country> countryTable = streamsBuilder.table(COUNTRY_TOPIC, Consumed.with(stringSerde, countrySerde));
        final BiFunction<KTable<String, DeveloperCountryJoinResult>, KTable<String, Country>, KStream<String, DeveloperCountryJoinResult>>  developerCountryDeveloperToCountryJoin = new KafkaJoinsApplication().developerCountryDeveloperToCountryJoin();

        KStream<String, DeveloperCountryJoinResult> resultStream = developerCountryDeveloperToCountryJoin.apply(devCountryToDevJoinResultTable, countryTable);

        resultStream.to(DEVELOPER_COUNTRY_DEVELOPER_TO_COUNTRY_TOPIC, Produced.with(stringSerde, developerCountryJoinResultSerde));


        try (TopologyTestDriver testDriver = new TopologyTestDriver(streamsBuilder.build(), properties)) {
            var devCountryDevInputTopic =
                    testDriver.createInputTopic(DEVELOPER_COUNTRY_TO_DEVELOPER_TOPIC, stringSerde.serializer(), developerCountryJoinResultSerde.serializer());

            var countryInputTopic =
                    testDriver.createInputTopic(COUNTRY_TOPIC, stringSerde.serializer(), countrySerde.serializer());

            var devCountryDevToCountryOutputTopic = testDriver.createOutputTopic(DEVELOPER_COUNTRY_DEVELOPER_TO_COUNTRY_TOPIC, stringSerde.deserializer(), developerCountryJoinResultSerde.deserializer());

            DeveloperCountryJoinResult developerCountryToDeveloperObj = getDeveloperCountryToDeveloper();
            devCountryDevInputTopic.pipeInput(developerCountryToDeveloperObj.getDeveloperId().toString(), developerCountryToDeveloperObj);


            Country countryObj = getCountry();
            countryInputTopic.pipeInput(countryObj.getId().toString(), countryObj);


            KeyValue<String, DeveloperCountryJoinResult> outputRecord = devCountryDevToCountryOutputTopic.readKeyValue();
            assertThat(outputRecord.key).isEqualTo(developerCountryToDeveloperObj.getDeveloperId().toString());
            assertThat(outputRecord.value.getCountryName().toString()).isEqualTo(countryObj.getDescription().toString());


        }
    }


    @NotNull
    private DeveloperCountry getDeveloperCountry() {
        DeveloperCountry devCountry = new DeveloperCountry();
        devCountry.setDeveloperId("100");
        devCountry.setCountryId("1");
        devCountry.setRelation("Citizen");
        return devCountry;
    }

    @NotNull
    private Country getCountry() {
        Country country = new Country();
        country.setId("1");
        country.setCode("IND");
        country.setCodeIso2("IN");
        country.setDescription("India");
        return country;
    }

    @NotNull
    private Developer getDeveloper() {
        Developer developer = new Developer();
        developer.setId("100");
        developer.setName("Tim");
        developer.setExperienceLevel("Beginner");
        developer.setSkill("Java");
        return developer;
    }

    @NotNull
    private DeveloperCountryJoinResult getDeveloperCountryToDeveloper() {
        DeveloperCountryJoinResult developerCountryToDeveloper = new DeveloperCountryJoinResult();
        developerCountryToDeveloper.setDeveloperId("100");
        developerCountryToDeveloper.setDeveloperName("Tim");
        developerCountryToDeveloper.setExpLevel("Beginner");
        developerCountryToDeveloper.setSkill("Java");
        developerCountryToDeveloper.setCountryId("1");
        developerCountryToDeveloper.setRelation("Citizen");
        developerCountryToDeveloper.setCountryName(" ");
        developerCountryToDeveloper.setCountryCode(" ");
        return developerCountryToDeveloper;
    }

}
