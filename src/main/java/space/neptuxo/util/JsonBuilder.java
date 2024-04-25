package space.neptuxo.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.List;

public class JsonBuilder {


    private final ObjectMapper mapper = new ObjectMapper();
    private final ObjectNode rootNode = mapper.createObjectNode();

    private final ObjectNode dataNode = mapper.createObjectNode();

    public JsonBuilder setStatus(Status status) {
        rootNode.put("status", status.getType());
        return this;
    }

    public JsonBuilder setData(String key, String val) {
        dataNode.put(key, val);
        return this;
    }

    public JsonBuilder setData(String key, int val) {
        dataNode.put(key, val);
        return this;
    }

    public JsonBuilder setData(String key, List<?> vals) {
        JsonNode listNode = mapper.valueToTree(vals);
        dataNode.set(key, listNode);
        return this;
    }

    public JsonBuilder setMessage(String message) {
        rootNode.put("message", message);
        return this;
    }

    public JsonBuilder setErrors(List<Error> errors) {
        ArrayNode errorsNode = mapper.createArrayNode();
        errors.forEach(e -> {
            ObjectNode error = mapper.createObjectNode();
            JsonNode errorCode = mapper.valueToTree(e.getCode());
            JsonNode errorMessage = mapper.valueToTree(e.getMessage());
            error.set("code", errorCode);
            error.set("message", errorMessage);
            errorsNode.add(error);
        });
        rootNode.set("errors", errorsNode);
        return this;
    }

    public byte[] build() {
        if (!dataNode.isEmpty()) {
            rootNode.set("data", dataNode);
        }
        return rootNode.toString().getBytes();
    }
}
