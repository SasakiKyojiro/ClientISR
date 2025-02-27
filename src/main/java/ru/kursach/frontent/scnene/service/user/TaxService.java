package ru.kursach.frontent.scnene.service.user;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.kursach.frontent.dto.Tax;
import ru.kursach.frontent.http.api.UserClient;
import ru.kursach.frontent.scnene.service.BaseService;

import java.io.IOException;

@Slf4j
@AllArgsConstructor
public class TaxService extends BaseService<Tax> {
    private UserClient client;
    private TableView<Tax> tableView;
    private TableColumn<Tax, String> columnNameTax, columnSumTax, columnDataTax, columnStatusTax;
    private Text textError;

    public void init() {
        columnNameTax.setCellValueFactory(new PropertyValueFactory<>("taxType"));
        columnSumTax.setCellValueFactory(new PropertyValueFactory<>("sum"));
        columnDataTax.setCellValueFactory(new PropertyValueFactory<>("payingDeadline"));
        columnStatusTax.setCellValueFactory(new PropertyValueFactory<>("status"));
        super.textError = textError;
        client.addPropertyChangeListener(evt -> {
                    if ("uuid".equals(evt.getPropertyName())) update();
                }
        );
    }

    public void update() {
        fetchData();
    }


    @Override
    protected String getClientResponse() throws IOException {
        return client.getTax();
    }

    @Override
    protected TableView<Tax> getTableView() {
        return tableView;
    }

    @Override
    protected Class<Tax> getTableViewDataClass() {
        return Tax.class;
    }

}
