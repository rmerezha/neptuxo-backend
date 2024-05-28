package space.neptuxo.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.rmerezha.di.model.BeanWithMocks;
import space.neptuxo.dto.*;
import space.neptuxo.entity.Product;
import space.neptuxo.exception.SQLConstraintException;
import space.neptuxo.repository.AbstractUserRepository;
import space.neptuxo.util.DependencyInjector;
import space.neptuxo.util.JsonBuilder;
import space.neptuxo.util.JsonBuilderFactory;
import space.neptuxo.util.JsonParser;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private UserService service;
    private AbstractUserRepository repository;
    private JsonParser jsonParser;
    private JsonBuilder jsonBuilder;

    @BeforeEach
    void setUp() {
        BeanWithMocks<UserService> beanForTest = DependencyInjector.getBeanForTest(UserService.class);
        service = beanForTest.bean();
        repository = (AbstractUserRepository) beanForTest.mocks().get(AbstractUserRepository.class);
        JsonBuilderFactory jsonBuilderFactory = (JsonBuilderFactory) beanForTest.mocks().get(JsonBuilderFactory.class);
        jsonBuilder = mock(JsonBuilder.class);
        jsonParser = (JsonParser) beanForTest.mocks().get(JsonParser.class);
        doReturn(jsonBuilder).when(jsonBuilderFactory).create();
    }

    @Test
    void registrationSuccess() {
        String json = """
                {
                  "id": 1,
                  "username": "testuser",
                  "email": "test@example.com",
                  "passwd": "password"
                }
                """;
        UserDto userDto = UserDto.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .passwd("password")
                .build();
        String expected = "{\"status\":\"success\"}";

        when(jsonParser.parse(any(InputStream.class), eq(UserDto.class))).thenReturn(userDto);
        when(jsonBuilder.setStatus(any())).thenReturn(jsonBuilder);
        when(jsonBuilder.build()).thenReturn(expected);

        assertEquals(expected, service.registration(new ByteArrayInputStream(json.getBytes())));
    }

    @Test
    void registrationFailUserExist() {
        String json = """
                {
                  "id": 1,
                  "username": "testuser",
                  "email": "test@example.com",
                  "passwd": "password"
                }
                """;
        UserDto userDto = UserDto.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .passwd("password")
                .build();
        String expected = "{\"status\":\"fail\",\"errors\":[\"USER_EXIST\"]}";

        when(jsonParser.parse(any(InputStream.class), eq(UserDto.class))).thenReturn(userDto);
        doThrow(SQLConstraintException.class).when(repository).save(userDto);
        when(jsonBuilder.setStatus(any())).thenReturn(jsonBuilder);
        when(jsonBuilder.setErrors(any())).thenReturn(jsonBuilder);
        when(jsonBuilder.build()).thenReturn(expected);

        assertEquals(expected, service.registration(new ByteArrayInputStream(json.getBytes())));
    }

    @Test
    void loginSuccess() {
        String json = """
                {
                  "id": 1,
                  "username": "testuser",
                  "email": "test@example.com",
                  "passwd": "password"
                }
                """;
        UserDto userDto = UserDto.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .passwd("password")
                .build();
        ReadUserDto readUserDto = new ReadUserDto(1L, "testuser", "test@example.com");
        String expectedJson = "{\"status\":\"success\"}";
        LoginDto expected = new LoginDto(Optional.of(readUserDto), expectedJson);

        when(jsonParser.parse(any(InputStream.class), eq(UserDto.class))).thenReturn(userDto);
        when(repository.findByEmailAndPasswd(userDto.email(), userDto.passwd())).thenReturn(Optional.of(readUserDto));
        when(jsonBuilder.setStatus(any())).thenReturn(jsonBuilder);
        when(jsonBuilder.build()).thenReturn(expectedJson);

        assertEquals(expected, service.login(new ByteArrayInputStream(json.getBytes())));
    }

    @Test
    void loginFailWrongEmailOrPasswd() {
        String json = """
                {
                  "id": 1,
                  "username": "testuser",
                  "email": "test@example.com",
                  "passwd": "password"
                }
                """;
        UserDto userDto = UserDto.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .passwd("password")
                .build();
        String expectedJson = "{\"status\":\"fail\",\"errors\":[\"WRONG_EMAIL_OR_PASSWD\"]}";
        LoginDto expected = new LoginDto(Optional.empty(), expectedJson);

        when(jsonParser.parse(any(InputStream.class), eq(UserDto.class))).thenReturn(userDto);
        when(repository.findByEmailAndPasswd(userDto.email(), userDto.passwd())).thenReturn(Optional.empty());
        when(jsonBuilder.setStatus(any())).thenReturn(jsonBuilder);
        when(jsonBuilder.setErrors(any())).thenReturn(jsonBuilder);
        when(jsonBuilder.build()).thenReturn(expectedJson);

        assertEquals(expected, service.login(new ByteArrayInputStream(json.getBytes())));
    }

    @Test
    void getProfileSuccess() {
        String username = "testuser";
        ReadUserDto userDto = new ReadUserDto(1L, "testuser", "test@example.com");
        List<Product> products = List.of(Product.builder()
                .id(1)
                .name("product1")
                .build());
        ProfileDto profileDto = new ProfileDto(Optional.of(userDto), products);
        String expected = "{\"status\":\"success\",\"id\":1,\"username\":\"testuser\",\"products\":[{\"id\":1,\"name\":\"product1\"}]}";

        when(repository.findProfile(username)).thenReturn(profileDto);
        when(jsonBuilder.setStatus(any())).thenReturn(jsonBuilder);
        when(jsonBuilder.setData(anyString(), any(String.class))).thenReturn(jsonBuilder);
        when(jsonBuilder.setData(anyString(), any(Long.class))).thenReturn(jsonBuilder);
        when(jsonBuilder.build()).thenReturn(expected);

        assertEquals(expected, service.getProfile(username));
    }

    @Test
    void getProfileFailProfileNotFound() {
        String username = "testuser";
        ProfileDto profileDto = new ProfileDto(Optional.empty(), List.of());
        String expected = "{\"status\":\"fail\",\"errors\":[\"PROFILE_NOT_FOUND\"]}";

        when(repository.findProfile(username)).thenReturn(profileDto);
        when(jsonBuilder.setStatus(any())).thenReturn(jsonBuilder);
        when(jsonBuilder.setErrors(any())).thenReturn(jsonBuilder);
        when(jsonBuilder.build()).thenReturn(expected);

        assertEquals(expected, service.getProfile(username));
    }

    @Test
    void updatePasswordSuccess() {
        long id = 1L;
        String json = """
                {
                  "oldPasswd": "oldpassword",
                  "newPasswd": "newpassword"
                }
                """;
        UpdatePasswordDto dto = new UpdatePasswordDto("oldpassword", "newpassword");
        String expected = "{\"status\":\"success\"}";

        when(jsonParser.parse(any(InputStream.class), eq(UpdatePasswordDto.class))).thenReturn(dto);
        when(repository.updatePasswd(id, dto)).thenReturn(true);
        when(jsonBuilder.setStatus(any())).thenReturn(jsonBuilder);
        when(jsonBuilder.build()).thenReturn(expected);

        assertEquals(expected, service.updatePassword(id, new ByteArrayInputStream(json.getBytes())));
    }

    @Test
    void updatePasswordFailWrongPasswd() {
        long id = 1L;
        String json = """
                {
                  "oldPasswd": "oldpassword",
                  "newPasswd": "newpassword"
                }
                """;
        UpdatePasswordDto dto = new UpdatePasswordDto("oldpassword", "newpassword");
        String expected = "{\"status\":\"fail\",\"errors\":[\"WRONG_PASSWD\"]}";

        when(jsonParser.parse(any(InputStream.class), eq(UpdatePasswordDto.class))).thenReturn(dto);
        when(repository.updatePasswd(id, dto)).thenReturn(false);
        when(jsonBuilder.setStatus(any())).thenReturn(jsonBuilder);
        when(jsonBuilder.setErrors(any())).thenReturn(jsonBuilder);
        when(jsonBuilder.build()).thenReturn(expected);

        assertEquals(expected, service.updatePassword(id, new ByteArrayInputStream(json.getBytes())));
    }
}