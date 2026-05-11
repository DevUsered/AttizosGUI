package Attizos.Frontend;

import Attizos.Backend.Attizos.*;
import Attizos.Backend.Listas.ListaDE;
import Attizos.Backend.Listas.NodoDE;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

import java.util.HashSet;
import java.util.Set;

public class VentasController {
    @FXML private TextField tfNombreCli;
    @FXML private HBox hBCategoria;
    @FXML private FlowPane flPProductos;
    @FXML private VBox vBoxCarrito;
    @FXML private Label lblTotal;

    private Factura facturaActual;
    private String categoriaActiva = "Todos";

    private Producto productoSeleccionadoEnCarrito;
    private HBox filaSeleccionada;

    @FXML
    public void initialize() {
        iniciarNuevaVenta();
    }

    private void iniciarNuevaVenta() {
        int numFactura = App.attizos.generarNumeroFactura();
        tfNombreCli.clear();
        facturaActual = new Factura(numFactura, "");
        productoSeleccionadoEnCarrito = null;
        filaSeleccionada = null;

        cargarCategorias();
        mostrarProductosPorCategoria("Todos");
        actualizarVistaCarrito();
    }
    private void cargarCategorias() {
        hBCategoria.getChildren().clear();

        Button btnAll = crearBotonCategoria("Todos");
        hBCategoria.getChildren().add(btnAll);

        Set<String> cats = new HashSet<>();
        NodoDE<Producto> actual = App.attizos.getMenu().getCabeza();
        while (actual != null) {
            cats.add(actual.getDato().getCategoria());
            actual = actual.getSiguiente();
        }

        for (String c : cats) {
            hBCategoria.getChildren().add(crearBotonCategoria(c));
        }
    }

    private Button crearBotonCategoria(String nombreCat) {
        Button btn = new Button(nombreCat);
        btn.getStyleClass().add(nombreCat.equals(categoriaActiva) ? "menu-button-active" : "menu-button");
        btn.setOnAction(e -> {
            categoriaActiva = nombreCat;
            cargarCategorias();
            mostrarProductosPorCategoria(categoriaActiva);
        });
        return btn;
    }
    private void mostrarProductosPorCategoria(String categoria) {
        flPProductos.getChildren().clear();
        NodoDE<Producto> actual = App.attizos.getMenu().getCabeza();

        while (actual != null) {
            Producto p = actual.getDato();
            if (categoria.equals("Todos") || p.getCategoria().equalsIgnoreCase(categoria)) {

                VBox card = new VBox(8);
                card.getStyleClass().add("sale-product-card");
                card.setAlignment(Pos.CENTER);

                Label name = new Label(p.getNombre());
                name.getStyleClass().add("product-name");
                name.setWrapText(true);
                name.setTextAlignment(TextAlignment.CENTER);

                Label price = new Label("Bs. " + String.format("%.2f", p.getPrecio()));
                price.getStyleClass().add("product-price");

                int stock = p.calcularDisponibilidad(App.attizos.getInventario());
                Label lblStock = new Label("Stock: " + stock);
                lblStock.setStyle("-fx-text-fill: " + (stock > 0 ? "#b39ddb;" : "#ff4c4c;"));

                card.getChildren().addAll(name, price, lblStock);

                // Al hacer clic, se agrega 1 al carrito
                card.setOnMouseClicked(e -> agregarAlCarrito(p));

                flPProductos.getChildren().add(card);
            }
            actual = actual.getSiguiente();
        }
    }
    private void agregarAlCarrito(Producto p) {
        boolean agregado = facturaActual.agregarProducto(p, 1, App.attizos.getInventario());
        if (agregado) {
            actualizarVistaCarrito();
            mostrarProductosPorCategoria(categoriaActiva);
        } else {
            System.out.println("❌ No hay stock suficiente para: " + p.getNombre());
        }
    }
    private void seleccionarItemCarrito(HBox row, Producto p) {
        // Quitamos el color de selección anterior
        if (filaSeleccionada != null) {
            filaSeleccionada.setStyle("-fx-background-color: rgba(255, 255, 255, 0.05); -fx-background-radius: 10;");
        }

        filaSeleccionada = row;
        productoSeleccionadoEnCarrito = p;
        filaSeleccionada.setStyle("-fx-background-color: rgba(126, 87, 194, 0.5); -fx-background-radius: 10;");
    }

    @FXML
    void reducirProducto() {
        if (productoSeleccionadoEnCarrito != null) {
            NodoDE<DetalleFactura> actual = facturaActual.getDetalles().getCabeza();
            while (actual != null) {
                if (actual.getDato().getProducto().getId() == productoSeleccionadoEnCarrito.getId()) {
                    int nuevaCant = actual.getDato().getCantidad() - 1;
                    if (nuevaCant > 0) {
                        facturaActual.modificarCantidad(productoSeleccionadoEnCarrito, nuevaCant, App.attizos.getInventario());
                    } else {
                        facturaActual.eliminarProducto(productoSeleccionadoEnCarrito, App.attizos.getInventario());
                    }
                    break;
                }
                actual = actual.getSiguiente();
            }
            actualizarVistaCarrito();
            mostrarProductosPorCategoria(categoriaActiva);
        }
    }

    @FXML
    void quitarProducto() {
        if (productoSeleccionadoEnCarrito != null) {
            facturaActual.eliminarProducto(productoSeleccionadoEnCarrito, App.attizos.getInventario());
            actualizarVistaCarrito();
            mostrarProductosPorCategoria(categoriaActiva);
        }
    }
    @FXML
    void finalizarVenta() {
        if (facturaActual.getTotal() > 0) {
            String nombreCli = tfNombreCli.getText().trim();
            if (nombreCli.isEmpty()) nombreCli = "Sin Nombre";
            facturaActual.imprimirFactura();

            ListaDE<DetalleFactura> productosParaCocina = new ListaDE<>();
            NodoDE<DetalleFactura> actual = facturaActual.getDetalles().getCabeza();
            boolean requiereCocina = false;

            while (actual != null){
                if(actual.getDato().getProducto().tieneReceta()){
                    productosParaCocina.insertarAlFinal(actual.getDato());
                    requiereCocina = true;
                }
                actual = actual.getSiguiente();
            }
            if(requiereCocina){
                Pedido nuevoPedido = new Pedido(facturaActual.getNumeroFactura(), nombreCli, productosParaCocina, facturaActual.getTotal());
                App.attizos.agregarPedido(nuevoPedido);
                System.out.println("🔔 ¡DING! Pedido enviado a la cocina con éxito.");
            }

            App.attizos.registrarVentaFinalizada(facturaActual);
            System.out.println("✅ Venta registrada correctamente.");

            iniciarNuevaVenta();
        } else {
            System.out.println("⚠️ El carrito está vacío. Agregue productos antes de confirmar.");
        }
    }
    private void crearTarjetaProducto(Producto p) {
        VBox card = new VBox(8);
        card.getStyleClass().add("sale-product-card");
        card.setAlignment(Pos.CENTER);
        ImageView imgView = new ImageView();
        try {
            Image img = new Image(getClass().getResourceAsStream(p.getImagenURL()));
            imgView.setImage(img);
        } catch (Exception e) {
            imgView.setImage(new Image(getClass().getResourceAsStream("/Attizos/Frontend/images/default.png")));
        }
        imgView.setFitHeight(80);
        imgView.setFitWidth(80);
        imgView.setPreserveRatio(true);
        imgView.getStyleClass().add("product-image-view");

        Label name = new Label(p.getNombre());
        name.getStyleClass().add("product-name");
        name.setWrapText(true);
        name.setTextAlignment(TextAlignment.CENTER);

        Label price = new Label("Bs. " + String.format("%.2f", p.getPrecio()));
        price.getStyleClass().add("product-price");

        int stock = p.calcularDisponibilidad(App.attizos.getInventario());
        Label lblStock = new Label("Stock: " + stock);
        lblStock.setStyle("-fx-text-fill: " + (stock > 0 ? "#b39ddb;" : "#ff4c4c;"));

        // Añadimos la imagen al inicio de la tarjeta
        card.getChildren().addAll(imgView, name, price, lblStock);

        card.setOnMouseClicked(e -> agregarAlCarrito(p));
        flPProductos.getChildren().add(card);
    }
    private void actualizarVistaCarrito() {
        vBoxCarrito.getChildren().clear();

        // Gracias a tu clase Factura.java, aquí los productos ya vienen sumados
        NodoDE<DetalleFactura> actual = facturaActual.getDetalles().getCabeza();

        while (actual != null) {
            DetalleFactura det = actual.getDato();
            Producto p = det.getProducto();

            HBox itemRow = new HBox(10);
            itemRow.getStyleClass().add("cart-item");
            itemRow.setAlignment(Pos.CENTER_LEFT);

            // --- IMAGEN MINIATURA PARA EL CARRITO ---
            ImageView imgCart = new ImageView();
            try {
                imgCart.setImage(new Image(getClass().getResourceAsStream(p.getImagenURL())));
            } catch (Exception e) {
                imgCart.setImage(new Image(getClass().getResourceAsStream("/Attizos/Frontend/images/default.png")));
            }
            imgCart.setFitHeight(40);
            imgCart.setFitWidth(40);
            imgCart.setPreserveRatio(true);

            // Información del producto y cantidad
            VBox infoCol = new VBox(2);
            Label name = new Label(p.getNombre());
            name.getStyleClass().add("cart-item-text");
            name.setStyle("-fx-font-weight: bold;");

            Label qty = new Label("Cantidad: " + det.getCantidad());
            qty.setStyle("-fx-text-fill: #9d8bc9; -fx-font-size: 11px;");

            infoCol.getChildren().addAll(name, qty);

            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            // Subtotal calculado (Precio * Cantidad)
            Label sub = new Label("Bs." + String.format("%.2f", det.getSubtotal()));
            sub.getStyleClass().add("cart-item-subtotal");

            itemRow.getChildren().addAll(imgCart, infoCol, spacer, sub);

            // Selección para botones reducir/quitar
            itemRow.setOnMouseClicked(e -> seleccionarItemCarrito(itemRow, p));

            vBoxCarrito.getChildren().add(itemRow);
            actual = actual.getSiguiente();
        }
        lblTotal.setText(String.format("%.2f", facturaActual.getTotal()));
    }
}