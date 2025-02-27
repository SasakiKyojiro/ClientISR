package ru.kursach.frontent.scnene.service.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.kursach.frontent.dto.User;
import ru.kursach.frontent.dto.enams.UserRole;
import ru.kursach.frontent.http.api.AdminClient;
import ru.kursach.frontent.scnene.service.BaseService;

import java.io.IOException;

@Slf4j
@AllArgsConstructor
public class AdminService extends BaseService<User> {
    private final AdminClient client = new AdminClient();
    private TableView<User> tableView;
    private TextField fioField, loginField, emailField, phoneField;
    private ChoiceBox<UserRole> roleBox;
    private TableColumn<User, String> columnFIO, columnLogin, columnRole, columnPhone, columnEmail;
    private Text errorText;
    private TextField findForNameField;
    private Button paginationUp, paginationDown;
    private final User duplicate = new User();

    public void initialize() {
        columnFIO.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnLogin.setCellValueFactory(new PropertyValueFactory<>("login"));
        columnRole.setCellValueFactory(new PropertyValueFactory<>("authority"));
        columnPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        columnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        roleBox.getItems().addAll(UserRole.values());
        super.textError = errorText;
        addListeners();
        update();
    }

    public void selectUser() {
        User selectedUser = tableView.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            duplicate.SetUser(selectedUser);
            fioField.setText(selectedUser.getName());
            loginField.setText(selectedUser.getLogin());
            emailField.setText(selectedUser.getEmail());
            phoneField.setText(selectedUser.getPhone());
            roleBox.setValue(selectedUser.getAuthority());
        }
    }

    public void addUser() {
        if (isAnyFieldEmpty()) {
            highlightEmptyFields();
        } else {
            User user = new User(fioField.getText(), loginField.getText(), emailField.getText(), phoneField.getText(), roleBox.getValue());
            try {
                client.addUser(user);
                update();
            } catch (IOException e) {
                log.warn("Ошибка при добавлении пользователя: {}", e.getLocalizedMessage());
            }
        }
    }

    public void unselectUser() {
        fioField.clear();
        loginField.clear();
        emailField.clear();
        phoneField.clear();
        roleBox.setValue(null);

        resetFieldStyle(fioField);
        resetFieldStyle(loginField);
        resetFieldStyle(emailField);
        resetFieldStyle(phoneField);
        resetFieldStyle(roleBox);
    }


    public void deleteUser() {
        User selectedUser = tableView.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            try {
                client.deleteUser(selectedUser);
                update();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void updateUser() {
        if (isAnyFieldEmpty()) {
            highlightEmptyFields();
        } else {
            User selectedUser = tableView.getSelectionModel().getSelectedItem();
            if (selectedUser != null) {
                selectedUser.setName(fioField.getText());
                selectedUser.setLogin(loginField.getText());
                selectedUser.setEmail(emailField.getText());
                selectedUser.setPhone(phoneField.getText());
                selectedUser.setAuthority(roleBox.getValue());

                log.debug("Обновление пользователя: {}", selectedUser);
                try {
                    client.updateUser(selectedUser);
                    update();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public void update() {
        unselectUser();
        fetchData();
    }

    @Override
    protected String getClientResponse() throws IOException {
        return client.getAllUser();
    }

    @Override
    protected TableView<User> getTableView() {
        return tableView;
    }

    @Override
    protected Class<User> getTableViewDataClass() {
        return User.class;
    }


    private boolean isAnyFieldEmpty() {
        return fioField.getText().isEmpty() || loginField.getText().isEmpty() || emailField.getText().isEmpty() || phoneField.getText().isEmpty() || roleBox.getValue() == null;
    }

    private void highlightEmptyFields() {
        highlightField(fioField, fioField.getText().isEmpty());
        highlightField(loginField, loginField.getText().isEmpty());
        highlightField(emailField, emailField.getText().isEmpty());
        highlightField(phoneField, phoneField.getText().isEmpty());
        highlightField(roleBox, roleBox.getValue() == null);
    }

    private void addListeners() {
        addFieldListener(fioField, duplicate::getName);
        addFieldListener(loginField, duplicate::getLogin);
        addFieldListener(emailField, duplicate::getEmail);
        addFieldListener(phoneField, duplicate::getPhone);
        addFieldListener(roleBox, duplicate::getAuthority);
    }

    public void passwordReset() {
        User selectedUser = tableView.getSelectionModel().getSelectedItem();
        try {
            client.passwordReset(selectedUser);
        } catch (IOException e) {
            log.warn("Ошибка при удалении пользователя: {}", e.getLocalizedMessage());
            textError.setText(e.getMessage());
        }
    }

    public void findUser() {
        String name = findForNameField.getText();
        log.debug("Попытка найти пользователя: {}", name);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            User[] user = objectMapper.readValue(client.getUser(name), User[].class);
            tableView.getItems().clear();
            tableView.getItems().addAll(user);
        } catch (IOException e) {
            log.warn("Ошибка при поиске пользователя: {}", e.getLocalizedMessage());
            textError.setText(e.getMessage());
        }
    }

    public void offsetUp() {
        paginationDown.setDisable(false);
        ObservableList<User> itemsDump = tableView.getItems();
        client.offsetUp();
        update();
        ObservableList<User> items = tableView.getItems();
        if (items.isEmpty()){
            paginationUp.setDisable(true);
            tableView.setItems(itemsDump);
            client.offsetDown();
            update();
        }
    }

    public void offsetDown() {
        paginationUp.setDisable(false);
        ObservableList<User> itemsDump = tableView.getItems();
        if(client.offsetDown()){
            update();
        }
        else {
            tableView.setItems(itemsDump);
            update();
            paginationDown.setDisable(true);
        }
    }
}
