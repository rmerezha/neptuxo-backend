package space.neptuxo.service;

import org.rmerezha.di.annotation.Inject;
import space.neptuxo.dto.*;
import space.neptuxo.exception.SQLConstraintException;
import space.neptuxo.repository.AbstractUserRepository;
import space.neptuxo.util.Error;
import space.neptuxo.util.*;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;


public class UserService {
    @Inject
    private AbstractUserRepository repository;
    @Inject
    private JsonBuilderFactory jsonBuilderFactory;
    @Inject
    private JsonParser jsonParser;


    private final static String KEY_PRODUCTS = "products";
    private final static String KEY_USERNAME = "username";
    private final static String KEY_ID = "id";

    public String registration(InputStream jsonStream) {
        JsonBuilder jsonBuilder = jsonBuilderFactory.create();
        try {
            UserDto userDto = jsonParser.parse(jsonStream, UserDto.class);

            repository.save(userDto);
            return jsonBuilder.setStatus(Status.SUCCESS)
                    .build();
        } catch (SQLConstraintException e) {
            return jsonBuilder.setStatus(Status.FAIL)
                    .setErrors(List.of(Error.USER_EXIST))
                    .build();
        }
    }

    public LoginDto login(InputStream jsonStream) {
        UserDto userDto = jsonParser.parse(jsonStream, UserDto.class);
        JsonBuilder jsonBuilder = jsonBuilderFactory.create();

        Optional<ReadUserDto> user = repository.findByEmailAndPasswd(userDto.email(), userDto.passwd());

        if (user.isPresent()) {
            return new LoginDto(user, jsonBuilder.setStatus(Status.SUCCESS).build());
        }
        return new LoginDto(Optional.empty(), jsonBuilder.setStatus(Status.FAIL)
                .setErrors(List.of(Error.WRONG_EMAIL_OR_PASSWD)).build());
    }


    public String getProfile(String username) {
        ProfileDto profileDto = repository.findProfile(username);
        JsonBuilder jsonBuilder = jsonBuilderFactory.create();

        profileDto.user().ifPresentOrElse(userDto -> {
                    jsonBuilder.setStatus(Status.SUCCESS)
                            .setData(KEY_ID, userDto.id())
                            .setData(KEY_USERNAME, userDto.username());
                    if (!profileDto.products().isEmpty()) {
                        jsonBuilder.setData(KEY_PRODUCTS, profileDto.products());
                    }
                },
                () -> jsonBuilder.setStatus(Status.FAIL)
                        .setErrors(List.of(Error.PROFILE_NOT_FOUND)));
        return jsonBuilder.build();
    }

    public String updatePassword(long id, InputStream jsonStream) {
        UpdatePasswordDto dto = jsonParser.parse(jsonStream, UpdatePasswordDto.class);
        JsonBuilder jsonBuilder = jsonBuilderFactory.create();

        if (repository.updatePasswd(id, dto)) {
            jsonBuilder.setStatus(Status.SUCCESS);
        } else {
            jsonBuilder.setStatus(Status.FAIL)
                    .setErrors(List.of(Error.WRONG_PASSWD));
        }
        return jsonBuilder.build();
    }
}





