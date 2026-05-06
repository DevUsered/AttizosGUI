package Attizos.Frontend;

import Attizos.Backend.Attizos.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;

public class DashboardController {

    @FXML private VBox panelAdmin;
    @FXML private AnchorPane panelCentral;
    @FXML private Label lblNombreUsuario;
    @FXML private Label lblRol;
    @FXML private ImageView imgPerfil; // <- Enlazamos la imagen

    @FXML
    public void initialize() {
        Circle clip = new Circle(22, 22, 22); // Radio de 22
        imgPerfil.setClip(clip);

        try {
            Image foto = new Image(getClass().getResourceAsStream("/perfil.png"));
            imgPerfil.setImage(foto);
        } catch (Exception e) {
            System.out.println("No se encontró la foto de perfil, se dejará vacía.");
        }

        if (App.usuarioLogueado != null) {
            lblNombreUsuario.setText(App.usuarioLogueado.getNombre());
            lblRol.setText(App.usuarioLogueado.getCargo().toUpperCase());

            if (!(App.usuarioLogueado instanceof Admin)) {
                panelAdmin.setVisible(false);
                panelAdmin.setManaged(false);
            }
        }
    }

    @FXML void abrirVentas(ActionEvent event) { System.out.println("Abriendo Ventas..."); }
    @FXML void abrirPedidos(ActionEvent event) { System.out.println("Abriendo Pedidos..."); }
    @FXML void abrirReservas(ActionEvent event) { System.out.println("Abriendo Reservas..."); }
    @FXML void abrirInventario(ActionEvent event) { System.out.println("Abriendo Inventario..."); }
    @FXML void abrirGestionMenu(ActionEvent event) { System.out.println("Abriendo Menú..."); }
    @FXML void abrirEmpleados(ActionEvent event) { System.out.println("Abriendo Empleados..."); }
    @FXML void abrirReportes(ActionEvent event) { System.out.println("Abriendo Reportes..."); }

    @FXML
    void cerrarSesion(ActionEvent event) {
        System.out.println("Cerrando sesión...");
        System.exit(0);
    }
}