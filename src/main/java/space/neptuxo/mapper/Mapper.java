package space.neptuxo.mapper;

public interface Mapper<F, T> {

    T map(F obj);

}
