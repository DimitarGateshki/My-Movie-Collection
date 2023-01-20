package dal;

import be.Movie;
import dal.database.DatabaseConnector;
import dal.database.SqlServerException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.Date;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

public class MovieDAO {
    public static ObservableList<Movie> getAllMovies() throws SQLException, SqlServerException {
        DatabaseConnector dbConnector = new DatabaseConnector();
        try (Connection connection = dbConnector.getConnection()) {
            ObservableList<Movie> allMovies = FXCollections.observableArrayList();

            String sqlGetMovies = "SELECT * FROM Movies;";
            Statement statement = connection.createStatement();
            if(statement.execute(sqlGetMovies))
            {
                ResultSet resultSet = statement.getResultSet();

                while (resultSet.next()){
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    double rating = resultSet.getFloat("IMDbRating");
                    String genre = resultSet.getString("Genre");
                    String path = resultSet.getString("FilePath");
                    String cast = resultSet.getString("Cast");
                    String description = resultSet.getString("Description");
                    Date lastView = resultSet.getDate("LastView");
                    String personalRating=resultSet.getString("PersonalRating");

                    Movie movie = new Movie(id,name, rating, genre, path, cast, description,personalRating, lastView);
                    allMovies.add(movie);
                }
            }
            return allMovies;
        }
    }

    public ObservableList<String>getMovieName() {
        DatabaseConnector dbConnector = new DatabaseConnector();
        try (Connection connection = dbConnector.getConnection()) {

            ObservableList<String> allNames = FXCollections.observableArrayList();
            String sqlGetCategories = "SELECT * FROM Movies;";

            Statement statement = connection.createStatement();
            if (statement.execute(sqlGetCategories)) {
                ResultSet resultSet = statement.getResultSet();

                while (resultSet.next()) {
                    String name = resultSet.getString("name");
                    allNames.add(name);
                }
            }
            return allNames;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void setPersonalRating(String rating,int movieID){
        DatabaseConnector dbConnector = new DatabaseConnector();
        try(Connection connection = dbConnector.getConnection()) {
            String sql = "UPDATE Movies SET PersonalRating="+"'"+rating+"'"+"WHERE id="+movieID;            Statement statement = connection.createStatement();
            if(statement.execute(sql)){
                ResultSet resultSet = statement.getResultSet();
                System.out.println(resultSet);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public static void postNewMovie(Movie newMovie) throws SQLException{
        DatabaseConnector dbConnector = new DatabaseConnector();
        try(Connection connection = dbConnector.getConnection()) {
            int movieID = newMovie.getId();
            String movieName = newMovie.getName();
            Double movieRating = newMovie.getIMDB();
            String movieCategory = newMovie.getCategories();
            String moviePath = newMovie.getPath();
            String movieCast = newMovie.getCast();
            String movieDescription = newMovie.getDescription();
            int personalRating = 0;
            String sql = "INSERT INTO Movies VALUES("+movieID+",'"+movieName+"', "+movieRating+", '"+movieCategory+"', '"+moviePath+"', '"+movieCast+"', '"+movieDescription+"', null, '"+ personalRating +"')";
            Statement statement = connection.createStatement();
            if(statement.execute(sql)){
                ResultSet resultSet = statement.getResultSet();
                System.out.println(resultSet);
            }
        }
    }

    public static void removeMovie(Movie movie) throws SQLException{
        DatabaseConnector dbConnector = new DatabaseConnector();

        try(Connection connection = dbConnector.getConnection()) {
            String sql = "Delete FROM MovieToCategory where movie_id='" + movie.getId() + "';";
            Statement statement = connection.createStatement();
            if(statement.execute(sql)){
                ResultSet resultSet = statement.getResultSet();
                System.out.println(resultSet);
            }
        }

        try(Connection connection = dbConnector.getConnection()) {
            String sql = "Delete FROM Movies where name='" + movie.getName() + "';";
            Statement statement = connection.createStatement();
            if(statement.execute(sql)){
                ResultSet resultSet = statement.getResultSet();
                System.out.println(resultSet);
            }
        }


    }

    public static void updateWatchTime(String name) throws SQLException {
        DatabaseConnector dbConnector = new DatabaseConnector();
        Date newWatchDate = Date.valueOf(LocalDate.now());

        try(Connection connection = dbConnector.getConnection()) {
            String sql = "UPDATE Movies SET LastView ='"+newWatchDate+"' WHERE name='" + name + "';";
            System.out.println(sql);
            Statement statement = connection.createStatement();
            if(statement.execute(sql)){
                ResultSet resultSet = statement.getResultSet();
                System.out.println(resultSet);
            }
        }
    }

    public void updateCategory(int id, String name) throws SQLException, SqlServerException {

        Movie movie=getAllMovies().filtered(movie1 -> movie1.getId()==id).get(0);
        String currentCategories=movie.getCategories();

        if (currentCategories.length()!=0)
         currentCategories+="  ;  ";

        currentCategories+=name;

        DatabaseConnector dbConnector = new DatabaseConnector();
        try(Connection connection = dbConnector.getConnection()) {
            String sql = "UPDATE Movies SET Genre ='"+currentCategories+"' WHERE id=" + id + ";";
            System.out.println(sql);
            Statement statement = connection.createStatement();
            if(statement.execute(sql)){
                ResultSet resultSet = statement.getResultSet();
                System.out.println(resultSet);
            }
        }

    }
}
