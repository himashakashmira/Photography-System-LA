package lk.ijse.photostudio.Controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import lk.ijse.photostudio.BO.BOFactory;
import lk.ijse.photostudio.BO.Booking.BookingBO;
import lk.ijse.photostudio.BO.Order.OrderBO;
import lk.ijse.photostudio.DTO.BookingDTO;
import lk.ijse.photostudio.DTO.OrderDTO;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class OrderPageController implements Initializable {

    @FXML
    private TextField txtOrderId, txtPkgPrice, txtOptPrice, txtTotal, txtSearch;
    @FXML
    private ComboBox<String> cmbBookingId;
    @FXML
    private TableView<OrderDTO> tblOrders;
    @FXML
    private TableColumn<OrderDTO, String> colOrderId, colBookingId, colCustomerId;
    @FXML
    private TableColumn<OrderDTO, Double> colPkgPrice, colOptPrice, colGrandTotal;

    private String tempCustomerId = null;

    OrderBO orderBO = (OrderBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.ORDER);
    BookingBO bookingBO = (BookingBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.BOOKING);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colOrderId.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        colBookingId.setCellValueFactory(new PropertyValueFactory<>("bookingId"));
        colCustomerId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        colPkgPrice.setCellValueFactory(new PropertyValueFactory<>("pkgPrice"));
        colOptPrice.setCellValueFactory(new PropertyValueFactory<>("optPrice"));
        colGrandTotal.setCellValueFactory(new PropertyValueFactory<>("grandTotal"));

        loadAllData();

        cmbBookingId.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) calculateTotals(newVal);
        });
    }

    @FXML
    private void handleTableClick(MouseEvent event) {
        OrderDTO selectedItem = tblOrders.getSelectionModel().getSelectedItem();

        if (selectedItem != null) {
            txtOrderId.setText(selectedItem.getOrderId());
            cmbBookingId.setValue(selectedItem.getBookingId());

            txtPkgPrice.setText(String.format("%.2f", selectedItem.getPkgPrice()));
            txtOptPrice.setText(String.format("%.2f", selectedItem.getOptPrice()));
            txtTotal.setText(String.format("%.2f", selectedItem.getGrandTotal()));
        }
    }

    private void calculateTotals(String bookingId) {
        try {
            BookingDTO booking = bookingBO.searchBooking(bookingId);
            if (booking != null) {
                this.tempCustomerId = booking.getCustomerId();
                double pkgPrice = orderBO.getPackagePrice(booking.getPackageId());
                double optPrice = orderBO.getOptionPrice(booking.getAdditionalOption());

                txtPkgPrice.setText(String.format("%.2f", pkgPrice));
                txtOptPrice.setText(String.format("%.2f", optPrice));
                txtTotal.setText(String.format("%.2f", pkgPrice + optPrice));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handlePlaceOrder() {
        if (cmbBookingId.getValue() == null) return;
        try {
            OrderDTO dto = new OrderDTO(txtOrderId.getText(), cmbBookingId.getValue(), tempCustomerId,
                    Double.parseDouble(txtPkgPrice.getText()), Double.parseDouble(txtOptPrice.getText()), Double.parseDouble(txtTotal.getText()));

            if (orderBO.placeOrder(dto)) {
                new Alert(Alert.AlertType.INFORMATION, "Order Placed Successfully!").show();
                refreshUI();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadAllData() {
        try {
            txtOrderId.setText(orderBO.generateNextOrderId());
            cmbBookingId.setItems(FXCollections.observableArrayList(orderBO.getPendingBookingIds()));
            tblOrders.setItems(FXCollections.observableArrayList(orderBO.getAllOrders()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void refreshUI() {
        txtOrderId.clear();
        cmbBookingId.setValue(null);
        txtPkgPrice.clear();
        txtOptPrice.clear();
        txtTotal.clear();
        loadAllData();
    }

    @FXML
    void handleReset() {
        refreshUI();
    }
}