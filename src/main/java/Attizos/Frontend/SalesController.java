/*package Attizos.Frontend;

import Attizos.Backend.*;
import Attizos.Backend.Attizos.App;
import Attizos.Backend.Attizos.Producto;
import Attizos.Backend.Listas.NodoDE;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Font;

import javax.swing.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
public class SalesController {
    //Left side
    @FXML private TableView<Producto> productsTable;
    @FXML private TableColumn<Producto, Integer> nameColumn;
    @FXML private TableColumn<Producto, Double> priceColumn;
    @FXML private TableColumn<Producto, Integer> stockColumn;
    @FXML private ComboBox<String> categoryColumn;
    //Right side
    @FXML private TextField txtCustomerName;
    @FXML private Label lblTotal;

    @FXML private TableView<ItemCarrito> cartTable;
    @FXML private TableColumn<ItemCarrito, String> colCarProduct;
    @FXML private  TableColumn<ItemCarrito, Integer> colCarQuantity;
    @FXML private TableColumn<ItemCarrito, Double> colCarPrecio;
    @FXML private TableColumn<ItemCarrito, Double> colCarSubtotal;

    private ObservableList<Producto> productosObservableList;
    private ObservableList<ItemCarrito> carritoObservableList;

    @FXML
    public void initialize(){
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("precio"));
        stockColumn.setCellValueFactory(cellData ->{
            Producto p = cellData.getValue();
            int actualInventory = p.calcularDisponibilidad(App.attizos.getInventario());
            int available = actualInventory - getCantidadEnCarrito(p);
            if(available <= 0){
                return new javafx.beans.property.SimpleStringProperty("❌ Agotado");
            }else{
                return new javafx.beans.property.SimpleStringProperty(String.valueOf(available));
            }
        });

        colCarProduct.setCellValueFactory(new PropertyValueFactory<>("productName"));
        colCarQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colCarPrecio.setCellValueFactory(new PropertyValueFactory<>("precioUnitario"));
        colCarSubtotal.setCellValueFactory(new PropertyValueFactory<>("subtotal"));

        productosObservableList = FXCollections.observableArrayList();
        carritoObservableList = FXCollections.observableArrayList();

        productsTable.setItems(productosObservableList);
        cartTable.setItems(carritoObservableList);
        cargarMenu();
    }
    private void cargarMenu(){
        productosObservableList.clear();
        try{
            NodoDE<Producto> actual = App.attizos.getMenu().getCabeza();
            while(actual != null){
                productosObservableList.add(actual.getDato());
                actual = actual.getSiguiente();
            }
            productosObservableList.sort((p1,p2) ->{
                int catCmp = p1.getCategoria().compareToIgnoreCase(p2.getCategoria());
                if (catCmp != 0) return catCmp;
                return p1.getNombre().compareToIgnoreCase(p2.getNombre());
            });

            Set<String> categoriasSet = new HashSet<>();
            for(Producto p : productosObservableList){
                categoriasSet.add(p.getCategoria());
            }
            java.util.List<String> categoriasList = new java.util.ArrayList<>(categoriasSet);
            categoriasList.sort(String::compareToIgnoreCase);
            ObservableList<String> listaCats = FXCollections.observableArrayList(categoriasList);
            listaCats.add(0, "Todos");
            comboCategoria.setItems(listaCats);
            comboCategoria.setValue("Todos");
            comboCategoria.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
                filtrarPorCategoria(newVal);
            });
        }catch (Exception e){
            System.out.println("⚠️ El menú aún está vacío o el método getMenu no está conectado.");
        }
    }
    @FXML
    void agregarAlCarrito(Action event){
        Producto seleccionado = productsTable.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta("Selección requerida", "Por favor, seleccione un producto del menú izquierdo.");
            return;
        }
        int stockReal = seleccionado.calcularDisponibilidad(App.attizos.getInventario());
        int disponible = stockReal - getCantidadEnCarrito(seleccionado);

        if (disponible <= 0) {
            mostrarAlerta("Sin Stock", "El producto '" + seleccionado.getNombre() + "' está agotado por falta de ingredientes o unidades.");
            return;
        }
        boolean encontrado = false;
        for (ItemCarrito item : carritoObservableList) {
            if (item.getProducto().getNombre().equals(seleccionado.getNombre())) {
                item.setCantidad(item.getCantidad() + 1);
                item.setSubtotal(item.getCantidad() * seleccionado.getPrecio());
                encontrado = true;
                break;
            }
        }
        if (!encontrado) {
            ItemCarrito nuevoItem = new ItemCarrito(seleccionado, 1);
            carritoObservableList.add(nuevoItem);
        }
        productsTable.refresh();
        cartTable.refresh();
        actualizarTotal();
    }
}*/
