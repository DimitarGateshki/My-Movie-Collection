package gui;

import dal.database.SqlServerException;
import gui.controllers.MainStageController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;


public class MyMovies extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException, SQLException, SqlServerException {
        //Creating controller instance
            MainStageController myMoviesController = new MainStageController();

                Parent root = FXMLLoader.load(getClass().getResource("view/MainStage.fxml"));
                primaryStage.setTitle("Movies");
                primaryStage.setScene(new Scene(root));
                primaryStage.setResizable(false);
                primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}