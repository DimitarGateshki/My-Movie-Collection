package dal;

import be.Category;
import be.Movie;
import dal.database.DatabaseConnector;
import dal.database.SqlServerException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

public class CategoryDAO {

    public ObservableList<String>getCategoryName() {
        DatabaseConnector dbConnector = new DatabaseConnector();
        try (Connection connection = dbConnector.getConnection()) {
            ObservableList<String> allNames = FXCollections.observableArrayList();

            String sqlGetCategories = "SELECT * FROM Categories;";

            Statement statement = connection.createStatement();


            if (statement.execute(sqlGetCategories)) {
                ResultSet resultSet = statement.getResultSet();

                while (resultSet.next()) {
                    String name = resultSet.getString("Name");
                    allNames.add(name);
                }
            }
            return allNames;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

        public static ObservableList<Category> getAllCategories() throws SQLException, SqlServerException {


        DatabaseConnector dbConnector = new DatabaseConnector();
        try (Connection connection = dbConnector.getConnection()) {
            ObservableList<Category> allCategories = FXCollections.observableArrayList();
            String sqlGetCategories = "SELECT * FROM Categories;";

            Statement statement = connection.createStatement();

            if (statement.execute(sqlGetCategories)) {
                ResultSet resultSet = statement.getResultSet();

                while (resultSet.next()) {
                    int id=resultSet.getInt("id");
                    String name = resultSet.getString("Name");

                    Category category = new Category(id,name);

                    allCategories.add(category);
                }
            }
            return allCategories;
        }
    }
    public static void postNewCategory(Category category) throws SQLException{

        DatabaseConnector dbConnector = new DatabaseConnector();
        try(Connection connection = dbConnector.getConnection()) {
            String sql = "INSERT INTO Categories VALUES ("+category.getId()+",'"+category.getName()+"')";
            Statement statement = connection.createStatement();
            if(statement.execute(sql)){
                ResultSet resultSet = statement.getResultSet();
                System.out.println(resultSet);
            }
        }
    }


    public static void removeCategory(Category category) throws SQLException{

        DatabaseConnector dbConnector = new DatabaseConnector();

        try(Connection connection = dbConnector.getConnection()) {
            String sql = "Delete FROM MovieToCategory where category_id='" + category.getId() + "';";
            Statement statement = connection.createStatement();
            if(statement.execute(sql)){
                ResultSet resultSet = statement.getResultSet();
                System.out.println(resultSet);
            }
        }

        try(Connection connection = dbConnector.getConnection()) {
            String sql = "Delete FROM Categories where Name='" + category.getName() + "';";
            Statement statement = connection.createStatement();
            if(statement.execute(sql)){
                ResultSet resultSet = statement.getResultSet();
                System.out.println(resultSet);
            }
        }
    }
}
