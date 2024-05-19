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

    private final static String STATUS_KEY = "status";
    private final static String MESSAGE_KEY = "message";
    private final static String CODE_KEY = "key";
    private final static String DATA_KEY = "data";
    private final static String ERRORS_KEY = "errors";



    public JsonBuilder setStatus(Status status) {
        rootNode.put(STATUS_KEY, status.getType());
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
        rootNode.put(MESSAGE_KEY, message);
        return this;
    }

    public JsonBuilder setErrors(List<Error> errors) {
        ArrayNode errorsNode = mapper.createArrayNode();
        errors.forEach(e -> {
            ObjectNode error = mapper.createObjectNode();
            JsonNode errorCode = mapper.valueToTree(e.getCode());
            JsonNode errorMessage = mapper.valueToTree(e.getMessage());
            error.set(CODE_KEY, errorCode);
            error.set(MESSAGE_KEY, errorMessage);
            errorsNode.add(error);
        });
        rootNode.set(ERRORS_KEY, errorsNode);
        return this;
    }

    public String build() {
        if (!dataNode.isEmpty()) {
            rootNode.set(DATA_KEY, dataNode);
        }
        return rootNode.toString();
    }
}
