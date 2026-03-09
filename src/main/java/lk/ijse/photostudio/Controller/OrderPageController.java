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
import lk.ijse.photostudio.Model.OrderModel;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class OrderPageController implements Initializable {

    @FXML private TextField txtOrderId;
    @FXML private ComboBox<String> cmbBookingId;
    @FXML private TextField txtPkgPrice;
    @FXML private TextField txtOptPrice;
    @FXML private TextField txtTotal;
    @FXML private TextField txtSearch;

    @FXML private TableView<OrderDTO> tblOrders;
    @FXML private TableColumn<OrderDTO, String> colOrderId;
    @FXML private TableColumn<OrderDTO, String> colBookingId;
    @FXML private TableColumn<OrderDTO, String> colCustomerId;
    @FXML private TableColumn<OrderDTO, Double> colPkgPrice;
    @FXML private TableColumn<OrderDTO, Double> colOptPrice;
    @FXML private TableColumn<OrderDTO, Double> colGrandTotal;

    private String tempCustomerId = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        colOrderId.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        colBookingId.setCellValueFactory(new PropertyValueFactory<>("bookingId"));
        colCustomerId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        colPkgPrice.setCellValueFactory(new PropertyValueFactory<>("pkgPrice"));
        colOptPrice.setCellValueFactory(new PropertyValueFactory<>("optPrice"));
        colGrandTotal.setCellValueFactory(new PropertyValueFactory<>("grandTotal"));

        // Load Data
        loadNextOrderId();
        loadPendingBookings();
        loadOrdersTable();

        // Auto Calculation
        cmbBookingId.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                calculateOrderDetails(newValue);
            }
        });

        if(txtSearch != null) {
            txtSearch.textProperty().addListener((obs, oldVal, newVal) -> searchFilter(newVal));
        }
    }

    private void calculateOrderDetails(String bookingId) {
        try {
            // Booking Details
            BookingDTO booking = BookingModel.searchBooking(bookingId);

            if (booking != null) {
                this.tempCustomerId = booking.getCustomerId();
                String packageId = booking.getPackageId(); // "P001"
                String optionName = booking.getAdditionalOption(); // "Drone" or "None"

                // Get Package Price OrderModel
                double pkgPrice = OrderModel.getPackagePrice(packageId);

                // Get Option Price PackageAdditionalModel
                double optPrice = 0.0; // Default to 0.0

                // Only fetch option price if an option is actually selected/found in booking
                if (optionName != null && !optionName.trim().equalsIgnoreCase("None") && !optionName.trim().isEmpty()) {
                    // Call the model method to get the price for this specific option name
                    optPrice = PackageAdditionalModel.getOptionPrice(optionName);
                }

                // Calculate Grand Total
                double grandTotal = pkgPrice + optPrice;

                // Update UI Text Fields
                txtPkgPrice.setText(String.format("%.2f", pkgPrice));
                txtOptPrice.setText(String.format("%.2f", optPrice));
                txtTotal.setText(String.format("%.2f", grandTotal));
            } else {
                // booking not found, clear fields
                txtPkgPrice.setText("0.00");
                txtOptPrice.setText("0.00");
                txtTotal.setText("0.00");
                this.tempCustomerId = null;
                new Alert(Alert.AlertType.WARNING, "Booking details not found for ID: " + bookingId).show();
            }

        } catch (SQLException e) {
            // Catch specific SQL errors
            new Alert(Alert.AlertType.ERROR, "Error during calculation: " + e.getMessage()).show();
            e.printStackTrace();
        } catch (Exception e) {
            // Catch any other unexpected errors
            new Alert(Alert.AlertType.ERROR, "General Calculation Error: " + e.getMessage()).show();
            e.printStackTrace();
        }
    }

    @FXML
    void handlePlaceOrder(ActionEvent event) {
        try {
            // Validation check
            if (cmbBookingId.getValue() == null) {
                new Alert(Alert.AlertType.WARNING, "Please select a booking first!").show();
                return;
            }
            if (txtTotal.getText().isEmpty() || txtTotal.getText().equals("0.00")) {
                new Alert(Alert.AlertType.WARNING, "Total is 0.00. Please re-select booking.").show();
                return;
            }

            // Create Order Object
            OrderDTO orderDTO = new OrderDTO(
                    txtOrderId.getText(),
                    cmbBookingId.getValue(),
                    tempCustomerId,
                    Double.parseDouble(txtPkgPrice.getText()),
                    Double.parseDouble(txtOptPrice.getText()),
                    Double.parseDouble(txtTotal.getText())
            );

            // TRANSACTION (Save Order + Update Booking Status)
            boolean isPlaced = OrderModel.placeOrder(orderDTO);

            if (isPlaced) {
                new Alert(Alert.AlertType.INFORMATION, "Order Placed Successfully! Booking Confirmed.").show();
                handleReset(event);
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to place order.").show();
            }

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "SQL Error: " + e.getMessage()).show();
            e.printStackTrace();
        }
    }

    @FXML
    void handleReset(ActionEvent event) {
        txtOrderId.clear();
        cmbBookingId.getSelectionModel().clearSelection();
        cmbBookingId.setValue(null);
        txtPkgPrice.clear();
        txtOptPrice.clear();
        txtTotal.clear();
        if(txtSearch != null) txtSearch.clear();
        this.tempCustomerId = null;

        loadNextOrderId();
        loadPendingBookings();
        loadOrdersTable();
    }

    private void loadNextOrderId() {
        try {
            txtOrderId.setText(OrderModel.getNextOrderId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadPendingBookings() {
        try {
            // Only load bookings status = 'Pending'
            cmbBookingId.setItems(OrderModel.getPendingBookingIds());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadOrdersTable() {
        try {
            tblOrders.setItems(OrderModel.getAllOrders());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void searchFilter(String keyword) {
        try {
            ObservableList<OrderDTO> allOrders = OrderModel.getAllOrders();
            if (keyword == null || keyword.isEmpty()) {
                tblOrders.setItems(allOrders);
                return;
            }
            ObservableList<OrderDTO> filtered = FXCollections.observableArrayList();
            for (OrderDTO dto : allOrders) {
                if (dto.getOrderId().toLowerCase().contains(keyword.toLowerCase()) ||
                        dto.getCustomerId().toLowerCase().contains(keyword.toLowerCase())) {
                    filtered.add(dto);
                }
            }
            tblOrders.setItems(filtered);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleTableClick() {
        OrderDTO selectedItem = tblOrders.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            txtOrderId.setText(selectedItem.getOrderId());
            cmbBookingId.setValue(selectedItem.getBookingId());
            txtPkgPrice.setText(String.valueOf(selectedItem.getPkgPrice()));
            txtOptPrice.setText(String.valueOf(selectedItem.getOptPrice()));
            txtTotal.setText(String.valueOf(selectedItem.getGrandTotal()));
        }
    }
}