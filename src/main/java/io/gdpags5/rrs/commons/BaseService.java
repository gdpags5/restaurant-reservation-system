package io.gdpags5.rrs.commons;

import java.util.List;
import java.util.Optional;

public interface BaseService<T, ID> {
    T saveOrUpdate(T dto);
    Optional<T> findById(ID id);
    void delete(T dto);
    List<T> findAll();
}
