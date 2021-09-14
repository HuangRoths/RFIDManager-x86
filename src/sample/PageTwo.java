package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import uhf.Reader;

import java.io.IOException;


public class PageTwo extends Application {


    private String com;
    public Reader reader;

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("PageTwo.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Controller target = loader.getController();
        com = target.ComName;
        reader = target.ReaderControllor;

        primaryStage.setScene(scene);
        primaryStage.setTitle("RFID·¢¿¨¿Í»§¶Ë");
        primaryStage.setHeight(800);
        primaryStage.setWidth(800);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
