package lk.ijse.photostudio.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import lk.ijse.photostudio.BO.BOFactory;
import lk.ijse.photostudio.BO.Package.PackageBO;
import lk.ijse.photostudio.DTO.PackageDTO;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class PackagePageController implements Initializable {

    @FXML
    private TextField txtPackageId, txtPackageName, txtPrice;
    @FXML
    private TextArea txtDescription;
    @FXML
    private ComboBox<String> cmbCategory;
    @FXML
    private TableView<PackageDTO> tblPackages;
    @FXML
    private TableColumn<PackageDTO, String> colPackageId, colPackageName, colDescription, colCategory;
    @FXML
    private TableColumn<PackageDTO, Double> colPrice;

    private final String PACKAGE_ID_REGEX = "^[A-Za-z0-9 ]+$";
    private final String PACKAGE_NAME_REGEX = ".{3,}";
    private final String PACKAGE_PRICE_REGEX = "^[0-9]+$";

    PackageBO packageBO = (PackageBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.PACKAGE);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colPackageId.setCellValueFactory(new PropertyValueFactory<>("packageId"));
        colPackageName.setCellValueFactory(new PropertyValueFactory<>("packageName"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("basePrice"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("packageDescription"));

        // Description Wrapping
        colDescription.setCellFactory(tc -> {
            TableCell<PackageDTO, String> cell = new TableCell<>();
            Text text = new Text();
            text.wrappingWidthProperty().bind(colDescription.widthProperty().subtract(10));
            cell.setGraphic(text);
            text.textProperty().bind(cell.itemProperty());
            return cell;
        });

        loadAllData();
    }

    private void loadAllData() {
        loadCategories();
        loadPackagesTable();
        loadNextPackageId();
    }

    private void loadCategories() {
        cmbCategory.setItems(FXCollections.observableArrayList("Wedding", "Pre Shoot", "Birthday", "Event", "Album Only", "Custom", "Other"));
    }

    @FXML
    private void handleSavePackage() {
        if (validateInput()) {
            try {
                PackageDTO dto = new PackageDTO(txtPackageId.getText(), txtPackageName.getText(), cmbCategory.getValue(), txtDescription.getText(), Double.parseDouble(txtPrice.getText()));
                if (packageBO.savePackage(dto)) {
                    new Alert(Alert.AlertType.INFORMATION, "Package Saved!").show();
                    refreshUI();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleUpdatePackage() {
        if (validateInput()) {
            try {
                PackageDTO dto = new PackageDTO(txtPackageId.getText(), txtPackageName.getText(), cmbCategory.getValue(), txtDescription.getText(), Double.parseDouble(txtPrice.getText()));
                if (packageBO.updatePackage(dto)) {
                    new Alert(Alert.AlertType.INFORMATION, "Package Updated!").show();
                    refreshUI();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleDeletePackage() {
        try {
            if (packageBO.deletePackage(txtPackageId.getText())) {
                new Alert(Alert.AlertType.INFORMATION, "Package Deleted!").show();
                refreshUI();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSearchPackage(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            try {
                PackageDTO dto = packageBO.searchPackage(txtPackageId.getText());
                if (dto != null) {
                    txtPackageId.setText(dto.getPackageId());
                    txtPackageName.setText(dto.getPackageName());
                    txtPrice.setText(String.valueOf(dto.getBasePrice()));
                    txtDescription.setText(dto.getPackageDescription());
                    cmbCategory.setValue(dto.getCategory());
                } else {
                    new Alert(Alert.AlertType.WARNING, "Not Found").show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void loadPackagesTable() {
        try {
            ArrayList<PackageDTO> list = packageBO.getAllPackages();
            tblPackages.setItems(FXCollections.observableArrayList(list));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadNextPackageId() {
        try {
            txtPackageId.setText(packageBO.generateNextPackageId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void refreshUI() {
        txtPackageId.clear();
        txtPackageName.clear();
        txtPrice.clear();
        txtDescription.clear();
        cmbCategory.setValue(null);
        loadNextPackageId();
        loadPackagesTable();
    }

    private boolean validateInput() {
        if (!txtPackageId.getText().matches(PACKAGE_ID_REGEX)) return false;
        if (!txtPackageName.getText().matches(PACKAGE_NAME_REGEX)) return false;
        if (!txtPrice.getText().matches(PACKAGE_PRICE_REGEX)) return false;
        if (cmbCategory.getValue() == null) return false;
        return true;
    }

    @FXML
    private void handleTableClick() {
        PackageDTO selected = tblPackages.getSelectionModel().getSelectedItem();
        if (selected != null) {
            txtPackageId.setText(selected.getPackageId());
            txtPackageName.setText(selected.getPackageName());
            cmbCategory.setValue(selected.getCategory());
            txtPrice.setText(String.valueOf(selected.getBasePrice()));
            txtDescription.setText(selected.getPackageDescription());
        }
    }

    @FXML
    private void handleResetPackage() {
        refreshUI();
    }

    private DashboardController dashboardController;

    public void setDashboardController(DashboardController dashboardController) {
        this.dashboardController = dashboardController;
    }

    @FXML
    private void onClickAdditionalServices() {
        try {
            dashboardController.loadPackageOptions();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}