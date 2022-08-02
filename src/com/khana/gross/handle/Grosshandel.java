package com.khana.gross.handle;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Grosshandel extends Application {


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("grosshandel.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("grosshandel.css").toString());

        GrossHandelController handelController = new GrossHandelController();
        FXMLLoader loader = new FXMLLoader();
        handelController.setDataFromDB();
        primaryStage.setScene(scene);        
        primaryStage.show();
    }
}