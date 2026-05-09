package Attizos.Frontend;

import Attizos.Backend.Attizos.*;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class DashboardController {
    @FXML
    private AnchorPane rootPane;
    @FXML
    private VBox sidebar;
    @FXML
    private Button btnToggle;

    private boolean menuAbierto = true;
    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    public void initialize() {
        System.out.println("Dashboard ControllerInicialized");
        configurarVentana();
    }

    private void configurarVentana() {
        rootPane.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        rootPane.setOnMouseDragged(event -> {
            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });
    }

    @FXML
    void toggleMenu(ActionEvent event) {
        Timeline timeline = new Timeline();

        // Configuramos los valores finales dependiendo de si abrimos o cerramos
        double anchoFinal = menuAbierto ? 0 : 260;
        double opacidadFinal = menuAbierto ? 0 : 1;
        double rotacionBoton = menuAbierto ? 90 : 0;

        double desplazamientoBoton = menuAbierto ? -5 : 0;

        // Animación de ancho
        KeyValue kvWidth = new KeyValue(sidebar.prefWidthProperty(), anchoFinal, Interpolator.EASE_BOTH);
        KeyValue kvMinWidth = new KeyValue(sidebar.minWidthProperty(), anchoFinal, Interpolator.EASE_BOTH);

        // Animación de transparencia (para un efecto suave)
        KeyValue kvOpacity = new KeyValue(sidebar.opacityProperty(), opacidadFinal, Interpolator.EASE_BOTH);


        KeyValue kvRotate = new KeyValue(btnToggle.rotateProperty(), rotacionBoton, Interpolator.EASE_BOTH);
        KeyValue kvTranslate = new KeyValue(btnToggle.translateXProperty(), desplazamientoBoton, Interpolator.EASE_BOTH);

        KeyFrame frame = new KeyFrame(Duration.millis(300), kvWidth, kvMinWidth, kvOpacity, kvRotate, kvTranslate);

        timeline.getKeyFrames().add(frame);
        timeline.play();
        menuAbierto = !menuAbierto;
    }
    //Buttons
    @FXML
    void openSales(ActionEvent event){
        System.out.println("Abriendo ventana de ventas... (Funcionalidad en proceso)");
    }
    @FXML
    void openOrders(ActionEvent event){
        System.out.println("Abriendo ventana de pedidos... (Funcionalidad en proceso)");
    }
    @FXML
    void openReservations(ActionEvent event){
        System.out.println("Abriendo ventana de reservas... (Funcionalidad en proceso)");
    }
    @FXML
    void openProducts(ActionEvent event){
        System.out.println("Abriendo ventana de productos... (Funcionalidad en proceso)");
    }
    @FXML
    void openInventory(ActionEvent event){
        System.out.println("Abriendo ventana de inventario... (Funcionalidad en proceso)");
    }
    @FXML
    void openReports(ActionEvent event){
        System.out.println("Abriendo ventana de reportes... (Funcionalidad en proceso)");
    }
    @FXML
    void openEmploys(ActionEvent event){
        System.out.println("Abriendo ventana de empleados... (Funcionalidad en proceso)");
    }

}