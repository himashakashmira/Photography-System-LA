package lk.ijse.photostudio.Controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

import javax.mail.MessagingException;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import lk.ijse.photostudio.App;
import lk.ijse.photostudio.DTO.AdminDTO;
import lk.ijse.photostudio.Model.AdminModel;

public class LoginController {

    @FXML private TextField userNameField;
    @FXML private PasswordField passwordField;

    @FXML
    public void login() throws IOException {
        String username = userNameField.getText();
        String password = passwordField.getText();

        try {
            AdminDTO admin = AdminModel.getAdminByUsername(username);

            if (admin != null && admin.getPassword().equals(password)) {
                System.out.println("Logged in Successfully.!");
                App.setRoot("Dashboard");
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Login Message");
                alert.setHeaderText("Invalid Username Or Password!");
                alert.show();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Database Error: " + e.getMessage()).show();
        }
    }

    @FXML
    void handleForgotPassword() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Forgot Password");
        dialog.setHeaderText("Recover Password via Email");
        dialog.setContentText("Please enter your registered email address:");

        Optional<String> result = dialog.showAndWait();

        result.ifPresent(email -> {
            if (email.trim().isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "Email cannot be empty!").show();
                return;
            }
            sendRecoveryEmail(email.trim());
        });
    }

    private void sendRecoveryEmail(String email) {
        new Thread(() -> {
            try {
                // Ensure this method exists in AdminModel
                String password = AdminModel.getPasswordByEmail(email);

                if (password != null) {
                    String subject = "Vision Photography - Password Recovery";
                    String body = "Hello,\n\nYour Current Password is: " + password +
                            "\n\nPlease keep it safe.";

                    // Ensure EmailUtil class exists
                    EmailUtil.sendEmail(email, subject, body);

                    Platform.runLater(() ->
                            new Alert(Alert.AlertType.INFORMATION, "Password has been sent to " + email).show()
                    );
                } else {
                    Platform.runLater(() ->
                            new Alert(Alert.AlertType.ERROR, "This email is not registered in our system.").show()
                    );
                }

            } catch (SQLException e) {
                e.printStackTrace();
            } catch (MessagingException e) {
                e.printStackTrace();
                Platform.runLater(() -> new Alert(Alert.AlertType.ERROR, "Could not send email. Check internet.").show());
            }
        }).start();
    }
}