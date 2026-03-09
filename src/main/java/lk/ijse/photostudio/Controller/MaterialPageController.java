package lk.ijse.photostudio.Controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import lk.ijse.photostudio.BO.BOFactory;
import lk.ijse.photostudio.BO.Material.MaterialBO;
import lk.ijse.photostudio.DTO.MaterialDTO;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MaterialPageController implements Initializable {

    @FXML
    private TextField txtMaterialId, txtMaterialName, txtQty, txtSearch, txtReduceQty;
    @FXML
    private ComboBox<String> cmbSupplier;
    @FXML
    private TableView<MaterialDTO> tblMaterials;
    @FXML
    private TableColumn<MaterialDTO, String> colMaterialId, colMaterialName, colSupplier;
    @FXML
    private TableColumn<MaterialDTO, Integer> colQty;

    private final String MAT_ID_REGEX = "^[A-Za-z0-9 ]+$";
    private final String QTY_REGEX = "^[0-9]+$";

    MaterialBO materialBO = (MaterialBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.MATERIAL);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colMaterialId.setCellValueFactory(new PropertyValueFactory<>("materialId"));
        colMaterialName.setCellValueFactory(new PropertyValueFactory<>("materialName"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colSupplier.setCellValueFactory(new PropertyValueFactory<>("supplierId"));

        loadAllMaterials();
        loadNextMaterialId();
        loadSupplierIds();
    }

    private void loadSupplierIds() {
        try {
            ArrayList<String> ids = materialBO.getAllSupplierIds();
            cmbSupplier.setItems(FXCollections.observableArrayList(ids));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleReduceStock() {
        if (txtMaterialId.getText().isEmpty() || txtReduceQty.getText().isEmpty() || !txtReduceQty.getText().matches(QTY_REGEX)) {
            new Alert(Alert.AlertType.WARNING, "Invalid input for reduction").show();
            return;
        }
        try {
            int currentQty = Integer.parseInt(txtQty.getText());
            int usedQty = Integer.parseInt(txtReduceQty.getText());
            if (usedQty > currentQty) {
                new Alert(Alert.AlertType.ERROR, "Insufficient stock").show();
                return;
            }
            int newQty = currentQty - usedQty;
            MaterialDTO dto = new MaterialDTO(txtMaterialId.getText(), txtMaterialName.getText(), newQty, cmbSupplier.getValue());
            if (materialBO.updateMaterial(dto)) {
                new Alert(Alert.AlertType.INFORMATION, "Stock Updated").show();
                refreshUI();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSave() {
        if (validateInput()) {
            try {
                boolean isSaved = materialBO.saveMaterial(new MaterialDTO(txtMaterialId.getText(), txtMaterialName.getText(), Integer.parseInt(txtQty.getText()), cmbSupplier.getValue()));
                if (isSaved) {
                    new Alert(Alert.AlertType.INFORMATION, "Saved!").show();
                    refreshUI();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleUpdate() {
        if (validateInput()) {
            try {
                boolean isUpdated = materialBO.updateMaterial(new MaterialDTO(txtMaterialId.getText(), txtMaterialName.getText(), Integer.parseInt(txtQty.getText()), cmbSupplier.getValue()));
                if (isUpdated) {
                    new Alert(Alert.AlertType.INFORMATION, "Updated!").show();
                    refreshUI();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleDelete() {
        try {
            if (materialBO.deleteMaterial(txtMaterialId.getText())) {
                new Alert(Alert.AlertType.INFORMATION, "Deleted!").show();
                refreshUI();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSearchMaterial(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            String term = txtSearch.getText().isEmpty() ? txtMaterialId.getText() : txtSearch.getText();
            try {
                MaterialDTO dto = materialBO.searchMaterial(term);
                if (dto != null) {
                    txtMaterialId.setText(dto.getMaterialId());
                    txtMaterialName.setText(dto.getMaterialName());
                    txtQty.setText(String.valueOf(dto.getQty()));
                    cmbSupplier.setValue(dto.getSupplierId());
                } else {
                    new Alert(Alert.AlertType.WARNING, "Not Found").show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void loadAllMaterials() {
        try {
            ArrayList<MaterialDTO> allMaterials = materialBO.getAllMaterials();
            tblMaterials.setItems(FXCollections.observableArrayList(allMaterials));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadNextMaterialId() {
        try {
            txtMaterialId.setText(materialBO.generateNextMaterialId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void refreshUI() {
        txtMaterialId.clear();
        txtMaterialName.clear();
        txtQty.clear();
        txtReduceQty.clear();
        cmbSupplier.setValue(null);
        txtSearch.clear();
        loadNextMaterialId();
        loadAllMaterials();
    }

    private boolean validateInput() {
        return txtMaterialId.getText().matches(MAT_ID_REGEX) && !txtMaterialName.getText().isEmpty() && txtQty.getText().matches(QTY_REGEX);
    }

    @FXML
    private void handleTableClick(MouseEvent event) {
        MaterialDTO selected = tblMaterials.getSelectionModel().getSelectedItem();
        if (selected != null) {
            txtMaterialId.setText(selected.getMaterialId());
            txtMaterialName.setText(selected.getMaterialName());
            txtQty.setText(String.valueOf(selected.getQty()));
            cmbSupplier.setValue(selected.getSupplierId());
        }
    }

    @FXML
    private void handleReset() {
        refreshUI();
    }
}