package dal;

import be.CategoryToMovie;
import dal.database.DatabaseConnector;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CategoryToMovieDAO {



    public static ObservableList<CategoryToMovie> getCategoryToMovie() {
        DatabaseConnector dbConnector = new DatabaseConnector();

        try (Connection connection = dbConnector.getConnection()) {
            ObservableList<CategoryToMovie> allCategoryToMovie = FXCollections.observableArrayList();

            String sqlGetCategories = "SELECT * FROM MovieToCategory;";

            Statement statement = connection.createStatement();
            if (statement.execute(sqlGetCategories)) {
                ResultSet resultSet = statement.getResultSet();

                while (resultSet.next()) {
                    int ID=resultSet.getInt("id");
                    int categoryId = resultSet.getInt("category_id");
                    int movieId = resultSet.getInt("movie_id");


                    CategoryToMovie categoryToMovie =new CategoryToMovie(ID,categoryId,movieId);
                    allCategoryToMovie.add(categoryToMovie);
                }
            }
            return allCategoryToMovie;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public static void removeCategoryToMovie(int catID,int movId) throws SQLException{
        //Creating dbConnector instance
        DatabaseConnector dbConnector = new DatabaseConnector();
        try(Connection connection = dbConnector.getConnection()) {
            String sql = "Delete FROM CategoryToMovie where CategoryID=" + catID + "and MovieID="+movId+';';
            System.out.println(sql);
            Statement statement = connection.createStatement();
            if(statement.execute(sql)){
                ResultSet resultSet = statement.getResultSet();
                System.out.println("Removed correctly");
            }
        }
    }

    public static void postNewCategoryToMovie(CategoryToMovie categoryToMovie) throws SQLException{
        DatabaseConnector dbConnector = new DatabaseConnector();
        try(Connection connection = dbConnector.getConnection()) {
            String sql = "INSERT INTO MovieToCategory VALUES ("+ categoryToMovie.getCategoryToMovieID()+ " ,"
                    + categoryToMovie.getCategoryID()+",'"+ categoryToMovie.getMovieID()+"')";
            Statement statement = connection.createStatement();
            if(statement.execute(sql)){
                ResultSet resultSet = statement.getResultSet();
                System.out.println( resultSet);
            }
        }
    }
}
