package lk.ijse.photostudio.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.TextField;
import lk.ijse.photostudio.DTO.SupplierDTO;
import lk.ijse.photostudio.Model.SupplierModel;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class SupplierPageController implements Initializable {

    @FXML
    private TextField SupId;

    @FXML
    private TextField SupName;

    @FXML
    private TextField SupMaterial;

    @FXML
    private TextField SupContact;

    @FXML
    private TableView<SupplierDTO> tblSupplier;

    @FXML
    private TableColumn<SupplierDTO, String> colSupplierId;

    @FXML
    private TableColumn<SupplierDTO, String> colSupplierName;

    @FXML
    private TableColumn<SupplierDTO, String> colSupplierMaterial;

    @FXML
    private TableColumn<SupplierDTO, String> colSupplierContact;


    private final String SUPPLIER_ID_REGEX = "^[A-Za-z0-9 ]+$";
    private final String SUPPLIER_NAME_REGEX = "^[A-Za-z]{3,}$";
    private final String SUPPLIER_MATERIAL_REGEX = "^[A-Za-z0-9 ]+$";
    private final String SUPPLIER_CONTACT_REGEX = "^[0-9]{10}$";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Supplier Page is loaded!");

        colSupplierId.setCellValueFactory(new PropertyValueFactory<>("supplierId"));
        colSupplierName.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
        colSupplierMaterial.setCellValueFactory(new PropertyValueFactory<>("materialType"));
        colSupplierContact.setCellValueFactory(new PropertyValueFactory<>("supplierContact"));

        tblSupplier.setItems(loadAllSuppliers());

        loadNextSupplierId();
    }

    @FXML
    private void handleTableClick() {
        SupplierDTO selectedItem = tblSupplier.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            SupId.setText(selectedItem.getSupplierId());
            SupName.setText(selectedItem.getSupplierName());
            SupMaterial.setText(selectedItem.getMaterialType());
            SupContact.setText(selectedItem.getSupplierContact());
        }
    }


    @FXML
    private void handleSaveSupplier() {
        String id = SupId.getText().trim();
        String name = SupName.getText().trim();
        String material = SupMaterial.getText().trim();
        String contact = SupContact.getText().trim();

        if (!id.matches(SUPPLIER_ID_REGEX)) {
            new Alert(Alert.AlertType.ERROR, "Please enter an ID!").show();
        } else if (!name.matches(SUPPLIER_NAME_REGEX)) {
            new Alert(Alert.AlertType.ERROR, "Please enter a valid name (at least 3 characters)! ").show();
        } else if (!material.matches(SUPPLIER_MATERIAL_REGEX)) {
            new Alert(Alert.AlertType.ERROR, "Please enter a valid material! ").show();
        } else if (!contact.matches(SUPPLIER_CONTACT_REGEX)) {
            new Alert(Alert.AlertType.ERROR, "Please enter a valid contact! ").show();
        } else {

            try {
                SupplierModel supplierModel = new SupplierModel();
                boolean result = supplierModel.saveSupplier(new SupplierDTO(id, name, material, contact));

                if (result) {
                    new Alert(Alert.AlertType.INFORMATION, "Supplier Saved Successfully!").show();
                    clearFields();
                    loadAllSuppliers();
                    loadNextSupplierId();
                } else {
                    new Alert(Alert.AlertType.WARNING, "Failed to Save Supplier!").show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Something Went Wrong!" + e.getMessage()).show();
            }

        }
    }

    @FXML
    private void handleUpdateSupplier() {
        String id = SupId.getText().trim();
        String name = SupName.getText().trim();
        String material = SupMaterial.getText().trim();
        String contact = SupContact.getText().trim();

        if (!id.matches(SUPPLIER_ID_REGEX)) {
            new Alert(Alert.AlertType.ERROR, "Please enter an ID!").show();
        } else if (!name.matches(SUPPLIER_NAME_REGEX)) {
            new Alert(Alert.AlertType.ERROR, "Please enter a valid name (at least 3 characters)! ").show();
        } else if (!material.matches(SUPPLIER_MATERIAL_REGEX)) {
            new Alert(Alert.AlertType.ERROR, "Please enter a valid material! ").show();
        } else if (!contact.matches(SUPPLIER_CONTACT_REGEX)) {
            new Alert(Alert.AlertType.ERROR, "Please enter a valid contact! ").show();
        } else {

            try {
                SupplierModel supplierModel = new SupplierModel();
                boolean result = supplierModel.updateSupplier(new SupplierDTO(id, name, material, contact));

                if (result) {
                    new Alert(Alert.AlertType.INFORMATION, "Supplier Updated Successfully!").show();
                    clearFields();
                    loadAllSuppliers();
                    loadNextSupplierId();
                } else {
                    new Alert(Alert.AlertType.WARNING, "Failed to Update Supplier!").show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Something Went Wrong!" + e.getMessage()).show();
            }
        }
    }

    @FXML
    private void handleDeleteSupplier() {
        String id = SupId.getText().trim();

        if (!id.matches(SUPPLIER_ID_REGEX)) {
            new Alert(Alert.AlertType.ERROR, "Please enter an ID!").show();
        } else {
            try {
                SupplierModel supplierModel = new SupplierModel();
                boolean result = supplierModel.deleteSupplier(id);

                if (result) {
                    new Alert(Alert.AlertType.INFORMATION, "Supplier Deleted Successfully!").show();
                    clearFields();
                    loadAllSuppliers();
                    loadNextSupplierId();
                } else {
                    new Alert(Alert.AlertType.WARNING, "Failed to Delete Supplier!").show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Something Went Wrong!" + e.getMessage()).show();
            }
        }
    }

    @FXML
    private void handleSearchSupplier(KeyEvent event) {
        try {
            if (event.getCode() == KeyCode.ENTER) {

                String id = SupId.getText().trim();

                if (!id.matches(SUPPLIER_ID_REGEX)) {
                    new Alert(Alert.AlertType.ERROR, "Please enter an ID!").show();
                } else {
                    SupplierModel supplierModel = new SupplierModel();
                    SupplierDTO supplierDTO = supplierModel.searchSupplier(id);

                    if (supplierDTO != null) {
                        SupId.setText(supplierDTO.getSupplierId());
                        SupName.setText(supplierDTO.getSupplierName());
                        SupMaterial.setText(supplierDTO.getMaterialType());
                        SupContact.setText(supplierDTO.getSupplierContact());
                    } else {
                        new Alert(Alert.AlertType.WARNING, "Supplier Not Found!").show();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Something Went Wrong!" + e.getMessage()).show();
        }
    }

    @FXML
    private void handleResetSupplier() {
        clearFields();
        loadNextSupplierId();
    }

    @FXML
    private void clearFields() {
        SupId.clear();
        SupName.clear();
        SupMaterial.clear();
        SupContact.clear();
    }

    private ObservableList<SupplierDTO> loadAllSuppliers() {
        try {
            SupplierModel supplierModel = new SupplierModel();
            ObservableList<SupplierDTO> obList = supplierModel.getSuppliers();
            tblSupplier.setItems(obList);
            return obList;
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Something went wrong!").show();
            return FXCollections.observableArrayList();
        }

    }

    private void loadNextSupplierId() {
        try {
            SupplierModel supplierModel = new SupplierModel();
            SupId.setText(supplierModel.getNextSupplierId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

