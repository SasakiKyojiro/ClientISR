package ru.kursach.frontent.http.api;

import ru.kursach.frontent.dto.Organization;
import ru.kursach.frontent.dto.Request;
import ru.kursach.frontent.dto.Tax;
import ru.kursach.frontent.dto.User;
import ru.kursach.frontent.http.Client;

import java.io.IOException;

public class WorkerClient extends Client {
    private final String url = baseUrl + "/worker/";
    public String getAllOrganizations() throws IOException {
        return get(url + "organizations");
    }

    public String getAllRequest() throws IOException {
        return get(url+"requests?limit="+limit+"&offset="+offset);
    }

    public String getAllTax() throws IOException {
        return get(url+"tax-assessments?limit="+limit+"&offset="+offset);
    }

    public String getAllUser() throws IOException {
        return get(url+"users");
    }

    public void changeUser(User selectedUser) throws IOException {
        put(url+"user/"+selectedUser.getId(), selectedUser);
    }

    public void sendRequest(Request selectedRequest) throws IOException {
        put(url+"request/"+selectedRequest.getId()+"/status?newStatus=" + selectedRequest.getStatus().name());
    }

    public void addOrganizations(Organization organization) throws IOException {
        post(url+"organization", organization);
    }

    public void addTax(Tax tax) throws IOException {
        post(url+"tax-assessment", tax);
    }
    public void changeOrganization(Organization organization) throws IOException {
        put(url+"organization/"+organization.getId().toString(), organization);

    }
    public void changeTax(Tax tax) throws IOException {
        put(url+"tax-assessment/"+tax.getId(), tax);
    }
    public void deleteOrganization(Organization organization) throws IOException {
        delete(url+"organization/"+organization.getId());
    }
    public void deleteTax(Tax tax) throws IOException {
        delete(url+"tax-assessment/"+tax.getId());
    }
}
