package Attizos.Frontend;

import Attizos.Backend.Attizos.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
public class LoginController {
    @FXML private ImageView logoEmpresa;
    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtPassword;
    @FXML private Button btnLogin;
    @FXML private Label lblMensaje;
    @FXML
    void enfocarPassword(ActionEvent event) {
        txtPassword.requestFocus();
    }
    @FXML
    void iniciarSesion(ActionEvent event) {
        String username = txtUsuario.getText();
        String password = txtPassword.getText();

      if(App.validarAcceso(username, password)){
          lblMensaje.setStyle("-fx-text-fill: #4caf50;");
          lblMensaje.setText("✅ ¡Bienvenido a Attizos!");
      }else{
          lblMensaje.setStyle("-fx-text-fill: #ff4c4c;");
          lblMensaje.setText("❌ Usuario o contraseña incorrectos");
          txtPassword.clear();
          txtUsuario.requestFocus();
      }
    }
}
