package space.neptuxo.dto;

import java.util.Optional;

public record LoginDto(Optional<ReadUserDto> user, String json) {
}
