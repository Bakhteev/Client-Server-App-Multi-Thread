package dao;

import dto.PersonDto;
import models.Coordinates;
import models.Location;

import java.util.List;

public class CoordinatesDao implements DAO<Coordinates>{

    @Override
    public List<Coordinates> getAll() {
        return null;
    }

    @Override
    public Coordinates getById(String id) {
        return null;
    }

    @Override
    public Coordinates create(PersonDto dto) {
        return null;
    }

    @Override
    public Coordinates update(String id, PersonDto dto) {
        return null;
    }

    @Override
    public void deleteById(String id) {

    }

    @Override
    public void delete(Coordinates element) {

    }
}
