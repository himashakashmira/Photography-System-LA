package lk.ijse.photostudio.Controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import lk.ijse.photostudio.BO.BOFactory;
import lk.ijse.photostudio.BO.Supplier.SupplierBO;
import lk.ijse.photostudio.DTO.SupplierDTO;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SupplierPageController implements Initializable {

    @FXML
    private TextField SupId, SupName, SupMaterial, SupContact;
    @FXML
    private TableView<SupplierDTO> tblSupplier;
    @FXML
    private TableColumn<SupplierDTO, String> colSupplierId, colSupplierName, colSupplierMaterial, colSupplierContact;

    private final String SUPPLIER_ID_REGEX = "^[A-Za-z0-9 ]+$";
    private final String SUPPLIER_NAME_REGEX = "^[A-Za-z]{3,}$";
    private final String SUPPLIER_MATERIAL_REGEX = "^[A-Za-z0-9 ]+$";
    private final String SUPPLIER_CONTACT_REGEX = "^[0-9]{10}$";

    SupplierBO supplierBO = (SupplierBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.SUPPLIER);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colSupplierId.setCellValueFactory(new PropertyValueFactory<>("supplierId"));
        colSupplierName.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
        colSupplierMaterial.setCellValueFactory(new PropertyValueFactory<>("materialType"));
        colSupplierContact.setCellValueFactory(new PropertyValueFactory<>("supplierContact"));

        loadAllSuppliers();
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
        if (validateInput()) {
            try {
                boolean result = supplierBO.saveSupplier(new SupplierDTO(SupId.getText(), SupName.getText(), SupMaterial.getText(), SupContact.getText()));
                if (result) {
                    new Alert(Alert.AlertType.INFORMATION, "Supplier Saved!").show();
                    refreshUI();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleUpdateSupplier() {
        if (validateInput()) {
            try {
                boolean result = supplierBO.updateSupplier(new SupplierDTO(SupId.getText(), SupName.getText(), SupMaterial.getText(), SupContact.getText()));
                if (result) {
                    new Alert(Alert.AlertType.INFORMATION, "Supplier Updated!").show();
                    refreshUI();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleDeleteSupplier() {
        try {
            if (supplierBO.deleteSupplier(SupId.getText())) {
                new Alert(Alert.AlertType.INFORMATION, "Supplier Deleted!").show();
                refreshUI();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSearchSupplier(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            try {
                SupplierDTO dto = supplierBO.searchSupplier(SupId.getText());
                if (dto != null) {
                    SupId.setText(dto.getSupplierId());
                    SupName.setText(dto.getSupplierName());
                    SupMaterial.setText(dto.getMaterialType());
                    SupContact.setText(dto.getSupplierContact());
                } else {
                    new Alert(Alert.AlertType.WARNING, "Not Found").show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void loadAllSuppliers() {
        try {
            ArrayList<SupplierDTO> list = supplierBO.getAllSuppliers();
            tblSupplier.setItems(FXCollections.observableArrayList(list));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadNextSupplierId() {
        try {
            SupId.setText(supplierBO.generateNextSupplierId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void refreshUI() {
        SupId.clear();
        SupName.clear();
        SupMaterial.clear();
        SupContact.clear();
        loadAllSuppliers();
        loadNextSupplierId();
    }

    private boolean validateInput() {
        if (!SupId.getText().matches(SUPPLIER_ID_REGEX)) return false;
        if (!SupName.getText().matches(SUPPLIER_NAME_REGEX)) return false;
        if (!SupContact.getText().matches(SUPPLIER_CONTACT_REGEX)) return false;
        return true;
    }

    @FXML
    private void handleResetSupplier() {
        refreshUI();
    }
}