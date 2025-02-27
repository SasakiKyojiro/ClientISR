package ru.kursach.frontent;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.kursach.frontent.dto.enams.UserRole;
import ru.kursach.frontent.scnene.interfase.UUIDReceiver;

import java.io.IOException;
import java.util.UUID;

public class HelloApplication extends Application {

    public static void openNewScene(String s, UUID uuid, UserRole userRole) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(s));
            Parent root = fxmlLoader.load();

            // Получаем контроллер загруженной сцены
            Object controller = fxmlLoader.getController();
            if (controller instanceof UUIDReceiver) {
                ((UUIDReceiver) controller).setUUID(uuid);
            }

            Scene scene = new Scene(root, 1024, 600);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle(userRole.toString());
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("authorizations-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Авторизация");
        stage.setScene(scene);
        stage.show();
    }
}