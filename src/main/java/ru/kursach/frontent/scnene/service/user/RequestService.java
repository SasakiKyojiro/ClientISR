package ru.kursach.frontent.scnene.service.user;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.kursach.frontent.dto.Request;
import ru.kursach.frontent.http.api.UserClient;
import ru.kursach.frontent.scnene.service.BaseService;

import java.io.IOException;

@Slf4j
@AllArgsConstructor
public class RequestService extends BaseService<Request> {
    private UserClient client;
    private TableView<Request> tableView;
    private TextField requestSubject;
    private TextArea bodySubject;

    private Text textError;
    private TableColumn<Request, String> columnTheme, columnDate, columnState, columnBody;

    public void init() {
        columnTheme.setCellValueFactory(new PropertyValueFactory<>("theme"));
        columnDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        columnState.setCellValueFactory(new PropertyValueFactory<>("status"));
        columnBody.setCellValueFactory(new PropertyValueFactory<>("body"));
        super.textError = textError;
        client.addPropertyChangeListener(evt -> {
                    if ("uuid".equals(evt.getPropertyName())) update();
                }
        );
    }

    public void update() {
        fetchData();
    }

    public void sendRequest() {
        if (requestSubject.getText().isEmpty()) {
            textError.setText("Введите тему запроса");
            return;
        }

        textError.setText("");
        Request tmp = new Request(requestSubject.getText(), bodySubject.getText());

        try {
            client.putRequest(tmp);
            log.info("Запрос отправлен успешно: {}", tmp);
            update();
        } catch (IOException e) {
            log.error("Ошибка при отправке запроса: {}", e.getLocalizedMessage());
        }

        requestSubject.setText("");
        bodySubject.setText("");
    }

    @Override
    protected String getClientResponse() throws IOException {
        return client.getRequest();
    }

    @Override
    protected TableView<Request> getTableView() {
        return tableView;
    }

    @Override
    protected Class<Request> getTableViewDataClass() {
        return Request.class;
    }

    public void canceled() {
        requestSubject.clear();
        bodySubject.clear();
    }
}
