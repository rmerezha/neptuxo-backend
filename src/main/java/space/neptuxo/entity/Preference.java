package space.neptuxo.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class Preference {

    private long id;
    private long userId;
    private Type type;

}
