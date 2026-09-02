package psu.se411.se411project;

import javafx.application.Application;
import javafx.stage.Stage;

public class MainClass extends Application {

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            primaryStage.setTitle("SE411 Project");
            primaryStage.setWidth(600);
            primaryStage.setHeight(400);

            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}	