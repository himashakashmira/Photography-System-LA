package lk.ijse.photostudio.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import lk.ijse.photostudio.DTO.AdminDTO;
import lk.ijse.photostudio.Model.AdminModel;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class SettingController implements Initializable {

    @FXML private PasswordField txtCurrentPass;
    @FXML private PasswordField txtNewPass;
    @FXML private PasswordField txtConfirmPass;

    private String loggedInUsername = "h1";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    @FXML
    private void handleChangePassword(ActionEvent event) {
        String currentPassword = txtCurrentPass.getText();
        String newPassword = txtNewPass.getText();
        String confirmPassword = txtConfirmPass.getText();

        // Input Validation
        if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please fill in all password fields.").show();
            return;
        }
        if (!newPassword.equals(confirmPassword)) {
            new Alert(Alert.AlertType.ERROR, "New password and Confirm password do not match.").show();
            return;
        }
        if (newPassword.length() < 3) { // password strength check
            new Alert(Alert.AlertType.WARNING, "New password must be at least 3 characters long.").show();
            return;
        }
        if (currentPassword.equals(newPassword)) {
            new Alert(Alert.AlertType.WARNING, "New password cannot be the same as the current password.").show();
            return;
        }

        try {
            // Verify Current Password
            AdminDTO admin = AdminModel.getAdminByUsername(loggedInUsername);

            if (admin == null) {
                new Alert(Alert.AlertType.ERROR, "Error: Logged-in user not found. Please contact support.").show();
                return;
            }

            if (!admin.getPassword().equals(currentPassword)) {
                new Alert(Alert.AlertType.ERROR, "Incorrect current password.").show();
                return;
            }

            // Update Password in Database
            boolean updated = AdminModel.updatePassword(loggedInUsername, newPassword);

            if (updated) {
                new Alert(Alert.AlertType.INFORMATION, "Password changed successfully!").show();
                clearFields();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to change password. Please try again.").show();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Database error: " + e.getMessage()).show();
        }
    }

    @FXML
    private void handleClear(ActionEvent event) {
        clearFields();
    }

    private void clearFields() {
        txtCurrentPass.clear();
        txtNewPass.clear();
        txtConfirmPass.clear();
    }
}