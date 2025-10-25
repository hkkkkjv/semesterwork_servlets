package ru.kpfu.itis.semester_work.dao;

import java.sql.SQLException;
import java.util.List;

public interface DAO<T> {
    T create(T x) throws SQLException;
    T get(int x) throws SQLException;
    void update(T x)throws SQLException;
    void delete(T x)throws SQLException;
    List<T> getAll()throws SQLException;
}
