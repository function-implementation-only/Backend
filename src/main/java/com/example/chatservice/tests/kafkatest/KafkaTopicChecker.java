package com.example.chatservice.tests.kafkatest;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.clients.admin.TopicListing;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class KafkaTopicChecker {

    public static boolean topicExists(String bootstrapServers, String topicName) throws ExecutionException, InterruptedException {
        Properties props = new Properties();
        props.setProperty(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);

        try (AdminClient adminClient = AdminClient.create(props)) {
            ListTopicsResult listTopicsResult = adminClient.listTopics();
            for (TopicListing topicListing : listTopicsResult.listings().get()) {
                if (topicListing.name().equals(topicName)) {
                    return true;
                }
            }
            return false;
        }
    }

}