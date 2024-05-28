package space.neptuxo.dto;

import lombok.Builder;

@Builder
public record ReadUserDto(long id, String username, String email) {

}
