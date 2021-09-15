package com.zabeer.joins;

import com.zabeer.joins.model.Country;
import com.zabeer.joins.model.Developer;
import com.zabeer.joins.model.DeveloperCountry;
import com.zabeer.joins.model.DeveloperCountryJoinResult;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.awaitility.Awaitility;
import org.awaitility.Duration;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.zabeer.joins.TopicConstants.*;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration test using Embedded Kafka and Mock Schema Registry
 */


@SpringBootTest
@EmbeddedKafka(topics = {COUNTRY_TOPIC, DEVELOPER_TOPIC, DEVELOPER_COUNTRY_TOPIC, DEVELOPER_COUNTRY_TO_DEVELOPER_TOPIC, DEVELOPER_COUNTRY_DEVELOPER_TO_COUNTRY_TOPIC},
        partitions = 1,
        bootstrapServersProperty = "spring.kafka.bootstrap-servers")
public class KafkaJoinApplicationIntegrationTests {


    @Test
    void testJoin(@Autowired EmbeddedKafkaBroker embeddedKafka) {
        Map<String, Object> senderProps = KafkaTestUtils.producerProps(embeddedKafka);
        senderProps.put("key.serializer", StringSerializer.class);
        senderProps.put("value.serializer", KafkaAvroSerializer.class);
        senderProps.put("schema.registry.url", "mock://8081");


        DefaultKafkaProducerFactory<String, DeveloperCountry> devCountryPf = new DefaultKafkaProducerFactory<>(senderProps);

        KafkaTemplate<String, DeveloperCountry> devCountryTemplate = new KafkaTemplate<>(devCountryPf, true);
        devCountryTemplate.setDefaultTopic(DEVELOPER_COUNTRY_TOPIC);
        DeveloperCountry devCountry = getDeveloperCountry();
        ProducerRecord<String, DeveloperCountry> devCountryRecord = new ProducerRecord<>(DEVELOPER_COUNTRY_TOPIC, devCountry.getDeveloperId().toString(), devCountry);
        devCountryTemplate.send(devCountryRecord);

        DefaultKafkaProducerFactory<String, Developer> devPf = new DefaultKafkaProducerFactory<>(senderProps);

        KafkaTemplate<String, Developer> devTemplate = new KafkaTemplate<>(devPf, true);
        devTemplate.setDefaultTopic(DEVELOPER_TOPIC);
        Developer developer = getDeveloper();
        ProducerRecord<String, Developer> devRecord = new ProducerRecord<>(DEVELOPER_TOPIC, developer.getId().toString(), developer);
        devTemplate.send(devRecord);


        DefaultKafkaProducerFactory<String, Country> countryPf = new DefaultKafkaProducerFactory<>(senderProps);

        KafkaTemplate<String, Country> countryTemplate = new KafkaTemplate<>(countryPf, true);
        countryTemplate.setDefaultTopic(COUNTRY_TOPIC);
        Country country = getCountry();
        ProducerRecord<String, Country> countryRecord = new ProducerRecord<>(COUNTRY_TOPIC, country.getId().toString(), country);
        countryTemplate.send(countryRecord);

        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps(GROUP_NAME, "false", embeddedKafka);
        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        consumerProps.put("key.deserializer", StringDeserializer.class);
        consumerProps.put("value.deserializer", KafkaAvroDeserializer.class);
        consumerProps.put("schema.registry.url", "mock://8081");

        consumerProps.put(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, true);


        DefaultKafkaConsumerFactory<String, DeveloperCountryJoinResult> cf = new DefaultKafkaConsumerFactory<>(consumerProps);

        Consumer<String, DeveloperCountryJoinResult> intermediateTopicConsumer = cf.createConsumer();
        intermediateTopicConsumer.assign(Collections.singleton(new TopicPartition(DEVELOPER_COUNTRY_TO_DEVELOPER_TOPIC, 0)));
        List<ConsumerRecord<String, DeveloperCountryJoinResult>> intermediateJoinResults = new ArrayList<>();

        long assertStartTimeForIntermediateTopic = System.currentTimeMillis();
        Awaitility.await()
                .atMost(Duration.TWO_MINUTES)
                .pollInterval(Duration.TWO_HUNDRED_MILLISECONDS).until(() -> {
            intermediateTopicConsumer.poll(java.time.Duration.ofMillis(50))
                    .iterator()
                    .forEachRemaining(intermediateJoinResults::add);
            intermediateTopicConsumer.commitSync();
            return intermediateJoinResults.size() > 0;
        });

        assertThat(intermediateJoinResults.size()).isGreaterThan(0);

        DeveloperCountryJoinResult intermediateResult = intermediateJoinResults.get(0).value();
        assertThat(intermediateResult.getDeveloperId().toString()).isEqualTo(developer.getId().toString());
        assertThat(intermediateResult.getCountryId().toString()).isEqualTo(country.getId().toString());
        assertThat(intermediateResult.getDeveloperName().toString()).isEqualTo(developer.getName().toString());
        assertThat(intermediateResult.getRelation().toString()).isEqualTo(devCountry.getRelation().toString());

        System.out.printf("Time taken for reading intermediate topic %d ms", System.currentTimeMillis() - assertStartTimeForIntermediateTopic);


        Consumer<String, DeveloperCountryJoinResult> finalTopicConsumer = cf.createConsumer();
        finalTopicConsumer.assign(Collections.singleton(new TopicPartition(DEVELOPER_COUNTRY_DEVELOPER_TO_COUNTRY_TOPIC, 0)));
        List<ConsumerRecord<String, DeveloperCountryJoinResult>> finalJoinResults = new ArrayList<>();



        long assertStartTimeForFinalTopic = System.currentTimeMillis();
        Awaitility.await()
                .atMost(Duration.TWO_MINUTES)
                .pollInterval(Duration.TWO_HUNDRED_MILLISECONDS).until(() -> {
            finalTopicConsumer.poll(java.time.Duration.ofMillis(50))
                    .iterator()
                    .forEachRemaining(finalJoinResults::add);
            finalTopicConsumer.commitSync();
            return finalJoinResults.size() > 0;
        });

        assertThat(finalJoinResults.size()).isGreaterThan(0);

        DeveloperCountryJoinResult finalResult = finalJoinResults.get(0).value();
        assertThat(finalResult.getDeveloperId().toString()).isEqualTo(developer.getId().toString());
        assertThat(finalResult.getCountryCode().toString()).isEqualTo(country.getCode().toString());
        assertThat(finalResult.getDeveloperName().toString()).isEqualTo(developer.getName().toString());
        assertThat(finalResult.getRelation().toString()).isEqualTo(devCountry.getRelation().toString());

        System.out.printf("Time taken for reading final topic %d ms", System.currentTimeMillis() - assertStartTimeForFinalTopic);

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
    private Country getCountry() {
        Country country = new Country();
        country.setId("1");
        country.setCode("IND");
        country.setCodeIso2("IN");
        country.setDescription("India");
        return country;
    }

    @NotNull
    private DeveloperCountry getDeveloperCountry() {
        DeveloperCountry devCountry = new DeveloperCountry();
        devCountry.setDeveloperId("100");
        devCountry.setCountryId("1");
        devCountry.setRelation("Citizen");
        return devCountry;
    }


}