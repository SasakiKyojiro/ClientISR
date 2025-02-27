package ru.kursach.frontent.scnene.service.worker;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.kursach.frontent.dto.Request;
import ru.kursach.frontent.dto.enams.Status;
import ru.kursach.frontent.http.api.WorkerClient;
import ru.kursach.frontent.scnene.service.BaseService;

import java.io.IOException;

@Slf4j
@AllArgsConstructor
public class RequestService extends BaseService<Request> {
    private TableView<Request> tableViewRequest;
    private TextField requestSubjectRequest;
    private TextArea bodySubjectRequest;
    private TableColumn<Request, String> columnThemeRequest, columnStateRequest, columnDateRequest, columnBodyRequest;
    private ComboBox<Status> statusRequest;
    private final WorkerClient client;
    private Text errorText;
    private Button paginationDownRequest, paginationUpRequest;
    private final Request duplicate= new Request();

    public void init() {
        columnBodyRequest.setCellValueFactory(new PropertyValueFactory<>("body"));
        columnDateRequest.setCellValueFactory(new PropertyValueFactory<>("date"));
        columnStateRequest.setCellValueFactory(new PropertyValueFactory<>("status"));
        columnThemeRequest.setCellValueFactory(new PropertyValueFactory<>("theme"));
        statusRequest.setItems(FXCollections.observableArrayList(Status.values()));
        super.textError = errorText;
        requestSubjectRequest.setEditable(false);
        bodySubjectRequest.setEditable(false);
        addListeners();
    }


    private boolean isAnyFieldEmpty() {
        return statusRequest.getValue() == null
                || requestSubjectRequest.getText().isEmpty() || bodySubjectRequest.getText().isEmpty();
    }

    private void highlightEmptyFields() {
        highlightField(requestSubjectRequest, requestSubjectRequest.getText().isEmpty());
        highlightField(bodySubjectRequest, bodySubjectRequest.getText().isEmpty());
        highlightField(statusRequest, statusRequest.getValue() == null);
    }

    private void addListeners() {
        addFieldListener(requestSubjectRequest, duplicate::getTheme);
        addFieldListener(bodySubjectRequest, duplicate::getBody);
        addFieldListener(statusRequest, duplicate::getStatus);
    }

    @Override
    protected String getClientResponse() throws IOException {
        return client.getAllRequest();
    }

    @Override
    protected TableView<Request> getTableView() {
        return tableViewRequest;
    }

    @Override
    protected Class<Request> getTableViewDataClass() {
        return Request.class;
    }

    public void update() {
        canceled();
        fetchData();
    }

    public void send() {
        if (isAnyFieldEmpty()) {
            highlightEmptyFields();
        }
        else {
            Request selectedRequest = tableViewRequest.getSelectionModel().getSelectedItem();
            if (selectedRequest != null) {
                selectedRequest.setTheme(requestSubjectRequest.getText());
                selectedRequest.setBody(bodySubjectRequest.getText());
                selectedRequest.setStatus(statusRequest.getValue());
                try {
                    client.sendRequest(selectedRequest);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                update();
            }
        }
    }

    public void canceled() {
        requestSubjectRequest.clear();
        bodySubjectRequest.clear();
        statusRequest.setValue(null);
    }

    public void select() {
        Request selectRequest = tableViewRequest.getSelectionModel().getSelectedItem();
        if (selectRequest != null) {
            duplicate.SetRequest(selectRequest);
            requestSubjectRequest.setText(selectRequest.getTheme());
            bodySubjectRequest.setText(selectRequest.getBody());
            statusRequest.setValue(selectRequest.getStatus());
        }
    }

    public void paginationDown() {
        paginationUpRequest.setDisable(false);
        ObservableList<Request> requests = tableViewRequest.getItems();
        if (client.offsetDown())
            update();
        else{
            paginationDownRequest.setDisable(true);
            tableViewRequest.setItems(requests);
            update();
        }
    }

    public void paginationUp() {
        paginationDownRequest.setDisable(false);
        ObservableList<Request> requestsDump = tableViewRequest.getItems();
        client.offsetUp();
        update();
        ObservableList<Request> requests = tableViewRequest.getItems();
        if (requests.isEmpty()){
            paginationUpRequest.setDisable(true);
            tableViewRequest.setItems(requestsDump);
            client.offsetDown();
            update();
        }
    }
}
