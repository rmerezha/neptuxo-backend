package space.neptuxo.util;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;


class PropertiesUtilTest {

    private final PropertiesUtil properties = new PropertiesUtil();

    private static Stream<Arguments> getPropertiesData() {
        return Stream.of(
                Arguments.of("test.key1", "123"),
                Arguments.of("test.key2", ""),
                Arguments.of("test.key3", null)
        );
    }

    @ParameterizedTest
    @MethodSource("getPropertiesData")
    void get(String key, String expectedValue) {

        String actualValue = properties.get(key);

        assertEquals(expectedValue, actualValue);

    }


}
