package ru.kursach.frontent.scnene.service.worker;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.kursach.frontent.dto.Organization;
import ru.kursach.frontent.http.api.WorkerClient;
import ru.kursach.frontent.scnene.service.BaseService;

import java.io.IOException;

@Slf4j
@AllArgsConstructor
public class OrganizationService extends BaseService<Organization> {
    private final Organization duplicate = new Organization();
    private TableView<Organization> tableViewOrganization;
    private TextField nameOrganization, kppOrganization, innOrganization, addressOrganization;
    private TableColumn<Organization, String> columnNameOrganization, columnKppOrganization, columnInnOrganization, columnAddressOrganization;
    private final WorkerClient client;
    private Text errorText;

    public void init() {
        columnNameOrganization.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnKppOrganization.setCellValueFactory(new PropertyValueFactory<>("kpp"));
        columnInnOrganization.setCellValueFactory(new PropertyValueFactory<>("inn"));
        columnAddressOrganization.setCellValueFactory(new PropertyValueFactory<>("address"));
        super.textError = errorText;
        addFieldListener(nameOrganization, duplicate::getName);
        addFieldListener(kppOrganization, duplicate::getKpp);
        addFieldListener(innOrganization, duplicate::getInn);
        addFieldListener(addressOrganization, duplicate::getAddress);
    }

    public void canceled() {
        nameOrganization.clear();
        kppOrganization.clear();
        innOrganization.clear();
        addressOrganization.clear();
        resetFieldStyle(nameOrganization);
        resetFieldStyle(kppOrganization);
        resetFieldStyle(innOrganization);
        resetFieldStyle(addressOrganization);
    }

    public void update() {
        canceled();
        fetchData();
    }


    private boolean isAnyFieldEmpty() {
        return nameOrganization.getText().isEmpty() || kppOrganization.getText().isEmpty()
                || innOrganization.getText().isEmpty() || addressOrganization.getText().isEmpty();
    }

    private void highlightEmptyFields() {
        highlightField(nameOrganization, nameOrganization.getText().isEmpty());
        highlightField(kppOrganization, kppOrganization.getText().isEmpty());
        highlightField(innOrganization, innOrganization.getText().isEmpty());
        highlightField(addressOrganization, addressOrganization.getText().isEmpty());
    }

    @Override
    protected String getClientResponse() throws IOException {
        return client.getAllOrganizations();
    }

    @Override
    protected TableView<Organization> getTableView() {
        return tableViewOrganization;
    }

    @Override
    protected Class<Organization> getTableViewDataClass() {
        return Organization.class;
    }

    public void add() {
        if(isAnyFieldEmpty()) {
            highlightEmptyFields();
        }
        else {
            Organization organization = new Organization();
            organization.setAddress(addressOrganization.getText());
            organization.setKpp(kppOrganization.getText());
            organization.setInn(innOrganization.getText());
            organization.setName(nameOrganization.getText());
            try {
                client.addOrganizations(organization);
                update();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void change() {
        if (isAnyFieldEmpty()) {
            highlightEmptyFields();
        }
        else {
            Organization organization = tableViewOrganization.getSelectionModel().getSelectedItem();
            organization.setAddress(addressOrganization.getText());
            organization.setKpp(kppOrganization.getText());
            organization.setInn(innOrganization.getText());
            organization.setName(nameOrganization.getText());
            try {
                client.changeOrganization(organization);
                update();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void delete() {
        Organization organization = tableViewOrganization.getSelectionModel().getSelectedItem();
        if(organization != null) {
            try {
                client.deleteOrganization(organization);
                update();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void select() {
        Organization organization = tableViewOrganization.getSelectionModel().getSelectedItem();
        if(organization != null) {
            duplicate.setOrganization(organization);
            nameOrganization.setText(organization.getName());
            kppOrganization.setText(organization.getKpp());
            innOrganization.setText(organization.getInn());
            addressOrganization.setText(organization.getAddress());
        }
    }
}
