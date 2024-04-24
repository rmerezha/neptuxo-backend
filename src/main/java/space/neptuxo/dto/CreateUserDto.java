package space.neptuxo.dto;

import lombok.Builder;

@Builder
public record CreateUserDto(long id, String username, String email, String passwd) {}
