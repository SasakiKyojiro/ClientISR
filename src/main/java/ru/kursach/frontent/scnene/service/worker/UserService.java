package ru.kursach.frontent.scnene.service.worker;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.kursach.frontent.dto.User;
import ru.kursach.frontent.http.api.WorkerClient;
import ru.kursach.frontent.scnene.service.BaseService;

import java.io.IOException;

@Slf4j
@AllArgsConstructor
public class UserService extends BaseService<User> {
    private TableView<User> tableViewUsers;
    private TextField phoneUsers, fioUsers, emailUsers;
    private TableColumn<User, String> columnPhoneUsers, columnFIOUsers, columnEmailUsers;
    private final WorkerClient client;
    private Text errorText;
    private final User duplicate = new User();

    public void init() {
        columnEmailUsers.setCellValueFactory(new PropertyValueFactory<>("email"));
        columnFIOUsers.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnPhoneUsers.setCellValueFactory(new PropertyValueFactory<>("phone"));
        super.textError = errorText;
        addListeners();
    }

    private boolean isAnyFieldEmpty() {
        return fioUsers.getText().isEmpty() || phoneUsers.getText().isEmpty() || emailUsers.getText().isEmpty();
    }

    private void highlightEmptyFields() {
        highlightField(fioUsers, fioUsers.getText().isEmpty());
        highlightField(phoneUsers, phoneUsers.getText().isEmpty());
        highlightField(emailUsers, emailUsers.getText().isEmpty());
    }


    private void addListeners() {
        addFieldListener(fioUsers, duplicate::getName);
        addFieldListener(phoneUsers, duplicate::getPhone);
        addFieldListener(emailUsers, duplicate::getEmail);
    }

    @Override
    protected String getClientResponse() throws IOException {
        return client.getAllUser();
    }

    @Override
    protected TableView<User> getTableView() {
        return tableViewUsers;
    }

    @Override
    protected Class<User> getTableViewDataClass() {
        return User.class;
    }

    public void update() {
        canceled();
        fetchData();
    }

    public void change() {
        if (isAnyFieldEmpty()) {
            highlightEmptyFields();
        }
        else {
            User selectedUser = tableViewUsers.getSelectionModel().getSelectedItem();
            if (selectedUser != null) {
                selectedUser.setPhone(phoneUsers.getText());
                selectedUser.setEmail(emailUsers.getText());
                selectedUser.setName(fioUsers.getText());
                log.debug("обновление пользователя: {}", selectedUser);
                try {
                    client.changeUser(selectedUser);
                } catch (IOException e) {
                    log.warn("Ошибка при изменении пользователя: {}", e.getLocalizedMessage());
                }
                update();
            }
        }
    }

    public void canceled() {
        fioUsers.clear();
        emailUsers.clear();
        phoneUsers.clear();
        resetFieldStyle(fioUsers);
        resetFieldStyle(phoneUsers);
        resetFieldStyle(emailUsers);
    }

    public void select() {
        User selectedUser = tableViewUsers.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            duplicate.SetUser(selectedUser);
            fioUsers.setText(selectedUser.getName());
            emailUsers.setText(selectedUser.getEmail());
            phoneUsers.setText(selectedUser.getPhone());
        }
    }
}
