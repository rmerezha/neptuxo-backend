package space.neptuxo.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class User {

    private long id;
    private String username;
    private String email;
    private String passwd;

}
