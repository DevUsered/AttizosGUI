package Attizos.Frontend;

import Attizos.Backend.Attizos.App;
import Attizos.Backend.Attizos.Cocinero;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
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
          try{
              String vistaDestino = "/Dashboard.fxml";
              String tituloVentana = "AttizosPOS - Panel de Control";
              if(App.usuarioLogueado instanceof Cocinero){
                  vistaDestino = "/Cocina.fxml";
                  tituloVentana = "AttizosPOS - Monitor de Cocina";
              }
              FXMLLoader loader = new FXMLLoader(getClass().getResource(vistaDestino));
              Parent root = loader.load();

              Stage stage = new Stage();
              Scene scene = new Scene(root);
              scene.setFill(javafx.scene.paint.Color.TRANSPARENT);
              stage.setTitle(tituloVentana);
              stage.setScene(scene);

              if (App.usuarioLogueado instanceof Cocinero) {
                  stage.setMaximized(true);
              }
              stage.show();
              Stage loginStage = (Stage) btnLogin.getScene().getWindow();
              loginStage.close();
          }catch (IOException e){
              e.printStackTrace();
              lblMensaje.setText("❌ Error al cargar la interfaz");
          }
      }else{
          lblMensaje.setStyle("-fx-text-fill: #ff4c4c;");
          lblMensaje.setText("❌ Usuario o contraseña incorrectos");
          txtPassword.clear();
          txtUsuario.requestFocus();
      }
    }
}
