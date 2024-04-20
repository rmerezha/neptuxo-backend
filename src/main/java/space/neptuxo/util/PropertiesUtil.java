package space.neptuxo.util;

import java.io.IOException;
import java.util.Properties;

public final class PropertiesUtil {

    private PropertiesUtil() {}

    private static final Properties properties = new Properties();

    static {
        loadProperties();
    }

    private static void loadProperties() {
        var resourceAsStream = PropertiesUtil.class.getClassLoader().getResourceAsStream("application.properties");
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
