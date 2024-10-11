package steparrik.utils.mapper.analytics;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import steparrik.dto.analytics.VisitTimeDto;

import java.time.ZoneOffset;

@Component
public class SerializerAnalyticsData {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ObjectNode serializeVisitTimeDto(VisitTimeDto dto) {
        ObjectNode schemaNode = objectMapper.createObjectNode();
        schemaNode.put("type", "struct");

        ObjectNode fieldsNode = objectMapper.createObjectNode();
        fieldsNode.put("type", "int64").put("field", "userId");
        ObjectNode timeFieldNode = objectMapper.createObjectNode();
        timeFieldNode.put("type", "int64").put("field", "time");

        ArrayNode fieldsArray = objectMapper.createArrayNode();
        fieldsArray.add(fieldsNode);
        fieldsArray.add(timeFieldNode);

        schemaNode.set("fields", fieldsArray);
        schemaNode.put("optional", false);
        schemaNode.put("name", "my.avro.VisitTimeDto");

        ObjectNode payloadNode = objectMapper.createObjectNode();
        payloadNode.put("userId", dto.getUserId());
        payloadNode.put("time", dto.getTime().toInstant(ZoneOffset.UTC).toEpochMilli());

        ObjectNode messageNode = objectMapper.createObjectNode();
        messageNode.set("schema", schemaNode);
        messageNode.set("payload", payloadNode);

        return messageNode;
    }
}
