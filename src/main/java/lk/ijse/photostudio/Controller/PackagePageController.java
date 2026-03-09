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
import lk.ijse.photostudio.DTO.PackageDTO;
import lk.ijse.photostudio.Model.PackageModel;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class PackagePageController implements Initializable {

    @FXML
    private TextField txtPackageId;

    @FXML
    private TextField txtPackageName;

    @FXML
    private TextField txtPrice;

    @FXML
    private TextArea txtDescription;

    @FXML
    private ComboBox<String> cmbCategory;

// Load Table

    @FXML
    private TableView<PackageDTO> tblPackages;

    @FXML
    private TableColumn<PackageDTO, String> colPackageId;

    @FXML
    private TableColumn<PackageDTO, String> colPackageName;

    @FXML
    private TableColumn<PackageDTO, Double> colPrice;

    @FXML
    private TableColumn<PackageDTO, String> colDescription;

    @FXML
    private TableColumn<PackageDTO, String> colCategory;


    private final String PACKAGE_ID_REGEX = "^[A-Za-z0-9 ]+$";
    private final String PACKAGE_NAME_REGEX = ".{3,}";
    private final String PACKAGE_PRICE_REGEX = "^[0-9]+$";

    private final PackageModel packageModel = new PackageModel();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Package Page is loaded!");

// Load Table

        colPackageId.setCellValueFactory(new PropertyValueFactory<>("packageId"));
        colPackageName.setCellValueFactory(new PropertyValueFactory<>("packageName"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("basePrice"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("packageDescription"));


        tblPackages.setItems(loadPackagesTable());

//Description Size Change

        colDescription.setCellFactory(tc -> {
                    TableCell<PackageDTO, String> cell = new TableCell<>();
                    Text text = new Text();

                    text.wrappingWidthProperty().bind(colDescription.widthProperty().subtract(10));
                    cell.setGraphic(text);

                    cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
                    text.textProperty().bind(cell.itemProperty());

                    return cell;
                });

        tblPackages.setFixedCellSize(-1);


// Load Categories

        loadCategories();
        loadPackagesTable();
        loadNextPackageId();

    }


    private void loadCategories() {
        ObservableList<String> categories = FXCollections.observableArrayList(
                "Wedding",
                "Pre Shoot",
                "Birthday",
                "Event",
                "Album Only",
                "Custom",
                "Other"
        );

        cmbCategory.setItems(categories);

// Load Package Id

        try {
            txtPackageId.setText(packageModel.getNextPackageId());
        } catch (SQLException e) {
            System.err.println("Error loading next package ID: " + e.getMessage());
        }

        loadNextPackageId();
    }

    @FXML
    private void handleTableClick() {
        PackageDTO selectedItem = tblPackages.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            txtPackageId.setText(selectedItem.getPackageId());
            txtPackageName.setText(selectedItem.getPackageName());
            cmbCategory.setValue(selectedItem.getCategory());
            txtPrice.setText(String.valueOf(selectedItem.getBasePrice()));
            txtDescription.setText(selectedItem.getPackageDescription());
        }
    }

    @FXML
    private void handleSavePackage() {
        String id = txtPackageId.getText().trim();
        String name = txtPackageName.getText().trim();
        String price = txtPrice.getText().trim();
        String description = txtDescription.getText().trim();

        if (!id.matches(PACKAGE_ID_REGEX)) {
            new Alert(Alert.AlertType.ERROR, "Please enter an ID!").show();
        } else if (!name.matches(PACKAGE_NAME_REGEX)) {
            new Alert(Alert.AlertType.ERROR, "Please enter a valid name (at least 3 characters)!").show();
        } else if (!price.matches(PACKAGE_PRICE_REGEX)) {
            new Alert(Alert.AlertType.ERROR, "Please enter a valid price!").show();
        } else {
            String category = cmbCategory.getValue();
            if (category == null || category.isEmpty()) {
                new Alert(Alert.AlertType.ERROR, "Please select a category!").show();
                return;
            }
            try {
                PackageDTO packageDTO = new PackageDTO(id, name, category, description, Double.parseDouble(price));
                boolean result = packageModel.savePackage(packageDTO);

                if (result) {
                    new Alert(Alert.AlertType.INFORMATION, "Package Saved Successfully!").show();
                    clearFields();
                    loadPackagesTable();
                    loadNextPackageId();
                } else {
                    new Alert(Alert.AlertType.WARNING, "Failed to save package!").show();
                }
            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR, "Something Went Wrong!" + e.getMessage()).show();
            }
        }
    }

    @FXML
    private void handleUpdatePackage() {
        String id = txtPackageId.getText().trim();
        String name = txtPackageName.getText().trim();
        String price = txtPrice.getText().trim();
        String description = txtDescription.getText().trim();

        if (!id.matches(PACKAGE_ID_REGEX)) {
            new Alert(Alert.AlertType.ERROR, "Please enter an ID!").show();
        } else if (!name.matches(PACKAGE_NAME_REGEX)) {
            new Alert(Alert.AlertType.ERROR, "Please enter a valid name (at least 3 characters)!").show();
        } else if (!price.matches(PACKAGE_PRICE_REGEX)) {
            new Alert(Alert.AlertType.ERROR, "Please enter a valid price!").show();
        } else {
            String category = cmbCategory.getValue();
            if (category == null || category.isEmpty()) {
                new Alert(Alert.AlertType.ERROR, "Please select a category!").show();
                return;
            }
            try {
                PackageDTO packageDTO = new PackageDTO(id, name, category, description, Double.parseDouble(price));
                boolean result = packageModel.updatePackage(packageDTO);

                if (result) {
                    new Alert(Alert.AlertType.INFORMATION, "Package Updated Successfully!").show();
                    clearFields();
                    loadPackagesTable();
                    loadNextPackageId();
                } else {
                    new Alert(Alert.AlertType.WARNING, "Failed to update package!").show();
                }
            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR, "Something Went Wrong!" + e.getMessage()).show();
            }
        }
    }

    @FXML
    private void handleDeletePackage() {
        String id = txtPackageId.getText().trim();

        if (!id.matches(PACKAGE_ID_REGEX)) {
            new Alert(Alert.AlertType.ERROR, "Please enter an ID!").show();
        } else {
            try {
                boolean result = packageModel.deletePackage(id);

                if (result) {
                    new Alert(Alert.AlertType.INFORMATION, "Package Deleted Successfully!").show();
                    clearFields();
                    loadPackagesTable();
                    loadNextPackageId();
                } else {
                    new Alert(Alert.AlertType.WARNING, "Failed to delete package!").show();
                }
            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR, "Something Went Wrong!" + e.getMessage()).show();
            }
        }
    }

    @FXML
    private void handleSearchPackage(KeyEvent event) {

        if(event.getCode() == KeyCode.ENTER) {
            String id = txtPackageId.getText().trim();

            try {
                PackageDTO packageDTO = packageModel.searchPackage(id);


                if (packageDTO != null) {
                    txtPackageId.setText(packageDTO.getPackageId());
                    txtPackageName.setText(packageDTO.getPackageName());
                    txtPrice.setText(String.valueOf(packageDTO.getBasePrice()));
                    txtDescription.setText(packageDTO.getPackageDescription());
                    cmbCategory.setValue(null);
                } else {
                    new Alert(Alert.AlertType.WARNING, "Package not found!").show();
                }
            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR, "Something Went Wrong!" + e.getMessage()).show();
            }
        }
    }

    @FXML
    private void handleResetPackage() {
        clearFields();
        loadNextPackageId();
    }

    private void clearFields() {
        txtPackageId.setText("");
        txtPackageName.setText("");
        txtPrice.setText("");
        txtDescription.setText("");
        cmbCategory.setValue(null);
    }

// Load Packages Table

    private ObservableList<PackageDTO> loadPackagesTable() {
        try {
            PackageModel packageModel = new PackageModel();
            ObservableList<PackageDTO> obList = packageModel.getAllPackages();
            tblPackages.setItems(obList);
            return obList;
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Something went wrong!").show();
            return FXCollections.observableArrayList();
        }
    }

// Auto Generate ID

    private void loadNextPackageId() {
        try {
            txtPackageId.setText(packageModel.getNextPackageId());
        } catch (SQLException e) {
            System.err.println("Error loading next package ID: " + e.getMessage());
        }


    }

// Additional Service UI Load

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
