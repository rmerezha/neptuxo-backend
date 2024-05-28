package space.neptuxo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class Product {

    private long id;
    private long createdBy;
    private String name;
    private String description;
    private ProductType type;
    private int price;
    private LocalDateTime createdAt;
    private String imagePath;

}
