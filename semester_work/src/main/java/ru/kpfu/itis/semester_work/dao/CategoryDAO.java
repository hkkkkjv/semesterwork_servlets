package ru.kpfu.itis.semester_work.dao;

import ru.kpfu.itis.semester_work.model.Category;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO implements DAO<Category> {
    private final Connection connection;

    public CategoryDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Category create(Category x) throws SQLException {
        return null;
    }

    @Override
    public Category get(int id) {
        Category category = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_CATEGORY_BY_ID)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                category = mapToCategory(resultSet);
            }
        }catch (SQLException e){
            throw new RuntimeException("Error retrieving category by ID",e);
        }
        return category;
    }

    @Override
    public void update(Category x) {

    }

    @Override
    public void delete(Category x) {

    }

    @Override
    public List<Category> getAll() {
        List<Category> categories = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_CATEGORIES)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                categories.add(mapToCategory(resultSet));
            }
        }catch (SQLException e){
            throw new RuntimeException("Error retrieving all categories",e);
        }
        return categories;
    }

    private Category mapToCategory(ResultSet resultSet) throws SQLException {
        Category category = new Category();
        category.setId(resultSet.getInt("id"));
        category.setName(resultSet.getString("name"));
        return category;
    }

    private static final String SELECT_CATEGORY_BY_ID = "SELECT * FROM category WHERE id = ?";
    private static final String SELECT_ALL_CATEGORIES = "SELECT * FROM category";

}
