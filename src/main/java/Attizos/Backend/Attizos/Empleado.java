package Attizos.Backend.Attizos;

public class Empleado {
    private int id;
    private String nombre;
    private String cargo;
    private double sueldo;

    // Constructor
    public Empleado(int id, String nombre, String cargo, double sueldo) {
        this.id = id;
        this.nombre = nombre;
        this.cargo = cargo;
        this.sueldo = sueldo;
    }

    // --- GETTERS ---
    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCargo() {
        return cargo;
    }

    public double getSueldo() {
        return sueldo;
    }
    public void setId(int id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public void setSueldo(double sueldo) {
        this.sueldo = sueldo;
    }
    @Override
    public String toString() {
        // String.format ayuda a que las columnas se vean alineadas en la consola
        return String.format("ID: [%d] | Nombre: %-15s | Cargo: %-15s | Sueldo: Bs. %.2f",
                id, nombre, cargo, sueldo);
    }
}
