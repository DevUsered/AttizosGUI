package Attizos.Frontend;

import Attizos.Backend.Attizos.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;

public class CocinaController {

    @FXML private Label lblNombreCocinero;
    @FXML private FlowPane panelPedidos;

    @FXML
    public void initialize() {
        if (App.usuarioLogueado != null) {
            lblNombreCocinero.setText(App.usuarioLogueado.getNombre());
        }
    }

    @FXML
    void cerrarSesion(ActionEvent event) {
        System.out.println("Cerrando sesión de cocina...");
        // Como es un entorno de prueba, cerramos el programa.
        // Luego lo cambiaremos para que vuelva a abrir la ventana de Login.
        System.exit(0);
    }
}
