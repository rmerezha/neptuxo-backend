package space.neptuxo.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class JsonBuilderFactoryTest {

    @Test
    void create() {
        JsonBuilderFactory factory = new JsonBuilderFactory();
        JsonBuilder builder = factory.create();
        assertNotNull(builder);
    }
}