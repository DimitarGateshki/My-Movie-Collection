package gui.controllers;

import be.Category;
import be.Movie;
import dal.CategoryDAO;
import dal.MovieDAO;
import dal.database.SqlServerException;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class SetPersonalRatingController implements Initializable {
    @FXML
    public Button submitBtn, closeButton;
    @FXML
    public ComboBox ratingBox, moviesBox;

    MovieDAO movieDAO = new MovieDAO();



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ratingBox.setItems(FXCollections.observableArrayList("BullShit 1","Horable 2","Waste of time 3"
                ,"Bad 4","Meh 5","Mid 6","Could be better 7","Good 8","Amazing 9","Must watch 10"));
        ratingBox.setVisibleRowCount(10);
        moviesBox.setItems(FXCollections.observableArrayList(movieDAO.getMovieName()));
        try {
            moviesBox.setVisibleRowCount(movieDAO.getAllMovies().size());
        } catch (SQLException | SqlServerException e) {
            throw new RuntimeException(e);
        }
    }
    public void submitBtn(ActionEvent actionEvent) throws SQLException, SqlServerException {
        String rating = ratingBox.getSelectionModel().getSelectedItem().toString();
        String movieIndex = moviesBox.getSelectionModel().getSelectedItem().toString();


        Movie movie=movieDAO.getAllMovies().filtered(e -> e.getName().equals(movieIndex)).get(0);

        movieDAO.setPersonalRating(rating,movie.getId());
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    public void close(ActionEvent actionEvent) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

}
