package ru.kursach.frontent.scnene;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kursach.frontent.dto.Request;
import ru.kursach.frontent.dto.Tax;
import ru.kursach.frontent.http.api.UserClient;
import ru.kursach.frontent.scnene.interfase.UUIDReceiver;
import ru.kursach.frontent.scnene.service.user.RequestService;
import ru.kursach.frontent.scnene.service.user.TaxService;

import java.util.UUID;

public class UserView implements UUIDReceiver {

    private static final Logger log = LoggerFactory.getLogger(UserView.class);
    @FXML
    private TextField requestSubjectRequest;
    @FXML
    private TextArea bodySubjectRequest;
    @FXML
    private TableView<Request> tableViewRequest;
    @FXML
    private TableView<Tax> tableViewTax;
    @FXML
    private Text textErrorRequest, textErrorTax;
    @FXML
    private TableColumn<Request, String> columnThemeRequest, columnDateRequest, columnStateRequest, columnBodyRequest;
    @FXML
    private TableColumn<Tax, String> columnNameTax, columnSumTax, columnDataTax, columnStatusTax;

    private final UserClient client = new UserClient();
    private RequestService serviceRequest;
    private TaxService serviceTax;

    @FXML
    public void initialize() {
        log.debug("инициализация сцены юзера");
        serviceRequest = new RequestService(client, tableViewRequest, requestSubjectRequest, bodySubjectRequest, textErrorRequest, columnThemeRequest, columnDateRequest, columnStateRequest, columnBodyRequest);
        serviceTax = new TaxService(client, tableViewTax, columnNameTax, columnSumTax, columnDataTax, columnStatusTax, textErrorTax);
        serviceRequest.init();
        serviceTax.init();
    }

    public void updateRequest() {
        serviceRequest.update();
    }

    public void sendRequest() {
        serviceRequest.sendRequest();
    }

    public void updateTax() {
        serviceTax.update();
    }

    public void canceledRequest() {
        serviceRequest.canceled();
    }


    public void selectionRequest() {
        if (serviceRequest != null)
            serviceRequest.update();
    }

    public void selectionTax() {
        if (serviceTax != null)
            serviceTax.update();
    }

    @Override
    public void setUUID(UUID uuid) {
        log.debug("uuid: {} установлен", uuid);
        client.setUUID(uuid);
    }
}
