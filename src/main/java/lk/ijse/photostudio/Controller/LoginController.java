package lk.ijse.photostudio.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import lk.ijse.photostudio.App;
import lk.ijse.photostudio.BO.BOFactory;
import lk.ijse.photostudio.BO.Admin.AdminBO;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField userNameField;
    @FXML
    private PasswordField passwordField;

    AdminBO adminBO = (AdminBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.ADMIN);

    @FXML
    public void login() throws IOException {
        String username = userNameField.getText();
        String password = passwordField.getText();

        try {
            if (adminBO.authenticate(username, password)) {
                System.out.println("Logged in Successfully!");
                App.setRoot("Dashboard");
            } else {
                new Alert(Alert.AlertType.ERROR, "Invalid Username Or Password!").show();
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Login Error: " + e.getMessage()).show();
        }
    }
}