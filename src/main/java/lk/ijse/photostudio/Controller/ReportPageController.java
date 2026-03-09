package lk.ijse.photostudio.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.photostudio.DTO.BookingDTO;
import lk.ijse.photostudio.DTO.OrderDTO;
import lk.ijse.photostudio.DTO.ReportDTO;
import lk.ijse.photostudio.Model.ReportModel;
import net.sf.jasperreports.engine.JRException;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class ReportPageController implements Initializable {

    // BOOKING TAB
    @FXML private DatePicker dpBookingFrom, dpBookingTo;
    @FXML private ComboBox<String> cmbBookingCustomer;
    @FXML private Label lblTotalBookings, lblUpcomingBookings, lblCompletedBookings;
    @FXML private TableView<BookingDTO> tblBookings;
    @FXML private TableColumn<BookingDTO, String> colBkId, colBkCustomer, colBkPackage, colBkStatus;
    @FXML private TableColumn<BookingDTO, LocalDate> colBkDate;

    // PACKAGE TAB
    @FXML private ComboBox<String> cmbPkgCategory;
    @FXML private ComboBox<String> cmbPkgName;
    @FXML private Label lblPopularPkg, lblTotalPkgCount;
    @FXML private TableView<ReportDTO> tblPackages;
    @FXML private TableColumn<ReportDTO, String> colPkgId, colPkgName, colPkgCat;
    @FXML private TableColumn<ReportDTO, Double> colPkgPrice;
    @FXML private TableColumn<ReportDTO, Integer> colPkgUsage;

    // ORDER & PROFIT TAB
    @FXML private DatePicker dpProfitFrom, dpProfitTo;
    @FXML private TableView<OrderDTO> tblProfit;
    @FXML private TableColumn<OrderDTO, String> colOrdId, colOrdBooking;
    @FXML private TableColumn<OrderDTO, Double> colOrdPkgPrice, colOrdOptPrice, colOrdTotal;
    @FXML private Label lblTotalPkgIncome, lblTotalOptIncome, lblTotalProfit;

    // STOCK TAB
    @FXML private TextField txtMatType;
    @FXML private ComboBox<String> cmbSupplier, cmbStockStatus;
    @FXML private TableView<ReportDTO> tblStock;
    @FXML private TableColumn<ReportDTO, String> colMatId, colMatName, colMatSupplier, colMatStatus;
    @FXML private TableColumn<ReportDTO, Integer> colMatQty;
    @FXML private Label lblTotalItems, lblLowStock;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Table Columns
        initBookingTable();
        initPackageTable();
        initProfitTable();
        initStockTable();

        // Load ComboBox
        loadComboBoxData();

        // AUTO LOAD DATA
        handleBookingReport(null);
        handlePackageReport(null);
        handleProfitReport(null);
        handleStockReport(null);
    }

    // Tables Initialize
    private void initBookingTable() {
        colBkId.setCellValueFactory(new PropertyValueFactory<>("bookingId"));
        colBkCustomer.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        colBkPackage.setCellValueFactory(new PropertyValueFactory<>("packageId"));
        colBkDate.setCellValueFactory(new PropertyValueFactory<>("eventDate"));
        colBkStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    private void initPackageTable() {
        colPkgId.setCellValueFactory(new PropertyValueFactory<>("packageId"));
        colPkgName.setCellValueFactory(new PropertyValueFactory<>("packageName"));
        colPkgCat.setCellValueFactory(new PropertyValueFactory<>("packageCategory"));
        colPkgPrice.setCellValueFactory(new PropertyValueFactory<>("packagePrice"));
        colPkgUsage.setCellValueFactory(new PropertyValueFactory<>("usageCount"));
    }

    private void initProfitTable() {
        colOrdId.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        colOrdBooking.setCellValueFactory(new PropertyValueFactory<>("bookingId"));
        colOrdPkgPrice.setCellValueFactory(new PropertyValueFactory<>("pkgPrice"));
        colOrdOptPrice.setCellValueFactory(new PropertyValueFactory<>("optPrice"));
        colOrdTotal.setCellValueFactory(new PropertyValueFactory<>("grandTotal"));
    }

    private void initStockTable() {
        colMatId.setCellValueFactory(new PropertyValueFactory<>("itemId"));
        colMatName.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        colMatQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colMatSupplier.setCellValueFactory(new PropertyValueFactory<>("supplierId"));
        colMatStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        colMatStatus.setCellFactory(column -> new TableCell<ReportDTO, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    if (item.equalsIgnoreCase("LOW STOCK")) {
                        setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;"); // Red
                    } else {
                        setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold;"); // Green
                    }
                }
            }
        });
    }

    // --- DATA LOADING & BUTTON ACTIONS ---

    @FXML
    void handleProfitReport(ActionEvent event) {
        try {
            LocalDate from = dpProfitFrom.getValue();
            LocalDate to = dpProfitTo.getValue();

            ObservableList<OrderDTO> list = ReportModel.getOrderReport(from, to);
            tblProfit.setItems(list);

            double totalPkg = list.stream().mapToDouble(OrderDTO::getPkgPrice).sum();
            double totalOpt = list.stream().mapToDouble(OrderDTO::getOptPrice).sum();
            double grandTotal = list.stream().mapToDouble(OrderDTO::getGrandTotal).sum();

            lblTotalPkgIncome.setText(String.format("Rs. %.2f", totalPkg));
            lblTotalOptIncome.setText(String.format("Rs. %.2f", totalOpt));
            lblTotalProfit.setText(String.format("Rs. %.2f", grandTotal));

        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Error calculating profit: " + e.getMessage()).show();
            e.printStackTrace();
        }
    }

    @FXML
    void handleBookingReport(ActionEvent event) {
        try {
            ObservableList<BookingDTO> list = ReportModel.getBookingsByFilter(dpBookingFrom.getValue(), dpBookingTo.getValue(), cmbBookingCustomer.getValue());
            tblBookings.setItems(list);
            lblTotalBookings.setText(String.valueOf(list.size()));
            lblUpcomingBookings.setText(String.valueOf(list.stream().filter(b -> b.getStatus().equalsIgnoreCase("Pending")).count()));
            lblCompletedBookings.setText(String.valueOf(list.stream().filter(b -> b.getStatus().equalsIgnoreCase("Confirmed")).count()));
        } catch (Exception e) { e.printStackTrace(); }
    }

    @FXML
    void handlePackageReport(ActionEvent event) {
        try {
            ObservableList<ReportDTO> list = ReportModel.getPackageReport(cmbPkgCategory.getValue());
            tblPackages.setItems(list);
            lblTotalPkgCount.setText(String.valueOf(list.size()));
            if (!list.isEmpty()) lblPopularPkg.setText(list.get(0).getPackageName());
        } catch (Exception e) { e.printStackTrace(); }
    }

    @FXML
    void handleStockReport(ActionEvent event) {
        try {
            String supplier = cmbSupplier.getValue();
            ObservableList<ReportDTO> list = ReportModel.getStockReport(supplier);

            String statusFilter = null;
            if(cmbStockStatus != null) statusFilter = cmbStockStatus.getValue();

            if(statusFilter != null && !statusFilter.equals("All") && !statusFilter.isEmpty()) {
                ObservableList<ReportDTO> filteredList = FXCollections.observableArrayList();
                for(ReportDTO dto : list) {
                    if((statusFilter.equals("Low") && dto.getStatus().equals("LOW STOCK")) ||
                            (statusFilter.equals("Normal") && dto.getStatus().equals("NORMAL"))) {
                        filteredList.add(dto);
                    }
                }
                tblStock.setItems(filteredList);
            } else {
                tblStock.setItems(list);
            }

            lblTotalItems.setText(String.valueOf(list.size()));
            long lowStockCount = list.stream().filter(i -> i.getStatus().equals("LOW STOCK")).count();
            lblLowStock.setText(String.valueOf(lowStockCount));

        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Error loading stock: " + e.getMessage()).show();
            e.printStackTrace();
        }
    }

    private void loadComboBoxData() {
        try {
            cmbBookingCustomer.setItems(BookingModel.getAllCustomerIds());
            cmbPkgCategory.setItems(FXCollections.observableArrayList("All Categories", "Wedding", "Birthday", "Event", "Other"));
            cmbSupplier.setItems(FXCollections.observableArrayList("All Suppliers", "S001", "S002"));
            cmbStockStatus.setItems(FXCollections.observableArrayList("All", "Low", "Normal"));
        } catch (Exception e) { e.printStackTrace(); }
    }

    // --- PRINT ACTIONS ---

    @FXML
    void handlePrintBooking(ActionEvent event) {
        try {
            ReportModel.printBookingReport();
        } catch (JRException | SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to print Booking Report: " + e.getMessage()).show();
        }
    }

    @FXML
    void handlePrintPackage(ActionEvent event) {
        try {
            ReportModel.printPackageReport();
        } catch (JRException | SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to print Package Report: " + e.getMessage()).show();
        }
    }

    @FXML
    void handlePrintProfit(ActionEvent event) {
        try {
            ReportModel.printOrderProfitReport();
        } catch (JRException | SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to print Profit Report: " + e.getMessage()).show();
        }
    }
}