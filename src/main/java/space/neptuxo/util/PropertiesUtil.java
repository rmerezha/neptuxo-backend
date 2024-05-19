package space.neptuxo.util;

import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.util.Properties;

@UtilityClass
public class PropertiesUtil {

    private static final Properties properties = new Properties();

    private static final String PROPERTIES_PATH = "application.properties";

    static {
        loadProperties();
    }

    private static void loadProperties() {
        var resourceAsStream = PropertiesUtil.class.getClassLoader().getResourceAsStream(PROPERTIES_PATH);
        try (resourceAsStream) {
            properties.load(resourceAsStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String get(String key) {
        return properties.getProperty(key);
    }


}
