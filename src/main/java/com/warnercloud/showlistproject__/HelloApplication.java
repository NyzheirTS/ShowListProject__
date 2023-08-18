package com.warnercloud.showlistproject__;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.util.prefs.Preferences;

public class HelloApplication extends Application {

    //this variable needs to go in the Main class, outside of the start() method
    long lastRefreshTime = 0;

    private static final double DEFAULT_WIDTH = 800;
    private static final double DEFAULT_HEIGHT = 600;
    @Override
    public void start(Stage primeStage) throws IOException {
        Preferences preferences;
        preferences = Preferences.userRoot().node(this.getClass().getName());

        double savedWidth = preferences.getDouble("windowWidth",DEFAULT_WIDTH);
        double savedHeight = preferences.getDouble("WindowHeight",DEFAULT_HEIGHT);

        primeStage.setWidth(savedWidth);
        primeStage.setHeight(savedHeight);

        primeStage.setOnCloseRequest((WindowEvent e)->{
            preferences.putDouble("windowWidth",primeStage.getWidth());
            preferences.putDouble("WindowHeight", primeStage.getHeight());
        });

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("main_FXML.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), DEFAULT_WIDTH, DEFAULT_HEIGHT);


        //this goes after you've defined your scene, but before you display your stage
        scene.addPreLayoutPulseListener(() -> {
            long refreshTime = System.nanoTime();
            System.out.println(refreshTime - lastRefreshTime);
            lastRefreshTime = refreshTime;
        });


        primeStage.setTitle("Hello!");
        primeStage.setScene(scene);
        primeStage.show();


    }

    public static void main(String[] args) {
        launch();
    }
}