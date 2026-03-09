package lk.ijse.photostudio.Controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import lk.ijse.photostudio.BO.BOFactory;
import lk.ijse.photostudio.BO.PackageAdditional.PackageAdditionalBO;
import lk.ijse.photostudio.DTO.PackageAdditionalDTO;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class PackageAdditionalController implements Initializable {

    @FXML
    private TextField txtOptionId;
    @FXML
    private TextField txtOptionName;
    @FXML
    private TextField txtOptionPrice;
    @FXML
    private TextArea txtDescription;
    @FXML
    private TextField txtSearch;

    @FXML
    private TableView<PackageAdditionalDTO> tblOptions;
    @FXML
    private TableColumn<PackageAdditionalDTO, String> colOptionId;
    @FXML
    private TableColumn<PackageAdditionalDTO, String> colOptionName;
    @FXML
    private TableColumn<PackageAdditionalDTO, Double> colPrice;
    @FXML
    private TableColumn<PackageAdditionalDTO, String> colDescription;

    private final String OPT_ID_REGEX = "^[A-Za-z0-9 ]+$";
    private final String PRICE_REGEX = "^[0-9]+(\\.[0-9]{1,2})?$";

    PackageAdditionalBO additionalBO = (PackageAdditionalBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.PACKAGEADDITIONAL);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colOptionId.setCellValueFactory(new PropertyValueFactory<>("optionId"));
        colOptionName.setCellValueFactory(new PropertyValueFactory<>("optionName"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));

        loadAllOptions();
        loadNextOptionId();

        txtSearch.setOnKeyPressed(this::handleSearchOption);
    }

    @FXML
    private void handleTableClick() {
        PackageAdditionalDTO selectedItem = tblOptions.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            txtOptionId.setText(selectedItem.getOptionId());
            txtOptionName.setText(selectedItem.getOptionName());
            txtOptionPrice.setText(String.valueOf(selectedItem.getPrice()));
            txtDescription.setText(selectedItem.getDescription());
        }
    }

    @FXML
    private void handleSave() {
        if (validateInput()) {
            try {
                PackageAdditionalDTO dto = new PackageAdditionalDTO(txtOptionId.getText(), txtOptionName.getText(), Double.parseDouble(txtOptionPrice.getText()), txtDescription.getText());
                if (additionalBO.saveOption(dto)) {
                    new Alert(Alert.AlertType.INFORMATION, "Saved Successfully!").show();
                    resetFields();
                }
            } catch (SQLException | ClassNotFoundException e) {
                new Alert(Alert.AlertType.ERROR, "Error: " + e.getMessage()).show();
            }
        }
    }

    @FXML
    private void handleUpdate() {
        if (validateInput()) {
            try {
                PackageAdditionalDTO dto = new PackageAdditionalDTO(txtOptionId.getText(), txtOptionName.getText(), Double.parseDouble(txtOptionPrice.getText()), txtDescription.getText());
                if (additionalBO.updateOption(dto)) {
                    new Alert(Alert.AlertType.INFORMATION, "Updated Successfully!").show();
                    resetFields();
                }
            } catch (SQLException | ClassNotFoundException e) {
                new Alert(Alert.AlertType.ERROR, "Error: " + e.getMessage()).show();
            }
        }
    }

    @FXML
    private void handleDelete() {
        try {
            if (additionalBO.deleteOption(txtOptionId.getText())) {
                new Alert(Alert.AlertType.INFORMATION, "Deleted Successfully!").show();
                resetFields();
            }
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Error: " + e.getMessage()).show();
        }
    }

    @FXML
    private void handleReset() {
        resetFields();
    }

    @FXML
    private void handleSearchOption(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            String term = txtSearch.getText().isEmpty() ? txtOptionId.getText() : txtSearch.getText();
            try {
                PackageAdditionalDTO dto = additionalBO.searchOption(term);
                if (dto != null) {
                    txtOptionId.setText(dto.getOptionId());
                    txtOptionName.setText(dto.getOptionName());
                    txtOptionPrice.setText(String.valueOf(dto.getPrice()));
                    txtDescription.setText(dto.getDescription());
                } else {
                    new Alert(Alert.AlertType.WARNING, "Not Found").show();
                }
            } catch (SQLException | ClassNotFoundException e) {
                new Alert(Alert.AlertType.ERROR, "Error: " + e.getMessage()).show();
            }
        }
    }

    private void loadAllOptions() {
        try {
            tblOptions.setItems(FXCollections.observableArrayList(additionalBO.getAllOptions()));
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Error: " + e.getMessage()).show();
        }
    }

    private void loadNextOptionId() {
        try {
            txtOptionId.setText(additionalBO.generateNextOptionId());
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Error: " + e.getMessage()).show();
        }
    }

    private void resetFields() {
        txtOptionId.clear();
        txtOptionName.clear();
        txtOptionPrice.clear();
        txtDescription.clear();
        txtSearch.clear();
        loadNextOptionId();
        loadAllOptions();
    }

    private boolean validateInput() {
        return txtOptionId.getText().matches(OPT_ID_REGEX) && !txtOptionName.getText().isEmpty() && txtOptionPrice.getText().matches(PRICE_REGEX);
    }
}

