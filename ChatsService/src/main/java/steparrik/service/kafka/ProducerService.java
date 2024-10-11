package steparrik.service.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import steparrik.dto.analytics.VisitTimeDto;
import steparrik.dto.message.MessageDTO;
import steparrik.utils.mapper.analytics.SerializerAnalyticsData;

import java.time.ZoneOffset;

@Service
@RequiredArgsConstructor
public class ProducerService {
    private final SerializerAnalyticsData serializerAnalyticsData;

    private final KafkaTemplate<String, Object> kafkaTemplate;
    public void sendMessage(MessageDTO message) {
        kafkaTemplate.send("message-topic", message);
    }

    public void sendTimeToAnalyticsService(VisitTimeDto message)  {
        kafkaTemplate.send("visit-time-topic", serializerAnalyticsData.serializeVisitTimeDto(message));
    }


}
