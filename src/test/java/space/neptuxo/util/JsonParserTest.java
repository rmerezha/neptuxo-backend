package space.neptuxo.util;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import space.neptuxo.dto.CreateUserDto;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

class JsonParserTest {

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
            CreateUserDto actual = JsonParser.parse(in, CreateUserDto.class);

            CreateUserDto expected = CreateUserDto.builder()
                    .username("test1")
                    .email("test1@neptuxo.space")
                    .passwd("$2y$10$gCbOKzKDn4cXHftxr/X6e.aoAEYH01NeTRWbD8MmiY0gjabMqgNuK")
                    .build();

            assertEquals(expected, actual);
        }


    }
}