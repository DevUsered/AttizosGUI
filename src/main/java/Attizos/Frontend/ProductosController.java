package Attizos.Frontend;

import Attizos.Backend.Attizos.*;
import Attizos.Backend.Listas.NodoDE;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashSet;
import java.util.Set;

public class GestionMenuController {

    @FXML private TableView<Producto> tablaMenu;
    @FXML private TableColumn<Producto, Integer> colId;
    @FXML private TableColumn<Producto, String> colNombre, colCategoria, colStock, colTipo;
    @FXML private TableColumn<Producto, Double> colPrecio;

    @FXML private TextField txtBuscador, txtNombre, txtPrecio, txtStock, txtAjusteCant, txtMotivoAjuste, txtCantidadReceta;
    @FXML private ComboBox<String> cmbCategoria, cmbTipoClase;
    @FXML private ComboBox<Insumo> cmbInsumos;
    @FXML private VBox panelAtributosDinamicos;
    @FXML private ImageView imgPreview;

    @FXML private TableView<DetalleRecetaUI> tablaRecetaFila;
    @FXML private TableColumn<DetalleRecetaUI, String> colRecetaInsumo;
    @FXML private TableColumn<DetalleRecetaUI, Double> colRecetaCant;

    private ObservableList<Producto> masterData = FXCollections.observableArrayList();
    private FilteredList<Producto> filteredData;
    private File archivoImagenSeleccionada;
    private ObservableList<DetalleRecetaUI> recetaTemporal = FXCollections.observableArrayList();

    // Nodos dinámicos (se crean según el tipo)
    private CheckBox chkExtraQueso = new CheckBox("¿Lleva Extra Queso?");
    private ComboBox<String> cmbTamanoPizza = new ComboBox<>(FXCollections.observableArrayList("Personal", "Mediana", "Familiar", "Gigante"));
    private ComboBox<TamanoBebida> cmbTamanoBebida = new ComboBox<>(FXCollections.observableArrayList(TamanoBebida.values()));
    private TextField txtSalsaPasta = new TextField();

    @FXML
    public void initialize() {
        configurarTabla();
        configurarBuscador();

        cmbTipoClase.setItems(FXCollections.observableArrayList("Pizza", "Bebida", "Pasta", "Calzone", "Postre", "Otro"));
        cmbTipoClase.setOnAction(e -> actualizarCamposDinamicos());

        cargarMenu();
        cargarInsumos();
    }

    private void configurarTabla() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colStock.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().tieneReceta() ? "Cocina" : String.valueOf(d.getValue().getStock())));
        colTipo.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getClass().getSimpleName()));

        filteredData = new FilteredList<>(masterData, p -> true);
        tablaMenu.setItems(filteredData);
    }

    private void configurarBuscador() {
        txtBuscador.textProperty().addListener((obs, old, newValue) -> {
            filteredData.setPredicate(producto -> {
                if (newValue == null || newValue.isEmpty()) return true;
                String lower = newValue.toLowerCase();
                return producto.getNombre().toLowerCase().contains(lower) ||
                        producto.getCategoria().toLowerCase().contains(lower) ||
                        String.valueOf(producto.getId()).contains(lower);
            });
        });
    }

    private void actualizarCamposDinamicos() {
        panelAtributosDinamicos.getChildren().clear();
        String tipo = cmbTipoClase.getValue();
        if (tipo == null) return;

        chkExtraQueso.setTextFill(javafx.scene.paint.Color.WHITE);
        txtSalsaPasta.setPromptText("Tipo de Salsa / Descripción");
        txtSalsaPasta.setStyle("-fx-background-color: rgba(0,0,0,0.3); -fx-text-fill: white;");

        switch (tipo) {
            case "Pizza":
                panelAtributosDinamicos.getChildren().addAll(new Label("Tamaño:"), cmbTamanoPizza, chkExtraQueso);
                break;
            case "Bebida":
                panelAtributosDinamicos.getChildren().addAll(new Label("Tamaño Bebida:"), cmbTamanoBebida);
                break;
            case "Pasta":
                panelAtributosDinamicos.getChildren().addAll(new Label("Salsa:"), txtSalsaPasta);
                break;
        }
    }

    @FXML
    void seleccionarImagen(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg"));
        archivoImagenSeleccionada = fileChooser.showOpenDialog(null);
        if (archivoImagenSeleccionada != null) {
            imgPreview.setImage(new Image(archivoImagenSeleccionada.toURI().toString()));
        }
    }

    @FXML
    void guardarNuevoProducto(ActionEvent event) {
        try {
            int id = generarSiguienteId();
            String nombre = txtNombre.getText();
            double precio = Double.parseDouble(txtPrecio.getText());
            String cat = cmbCategoria.getValue();
            int stock = Integer.parseInt(txtStock.getText());
            String tipo = cmbTipoClase.getValue();

            // 1. Guardar Imagen físicamente
            String nombreImagen = "default.png";
            if (archivoImagenSeleccionada != null) {
                nombreImagen = id + "_" + archivoImagenSeleccionada.getName();
                File destino = new File("src/main/resources/images/Productos/" + nombreImagen);
                Files.copy(archivoImagenSeleccionada.toPath(), destino.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }

            // 2. Crear instancia según Subclase (Polimorfismo)
            Producto nuevo;
            switch (tipo) {
                case "Pizza":
                    nuevo = new Pizza(id, nombre, precio, cat, stock, cmbTamanoPizza.getValue(), "", chkExtraQueso.isSelected());
                    break;
                case "Bebida":
                    nuevo = new Bebida(id, nombre, precio, cat, stock, cmbTamanoBebida.getValue(), "Gaseosa");
                    break;
                default:
                    nuevo = new Producto(id, nombre, precio, cat, stock);
            }

            nuevo.setImagenURL(nombreImagen); // Suponiendo que agregaste este setter en Producto

            App.attizos.getMenu().insertarAlFinal(nuevo);
            cargarMenu();
            mostrarExito("Guardado", "Producto registrado correctamente.");

        } catch (Exception e) {
            mostrarAlerta("Error", "Verifique los datos e imagen.");
            e.printStackTrace();
        }
    }

    private int generarSiguienteId() {
        int max = 0;
        NodoDE<Producto> act = App.attizos.getMenu().getCabeza();
        while (act != null) {
            if (act.getDato().getId() > max) max = act.getDato().getId();
            act = act.getSiguiente();
        }
        return max + 1;
    }

    @FXML
    void sumarStockExistente(ActionEvent event) {
        Producto sel = tablaMenu.getSelectionModel().getSelectedItem();
        if (sel != null) {
            sel.aumentarStock(Integer.parseInt(txtAjusteCant.getText()));
            tablaMenu.refresh();
        }
    }

    @FXML
    void restarStock(ActionEvent event) {
        Producto sel = tablaMenu.getSelectionModel().getSelectedItem();
        String motivo = txtMotivoAjuste.getText();
        if (sel != null && !motivo.isEmpty()) {
            sel.reducirStock(Integer.parseInt(txtAjusteCant.getText()));
            tablaMenu.refresh();
        } else {
            mostrarAlerta("Motivo Requerido", "Debe justificar la reducción de stock.");
        }
    }

    private void cargarMenu() {
        masterData.clear();
        NodoDE<Producto> act = App.attizos.getMenu().getCabeza();
        while (act != null) {
            masterData.add(act.getDato());
            act = act.getSiguiente();
        }
    }

    // --- Métodos de apoyo ---
    private void cargarInsumos() { /* Carga cmbInsumos desde el Inventario */ }

    @FXML void cerrarVentana(ActionEvent e) { /* Lógica de regreso al Home */ }

    private void mostrarAlerta(String t, String m) {
        new Alert(Alert.AlertType.WARNING, m).show();
    }
    private void mostrarExito(String t, String m) {
        new Alert(Alert.AlertType.INFORMATION, m).show();
    }

    // Clase estática para la tabla de recetas (Igual que antes)
    public static class DetalleRecetaUI { /* ... */ }
}