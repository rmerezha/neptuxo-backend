package space.neptuxo.dto;

import space.neptuxo.entity.Product;

import java.util.List;
import java.util.Optional;

public record ProfileDto(Optional<ReadUserDto> user, List<Product> products) {
}
