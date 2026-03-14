package lk.ijse.photostudio.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import lk.ijse.photostudio.BO.BOFactory;
import lk.ijse.photostudio.BO.Booking.BookingBO;
import lk.ijse.photostudio.DTO.BookingDTO;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class BookingPageController implements Initializable {

    @FXML
    private TextField txtBookingId;
    @FXML
    private ComboBox<String> cmbCustomer;
    @FXML
    private ComboBox<String> cmbPackage;
    @FXML
    private ComboBox<String> cmbAdditionalOption;
    @FXML
    private ComboBox<String> cmbTimeSlot;
    @FXML
    private DatePicker EventDate;

    @FXML
    private TableView<BookingDTO> tblBookings;
    @FXML
    private TableColumn<BookingDTO, String> colId;
    @FXML
    private TableColumn<BookingDTO, String> colCustomer;
    @FXML
    private TableColumn<BookingDTO, String> colPackage;
    @FXML
    private TableColumn<BookingDTO, String> colOption;
    @FXML
    private TableColumn<BookingDTO, LocalDate> colDate;
    @FXML
    private TableColumn<BookingDTO, String> colTimeSlot;
    @FXML
    private TableColumn<BookingDTO, String> colStatus;

    BookingBO bookingBO = (BookingBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.BOOKING);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colId.setCellValueFactory(new PropertyValueFactory<>("bookingId"));
        colCustomer.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        colPackage.setCellValueFactory(new PropertyValueFactory<>("packageId"));
        colOption.setCellValueFactory(new PropertyValueFactory<>("additionalOption"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("eventDate"));
        colTimeSlot.setCellValueFactory(new PropertyValueFactory<>("timeSlot"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        loadInitialData();
    }

    private void loadInitialData() {
        loadTimeSlots();
        loadCustomers();
        loadPackages();
        loadAdditionalOptions();
        loadBookingsTable();
        loadNextBookingId();
    }

    @FXML
    private void handleTableClick(MouseEvent event) {
        BookingDTO selectedItem = tblBookings.getSelectionModel().getSelectedItem();

        if (selectedItem != null) {
            txtBookingId.setText(selectedItem.getBookingId());
            selectComboBoxValue(cmbCustomer, selectedItem.getCustomerId());
            selectComboBoxValue(cmbPackage, selectedItem.getPackageId());
            EventDate.setValue(selectedItem.getEventDate());
            cmbTimeSlot.setValue(selectedItem.getTimeSlot());
        }
    }

    private void loadTimeSlots() {
        cmbTimeSlot.setItems(FXCollections.observableArrayList("Morning", "Afternoon", "Night"));
    }

    private void loadCustomers() {
        try {
            cmbCustomer.setItems(bookingBO.getAllCustomerIds());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadPackages() {
        try {
            cmbPackage.setItems(bookingBO.getAllPackageIds());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadAdditionalOptions() {
        try {
            ObservableList<String> options = bookingBO.getAllOptionNames();
            options.add(0, "None");
            cmbAdditionalOption.setItems(options);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadBookingsTable() {
        try {
            ArrayList<BookingDTO> allBookings = bookingBO.getAllBookings();

            System.out.println("Bookings loaded: " + allBookings.size());

            tblBookings.setItems(FXCollections.observableArrayList(allBookings));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadNextBookingId() {
        try {
            txtBookingId.setText(bookingBO.generateNextBookingId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSave() {
        try {
            String customerId = cmbCustomer.getValue().split(" - ")[0];
            String packageId = cmbPackage.getValue().split(" - ")[0];

            BookingDTO dto = new BookingDTO(txtBookingId.getText(), customerId, packageId,
                    cmbAdditionalOption.getValue(), EventDate.getValue(), cmbTimeSlot.getValue(), "Pending");

            if (bookingBO.saveBooking(dto)) {
                new Alert(Alert.AlertType.INFORMATION, "Booking Saved!").show();
                reset();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleUpdate() {
        try {
            String customerId = cmbCustomer.getValue().split(" - ")[0];
            String packageId = cmbPackage.getValue().split(" - ")[0];

            BookingDTO dto = new BookingDTO(txtBookingId.getText(), customerId, packageId,
                    cmbAdditionalOption.getValue(), EventDate.getValue(), cmbTimeSlot.getValue(), "Pending");

            if (bookingBO.updateBooking(dto)) {
                new Alert(Alert.AlertType.INFORMATION, "Booking Updated!").show();
                reset();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDelete() {
        String id = txtBookingId.getText();
        try {
            if (bookingBO.deleteBooking(id)) {
                new Alert(Alert.AlertType.INFORMATION, "Deleted!").show();
                reset();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSearch(KeyEvent e) {
        if (e.getCode() == KeyCode.ENTER) {
            try {
                BookingDTO dto = bookingBO.searchBooking(txtBookingId.getText());
                if (dto != null) {
                    selectComboBoxValue(cmbCustomer, dto.getCustomerId());
                    selectComboBoxValue(cmbPackage, dto.getPackageId());
                    cmbAdditionalOption.setValue(dto.getAdditionalOption());
                    EventDate.setValue(dto.getEventDate());
                    cmbTimeSlot.setValue(dto.getTimeSlot());
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void selectComboBoxValue(ComboBox<String> comboBox, String id) {
        if (id == null) return;
        for (String item : comboBox.getItems()) {
            if (item.startsWith(id + " - ")) {
                comboBox.setValue(item);
                return;
            }
        }
        comboBox.setValue(id);
    }

    @FXML
    private void reset() {
        cmbCustomer.setValue(null);
        cmbPackage.setValue(null);
        cmbAdditionalOption.setValue(null);
        cmbTimeSlot.setValue(null);
        EventDate.setValue(null);
        loadBookingsTable();
        loadNextBookingId();
    }
}