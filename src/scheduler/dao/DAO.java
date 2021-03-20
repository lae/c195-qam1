package scheduler.dao;

import javafx.collections.ObservableList;

/**
 * A generic interface for our database models.
 *
 * @param <T> the application model to create a DAO for.
 */
public interface DAO<T> {
    ObservableList<T> listAll();

    T find(T t);

    void update(T t);

    void delete(T t);

    void add(T t);
}
