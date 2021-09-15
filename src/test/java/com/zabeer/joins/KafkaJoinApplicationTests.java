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

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@EmbeddedKafka(topics = {KafkaJoinApplicationTests.COUNTRY_TOPIC, KafkaJoinApplicationTests.DEVELOPER_TOPIC, KafkaJoinApplicationTests.DEVELOPER_COUNTRY_TOPIC, KafkaJoinApplicationTests.DEVELOPER_COUNTRY_TO_DEVEOPER_TOPIC, KafkaJoinApplicationTests.DEVELOPER_COUNTRY_DEVELOPER_TO_COUNTRY_TOPIC},
        partitions = 1,
        bootstrapServersProperty = "spring.kafka.bootstrap-servers")
public class KafkaJoinApplicationTests {

    public static final String COUNTRY_TOPIC = "country";
    public static final String DEVELOPER_TOPIC = "developer";
    public static final String DEVELOPER_COUNTRY_TOPIC = "developer-country";
    public static final String DEVELOPER_COUNTRY_TO_DEVEOPER_TOPIC = "developercountry-to-developer";
    public static final String DEVELOPER_COUNTRY_DEVELOPER_TO_COUNTRY_TOPIC = "developercountrydeveloper-to-country";
    public static final String GROUP_NAME = "testGrp";


    @Test
    void testJoin(@Autowired EmbeddedKafkaBroker embeddedKafka) {
        Map<String, Object> senderProps = KafkaTestUtils.producerProps(embeddedKafka);
        senderProps.put("key.serializer", StringSerializer.class);
        senderProps.put("value.serializer", KafkaAvroSerializer.class);
        senderProps.put("schema.registry.url", "mock://8081");


        DefaultKafkaProducerFactory<String, DeveloperCountry> devCountryPf = new DefaultKafkaProducerFactory<>(senderProps);

        KafkaTemplate<String, DeveloperCountry> devCountryTemplate = new KafkaTemplate<>(devCountryPf, true);
        devCountryTemplate.setDefaultTopic(DEVELOPER_COUNTRY_TOPIC);
        DeveloperCountry devCountry = new DeveloperCountry();
        devCountry.setDeveloperId("100");
        devCountry.setCountryId("1");
        devCountry.setRelation("Citizen");
        ProducerRecord<String, DeveloperCountry> devCountryRecord = new ProducerRecord<>(DEVELOPER_COUNTRY_TOPIC, devCountry.getDeveloperId().toString(), devCountry);
        devCountryTemplate.send(devCountryRecord);

        DefaultKafkaProducerFactory<String, Developer> devPf = new DefaultKafkaProducerFactory<>(senderProps);

        KafkaTemplate<String, Developer> devTemplate = new KafkaTemplate<>(devPf, true);
        devTemplate.setDefaultTopic(DEVELOPER_TOPIC);
        Developer developer = new Developer();
        developer.setId("100");
        developer.setName("Tim");
        developer.setExperienceLevel("Beginner");
        developer.setSkill("Java");
        ProducerRecord<String, Developer> devRecord = new ProducerRecord<>(DEVELOPER_TOPIC, developer.getId().toString(), developer);
        devTemplate.send(devRecord);


        DefaultKafkaProducerFactory<String, Country> countryPf = new DefaultKafkaProducerFactory<>(senderProps);

        KafkaTemplate<String, Country> countryTemplate = new KafkaTemplate<>(countryPf, true);
        countryTemplate.setDefaultTopic(COUNTRY_TOPIC);
        Country country = new Country();
        country.setId("1");
        country.setCode("IND");
        country.setCodeIso2("IN");
        country.setDescription("India");
        ProducerRecord<String, Country> countryRecord = new ProducerRecord<>(COUNTRY_TOPIC, country.getId().toString(), country);
        countryTemplate.send(countryRecord);

        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps(GROUP_NAME, "false", embeddedKafka);
        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        consumerProps.put("key.deserializer", StringDeserializer.class);
        consumerProps.put("value.deserializer", KafkaAvroDeserializer.class);
        consumerProps.put("schema.registry.url", "mock://8081");

        consumerProps.put(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, true);
        DefaultKafkaConsumerFactory<String, DeveloperCountryJoinResult> cf = new DefaultKafkaConsumerFactory<>(consumerProps);

        Consumer<String, DeveloperCountryJoinResult> consumer = cf.createConsumer();
        consumer.assign(Collections.singleton(new TopicPartition(DEVELOPER_COUNTRY_DEVELOPER_TO_COUNTRY_TOPIC, 0)));


        List<ConsumerRecord<String, DeveloperCountryJoinResult>> allRecords = new ArrayList<>();
        long assertStartTime = System.currentTimeMillis();
        Awaitility.await()
                .atMost(Duration.TWO_MINUTES)
                .pollInterval(Duration.TWO_HUNDRED_MILLISECONDS).until(() -> {
            consumer.poll(java.time.Duration.ofMillis(50))
                    .iterator()
                    .forEachRemaining(allRecords::add);
            consumer.commitSync();
            return allRecords.size() > 0;
        });

        assertThat(allRecords.size()).isEqualTo(1);

        DeveloperCountryJoinResult finalResult = allRecords.get(0).value();
        assertThat(finalResult.getDeveloperId().toString()).isEqualTo(developer.getId().toString());
        assertThat(finalResult.getCountryCode().toString()).isEqualTo(country.getCode().toString());
        assertThat(finalResult.getDeveloperName().toString()).isEqualTo(developer.getName().toString());
        assertThat(finalResult.getRelation().toString()).isEqualTo(devCountry.getRelation().toString());

        long assertEndTime = System.currentTimeMillis();

        System.out.printf("Time taken  %d ms", assertEndTime - assertStartTime);

    }


}