package lk.ijse.photostudio.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import lk.ijse.photostudio.BO.BOFactory;
import lk.ijse.photostudio.BO.Customer.CustomerBO;
import lk.ijse.photostudio.DTO.CustomerDTO;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class CustomerPageController implements Initializable {

    @FXML
    private TextField txtId;
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtPhone;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextArea txtAddress;

    // Table View
    @FXML
    private TableView<CustomerDTO> tblCustomers;
    @FXML
    private TableColumn<CustomerDTO, String> colId;
    @FXML
    private TableColumn<CustomerDTO, String> colName;
    @FXML
    private TableColumn<CustomerDTO, String> colPhone;
    @FXML
    private TableColumn<CustomerDTO, String> colEmail;
    @FXML
    private TableColumn<CustomerDTO, String> colAddress;

    private final String CUSTOMER_ID_REGEX = "^[A-Za-z0-9 ]+$";
    private final String CUSTOMER_NAME_REGEX = "^[A-Za-z]{3,}$";
    private final String CUSTOMER_PHONE_REGEX = "^[0-9]{10}$";
    private final String CUSTOMER_EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    private final String CUSTOMER_ADDRESS_REGEX = "^[A-Za-z0-9 ]{5,}$";

    CustomerBO customerBO = (CustomerBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.CUSTOMER);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));

        loadCustomersTable();
        loadNextCustomerId();
    }

    @FXML
    private void handleTableClick(MouseEvent event) {
        CustomerDTO selectedCustomer = tblCustomers.getSelectionModel().getSelectedItem();

        if (selectedCustomer != null) {
            txtId.setText(selectedCustomer.getId());
            txtName.setText(selectedCustomer.getName());
            txtPhone.setText(selectedCustomer.getPhone());
            txtAddress.setText(selectedCustomer.getAddress());
            txtEmail.setText(selectedCustomer.getEmail());
        }
    }

    @FXML
    private void handleSaveCustomer() {
        String id = txtId.getText().trim();
        String name = txtName.getText().trim();
        String phone = txtPhone.getText().trim();
        String email = txtEmail.getText().trim();
        String address = txtAddress.getText().trim();

        if (validateFields(id, name, phone, email, address)) {
            try {

                boolean result = customerBO.saveCustomer(new CustomerDTO(id, name, phone, email, address));

                if (result) {
                    new Alert(Alert.AlertType.INFORMATION, "Customer Saved Successfully!").show();
                    refreshUI();
                } else {
                    new Alert(Alert.AlertType.WARNING, "Failed to save customer!").show();
                }
            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR, "Something Went Wrong: " + e.getMessage()).show();
            }
        }
    }

    @FXML
    private void handleUpdateCustomer() {
        String id = txtId.getText().trim();
        String name = txtName.getText().trim();
        String phone = txtPhone.getText().trim();
        String email = txtEmail.getText().trim();
        String address = txtAddress.getText().trim();

        if (validateFields(id, name, phone, email, address)) {
            try {

                boolean result = customerBO.updateCustomer(new CustomerDTO(id, name, phone, email, address));

                if (result) {
                    new Alert(Alert.AlertType.INFORMATION, "Customer Updated Successfully!").show();
                    refreshUI();
                } else {
                    new Alert(Alert.AlertType.WARNING, "Failed to Update customer!").show();
                }
            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR, "Error: " + e.getMessage()).show();
            }
        }
    }

    @FXML
    private void handleDeleteCustomer() {
        String id = txtId.getText().trim();
        if (id.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Please enter an ID!").show();
            return;
        }

        try {

            boolean result = customerBO.deleteCustomer(id);
            if (result) {
                new Alert(Alert.AlertType.INFORMATION, "Customer Deleted Successfully!").show();
                refreshUI();
            } else {
                new Alert(Alert.AlertType.WARNING, "Failed to Delete customer!").show();
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Error: " + e.getMessage()).show();
        }
    }

    @FXML
    private void handleResetCustomer() {
        clearFields();
        loadNextCustomerId();
    }

    private void clearFields() {
        txtId.clear();
        txtName.clear();
        txtPhone.clear();
        txtEmail.clear();
        txtAddress.clear();
    }

    private void refreshUI() {
        clearFields();
        loadCustomersTable();
        loadNextCustomerId();
    }

    private void loadCustomersTable() {
        try {
            ArrayList<CustomerDTO> allCustomers = customerBO.getAllCustomers();

            System.out.println("Customers loaded: " + allCustomers.size());

            ObservableList<CustomerDTO> obList = FXCollections.observableArrayList(allCustomers);
            tblCustomers.setItems(obList);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadNextCustomerId() {
        try {

            txtId.setText(customerBO.generateNewCustomerId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean validateFields(String id, String name, String phone, String email, String address) {
        if (!id.matches(CUSTOMER_ID_REGEX)) {
            new Alert(Alert.AlertType.ERROR, "Invalid ID!").show();
            return false;
        }
        if (!name.matches(CUSTOMER_NAME_REGEX)) {
            new Alert(Alert.AlertType.ERROR, "Invalid Name!").show();
            return false;
        }
        if (!phone.matches(CUSTOMER_PHONE_REGEX)) {
            new Alert(Alert.AlertType.ERROR, "Invalid Phone!").show();
            return false;
        }
        if (!email.matches(CUSTOMER_EMAIL_REGEX)) {
            new Alert(Alert.AlertType.ERROR, "Invalid Email!").show();
            return false;
        }
        if (!address.matches(CUSTOMER_ADDRESS_REGEX)) {
            new Alert(Alert.AlertType.ERROR, "Invalid Address!").show();
            return false;
        }
        return true;
    }
}