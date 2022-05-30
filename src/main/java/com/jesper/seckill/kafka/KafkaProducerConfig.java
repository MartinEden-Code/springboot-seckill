//package com.jesper.seckill.kafka;
//
//import org.apache.kafka.clients.producer.ProducerConfig;
//import org.apache.kafka.common.serialization.StringSerializer;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.kafka.annotation.EnableKafka;
//import org.springframework.kafka.core.DefaultKafkaProducerFactory;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.kafka.core.ProducerFactory;
//
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * @author hongxingyi
// * @description TODO 配置kafkatemplate 模板类
// * @date 2022/5/24 23:25
// */
//@Configuration
//@EnableKafka
//public class KafkaProducerConfig {
//
//    @Value("${spring.kafka.bootstrap-servers}")
//    private String servers;
//    @Value("${spring.kafka.producer.retries}")
//    private int retries;
//    @Value("${spring.kafka.producer.batch-size}")
//    private int batchSize;
//    @Value("${spring.kafka.producer.properties.linger}")
//    private int linger;
//    @Value("${spring.kafka.producer.buffer-memory}")
//    private int bufferMemory;
//
//
//    public Map<String, Object> producerConfigs() {
//        Map<String, Object> props = new HashMap<>();
//        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
//        props.put(ProducerConfig.RETRIES_CONFIG, retries);
//        props.put(ProducerConfig.BATCH_SIZE_CONFIG, batchSize);
//        props.put(ProducerConfig.LINGER_MS_CONFIG, linger);
//        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, bufferMemory);
//        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//        return props;
//    }
//
//    public ProducerFactory<String, String> producerFactory() {
//        return new DefaultKafkaProducerFactory<>(producerConfigs());
//    }
//
//    /**
//     * 配置kafkatemplate 模板类
//     * @return
//     */
//    @Bean
//    public KafkaTemplate<String, String> kafkaTemplate() {
//        return new KafkaTemplate<String, String>(producerFactory());
//    }
//}
