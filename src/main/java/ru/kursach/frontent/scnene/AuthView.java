package ru.kursach.frontent.scnene;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import ru.kursach.frontent.scnene.service.auth.AuthService;

public class AuthView {
    @FXML
    private TextField login, password;
    @FXML
    private Text welcomeText;
    private AuthService service ;

    @FXML
    private void initialize() {
        service = new AuthService(login, password, welcomeText);
    }
    @FXML
    public void onLoginButtonClick() {
        service.authorization();
    }
}