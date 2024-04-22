package space.neptuxo.dto;

import space.neptuxo.entity.ProductType;

public record PreferenceDto(long id, long userId, ProductType type) {}
