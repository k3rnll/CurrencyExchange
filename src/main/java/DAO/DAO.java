package DAO;

import java.sql.SQLException;
import java.util.List;

public interface DAO<T> {
    T get(Long id) throws SQLException;

    List<T> getAll() throws SQLException;

    Long save(T t) throws SQLException;

    Long insert(T t) throws SQLException;

    Long update(T t) throws SQLException;

    Long delete(T t);
}
