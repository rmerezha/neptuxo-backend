package space.neptuxo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class Product {

    private long id;
    private User user;
    private String description;
    private ProductType type;
    private int count;
    private String imagePath;

}
