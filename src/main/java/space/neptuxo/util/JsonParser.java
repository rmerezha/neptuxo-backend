package space.neptuxo.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import java.io.InputStream;

public class JsonParser {

    @SneakyThrows
    public <T> T parse(InputStream in, Class<T> clazz) {

        var objMapper = new ObjectMapper();

        return objMapper.readValue(in, clazz);
    }

}
