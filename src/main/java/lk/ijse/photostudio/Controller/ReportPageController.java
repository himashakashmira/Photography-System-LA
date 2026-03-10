package lk.ijse.photostudio.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.photostudio.BO.BOFactory;
import lk.ijse.photostudio.BO.Booking.BookingBO;
import lk.ijse.photostudio.BO.Report.ReportBO;
import lk.ijse.photostudio.DTO.BookingDTO;
import lk.ijse.photostudio.DTO.OrderDTO;
import lk.ijse.photostudio.DTO.ReportDTO;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ReportPageController implements Initializable {

    // BOOKING TAB
    @FXML
    private DatePicker dpBookingFrom, dpBookingTo;
    @FXML
    private ComboBox<String> cmbBookingCustomer;
    @FXML
    private Label lblTotalBookings, lblUpcomingBookings, lblCompletedBookings;
    @FXML
    private TableView<BookingDTO> tblBookings;
    @FXML
    private TableColumn<BookingDTO, String> colBkId, colBkCustomer, colBkPackage, colBkStatus;
    @FXML
    private TableColumn<BookingDTO, LocalDate> colBkDate;

    // PACKAGE TAB
    @FXML
    private ComboBox<String> cmbPkgCategory;
    @FXML
    private Label lblPopularPkg, lblTotalPkgCount;
    @FXML
    private TableView<ReportDTO> tblPackages;
    @FXML
    private TableColumn<ReportDTO, String> colPkgId, colPkgName, colPkgCat;
    @FXML
    private TableColumn<ReportDTO, Double> colPkgPrice;
    @FXML
    private TableColumn<ReportDTO, Integer> colPkgUsage;

    // ORDER & PROFIT TAB
    @FXML
    private DatePicker dpProfitFrom, dpProfitTo;
    @FXML
    private TableView<OrderDTO> tblProfit;
    @FXML
    private TableColumn<OrderDTO, String> colOrdId, colOrdBooking;
    @FXML
    private TableColumn<OrderDTO, Double> colOrdPkgPrice, colOrdOptPrice, colOrdTotal;
    @FXML
    private Label lblTotalPkgIncome, lblTotalOptIncome, lblTotalProfit;

    // STOCK TAB
    @FXML
    private ComboBox<String> cmbSupplier, cmbStockStatus;
    @FXML
    private TableView<ReportDTO> tblStock;
    @FXML
    private TableColumn<ReportDTO, String> colMatId, colMatName, colMatSupplier, colMatStatus;
    @FXML
    private TableColumn<ReportDTO, Integer> colMatQty;
    @FXML
    private Label lblTotalItems, lblLowStock;


    ReportBO reportBO = (ReportBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.REPORT);
    BookingBO bookingBO = (BookingBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.BOOKING);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initTableColumns();
        loadComboBoxData();
        refreshAllReports();
    }

    private void initTableColumns() {
        // Booking Table
        colBkId.setCellValueFactory(new PropertyValueFactory<>("bookingId"));
        colBkCustomer.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        colBkPackage.setCellValueFactory(new PropertyValueFactory<>("packageId"));
        colBkDate.setCellValueFactory(new PropertyValueFactory<>("eventDate"));
        colBkStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Package Table
        colPkgId.setCellValueFactory(new PropertyValueFactory<>("packageId"));
        colPkgName.setCellValueFactory(new PropertyValueFactory<>("packageName"));
        colPkgCat.setCellValueFactory(new PropertyValueFactory<>("packageCategory"));
        colPkgPrice.setCellValueFactory(new PropertyValueFactory<>("packagePrice"));
        colPkgUsage.setCellValueFactory(new PropertyValueFactory<>("usageCount"));

        // Profit Table
        colOrdId.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        colOrdBooking.setCellValueFactory(new PropertyValueFactory<>("bookingId"));
        colOrdPkgPrice.setCellValueFactory(new PropertyValueFactory<>("pkgPrice"));
        colOrdOptPrice.setCellValueFactory(new PropertyValueFactory<>("optPrice"));
        colOrdTotal.setCellValueFactory(new PropertyValueFactory<>("grandTotal"));

        // Stock Table
        colMatId.setCellValueFactory(new PropertyValueFactory<>("itemId"));
        colMatName.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        colMatQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colMatSupplier.setCellValueFactory(new PropertyValueFactory<>("supplierId"));
        colMatStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Stock Status (Red for Low Stock)
        colMatStatus.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    if (item.equalsIgnoreCase("LOW STOCK")) {
                        setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;");
                    } else {
                        setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold;");
                    }
                }
            }
        });
    }

    private void loadComboBoxData() {
        try {
            cmbBookingCustomer.setItems(bookingBO.getAllCustomerIds());
            cmbPkgCategory.setItems(FXCollections.observableArrayList("All Categories", "Wedding", "Birthday", "Event", "Other"));
            cmbSupplier.setItems(FXCollections.observableArrayList("All Suppliers", "S001", "S002"));
            cmbStockStatus.setItems(FXCollections.observableArrayList("All", "Low", "Normal"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void refreshAllReports() {
        handleBookingReport(null);
        handlePackageReport(null);
        handleProfitReport(null);
        handleStockReport(null);
    }

    @FXML
    void handleProfitReport(ActionEvent event) {
        try {
            ArrayList<OrderDTO> list = reportBO.getOrderReport(dpProfitFrom.getValue(), dpProfitTo.getValue());
            tblProfit.setItems(FXCollections.observableArrayList(list));

            double totalPkg = list.stream().mapToDouble(OrderDTO::getPkgPrice).sum();
            double totalOpt = list.stream().mapToDouble(OrderDTO::getOptPrice).sum();
            double grandTotal = list.stream().mapToDouble(OrderDTO::getGrandTotal).sum();

            lblTotalPkgIncome.setText(String.format("Rs. %.2f", totalPkg));
            lblTotalOptIncome.setText(String.format("Rs. %.2f", totalOpt));
            lblTotalProfit.setText(String.format("Rs. %.2f", grandTotal));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleBookingReport(ActionEvent event) {
        try {
            ArrayList<BookingDTO> list = reportBO.getBookingsByFilter(dpBookingFrom.getValue(), dpBookingTo.getValue(), cmbBookingCustomer.getValue());
            tblBookings.setItems(FXCollections.observableArrayList(list));
            lblTotalBookings.setText(String.valueOf(list.size()));
            lblUpcomingBookings.setText(String.valueOf(list.stream().filter(b -> b.getStatus().equalsIgnoreCase("Pending")).count()));
            lblCompletedBookings.setText(String.valueOf(list.stream().filter(b -> b.getStatus().equalsIgnoreCase("Confirmed")).count()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handlePackageReport(ActionEvent event) {
        try {
            ArrayList<ReportDTO> list = reportBO.getPackageReport(cmbPkgCategory.getValue());
            tblPackages.setItems(FXCollections.observableArrayList(list));
            lblTotalPkgCount.setText(String.valueOf(list.size()));
            if (!list.isEmpty()) lblPopularPkg.setText(list.get(0).getPackageName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleStockReport(ActionEvent event) {
        try {
            ArrayList<ReportDTO> list = reportBO.getStockReport(cmbSupplier.getValue());

            // Status Filtering
            String filter = cmbStockStatus.getValue();
            ObservableList<ReportDTO> filteredList = FXCollections.observableArrayList();
            for (ReportDTO d : list) {
                if (filter == null || filter.equals("All") || (filter.equals("Low") && d.getStatus().equals("LOW STOCK")) || (filter.equals("Normal") && d.getStatus().equals("NORMAL"))) {
                    filteredList.add(d);
                }
            }
            tblStock.setItems(filteredList);
            lblTotalItems.setText(String.valueOf(list.size()));
            lblLowStock.setText(String.valueOf(list.stream().filter(i -> i.getStatus().equals("LOW STOCK")).count()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // PRINT ACTIONS

    @FXML
    void handlePrintBooking(ActionEvent event) {
        try {
            reportBO.printBookingReport();
        } catch (Exception e) {
            showError("Booking Print Failed");
        }
    }

    @FXML
    void handlePrintPackage(ActionEvent event) {
        try {
            reportBO.printPackageReport();
        } catch (Exception e) {
            showError("Package Print Failed");
        }
    }

    @FXML
    void handlePrintProfit(ActionEvent event) {
        try {
            reportBO.printOrderProfitReport();
        } catch (Exception e) {
            showError("Profit Print Failed");
        }
    }

    private void showError(String msg) {
        new Alert(Alert.AlertType.ERROR, msg).show();
    }
}