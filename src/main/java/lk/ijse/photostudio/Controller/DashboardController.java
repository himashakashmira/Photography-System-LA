package lk.ijse.photostudio.Controller;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import lk.ijse.photostudio.App;
import lk.ijse.photostudio.BO.BOFactory;
import lk.ijse.photostudio.BO.Dashboard.DashboardBO;
import lk.ijse.photostudio.DTO.BookingDTO;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    @FXML
    private ScrollPane mainContent;
    @FXML
    private VBox overViewPane;
    @FXML
    private Label lblTotalBookings, lblPendingOrders, lblTotalIncome, lblTime, lblDate;

    @FXML
    private Button clickDashboard, clickCustomer, clickPackages, clickBookings, clickOrder, clickMaterial, clickSupplier, clickReport, clickSetting;

    @FXML
    private TableView<BookingDTO> tblTodayBookings;
    @FXML
    private TableColumn<BookingDTO, String> colId, colCustomer, colType, colTime;

    DashboardBO dashboardBO = (DashboardBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.DASHBOARD);

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colId.setCellValueFactory(new PropertyValueFactory<>("bookingId"));
        colCustomer.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        colType.setCellValueFactory(new PropertyValueFactory<>("packageId"));
        colTime.setCellValueFactory(new PropertyValueFactory<>("timeSlot"));

        setActive(clickDashboard);
        refreshDashboard();
        initClock();
    }

    public void refreshDashboard() {
        try {
            // Load Counts
            lblTotalBookings.setText(String.valueOf(dashboardBO.getTotalBookings()));
            lblPendingOrders.setText(String.valueOf(dashboardBO.getPendingBookings()));
            lblTotalIncome.setText(String.format("Rs. %.2f", dashboardBO.getTotalIncome()));

            // Load Table
            ArrayList<BookingDTO> list = dashboardBO.getTodaysBookings();
            tblTodayBookings.setItems(FXCollections.observableArrayList(list));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onClickDashboard() {
        refreshDashboard();
        mainContent.setContent(overViewPane);
        setActive(clickDashboard);
    }

    @FXML
    private void onClickCustomer() throws IOException {
        mainContent.setContent(App.loadFXML("CustomerPage"));
        setActive(clickCustomer);
    }

    @FXML
    private void onClickPackages() throws IOException {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("PackagePage.fxml"));
        mainContent.setContent(loader.load());
        ((PackagePageController) loader.getController()).setDashboardController(this);
        setActive(clickPackages);
    }

    @FXML
    private void onClickBookings() throws IOException {
        mainContent.setContent(App.loadFXML("BookingPage"));
        setActive(clickBookings);
    }

    @FXML
    private void onClickOrder() throws IOException {
        mainContent.setContent(App.loadFXML("OrderPage"));
        setActive(clickOrder);
    }

    @FXML
    private void onClickMaterial() throws IOException {
        mainContent.setContent(App.loadFXML("MaterialPage"));
        setActive(clickMaterial);
    }

    @FXML
    private void onClickSupplier() throws IOException {
        mainContent.setContent(App.loadFXML("SupplierPage"));
        setActive(clickSupplier);
    }

    @FXML
    private void onClickReport() throws IOException {
        mainContent.setContent(App.loadFXML("ReportPage"));
        setActive(clickReport);
    }

    @FXML
    private void onClickSetting() throws IOException {
        mainContent.setContent(App.loadFXML("Setting"));
        setActive(clickSetting);
    }

    public void loadPackageOptions() throws IOException {
        Parent optionPage = App.loadFXML("PackageAdditional");
        mainContent.setContent(optionPage);
    }

    @FXML
    private void handleLogout(ActionEvent event) throws IOException {
        if (new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to exit?", ButtonType.OK, ButtonType.CANCEL).showAndWait().get() == ButtonType.OK) {
            App.setRoot("Login");
        }
    }

    private void setActive(Button button) {
        Button[] buttons = {clickDashboard, clickCustomer, clickPackages, clickBookings, clickOrder, clickMaterial, clickSupplier, clickReport, clickSetting};
        for (Button b : buttons) b.getStyleClass().remove("nav-button-active");
        button.getStyleClass().add("nav-button-active");
    }

    private void initClock() {
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            lblTime.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss a")));
            lblDate.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMM dd, yyyy")));
        }), new KeyFrame(Duration.seconds(1)));
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }
}