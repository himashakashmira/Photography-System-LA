package lk.ijse.photostudio.Controller;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import lk.ijse.photostudio.App;
import lk.ijse.photostudio.DTO.BookingDTO;
import lk.ijse.photostudio.Model.DashboardModel;

public class DashboardController implements Initializable {

    @FXML
    private ScrollPane mainContent;

    @FXML
    private VBox overViewPane;

    // label card
    @FXML private Label lblTotalBookings;
    @FXML private Label lblPendingOrders;
    @FXML private Label lblTotalIncome;

    @FXML private Label lblTime;
    @FXML private Label lblDate;

    // Button Highlight Change
    @FXML private javafx.scene.control.Button clickDashboard;
    @FXML private javafx.scene.control.Button clickCustomer;
    @FXML private javafx.scene.control.Button clickPackages;
    @FXML private javafx.scene.control.Button clickBookings;
    @FXML private javafx.scene.control.Button clickOrder;
    @FXML private javafx.scene.control.Button clickMaterial;
    @FXML private javafx.scene.control.Button clickSupplier;
    @FXML private javafx.scene.control.Button clickReport;
    @FXML private javafx.scene.control.Button clickSetting;

    // Pending Table
    @FXML private TableView<BookingDTO> tblTodayBookings;
    @FXML private TableColumn<BookingDTO, String> colId;
    @FXML private TableColumn<BookingDTO, String> colCustomer;
    @FXML private TableColumn<BookingDTO, String> colType; // Package
    @FXML private TableColumn<BookingDTO, String> colTime;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("Dashboard is loaded!");
        initializeButtons();
        loadDashboardCounts();

        colId.setCellValueFactory(new PropertyValueFactory<>("bookingId"));
        colCustomer.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        colType.setCellValueFactory(new PropertyValueFactory<>("packageId"));
        colTime.setCellValueFactory(new PropertyValueFactory<>("timeSlot"));

        // Load Data Immediately
        refreshDashboard();

        initClock();
    }

    public void refreshDashboard() {
        loadDashboardCounts();
        loadTodaysBookings();
    }

    // Clear Active Button
    @FXML
    private void clearActive() {
        clickDashboard.getStyleClass().remove("nav-button-active");
        clickCustomer.getStyleClass().remove("nav-button-active");
        clickPackages.getStyleClass().remove("nav-button-active");
        clickBookings.getStyleClass().remove("nav-button-active");
        clickOrder.getStyleClass().remove("nav-button-active");
        clickMaterial.getStyleClass().remove("nav-button-active");
        clickSupplier.getStyleClass().remove("nav-button-active");
        clickReport.getStyleClass().remove("nav-button-active");
        clickSetting.getStyleClass().remove("nav-button-active");
    }

    @FXML
    private void setActive(javafx.scene.control.Button button) {
        clearActive();
        button.getStyleClass().add("nav-button-active");
    }

    public void initializeButtons() {
        setActive(clickDashboard);
    }

    @FXML
    private void onClickDashboard() {
        refreshDashboard();
        mainContent.setContent(overViewPane);
        setActive(clickDashboard);
    }

    @FXML
    private void onClickCustomer() throws IOException {
        Parent customerPageFXML = App.loadFXML("CustomerPage");
        mainContent.setContent(customerPageFXML);
        setActive(clickCustomer);
    }

    @FXML
    private void onClickPackages() throws IOException {
        FXMLLoader loader = new FXMLLoader(
                App.class.getResource("PackagePage.fxml")
        );
        Parent packagePageFXML = loader.load();
        PackagePageController controller = loader.getController();
        controller.setDashboardController(this);

        mainContent.setContent(packagePageFXML);
        setActive(clickPackages);
    }

    public void loadPackageOptions() throws IOException {
        Parent optionPage = App.loadFXML("packageAdditional");
        mainContent.setContent(optionPage);
    }

    @FXML
    private void onClickBookings() throws IOException {
        Parent bookingPageFXML = App.loadFXML("BookingPage");
        mainContent.setContent(bookingPageFXML);
        setActive(clickBookings);
    }

    @FXML
    private void onClickOrder() throws IOException {
        Parent orderPageFXML = App.loadFXML("OrderPage");
        mainContent.setContent(orderPageFXML);
        setActive(clickOrder);
    }

    @FXML
    private void onClickMaterial() throws IOException {
        Parent materialPageFXML = App.loadFXML("MaterialPage");
        mainContent.setContent(materialPageFXML);
        setActive(clickMaterial);
    }

    @FXML
    private void onClickSupplier() throws IOException {
        Parent supplierPageFXML = App.loadFXML("SupplierPage");
        mainContent.setContent(supplierPageFXML);
        setActive(clickSupplier);
    }

    @FXML
    private void onClickReport() throws IOException {
        Parent reportPageFXML = App.loadFXML("ReportPage");
        mainContent.setContent(reportPageFXML);
        setActive(clickReport);
    }

    @FXML
    private void onClickSetting() throws IOException {
        Parent reportPageFXML = App.loadFXML("Setting");
        mainContent.setContent(reportPageFXML);
        setActive(clickSetting);
    }

    // Logout Button Set
    @FXML
    private void handleLogout(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText("You are about to logout!");
        alert.setContentText("Are you sure you want to exit?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            App.setRoot("Login");
        }
    }

    // label load
    private void loadDashboardCounts() {
        try {
            int total = DashboardModel.getTotalBookingsCount();
            int pending = DashboardModel.getPendingBookingsCount();
            double income = DashboardModel.getTotalIncome();

            lblTotalBookings.setText(String.valueOf(total));
            lblPendingOrders.setText(String.valueOf(pending));
            lblTotalIncome.setText(String.format("Rs. %.2f", income));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadTodaysBookings() {

        colId.setCellValueFactory(new PropertyValueFactory<>("bookingId"));
        colCustomer.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        colType.setCellValueFactory(new PropertyValueFactory<>("packageId"));
        colTime.setCellValueFactory(new PropertyValueFactory<>("timeSlot"));

        try {

            ObservableList<BookingDTO> list = DashboardModel.getTodaysPendingBookings();

            System.out.println("Today's Booking Count: " + list.size());

            // Set Items
            tblTodayBookings.setItems(list);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void initClock() {

        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {

            // Get Current Time
            LocalDateTime now = LocalDateTime.now();

            // Format Time
            String formattedTime = now.format(DateTimeFormatter.ofPattern("hh:mm:ss a"));

            // Format Date
            String formattedDate = now.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"));

            // Update Labels
            if (lblTime != null) lblTime.setText(formattedTime);
            if (lblDate != null) lblDate.setText(formattedDate);

        }), new KeyFrame(Duration.seconds(1))); // Update every 1 second

        clock.setCycleCount(Animation.INDEFINITE);

        // Start clock
        clock.play();
    }

}