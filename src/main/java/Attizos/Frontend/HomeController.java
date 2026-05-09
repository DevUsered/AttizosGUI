package Attizos.Frontend;

import Attizos.Backend.Attizos.App;
import Attizos.Backend.Attizos.Producto;
import Attizos.Backend.Listas.NodoDE;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import Attizos.Backend.Attizos.*;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class HomeController {
    @FXML private FlowPane containerProducts;
    @FXML private ComboBox<String> comboBoxCategories;

    @FXML
    public void initialize(){
        updateCategories();
        filterAndDisplay("Todo");
        comboBoxCategories.setOnAction(e ->
                filterAndDisplay(comboBoxCategories.getValue()));
    }
    private void updateCategories(){
        comboBoxCategories.getItems().clear();
        comboBoxCategories.getItems().add("Todo");

        Set<String> categorias = new HashSet<>();
        NodoDE<Producto> actual = App.attizos.getMenu().getCabeza();
        while(actual != null){
            categorias.add(actual.getDato().getCategoria());
            actual = actual.getSiguiente();
        }
        comboBoxCategories.getItems().addAll(categorias);
        comboBoxCategories.getSelectionModel().selectFirst();
    }
    private void filterAndDisplay(String categorie){
        containerProducts.getChildren().clear();

        NodoDE<Producto> actual = App.attizos.getMenu().getCabeza();
        while(actual != null){
            Producto p = actual.getDato();
            if(categorie.equals("Todo") || p.getCategoria().equalsIgnoreCase(categorie)){
               VBox newCard = createCard(p);
               containerProducts.getChildren().add(newCard);
            }
            actual = actual.getSiguiente();
        }
    }
    private  VBox createCard(Producto p){
        VBox card = new VBox();
        card.getStyleClass().add("product-card");
        card.setPrefWidth(240);
        card.setSpacing(10);

        Label name = new Label(p.getNombre());
        name.getStyleClass().add("product-name");

        Label price = new Label(String.format("Bs.%.2f", p.getPrecio()));
        price.getStyleClass().add("product-price");

        ImageView imagen = new ImageView();
        try{
            String rutaImg = "/images/Productos/"+p.getImagenURL();
            imagen.setImage(new Image(getClass().getResourceAsStream(rutaImg)));
            imagen.setFitHeight(120);
            imagen.setPreserveRatio(true);
        }catch (Exception e){
            System.out.println("No se encontró la imagen para el producto: " + p.getNombre());
        }
        card.getChildren().addAll(name,imagen,price);
        return card;
    }
    @FXML
    void login(ActionEvent event){
        try{
            Parent root = FXMLLoader.load(getClass().getResource("/Login.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Iniciar sesión");
            stage.show();

            Stage ventanaActual = (Stage) ((Node) event.getSource()).getScene().getWindow();
            ventanaActual.close();
        }catch (IOException e){
            System.out.println("Error al cargar la ventana de Login: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
