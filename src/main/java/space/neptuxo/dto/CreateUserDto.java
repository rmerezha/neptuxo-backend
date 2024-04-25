package space.neptuxo.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
public record CreateUserDto(String username, String email, String passwd) {}
