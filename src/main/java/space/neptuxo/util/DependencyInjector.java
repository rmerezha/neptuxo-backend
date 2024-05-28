package space.neptuxo.util;

import org.rmerezha.di.configuration.Configuration;
import org.rmerezha.di.configuration.JavaConfiguration;
import org.rmerezha.di.context.ApplicationContext;
import org.rmerezha.di.model.BeanWithMocks;

public class DependencyInjector {

    private static final ApplicationContext context;

    static {
        context = new ApplicationContext(loadConfiguration());
    }

    private static Configuration loadConfiguration() {
        return new JavaConfiguration()
                .configure();
    }

    public static <T> T getBean(Class<T> clazz) {
        return context.getBean(clazz);
    }

    public static <T> BeanWithMocks<T> getBeanForTest(Class<T> clazz) {
        return context.getBeanForTest(clazz);
    }
}
