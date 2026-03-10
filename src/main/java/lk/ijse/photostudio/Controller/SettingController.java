package lk.ijse.photostudio.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import lk.ijse.photostudio.BO.BOFactory;
import lk.ijse.photostudio.BO.Admin.AdminBO;

import java.net.URL;
import java.util.ResourceBundle;

public class SettingController implements Initializable {

    @FXML
    private PasswordField txtCurrentPass;
    @FXML
    private PasswordField txtNewPass;
    @FXML
    private PasswordField txtConfirmPass;

    private String loggedInUsername = "h1";

    AdminBO adminBO = (AdminBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.ADMIN);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    @FXML
    private void handleChangePassword(ActionEvent event) {
        String current = txtCurrentPass.getText();
        String next = txtNewPass.getText();
        String confirm = txtConfirmPass.getText();

        if (current.isEmpty() || next.isEmpty() || confirm.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please fill all fields!").show();
            return;
        }

        if (!next.equals(confirm)) {
            new Alert(Alert.AlertType.ERROR, "Passwords do not match!").show();
            return;
        }

        try {
            // check old password
            if (adminBO.authenticate(loggedInUsername, current)) {

                // update new password
                if (adminBO.updatePassword(loggedInUsername, next)) {
                    new Alert(Alert.AlertType.INFORMATION, "Password Changed Successfully!").show();
                    handleClear();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Update Failed!").show();
                }

            } else {
                new Alert(Alert.AlertType.ERROR, "Current Password Incorrect!").show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleClear() {
        txtCurrentPass.clear();
        txtNewPass.clear();
        txtConfirmPass.clear();
    }
}