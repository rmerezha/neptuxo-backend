package space.neptuxo.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import space.neptuxo.util.ConnectionPool;

@Data
@AllArgsConstructor
@Builder
public class Preference {

    private long id;
    private long userId;
    private Type type;

}
