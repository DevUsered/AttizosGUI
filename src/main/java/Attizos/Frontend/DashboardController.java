package Attizos.Frontend;

import Attizos.Backend.Attizos.*;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.IOException;

public class DashboardController {
    @FXML
    private AnchorPane rootPane;
    @FXML
    private VBox sidebar;
    @FXML
    private Button btnToggle;
    @FXML private Label lblName;
    @FXML private Label lblCargo;
    @FXML private VBox vBCajero;
    @FXML private VBox vBAdmin;
    @FXML private Label lblAdmin;
    @FXML private StackPane contentArea;


    private boolean menuAbierto = true;
    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    public void initialize() {
        configurarVentana();
        if(App.usuarioLogueado !=null){
            lblName.setText(App.usuarioLogueado.getNombre());
            lblCargo.setText(App.usuarioLogueado.getCargo());
            restriccion();
        }
        else{
            lblName.setText("Modo Prueba");
            lblCargo.setText("Desarrollador");
        }
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
    void cerrarVentana(ActionEvent event){
        System.exit(0);
    }
    @FXML
    void minimizarVentana(ActionEvent event){
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.setIconified(true);
    }
    @FXML
    void maximizarVentana(ActionEvent event){
        Stage stage = (Stage) rootPane.getScene().getWindow();
        javafx.scene.layout.HBox contenedor = (javafx.scene.layout.HBox) rootPane.getChildren().get(0);
        if(stage.isMaximized()) {
            stage.setMaximized(false);
            AnchorPane.setTopAnchor(contenedor, 20.0);
            AnchorPane.setBottomAnchor(contenedor, 20.0);
            AnchorPane.setLeftAnchor(contenedor, 20.0);
            AnchorPane.setRightAnchor(contenedor, 20.0);

            contenedor.setStyle("-fx-background-color: rgba(38, 15, 65, 0.75); -fx-background-radius: 25; -fx-border-radius: 25;");
        }else{
            stage.setMaximized(true);
            stage.setFullScreenExitHint("");
            AnchorPane.setTopAnchor(contenedor, 0.0);
            AnchorPane.setBottomAnchor(contenedor, 0.0);
            AnchorPane.setLeftAnchor(contenedor, 0.0);
            AnchorPane.setRightAnchor(contenedor, 0.0);
            contenedor.setStyle("-fx-background-color: #1a0a2a; -fx-background-radius: 0; -fx-border-radius: 0;");
        }
    }
    @FXML
    void openSales(ActionEvent event){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Sales.fxml"));
            loader.setController(new VentasController());

            Parent view = loader.load();
            contentArea.getChildren().setAll(view);
        }catch (Exception e){
            e.printStackTrace();
        }
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
    private void restriccion(){
        if(App.usuarioLogueado instanceof Cajero){
            vBAdmin.setVisible(false);
            lblAdmin.setVisible(false);
        }
    }
    @FXML
    void logout(ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Home.fxml"));
            Stage stage = new Stage();
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

            Stage vAc = (Stage) ((Node) event.getSource()).getScene().getWindow();
            vAc.close();
        }catch(IOException e ){
            System.out.println("Error al cargar la ventana de Home: " + e.getMessage());
        }
    }
}