package space.neptuxo.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class PropertiesUtilTest {

    @Test
    public void testGetProperty() {
        assertAll(
                () -> assertEquals("123", PropertiesUtil.get("test.key1")),
                () -> assertEquals("", PropertiesUtil.get("test.key2"))
                );
    }




}
