package space.neptuxo.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import space.neptuxo.dto.UserDto;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JsonBuilderTest {

    private final JsonBuilder jsonBuilder = new JsonBuilder();
    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void setStatus() throws JsonProcessingException {

        String actual = jsonBuilder.setStatus(Status.FAIL).build();

        String expected = "{\"status\": \"fail\"}";


        JsonNode actualNode = mapper.readTree(actual);
        JsonNode expectedNode = mapper.readTree(expected);
        assertEquals(expectedNode, actualNode);

    }

    @Test
    void setDataString() throws JsonProcessingException {

        String actual = jsonBuilder.setData("key1", "value1").build();

        String expected = "{\"data\": {\"key1\": \"value1\"}}";

        JsonNode actualNode = mapper.readTree(actual);
        JsonNode expectedNode = mapper.readTree(expected);
        assertEquals(expectedNode, actualNode);

    }

    @Test
    void setDataInt() throws JsonProcessingException {

        String actual = jsonBuilder.setData("key1", 1).build();

        String expected = "{\"data\": {\"key1\":1}}";

        JsonNode actualNode = mapper.readTree(actual);
        JsonNode expectedNode = mapper.readTree(expected);
        assertEquals(expectedNode, actualNode);

    }

    @Test
    void setDataList() throws JsonProcessingException {

        List<UserDto> list = List.of(UserDto.builder().username("user1").email("gmail.com").passwd("qwerty").build(),
                UserDto.builder().id(25).username("user2").email("gmail").passwd("qwerty").build());

        String actual = jsonBuilder.setData("list", list).build();

        String expected = "{\"data\":{\"list\":[{\"id\": 0,\"username\":\"user1\",\"email\":\"gmail.com\",\"passwd\":\"qwerty\"}," +
                          "{\"id\": 25,\"username\":\"user2\",\"email\":\"gmail\",\"passwd\":\"qwerty\"}]}}";

        JsonNode actualNode = mapper.readTree(actual);
        JsonNode expectedNode = mapper.readTree(expected);
        assertEquals(expectedNode, actualNode);

    }

    @Test
    void setMessage() throws JsonProcessingException {

        String actual = jsonBuilder.setMessage("message test").build();

        String expected = "{\"message\":\"message test\"}";

        JsonNode actualNode = mapper.readTree(actual);
        JsonNode expectedNode = mapper.readTree(expected);
        assertEquals(expectedNode, actualNode);

    }

    @Test
    void setErrors() throws JsonProcessingException {

        var list = List.of(Error.USER_EXIST, Error.LOGIN);

        String actual = jsonBuilder.setErrors(list).build();

        String expected = "{\"errors\":[{\"code\":1,\"message\":\"User with this username or email already exists\"},{\"code\":2,\"message\":\"User is already logged in\"}]}";

        JsonNode actualNode = mapper.readTree(actual);
        JsonNode expectedNode = mapper.readTree(expected);
        assertEquals(expectedNode, actualNode);
    }
}