package space.neptuxo.dto;

import lombok.Builder;

@Builder
public record UserDto(long id, String username, String email, String passwd) {
}
