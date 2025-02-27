package ru.kursach.frontent.scnene.service.worker;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.kursach.frontent.dto.Tax;
import ru.kursach.frontent.dto.enams.TaxStatus;
import ru.kursach.frontent.http.api.WorkerClient;
import ru.kursach.frontent.scnene.service.BaseService;

import java.io.IOException;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

@Slf4j
@AllArgsConstructor
public class TaxService extends BaseService<Tax> {
    private TableView<Tax> tableViewTax;
    private TableColumn<Tax, String> columnTypeTax, columnSumTax, columnStatusTax, columnNameOrganizationsTax, columnFIOTax, columnDateTax;
    private DatePicker dateTax;
    private TextField sumTax;
    private ComboBox<String> nameOrganizationTax, fioTax, typeTax;
    private ComboBox<TaxStatus> statusTax;
    private final WorkerClient client;
    private Text errorText;
    private Button paginationDownTax, paginationUpTax;
    private final Tax duplicate = new Tax();

    public void init(){
        columnDateTax.setCellValueFactory(new PropertyValueFactory<>("payingDeadline"));
        columnNameOrganizationsTax.setCellValueFactory(new PropertyValueFactory<>("organizationName"));
        columnFIOTax.setCellValueFactory(new PropertyValueFactory<>("userName"));
        columnTypeTax.setCellValueFactory(new PropertyValueFactory<>("taxType"));
        columnStatusTax.setCellValueFactory(new PropertyValueFactory<>("status"));
        columnSumTax.setCellValueFactory(new PropertyValueFactory<>("sum"));
        super.textError = errorText;
        Pattern pattern = Pattern.compile("-?\\d*(\\.\\d*)?");
        UnaryOperator<TextFormatter.Change> filter = change ->
                pattern.matcher(change.getControlNewText()).matches() ? change : null;

        sumTax.setTextFormatter(new TextFormatter<>(filter));
        statusTax.setItems(FXCollections.observableArrayList(TaxStatus.values()));
        addListeners();

    }

    private boolean isAnyFieldEmpty() {
            return sumTax.getText().isEmpty() || dateTax.getValue() == null
                    || nameOrganizationTax.getValue() == null || fioTax.getValue() == null
                    || typeTax.getValue() == null || statusTax.getValue() == null;
    }

    private void highlightEmptyFields() {
        highlightField(nameOrganizationTax, nameOrganizationTax.getValue() == null);
        highlightField(dateTax, dateTax.getValue() == null);
        highlightField(fioTax, fioTax.getValue() == null);
        highlightField(typeTax, typeTax.getValue() == null);
        highlightField(statusTax, statusTax.getValue() == null);
        highlightField(sumTax, sumTax.getText().isEmpty());
    }

    private void addListeners() {
        addFieldListener(nameOrganizationTax, duplicate::getOrganizationName);
        addFieldListener(dateTax, duplicate::getPayingDeadline);
        addFieldListener(fioTax, duplicate::getUserName);
        addFieldListener(typeTax, duplicate::getTaxType);
        addFieldListener(statusTax, duplicate::getStatus);
        addFieldListener(sumTax, duplicate::getSum);
    }

    @Override
    protected String getClientResponse() throws IOException {
        return client.getAllTax();
    }

    @Override
    protected TableView<Tax> getTableView() {
        return tableViewTax;
    }

    @Override
    protected Class<Tax> getTableViewDataClass() {
        return Tax.class;
    }

    public void update() {
        canceled();
        fetchData();
        updateComboBox();
    }

    public void add() {
        if (isAnyFieldEmpty()) {
            highlightEmptyFields();
        }
        else {
            Tax tax = new Tax(fioTax.getValue(),nameOrganizationTax.getValue(), sumTax.getText(), typeTax.getValue(), dateTax.getValue(), statusTax.getValue());
            try {
                client.addTax(tax);
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
            Tax selectedTax = tableViewTax.getSelectionModel().getSelectedItem();
            if (selectedTax != null) {
                selectedTax.setUserName(fioTax.getValue());
                selectedTax.setTaxType(typeTax.getValue());
                selectedTax.setStatus(statusTax.getValue());
                selectedTax.setOrganizationName(nameOrganizationTax.getValue());
                selectedTax.setPayingDeadline(dateTax.getValue());
                selectedTax.setSum(sumTax.getText());
                try {

                    client.changeTax(selectedTax);
                    update();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public void delete() {
        Tax selectedTax = tableViewTax.getSelectionModel().getSelectedItem();
        if (selectedTax != null) {
            try {
                client.deleteTax(selectedTax);
                update();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void canceled() {
        fioTax.setValue(null);
        typeTax.setValue(null);
        statusTax.setValue(null);
        sumTax.clear();
        dateTax.setValue(null);
        nameOrganizationTax.setValue(null);
    }

    public void select() {
        Tax selectedTax = tableViewTax.getSelectionModel().getSelectedItem();
        if (selectedTax != null) {
            duplicate.setTax(selectedTax);
            nameOrganizationTax.setValue(selectedTax.getOrganizationName());
            fioTax.setValue(selectedTax.getUserName());
            typeTax.setValue(selectedTax.getTaxType());
            dateTax.setValue(selectedTax.getPayingDeadline());
            sumTax.setText(String.valueOf(selectedTax.getSum()));
            statusTax.setValue(selectedTax.getStatus());
        }
    }

    private void updateComboBox(){
        ObservableList<Tax> items = tableViewTax.getItems();
        nameOrganizationTax.getItems().clear();
        fioTax.getItems().clear();
        typeTax.getItems().clear();
        for (Tax tax : items) {
            nameOrganizationTax.getItems().add(tax.getOrganizationName());
            fioTax.getItems().add(tax.getUserName());
            typeTax.getItems().add(tax.getTaxType());
        }
    }

    public void paginationDown() {
        paginationUpTax.setDisable(false);
        ObservableList<Tax> itemsDump = tableViewTax.getItems();
        if (client.offsetDown()){
            update();
        }
        else {
            tableViewTax.setItems(itemsDump);
            update();
            paginationDownTax.setDisable(true);
        }

    }

    public void paginationUp() {
        paginationDownTax.setDisable(false);
        client.offsetUp();
        ObservableList<Tax> itemsDump = tableViewTax.getItems();
        update();
        ObservableList<Tax> items = tableViewTax.getItems();
        if(items.isEmpty()){
            tableViewTax.setItems(itemsDump);
            paginationUpTax.setDisable(true);
            client.offsetDown();
            update();
        }
    }
}
