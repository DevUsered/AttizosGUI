package Attizos.Backend.Attizos;
import Attizos.Backend.Listas.NodoDE;

public class App {
    public static Restaurante attizos;
    public static Usuario usuarioLogueado; // <--- ¡Esta variable es VITAL para el Dashboard!

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

        System.out.println("✅ Datos cargados exitosamente.");
    }

    public static boolean validarAcceso(String username, String password) {
        Usuario user = attizos.autenticarEmpleado(username, password);
        if (user != null) {
            usuarioLogueado = user; // <--- ¡Guardamos quién entró aquí!
            System.out.println("✅ Ingresó el usuario: " + user.getNombre() + " (" + user.getCargo() + ")");
            return true;
        } else {
            System.out.println("❌ Intento fallido para el usuario: " + username);
            return false;
        }
    }
}