package ru.kursach.frontent.scnene.service.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.kursach.frontent.HelloApplication;
import ru.kursach.frontent.dto.AuthObject;
import ru.kursach.frontent.dto.AuthRequest;
import ru.kursach.frontent.dto.enams.UserRole;
import ru.kursach.frontent.http.api.AuthorizationsClient;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@AllArgsConstructor
public class AuthService  {
    private final AuthorizationsClient client = new AuthorizationsClient();
    private TextField login, password;
    private Text welcomeText;

    public void authorization() {
        log.debug("Вход в систему");
        try {
            AuthObject authObject = getUserRole(new AuthRequest(login.getText(), password.getText()));
            UserRole role = authObject.getRole();
            if (role == null) {
                throw new IllegalArgumentException("Неправильный логин или пароль");
            }
            log.debug("Авторизованная роль: {}", role);
            log.debug("uuid: {}", authObject.getId());
            log.info("Пользователь авторизован с ролью: {}", role);
            openSceneForRole(role, authObject.getId());
        } catch (IOException e) {
            log.error("Ошибка при выполнении запроса к серверу: {}", e.getLocalizedMessage());
            welcomeText.setText("Ошибка авторизации");
        } catch (IllegalArgumentException e) {
            welcomeText.setText("Ошибка авторизации");
            log.warn("Ошибка авторизации: некорректная роль");
        }
    }

    private AuthObject getUserRole(AuthRequest request) throws IOException {
        String tmp = client.getUser(request);
        log.debug(tmp);
        return new ObjectMapper().readValue(tmp, AuthObject.class);
    }

    private void openSceneForRole(UserRole role, UUID uuid) {
        Stage stage = (Stage) welcomeText.getScene().getWindow();
        String scenePath = switch (role) {
            case Admin -> "admin-view.fxml";
            case Worker -> "workerScene.fxml";
            case User -> "userScene.fxml";
        };
        HelloApplication.openNewScene(scenePath, uuid, role);
        stage.close();
    }
}
