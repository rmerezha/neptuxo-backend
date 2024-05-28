package space.neptuxo.util;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import space.neptuxo.dto.UserDto;
import space.neptuxo.entity.User;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JsonParserTest {

    private final JsonParser parser = new JsonParser();

    @Test
    @SneakyThrows
    void parse() {
        String json = """
                {
                "username": "test1",
                "email":"test1@neptuxo.space",
                "passwd":"$2y$10$gCbOKzKDn4cXHftxr/X6e.aoAEYH01NeTRWbD8MmiY0gjabMqgNuK"
                }
                """;

        try (InputStream in = new ByteArrayInputStream(json.getBytes())) {
            UserDto actual = parser.parse(in, UserDto.class);

            UserDto expected = UserDto.builder()
                    .username("test1")
                    .email("test1@neptuxo.space")
                    .passwd("$2y$10$gCbOKzKDn4cXHftxr/X6e.aoAEYH01NeTRWbD8MmiY0gjabMqgNuK")
                    .build();

            assertEquals(expected, actual);
        }


    }

    @Test
    @SneakyThrows
    void parseWithException() {
        String json = """
                {
                "username": "test1",
                "email":"test1@neptuxo.space",
                "passwd":"$2y$10$gCbOKzKDn4cXHftxr/X6e.aoAEYH01NeTRWbD8MmiY0gjabMqgNuK"
                }
                """;

        try (InputStream in = new ByteArrayInputStream(json.getBytes())) {
            assertThrows(Exception.class, () -> parser.parse(in, User.class));
        }
    }
}