package ru.kursach.frontent.scnene;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import ru.kursach.frontent.dto.*;
import ru.kursach.frontent.dto.enams.Status;
import ru.kursach.frontent.dto.enams.TaxStatus;
import ru.kursach.frontent.http.api.WorkerClient;
import ru.kursach.frontent.scnene.service.worker.OrganizationService;
import ru.kursach.frontent.scnene.service.worker.RequestService;
import ru.kursach.frontent.scnene.service.worker.TaxService;
import ru.kursach.frontent.scnene.service.worker.UserService;

public class WorkerView {
    @FXML
    private TableView<User> tableViewUsers;
    @FXML
    private TextField phoneUsers, fioUsers, emailUsers;
    @FXML
    private TableColumn<User, String> columnPhoneUsers, columnFIOUsers, columnEmailUsers;

    @FXML
    private TableView<Request> tableViewRequest;
    @FXML
    private TextField requestSubjectRequest;
    @FXML
    private TextArea bodySubjectRequest;
    @FXML
    private TableColumn<Request, String> columnThemeRequest, columnStateRequest, columnDateRequest, columnBodyRequest;
    @FXML
    private ComboBox<Status> statusRequest;

    @FXML
    private TableView<Tax> tableViewTax;
    @FXML
    private TableColumn<Tax, String> columnTypeTax, columnSumTax, columnStatusTax, columnNameOrganizationsTax, columnFIOTax, columnDateTax;
    @FXML
    private DatePicker dateTax;
    @FXML
    private TextField sumTax;
    @FXML
    private ComboBox<String> nameOrganizationTax, fioTax, typeTax;
    @FXML
    private ComboBox<TaxStatus> statusTax;

    @FXML
    private TableView<Organization> tableViewOrganization;
    @FXML
    private TextField nameOrganization, kppOrganization, innOrganization, addressOrganization;
    @FXML
    private TableColumn<Organization, String> columnNameOrganization, columnKppOrganization, columnInnOrganization, columnAddressOrganization;

    @FXML
    private Text errorTextUser, errorTextTax, errorTextRequest, errorTextOrganizations;
    @FXML
    private Button paginationDownRequest, paginationUpRequest, paginationDownTax, paginationUpTax;
    private final WorkerClient client = new WorkerClient();
    private  UserService userService;
    private  RequestService requestService;
    private  TaxService taxService;
    private  OrganizationService organizationService;
    @FXML
    private void initialize() {
        userService = new UserService(tableViewUsers, phoneUsers, fioUsers, emailUsers, columnPhoneUsers, columnFIOUsers, columnEmailUsers, client, errorTextUser);
        requestService = new RequestService(tableViewRequest, requestSubjectRequest, bodySubjectRequest, columnThemeRequest, columnStateRequest, columnDateRequest, columnBodyRequest, statusRequest, client, errorTextRequest,paginationDownRequest, paginationUpRequest);
        taxService = new TaxService(tableViewTax, columnTypeTax, columnSumTax, columnStatusTax, columnNameOrganizationsTax, columnFIOTax, columnDateTax, dateTax, sumTax, nameOrganizationTax, fioTax, typeTax, statusTax, client, errorTextTax, paginationDownTax, paginationUpTax);
        organizationService = new OrganizationService(tableViewOrganization, nameOrganization, kppOrganization, innOrganization, addressOrganization, columnNameOrganization, columnKppOrganization, columnInnOrganization, columnAddressOrganization, client, errorTextOrganizations);
        userService.init();
        requestService.init();
        taxService.init();
        organizationService.init();
        userService.update();
    }

    public void selectUser() {
        userService.select();
    }

    public void updateTableUsers() {
        if (userService != null)
            userService.update();
    }

    public void updateUsers() {
        userService.change();
    }

    public void canceledUsers() {
        userService.canceled();
    }

    public void selectRequest() {
        requestService.select();
    }

    public void offsetUpRequest(){
        requestService.paginationUp();
    }
    public void offsetDownRequest() {
        requestService.paginationDown();
    }
    public void offsetDownTax(){
        taxService.paginationDown();
    }
    public void offsetUpTax() {
        taxService.paginationUp();
    }

    public void updateTableRequest() {
        if (requestService != null)
            requestService.update();
    }

    public void sendRequest() {
        requestService.send();
    }

    public void canceledRequest() {
        requestService.canceled();
    }
    public void selectTax(){
        taxService.select();
    }

    public void updateTableTax() {
        if (taxService != null)
            taxService.update();
    }

    public void addRecordTax() {
        taxService.add();
    }

    public void updateRecordTax() {
        taxService.change();
    }

    public void deleteRecordTax() {
        taxService.delete();
    }

    public void canceledTax() {
        taxService.canceled();
    }

    public void selectOrganization() {
        organizationService.select();
    }

    public void updateTableOrganization() {
        if (organizationService != null)
            organizationService.update();
    }

    public void addOrganization() {
        organizationService.add();
    }

    public void updateOrganization() {
        organizationService.change();
    }

    public void deleteOrganization() {
        organizationService.delete();
    }

    public void canceledOrganization() {
        organizationService.canceled();
    }

}
