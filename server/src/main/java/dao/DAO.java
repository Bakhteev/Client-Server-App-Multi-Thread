package dao;

import dto.PersonDto;

import java.util.List;

public interface DAO <T>{
    public List<T> getAll();

    public T getById(String id);

    public T create(PersonDto dto);

    public T update(String id, PersonDto dto);

    public void deleteById(String id);

    public void delete(T element);
}
