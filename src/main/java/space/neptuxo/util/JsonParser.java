package space.neptuxo.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.io.InputStream;

@UtilityClass
public class JsonParser {

    @SneakyThrows
    public static <T> T parse(InputStream in, Class<T> clazz) {

        var objMapper = new ObjectMapper();

        return objMapper.readValue(in, clazz);
    }

}
