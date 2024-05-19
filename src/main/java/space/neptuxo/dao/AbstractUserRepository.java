package space.neptuxo.dao;

import space.neptuxo.entity.User;

import java.util.Optional;


public interface AbstractUserRepository extends Repository<User> {

    Optional<User> findByEmailAndPasswd(User user);

}
