package gui.controllers;


import be.*;

import dal.CategoryToMovieDAO;
import dal.CategoryDAO;
import dal.MovieDAO;
import dal.database.SqlServerException;
import gui.MyMovies;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;



public class MainStageController implements Initializable {

   @FXML
   public TableView<Category> categoryTableView;
  @FXML
  public TableColumn<Category, String> categoryTableColumn;

  @FXML
  public TableColumn<Movie, Date> lastViewTableColumn;
    @FXML
    private TableColumn<Movie, String> castTableColumn;

    @FXML
    private TableColumn<Movie, String> descriptionTableColumn;
    @FXML
    public TableColumn <Movie, String> personalRatingTableColumn;

    @FXML
    private TableColumn<Movie, String> genreTableColumn;

    @FXML
    private TableColumn<Movie, Double> imdbTableColumn;

    @FXML
    private TextField movieSearchBox;

    @FXML
    private TableView<Movie> movieTableView;
    @FXML
    private TableColumn<Movie, String> nameTableColumn;

    @FXML
    private Button playMovieButton;

    PlayerFunctions playerFunctions = new PlayerFunctions();

    Filter filter = new Filter();

    DataRoute dataRoute = new DataRoute();


    @Override
    public void initialize(URL location, ResourceBundle resources) {


        nameTableColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));
        imdbTableColumn.setCellValueFactory(new PropertyValueFactory<>("IMDB"));
        lastViewTableColumn.setCellValueFactory(new PropertyValueFactory<>("LastView"));
        genreTableColumn.setCellValueFactory(new PropertyValueFactory<>("Categories"));
        castTableColumn.setCellValueFactory(new PropertyValueFactory<>("Cast"));
        descriptionTableColumn.setCellValueFactory(new PropertyValueFactory<>("Description"));
        personalRatingTableColumn.setCellValueFactory(new PropertyValueFactory<>("PersonalRating"));

        DataRoute dataRoute = new DataRoute();

        try {
            ObservableList<Movie> allMovies = dataRoute.routeMovie();
            updateMovieTable(allMovies);
            testForDeletableMovies();

        } catch (SQLException | SqlServerException e) {

            throw new RuntimeException(e);
        }

        categoryTableColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));
        try {
            updateCategoryTable();
        } catch (SQLException | SqlServerException e) {
            throw new RuntimeException(e);
        }

    }

    public void updateMovieTable(ObservableList<Movie> selectedMovies)
            throws SQLException, SqlServerException {

        movieTableView.setItems(selectedMovies);
    }


    public void updateCategoryTable() throws SQLException, SqlServerException{
        DataRoute dataRoute = new DataRoute();
        ObservableList<Category> allCategories = dataRoute.routeCategory();
        categoryTableView.setItems(allCategories);
    }

    public void searchForName() throws SQLException, SqlServerException {
        updateMovieTable(filter.searchMovie(movieSearchBox.getText(), "name"));
    }
    public void searchForIMDb() throws SQLException, SqlServerException {
        updateMovieTable(filter.searchMovie(movieSearchBox.getText(), "IMDb"));
    }
    public void searchForCategory() throws SQLException, SqlServerException {
        updateMovieTable(filter.searchMovie(movieSearchBox.getText(), "category"));
    }

    public void playMovie() throws SQLException {
        if (movieTableView.getSelectionModel().getSelectedItem() != null) {
            Movie selectedMovie = movieTableView.getSelectionModel().getSelectedItem();
            MovieDAO.updateWatchTime(selectedMovie.getName());
            playerFunctions.playVideo(selectedMovie.getPath());
        }
    }

    public void addCategory() throws IOException{
        FXMLLoader loader = new FXMLLoader(MyMovies.class.getResource("view/AddCategory.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stageAddCategory = new Stage();
        stageAddCategory.setTitle("Add a category");
        stageAddCategory.setScene(scene);
        stageAddCategory.show();
        stageAddCategory.setResizable(false);
    }


    public  void removeCategory() throws IOException{
        FXMLLoader loader = new FXMLLoader(MyMovies.class.getResource("view/RemoveCategory.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stageAddCategory = new Stage();
        stageAddCategory.setTitle("Remove a category");
        stageAddCategory.setScene(scene);
        stageAddCategory.show();
        stageAddCategory.setResizable(false);
    }

    public void refreshTable() throws SQLException, SqlServerException {
        updateCategoryTable();
        updateMovieTable(MovieDAO.getAllMovies());
    }



    public void setCategory() throws IOException{
        FXMLLoader loader = new FXMLLoader(MyMovies.class.getResource("view/SetCategory.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stageAddCategory = new Stage();
        stageAddCategory.setTitle("Set a category");
        stageAddCategory.setScene(scene);
        stageAddCategory.show();
        stageAddCategory.setResizable(false);
    }


    public void  getPlaylist(MouseEvent mouseEvent) throws SQLException, SqlServerException {

        if (categoryTableView.getSelectionModel().getSelectedItem() != null) {
            String categorySelection = categoryTableView.getSelectionModel().getSelectedItem().getName();

            Category cat = CategoryDAO.getAllCategories().filtered(category -> category.getName()
                    .equals(categorySelection)).get(0);

            ObservableList<CategoryToMovie> categoryToMovies = CategoryToMovieDAO.getCategoryToMovie()
                    .filtered(categoryToMovie -> categoryToMovie.getCategoryID() == cat.getId());

            List<Integer> ids = new ArrayList<>();

            for (CategoryToMovie e : categoryToMovies) {
                ids.add(e.getMovieID());
            }

            ObservableList<Movie> movies = MovieDAO.getAllMovies().filtered(movie -> ids.contains(movie.getId()));
            movieTableView.setItems(movies);
        }
    }

    public void setPersonalRating() throws IOException {
        FXMLLoader loader = new FXMLLoader(MyMovies.class.getResource("view/SetPersonalRating.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stageAddRating = new Stage();
        stageAddRating.setTitle("Set a personal rating");
        stageAddRating.setScene(scene);
        stageAddRating.show();
        stageAddRating.setResizable(false);
    }

    public void openAddMovie() throws IOException, SQLException, SqlServerException {
        FXMLLoader loader = new FXMLLoader(MyMovies.class.getResource("view/AddMovie.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stageAddMovie = new Stage();
        stageAddMovie.setTitle("Add a movie");
        stageAddMovie.setScene(scene);
        stageAddMovie.show();
        stageAddMovie.setResizable(false);
        updateMovieTable(dataRoute.routeMovie());
    }

    public void openRemoveMovie() throws IOException {
        FXMLLoader loader = new FXMLLoader(MyMovies.class.getResource("view/RemoveMovie.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stageRemoveMovie = new Stage();
        stageRemoveMovie.setTitle("Remove a movie");
        stageRemoveMovie.setScene(scene);
        stageRemoveMovie.show();
        stageRemoveMovie.setResizable(false);
    }

    public void openDeleteMoviePopup() throws IOException {
        FXMLLoader loader = new FXMLLoader(MyMovies.class.getResource("view/DeleteMoviePopup.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stageOpenPopup = new Stage();
        stageOpenPopup.setScene(scene);
        stageOpenPopup.setAlwaysOnTop(true);
        stageOpenPopup.show();
        stageOpenPopup.setResizable(false);
    }

    public void testForDeletableMovies(){
        DataRoute dataRoute = new DataRoute();
        try {
            ObservableList<Movie> allMovies = dataRoute.routeMovie();
            for (Movie movie:allMovies) {
                if(movie.getLastView()!=null) {
                    LocalDate lastView = LocalDate.parse(movie.getLastView().toString());
                    LocalDate timeNow = LocalDate.now();
                    long daysBetween = Duration.between(lastView.atTime(0, 0), timeNow.atTime(0, 0)).toDays();
                    if (Integer.parseInt(movie.getPersonalRating()) < 6 && daysBetween > 730) {
                        openDeleteMoviePopup();
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (SqlServerException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}