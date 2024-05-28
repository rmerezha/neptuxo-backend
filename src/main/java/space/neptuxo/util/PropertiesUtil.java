package space.neptuxo.util;

import java.io.IOException;
import java.util.Properties;

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

    public String get(String key) {
        return properties.getProperty(key);
    }


}
