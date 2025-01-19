package com.imagedb.image_db_backend.service;

import com.imagedb.image_db_backend.model.MessageToKafkaForDeleteFileTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class DeleteFileProducerService {

    private final KafkaTemplate<String, MessageToKafkaForDeleteFileTopic> kafkaTemplate;

    @Autowired
    public DeleteFileProducerService(KafkaTemplate<String, MessageToKafkaForDeleteFileTopic> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String topic, String email, String fileName) {
        MessageToKafkaForDeleteFileTopic message = new MessageToKafkaForDeleteFileTopic(email, fileName);
        kafkaTemplate.send(topic, message);
    }
}
