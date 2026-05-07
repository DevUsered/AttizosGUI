package Attizos.Backend.Attizos;
import Attizos.Backend.Listas.NodoDE;

import java.time.LocalDate;

public class App {
    public static Restaurante attizos;
    public static Usuario usuarioLogueado;

    public static void cargarDatosEnRAM() {
        System.out.println("Cargando el sistema [EP]::Core para Pizzería Attizos en RAM...");
        attizos = new Restaurante("Pizzería Attizos");

        // CREACIÓN DE EMPLEADOS
        Admin jefe = new Admin(1, "Edgar Perez", "admin", "admin123", 12000);
        Cajero cajero1 = new Cajero(2, "Fulanito (Cajero)", 3500.0, "caja1", "123");
        Cocinero cocinero = new Cocinero(4, "Vinícius Jr.", 5000, "cocina1","abc");
        Empleado mesero1 = new Empleado(5, "Cristiano Ronaldo", "Mesero", 3300);
        Empleado limpieza = new Empleado(6, "Doña María", "Limpieza", 2500);

        attizos.agregarEmpleado(jefe);
        attizos.agregarEmpleado(cajero1);
        attizos.agregarEmpleado(cocinero);
        attizos.agregarEmpleado(mesero1);
        attizos.agregarEmpleado(limpieza);

        Inventario almacen = attizos.getInventario();
        LocalDate fechaBuena = LocalDate.now().plusMonths(6);
        LocalDate fechaCasiVencida = LocalDate.now().plusDays(3);
        LocalDate fechaVencida = LocalDate.now().minusDays(5);
        LocalDate fechaLejana = LocalDate.now().plusYears(1);

        // Masas y Bases
        almacen.agregarInsumo(new Insumo("I001", "Harina de trigo 000", "Masas", "kg", 50.0, 10.0, 100.0, fechaBuena));
        almacen.agregarInsumo(new Insumo("I002", "Levadura Fresca", "Masas", "gr", 500.0, 100.0, 1000.0, fechaCasiVencida));
        almacen.agregarInsumo(new Insumo("I003", "Aceite de Oliva", "Líquidos", "lt", 15.0, 3.0, 30.0, fechaLejana));

        // Lácteos y Salsas
        almacen.agregarInsumo(new Insumo("I006", "Salsa de tomate", "Salsas", "lt", 12.0, 5.0, 25.0, fechaVencida)); // Para mostrar la alerta roja
        almacen.agregarInsumo(new Insumo("I012", "Queso Mozzarella", "Lácteos", "kg", 20.0, 5.0, 40.0, fechaCasiVencida)); // Alerta amarilla
        almacen.agregarInsumo(new Insumo("I013", "Crema de Leche", "Lácteos", "lt", 10.0, 2.0, 20.0, fechaBuena));

        // Carnes y Fiambres
        almacen.agregarInsumo(new Insumo("I018", "Pepperoni", "Carnes", "kg", 8.0, 2.0, 15.0, fechaBuena));
        almacen.agregarInsumo(new Insumo("I019", "Jamón Ahumado", "Carnes", "kg", 10.0, 3.0, 20.0, fechaBuena));
        almacen.agregarInsumo(new Insumo("I020", "Tocino", "Carnes", "kg", 5.0, 1.5, 10.0, fechaBuena));
        almacen.agregarInsumo(new Insumo("I021", "Carne Molida", "Carnes", "kg", 15.0, 4.0, 30.0, fechaBuena));

        // Vegetales y Aderezos
        almacen.agregarInsumo(new Insumo("I025", "Aceitunas Negras", "Aderezos", "kg", 6.0, 2.0, 15.0, fechaLejana));
        almacen.agregarInsumo(new Insumo("I026", "Piña en Almíbar", "Aderezos", "lt", 8.0, 2.0, 20.0, fechaLejana));
        almacen.agregarInsumo(new Insumo("I027", "Champiñones Frescos", "Vegetales", "kg", 4.0, 1.0, 10.0, fechaCasiVencida));

        // 4. CREACIÓN DE RECETAS
        Receta rPepperoni = new Receta();
        rPepperoni.agregarIngrediente("I001", 0.30);
        rPepperoni.agregarIngrediente("I012", 0.25);
        rPepperoni.agregarIngrediente("I006", 0.15);
        rPepperoni.agregarIngrediente("I018", 0.20);

        Receta rHawaiana = new Receta();
        rHawaiana.agregarIngrediente("I001", 0.30);
        rHawaiana.agregarIngrediente("I012", 0.20);
        rHawaiana.agregarIngrediente("I006", 0.15);
        rHawaiana.agregarIngrediente("I019", 0.15);
        rHawaiana.agregarIngrediente("I026", 0.20);

        Receta rCarnivora = new Receta();
        rCarnivora.agregarIngrediente("I001", 0.40);
        rCarnivora.agregarIngrediente("I012", 0.30);
        rCarnivora.agregarIngrediente("I006", 0.20);
        rCarnivora.agregarIngrediente("I018", 0.10);
        rCarnivora.agregarIngrediente("I019", 0.10);
        rCarnivora.agregarIngrediente("I020", 0.15);
        rCarnivora.agregarIngrediente("I021", 0.20);

        Receta rPastaBol = new Receta();
        rPastaBol.agregarIngrediente("I001", 0.25);
        rPastaBol.agregarIngrediente("I006", 0.20);
        rPastaBol.agregarIngrediente("I021", 0.30);

        Receta rPastaAlf = new Receta();
        rPastaAlf.agregarIngrediente("I001", 0.25);
        rPastaAlf.agregarIngrediente("I013", 0.20);
        rPastaAlf.agregarIngrediente("I027", 0.15);

        Receta rCalzone = new Receta();
        rCalzone.agregarIngrediente("I001", 0.25);
        rCalzone.agregarIngrediente("I012", 0.20);
        rCalzone.agregarIngrediente("I006", 0.10);
        rCalzone.agregarIngrediente("I019", 0.15);


        // 5. REGISTRO DE PRODUCTOS AL MENÚ CON IMÁGENES

        // PIZZAS
        Pizza pPepp = new Pizza(101, "Pizza Pepperoni", 60.0, "Pizzas", "resources/images/pizza.png", "Pequeño", "Queso, salsa, pepperoni", false);
        pPepp.setReceta(rPepperoni);
        pPepp.setImagenURL("pizza.png"); // <-- Conectado a tu interfaz
        attizos.agregarProducto(pPepp);

        Pizza pHawaiana = new Pizza(102, "Pizza Hawaiana", 55.0, "Pizzas", "resources/images/pizza.png", "Grande", "Queso, salsa, hawaiana", false);
        pHawaiana.setReceta(rHawaiana);
        pHawaiana.setImagenURL("pizza.png");
        attizos.agregarProducto(pHawaiana);

        // PASTAS
        Calzone calzoneClas = new Calzone(301, "Calzone Italiano", 35.0, "Calzones","resources/images/Lazana.png" ,"Masa crujiente, queso fundido, jamón, salsa base");
        calzoneClas.setReceta(rCalzone);// Si tienes una de calzone, cambias el nombre aquí
        calzoneClas.setImagenURL("Lazana.png"); // <-- Conectado a tu interfaz
        attizos.agregarProducto(calzoneClas);

        // BEBIDAS (Sin receta, stock directo)
        // Guardamos la bebida en una variable antes de agregarla para ponerle la foto
        Bebida b1 = new Bebida(401, "Coca Cola 2L", 15.0, "Bebidas", 24, "resources/images/coca_cola_2L.png",TamanoBebida.DOS_LITROS, "Gaseosa");
        attizos.agregarProducto(b1);
        b1.setImagenURL("coca_cola_2L.png");

        System.out.println("✅ Datos cargados exitosamente.");
    }

    public static boolean validarAcceso(String username, String password) {
        Usuario user = attizos.autenticarEmpleado(username, password);
        if (user != null) {
            usuarioLogueado = user;
            System.out.println("✅ Ingresó el usuario: " + user.getNombre() + " (" + user.getCargo() + ")");
            return true;
        } else {
            System.out.println("❌ Intento fallido para el usuario: " + username);
            return false;
        }
    }
}