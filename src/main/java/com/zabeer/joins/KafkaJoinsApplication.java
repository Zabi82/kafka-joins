package com.zabeer.joins;

import com.zabeer.joins.model.Country;
import com.zabeer.joins.model.Developer;
import com.zabeer.joins.model.DeveloperCountry;
import com.zabeer.joins.model.DeveloperCountryJoinResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.KeyValueMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;


@SpringBootApplication
@EnableScheduling
@Slf4j
public class KafkaJoinsApplication {


    public static void main(String[] args) {
        SpringApplication.run(KafkaJoinsApplication.class, args);
    }


    //join between DeveloperCountry and Developer so we get intermediate join result with all Developer information along with countryId
    @Bean
    public BiFunction<KTable<String, DeveloperCountry>, KTable<String, Developer>, KStream<String, DeveloperCountryJoinResult>> developerCountryToDeveloperJoin() {

        return (devCountryTable, developer) -> devCountryTable.join(developer, (devCountryProcessed, devProcessed) -> new JoinResultHolder<>(devCountryProcessed.getDeveloperId().toString(), devCountryProcessed, devProcessed)).toStream().map(new KeyValueMapper<String, JoinResultHolder<String, DeveloperCountry, Developer>, KeyValue<? extends String, ? extends DeveloperCountryJoinResult>>() {
            @Override
            public KeyValue<? extends String, ? extends DeveloperCountryJoinResult> apply(String s, JoinResultHolder<String, DeveloperCountry, Developer> developerCountryDeveloperJoinResultHolder) {
                DeveloperCountryJoinResult joinResult = new DeveloperCountryJoinResult();
                joinResult.setDeveloperId(developerCountryDeveloperJoinResultHolder.getKey());
                joinResult.setDeveloperName(developerCountryDeveloperJoinResultHolder.getValue2().getName());
                joinResult.setCountryId(developerCountryDeveloperJoinResultHolder.getValue1().getCountryId());
                joinResult.setRelation(developerCountryDeveloperJoinResultHolder.getValue1().getRelation());
                joinResult.setExpLevel(developerCountryDeveloperJoinResultHolder.getValue2().getExperienceLevel());
                joinResult.setSkill(developerCountryDeveloperJoinResultHolder.getValue2().getSkill());
                //no nullable fields configured in avro schema, so setting default string
                joinResult.setCountryCode(" ");
                joinResult.setCountryName(" ");
                return new KeyValue<>(joinResult.getDeveloperId().toString(),joinResult);
            }
        });

    }

    //join between intermediate join result with Country Ktable to get final result
    @Bean
    public BiFunction<KTable<String, DeveloperCountryJoinResult>, KTable<String, Country>, KStream<String, DeveloperCountryJoinResult>> developerCountryDeveloperToCountryJoin() {

        final DeveloperCountryDeveloperToCountryJoiner devCountryDevToCountryJoiner = new DeveloperCountryDeveloperToCountryJoiner();
        //This foreignKeyExtractor simply uses the left-value to map to the right-key.
        Function<DeveloperCountryJoinResult, String> foreignKeyExtractor = (x) -> x.getCountryId().toString();

        return (developerCountryDeveloper, country) -> developerCountryDeveloper.join(country, foreignKeyExtractor, devCountryDevToCountryJoiner).toStream();
    }

    //Consumer to test output of joined final result
    @Bean
    public Consumer<KStream<String, DeveloperCountryJoinResult>> consumeFinalMessage() {
        return input -> {
            input.peek(((key, value) -> log.info("key for final message is {} and value {} ", key, value.toString())));
        };
    }


    private static final class JoinResultHolder<K, V1, V2> {

        private final K key;
        private final V1 value1;
        private final V2 value2;

        public JoinResultHolder(K key, V1 value1, V2 value2) {

            this.key = key;
            this.value1 = value1;
            this.value2 = value2;
        }


        public K getKey() {
            return key;
        }

        public V1 getValue1() {
            return value1;
        }

        public V2 getValue2() {
            return value2;
        }

    }


}
