package space.neptuxo.util;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.rmerezha.di.model.BeanWithMocks;
import space.neptuxo.repository.AbstractUserRepository;
import space.neptuxo.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.*;

class DependencyInjectorTest {

    @Test
    public void getBean() {
        AbstractUserRepository repository = DependencyInjector.getBean(AbstractUserRepository.class);

        assertNotNull(repository);

        assertEquals(repository.getClass(), UserRepository.class);

    }

    @Test
    public void getBeanForTest() {
        BeanWithMocks<AbstractUserRepository> bean = DependencyInjector.getBeanForTest(AbstractUserRepository.class);

        assertNotNull(bean.bean());

        assertEquals(bean.bean().getClass(), UserRepository.class);

        bean.mocks().values().forEach(m -> assertTrue(Mockito.mockingDetails(m).isMock()));
    }
}