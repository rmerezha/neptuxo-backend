package space.neptuxo.util;

import lombok.Getter;
import lombok.Value;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ErrorHandler {

    private final List<Error> errors = new ArrayList<>();

    public void add(Error error) {
        errors.add(error);
    }

}
