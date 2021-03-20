package scheduler.dao;

import javafx.collections.ObservableList;

public interface DAO<T> {
    ObservableList<T> listAll();
    T find(T t);
    void update(T t);
    void delete(T t);
    void add(T t);
}
