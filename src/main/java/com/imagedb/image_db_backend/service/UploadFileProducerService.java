package com.imagedb.image_db_backend.service;

import com.imagedb.image_db_backend.model.MessageToKafkaForUploadFileTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class UploadFileProducerService {

    private final KafkaTemplate<String, MessageToKafkaForUploadFileTopic> kafkaTemplate;

    @Autowired
    public UploadFileProducerService(KafkaTemplate<String, MessageToKafkaForUploadFileTopic> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String topic, String email, String fileName, byte[] fileContent) {
        MessageToKafkaForUploadFileTopic message = new MessageToKafkaForUploadFileTopic(email, fileName, fileContent);
        kafkaTemplate.send(topic, message);
    }
}
