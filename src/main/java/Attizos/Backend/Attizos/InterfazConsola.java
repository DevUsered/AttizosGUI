/*package Attizos.Backend.Attizos;

import Attizos.Backend.Listas.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class InterfazConsola {
    private static Scanner sc = new Scanner(System.in);

    public static void iniciar(Restaurante restaurante) {
        boolean salir = false;
        while (!salir) {
            System.out.println("\n     ===============================");
            System.out.println("       ¡BIENVENIDO A PIZZERÍA ATTIZOS!");
            System.out.println("       ===============================");
            System.out.println("1. Iniciar Sesión");
            System.out.println("0. Salir del Sistema");
            System.out.print("\nSeleccione una opción: ");

            int opcion = leerEntero();
            switch (opcion) {
                case 1: login(restaurante); break;
                case 0:
                    System.out.println("Saliendo del sistema... ¡Hasta pronto!");
                    salir = true;
                    break;
                default: System.out.println("❌ Opción no válida.");
            }
        }
    }

    private static void login(Restaurante restaurante) {
        System.out.print("\nUsuario: "); String user = sc.nextLine();
        System.out.print("Contraseña: "); String pass = sc.nextLine();

        Usuario usuarioLogueado = restaurante.autenticarEmpleado(user, pass);
        if (usuarioLogueado != null) {
            System.out.println("\n✅ Bienvenid@, " + usuarioLogueado.getNombre() + " (" + usuarioLogueado.getCargo() + ")");
            if(usuarioLogueado instanceof Cocinero){
                gestionarPedidos(restaurante,"Cerrar Sesión");
            }
            menuPrincipal(usuarioLogueado,restaurante);
        } else {
            System.out.println("❌ Credenciales incorrectas.");
        }
    }

    private static void menuPrincipal(Usuario usuario, Restaurante restaurante) {
        boolean cerrarSesion = false;
        while (!cerrarSesion) {
            usuario.mostrarMenu();
            System.out.print("Seleccione: ");
            int opcion = leerEntero();

            if (opcion == 0) {
                cerrarSesion = true;
                System.out.println("Sesión cerrada.");
            } else {
                if (usuario instanceof Admin) procesarAdmin(opcion, restaurante);
                else if (usuario instanceof Cajero) procesarCajero(opcion, restaurante);
            }
        }
    }

    private static void procesarAdmin(int opcion, Restaurante restaurante) {
        switch (opcion) {
            case 1: realizarVenta(restaurante); break;
            case 2: registrarProducto(restaurante); break;
            //case 3: consultarPorCategoria(restaurante); break;
            case 3: restaurante.mostrarMenuCompleto(); break;
            case 4: editarProducto(restaurante); break;
            case 5: eliminarProductoDelMenu(restaurante); break;
            case 6: gestionarEmpleados(restaurante); break;
            case 7: gestionarReservas(restaurante); break;
            case 8: gestionarPedidos(restaurante, "Volver al menú"); break;
            case 9: gestionarAlmacen(restaurante); break;
            case 10: menuReportes(restaurante); break;
            default: System.out.println("❌ Opción no válida.");
        }
    }

    private static void procesarCajero(int opcion, Restaurante restaurante) {
        switch (opcion) {
            case 1: realizarVenta(restaurante); break;
            case 2: gestionarReservas(restaurante); break;
            case 3: gestionarPedidos(restaurante,"Volver al menu"); break;
            default: System.out.println("❌ Opción no válida.");
        }
    }

    // ==========================================
    //           LÓGICA DE VENTAS
    // ==========================================
    private static void realizarVenta(Restaurante restaurante) {
        while (true){
            System.out.println("\n=================================================");
            System.out.println("              CAJA REGISTRADORA ABIERTA          ");
            System.out.println("=================================================");
            System.out.print("Nombre del Cliente (Enter para omitir, o '0' para SALIR de caja): ");
            String nombreCli = sc.nextLine();

            if(nombreCli.trim().equals("0")){
                System.out.println("Cerrando caja y regresando al menú principal...");
                break;
            }
            if (nombreCli.trim().isEmpty()) {
                nombreCli = "Sin Nombre";
            }
            int numeroFactura = restaurante.generarNumeroFactura();
            Factura f = new Factura(numeroFactura, nombreCli);
            boolean terminada = false;
            mostrarMenuAgrupado(restaurante);

            while(!terminada){
                System.out.println("\n[1] Agregar [2] Modificar Cantidad [3] Ver Ticket [4] Finalizar Venta [5] Ver Menú [6] Ver producto [0] Cancelar");
                System.out.print("Seleccione: ");
                int op = leerEntero();
                switch (op){
                    case 1:
                    case 2:
                        System.out.print("ID Producto: ");
                        int id = leerEntero();
                        Producto p = restaurante.buscarPorId(id);
                        if(p != null){
                            if(!p.tieneReceta() && p.getStock() <= 0){
                                System.out.println("❌ Lo sentimos, el producto '" + p.getNombre() + "' se encuentra agotado.");
                                break;
                            }
                            System.out.print("Cantidad: ");
                            int cant = leerEntero();

                            if(cant > 0){
                                if(op == 1){
                                    if(cant == 0){
                                        System.out.println("⚠️ Para agregar un producto, la cantidad debe ser mayor a 0.");
                                    }else{
                                        f.agregarProducto(p,cant,restaurante.getInventario());
                                    }
                                }else{
                                    f.modificarCantidad(p,cant,restaurante.getInventario());
                                }
                            }else{
                                System.out.println("⚠️ Cantidad no válida.");
                            }
                        }else{
                            System.out.println("❌ Producto no encontrado en el menú.");
                        }
                        break;
                    case 3:
                        f.imprimirFactura();
                        break;
                    case 4:
                        if (f.getTotal() > 0) {
                            f.imprimirFactura();

                            ListaDE<DetalleFactura> productosParaCocina = new ListaDE<>();
                            NodoDE<DetalleFactura> actual = f.getDetalles().getCabeza();
                            boolean requiereCocina = false;

                            while (actual != null){
                                if(actual.getDato().getProducto().tieneReceta()){
                                    productosParaCocina.insertarAlFinal(actual.getDato());
                                    requiereCocina = true;
                                }
                                actual = actual.getSiguiente();
                            }
                            if(requiereCocina){
                                Pedido nuevoPedido = new Pedido(numeroFactura, nombreCli, productosParaCocina, f.getTotal());
                                restaurante.agregarPedido(nuevoPedido);
                                System.out.println("🔔 ¡DING! Pedido enviada a la cocina con éxito.");
                            }else{
                                System.out.println("✅ Venta registrada.");
                            }
                            restaurante.registrarVentaFinalizada(f);
                            terminada = true;
                        }else {
                            System.out.println("⚠️ La factura está vacía. Venta finalizada sin cargos.");
                            terminada = true;
                        }
                        break;
                    case 5:
                        mostrarMenuAgrupado(restaurante);
                        break;
                    case 6:
                        System.out.print("Ingrese el id del producto: ");
                        int idBuscado = leerEntero();
                        consultarDetalles(restaurante,idBuscado);
                        break;
                    case 0:
                        System.out.println("\n⚠️ ATENCIÓN: ¿Está seguro que desea cancelar esta venta y borrar la factura?");
                        System.out.print("Presione 'S' para confirmar o cualquier tecla para continuar vendiendo: ");
                        String confirmar = sc.nextLine().toUpperCase();

                        if (confirmar.equals("S")) {
                            System.out.println("Cancelando venta en curso...");
                            if (f.getTotal() > 0) {
                                NodoDE<DetalleFactura> actual = f.getDetalles().getCabeza();
                                while (actual != null) {
                                    f.eliminarProducto(actual.getDato().getProducto(), restaurante.getInventario());
                                    actual = actual.getSiguiente();
                                }
                                System.out.println("✅ Todos los ingredientes fueron devueltos al almacén correctamente.");
                            }
                            restaurante.retrocederCorrelativoFactura();
                            System.out.println("Cambiando al siguiente cliente...");
                            terminada = true; // Termina esta factura y pasa al siguiente cliente en la fila
                        } else {
                            System.out.println("❌ Cancelación abortada. Puede continuar con la venta.");
                        }
                        break;
                    default:
                        System.out.println("❌ Opción no válida. Intente de nuevo.");
                        break;
                }
            }
        }
    }
    private static void consultarDetalles(Restaurante restaurante, int id) {
        Producto prodConsulta = restaurante.buscarPorId(id);
        if(prodConsulta != null){
            prodConsulta.mostrarDetalles();
        }else {
            System.out.println("❌ Producto no encontrado en el menú.");
        }
    }
    private static void mostrarMenuAgrupado(Restaurante restaurante){
        System.out.println("\n=================================================");
        System.out.println("              CARTA DE PRODUCTOS                 ");
        System.out.println("=================================================");

        ArrayList<Producto> listaTemp = new ArrayList<>();
        NodoDE<Producto> aux = restaurante.getMenu().getCabeza();
        while (aux != null) {
            listaTemp.add(aux.getDato());
            aux = aux.getSiguiente();
        }
        if (listaTemp.isEmpty()) {
            System.out.println("          (El menú está vacío)                   ");
            return;
        }

        ArrayList<String> categorias = new ArrayList<>();
        for(Producto p : listaTemp){
            if(!categorias.contains(p.getCategoria())){
                categorias.add(p.getCategoria());
            }
        }
        categorias.sort(String::compareToIgnoreCase);
        for (String cat : categorias) {
            System.out.println("\n--- " + cat.toUpperCase() + " ---");

            // Filtramos los productos de esta categoría
            ArrayList<Producto> prodsCat = new ArrayList<>();
            for (Producto p : listaTemp) {
                if (p.getCategoria().equalsIgnoreCase(cat)) {
                    prodsCat.add(p);
                }
            }
            prodsCat.sort((p1, p2) -> p1.getNombre().compareToIgnoreCase(p2.getNombre()));
            for (Producto p : prodsCat){
                int stockDisponible = p.calcularDisponibilidad(restaurante.getInventario());
                String estadoStock = "";
                if(stockDisponible <= 0){
                    estadoStock = " ❌ [AGOTADO]";
                }else{
                    estadoStock = " | Disp: "+ stockDisponible;
                }
                System.out.printf("  [%d] %-25s - Bs. %5.2f%s\n", p.getId(), p.getNombre(), p.getPrecio(), estadoStock);
            }
        }
        System.out.println("=================================================");
    }

    // ==========================================
    //       GESTIÓN DE ALMACÉN E INSUMOS
    // ==========================================
    private static void gestionarAlmacen(Restaurante restaurante){
        Inventario inv = restaurante.getInventario();
        inv.mostrarAlertasVencimiento();
        boolean salir = false;
        while(!salir){
            System.out.println("\n--- GESTIÓN DE ALMACÉN E INSUMOS ---");
            System.out.println("1. Ver Inventario Completo");
            System.out.println("2. Ver Insumos con Stock Bajo (Compras Sugeridas)");
            System.out.println("3. Registrar Ingreso de Insumos (Llegó el proveedor)");
            System.out.println("4: Registrar NUEVO Insumo al Catálogo");
            System.out.println("5. 🗑️ Vaciar insumo vencido ");
            System.out.println("0: Volver al menu");
            System.out.print("Seleccione: ");

            int op = leerEntero();
            switch (op) {
                case 1:
                    visorInventarioInteractivo(inv);
                    break;
                case 2:
                    inv.mostrarCompraSugerida();
                    break;
                case 3:
                    registrarIngresoInsumo(inv, restaurante);
                    break;
                case 4:
                    registrarNuevoInsumo(inv);
                    break;
                case 5:
                    registrarMerma(inv);
                    break;
                case 0:
                    salir = true;
                    break;
                default:
                    System.out.println("❌ Opción no válida.");
            }
        }
    }
    private static void registrarMerma(Inventario inv) {
        System.out.println("\n--- 🗑️ VACIAR INSUMO / REGISTRAR MERMA ---");
        System.out.println("Puede vaciar un código base (Ej: I006) o un lote específico (Ej: I006-L01)");
        System.out.print("Ingrese el código exacto a desechar: ");
        String cod = sc.nextLine().trim();

        Insumo ins = inv.buscarInsumo(cod);

        if (ins != null) {
            if (ins.getStockActual() == 0) {
                System.out.println("⚠️ Este insumo ya se encuentra vacío (Stock 0).");
                return;
            }

            System.out.println("\n📦 Producto encontrado: " + ins.getNombre());
            System.out.println("   Stock actual a desechar: " + ins.getStockActual() + " " + ins.getUnidad());

            if (ins.isVencido()) {
                System.out.println("   🚨 MOTIVO: Producto VENCIDO.");
            }

            System.out.print("\n⚠️ ¿Está SEGURO que desea vaciar completamente este stock y enviarlo a la basura? (S/N): ");
            String confirmar = sc.nextLine().toUpperCase();

            if (confirmar.equals("S")) {
                // Vaciamos el stock
                ins.setStockActual(0.0);

                ins.setFechaVencimiento(null);

                System.out.println("✅ Stock vaciado correctamente. La pérdida ha sido registrada.");
            } else {
                System.out.println("❌ Operación cancelada.");
            }
        } else {
            System.out.println("❌ No se encontró ningún insumo o lote con ese código.");
        }
    }
    private static void registrarNuevoInsumo(Inventario inv) {
        System.out.println("\n--- CREAR NUEVO INSUMO ---");
        System.out.print("Código único (Ej: I005): ");
        String nuevoCod = sc.nextLine();

        if (inv.buscarInsumo(nuevoCod) != null) {
            System.out.println("❌ Ya existe un insumo registrado con el código " + nuevoCod);
            return;
        }

        System.out.print("Nombre (Ej: Queso Mozzarella): ");
        String nuevoNom = sc.nextLine();
        System.out.print("Categoría (Ej: Lácteos, Verduras, Secos): ");
        String nuevaCat = sc.nextLine();
        System.out.print("Unidad de medida (Ej: kg, lt, gr, und): ");
        String nuevaUni = sc.nextLine();

        System.out.print("Stock Inicial (puede ser 0): ");
        double stockIni = leerDouble();
        System.out.print("Stock Mínimo (Para generar alerta de compra): ");
        double stockMin = leerDouble();
        System.out.print("Stock Máximo (Capacidad del almacén): ");
        double stockMax = leerDouble();

        System.out.print("¿Es un producto perecedero (tiene vencimiento)? (S/N): ");
        String perecedero = sc.nextLine().toUpperCase();

        LocalDate fechaVenc = null;

        if (perecedero.equals("S")) {
            System.out.println("Ingrese la fecha de caducidad del lote inicial:");
            System.out.print("Día: "); int d = leerEntero();
            System.out.print("Mes: "); int m = leerEntero();
            System.out.print("Año (ej. 2026): "); int a = leerEntero();
            try {
                fechaVenc = LocalDate.of(a, m, d);
            } catch (Exception e) {
                System.out.println("⚠️ Fecha inválida. Se registrará sin fecha. Podrá actualizarla al registrar una nueva compra.");
            }
        }

        Insumo nuevoInsumo = new Insumo(nuevoCod, nuevoNom, nuevaCat, nuevaUni, stockIni, stockMin, stockMax, fechaVenc);

        // Lo guardamos en el inventario
        inv.agregarInsumo(nuevoInsumo);

        System.out.println("✅ ¡Insumo '" + nuevoNom + "' agregado exitosamente al catálogo del restaurante!");
    }
    private static void visorInventarioInteractivo(Inventario inv) {
        System.out.println("\n--- VISOR DE INVENTARIO ---");
        System.out.println("1. Mostrar TODO el inventario");
        System.out.println("2. Buscar insumos por nombre o categoría");
        System.out.print("Seleccione: ");
        int opInv = leerEntero();

        if (opInv == 1) {
            inv.mostrarInventario();
        } else if (opInv == 2) {
            System.out.print("Ingrese el nombre o categoría a buscar (Ej: Queso, Lácteos): ");
            String filtro = sc.nextLine().toLowerCase();

            boolean hayResultados = false;
            System.out.println("\n=======================================================");
            System.out.println("Resultados de la búsqueda para: '" + filtro + "'");
            System.out.println("=======================================================");

            for (Insumo i : inv.getInventarioInsumos().values()) {
                if (i.getNombre().toLowerCase().contains(filtro) ||
                        i.getCategoria().toLowerCase().contains(filtro)) {

                    String alerta = i.isVencido() ? " ❌ [VENCIDO]" :
                            (i.getStockActual() <= i.getStockMinimo() ? " ⚠️ [STOCK BAJO]" : "");

                    System.out.printf("[%s] %-20s | Cat: %-10s | Stock: %.2f %s %s\n",
                            i.getCodigo(), i.getNombre(), i.getCategoria(),
                            i.getStockActual(), i.getUnidad(), alerta);
                    hayResultados = true;
                }
            }

            if (!hayResultados) {
                System.out.println("No se encontraron insumos que coincidan con la búsqueda.");
            }
            System.out.println("=======================================================");
        } else {
            System.out.println("❌ Opción no válida.");
        }
    }
    private static void registrarIngresoInsumo(Inventario inv, Restaurante restaurante) {
        System.out.println("\n--- REGISTRAR INGRESO DE INSUMOS ---");
        System.out.print("Ingrese el código exacto (Ej: I001) o el nombre para buscar: ");
        String entrada = sc.nextLine().trim();

        // 1. Intentamos buscar primero por código exacto
        Insumo insBase = inv.buscarInsumo(entrada);

        if (insBase == null) {
            boolean encontrados = false;
            System.out.println("\n🔍 Buscando coincidencias por nombre...");

            for (Insumo ins : inv.getInventarioInsumos().values()) {
                if (ins.getNombre().toLowerCase().contains(entrada.toLowerCase())) {
                    // Mostramos las coincidencias con su código
                    System.out.println(" 📌 [" + ins.getCodigo() + "] - " + ins.getNombre() + " (Cat: " + ins.getCategoria() + ")");
                    encontrados = true;
                }
            }

            if (encontrados) {
                System.out.print("\nIngrese el CÓDIGO EXACTO del insumo que desea ingresar: ");
                String codExacto = sc.nextLine().trim();
                insBase = inv.buscarInsumo(codExacto); // Volvemos a buscar con el código que eligió
            } else {
                System.out.println("❌ No se encontró ningún insumo con ese código o nombre.");
                return;
            }
        }

        if (insBase == null) {
            System.out.println("❌ Código inválido. Operación cancelada.");
            return;
        }

        System.out.println("\n📦 Producto seleccionado: " + insBase.getNombre());
        System.out.print("Cantidad que ingresa en [" + insBase.getUnidad() + "]: ");
        double cant = leerDouble();

        while(cant <= 0){
            System.out.println("⚠️ La cantidad debe ser mayor a 0. Intente de nuevo: ");
            cant = leerDouble();
        }
        double costoTotal = calcularCostoCompra(cant);

        LocalDate nuevaFecha = null;
        while(nuevaFecha == null) {
            System.out.println("Ingrese la fecha de caducidad del nuevo lote:");
            System.out.print("Día (ej. 15): "); int dia = leerEntero();
            System.out.print("Mes (ej. 8): "); int mes = leerEntero();
            System.out.print("Año (ej. 2026): "); int anio = leerEntero();

            try{
                nuevaFecha = LocalDate.of(anio,mes,dia);
                if(nuevaFecha.isBefore(LocalDate.now())){
                    System.out.println("❌ ERROR: El producto ya está vencido. No se puede ingresar mercadería caducada al almacén.\n");
                    nuevaFecha = null;
                }
            } catch (Exception e) {
                System.out.println("⚠️ La fecha ingresada no existe en el calendario. Intente nuevamente.\n");
            }
        }

        // Validar si tienen la misma fecha de vencimiento
        boolean mismaFecha = false;
        if (insBase.getFechaVencimiento() != null) {
            mismaFecha = insBase.getFechaVencimiento().equals(nuevaFecha);
        }

        // Lógica de Decisión (Stock 0 o Misma Fecha)
        if (insBase.getStockActual() == 0 || mismaFecha) {
            insBase.setStockActual(insBase.getStockActual() + cant);
            insBase.setFechaVencimiento(nuevaFecha);

            System.out.println("✅ Ingreso sumado al stock principal.");
            System.out.println("Nuevo stock total: " + insBase.getStockActual() + " " + insBase.getUnidad());

        } else {
            // Crear un NUEVO LOTE
            int contadorLote = 1;
            String nuevoCodLote = insBase.getCodigo() + "-L0" + contadorLote;

            while (inv.buscarInsumo(nuevoCodLote) != null) {
                contadorLote++;
                nuevoCodLote = insBase.getCodigo() + (contadorLote < 10 ? "-L0" : "-L") + contadorLote;
            }

            Insumo nuevoLote = new Insumo(
                    nuevoCodLote,
                    insBase.getNombre() + " (Lote " + contadorLote + ")",
                    insBase.getCategoria(),
                    insBase.getUnidad(),
                    cant,
                    insBase.getStockMinimo(),
                    insBase.getStockMaximo(),
                    nuevaFecha
            );

            inv.agregarInsumo(nuevoLote);

            System.out.println("✅ Ingreso registrado como un NUEVO LOTE por diferencia de fechas.");
            restaurante.registerExpense(new Egreso("Compra Insumo: " + insBase.getNombre() + " (" + cant + " " + insBase.getUnidad() + ")", costoTotal));
            System.out.println("💸 Gasto de Bs. " + costoTotal + " registrado en los egresos.");
        }
    }

    // ==========================================
    //       GESTIÓN DE PEDIDOS Y RESERVAS
    // ==========================================
    private static void gestionarPedidos(Restaurante restaurante, String textoSalida) {
        boolean enCocina = true;
        while(enCocina){
            System.out.println("\n==========================================================");
            System.out.println("       👨‍🍳 MONITOR DE COCINA (EN VIVO) - ATTIZOS 👨‍🍳   ");
            System.out.println("==========================================================");

            if (restaurante.getPedidos().getCabeza() == null) {
                System.out.println("\n        ✅ Todo tranquilo. ¡No hay comandas pendientes!\n");
            }else{
                System.out.println("--- COMANDAS EN ESPERA ---\n");
                restaurante.getPedidos().mostrarLista();
            }
            System.out.println("==========================================================");
            System.out.println("ACCIONES DE COCINA:");
            System.out.println("[1] 🔔 ¡DING! Entregar PRÓXIMO pedido (El más antiguo)");
            System.out.println("[2] ❌ Cancelar pedido por error (Devuelve ingredientes)");
            System.out.println("[9] 🔄 Actualizar Monitor");

            System.out.println("[0] 🚪 " + textoSalida);
            System.out.print("Comando: ");

            int op = leerEntero();
            switch (op){
                case 1:
                    Pedido pedidoAtendido = restaurante.atenderSiguientePedido();
                    if (pedidoAtendido != null) {
                        pedidoAtendido.setEstado("Entregado");
                        System.out.println("\n✅ Pedido Nro " + pedidoAtendido.getIdPedido() + " marcado como ENTREGADO.");
                        System.out.println("🧹 La comanda ha sido retirada de la pantalla.");
                        // Pequeña pausa visual antes de refrescar el monitor
                        try { Thread.sleep(1500); } catch (Exception e) {}
                    } else {
                        System.out.println("\n⚠️ No hay pedidos para atender.");
                    }
                    break;
                case 2:
                   procesarCancelacionIntegral(restaurante);
                   break;
                case 9:
                    System.out.println("Refrescando comandas...");
                    break;
                case 0:
                    enCocina = false;
                    System.out.println("Saliendo del monitor de cocina...");
                    break;
                default:
                    System.out.println("❌ Comando no válido.");
            }
        }
    }
    private static void procesarCancelacionIntegral(Restaurante restaurante) {
        System.out.print("\nIngrese ID del pedido a cancelar: ");
        int idCancelar = leerEntero();

        if (restaurante.cancelarPedido(idCancelar)) {
            System.out.println("✅ Pedido Nro " + idCancelar + " cancelado en cocina. Ingredientes devueltos.");

            if (restaurante.anularFacturaFinanciera(idCancelar)) {
                System.out.println("✅ Factura Nro " + idCancelar + " anulada con éxito.");
                //System.out.println("   (El dinero no se sumará al reporte de caja de hoy).");
            } else {
                System.out.println("⚠️ Alerta: Se canceló la comida, pero no se encontró la factura para anular el cobro.");
            }

            try { Thread.sleep(2500); } catch (Exception e) {}
        } else {
            System.out.println("❌ No se encontró el pedido en la cola de la cocina.");
        }
    }

    private static void gestionarReservas(Restaurante restaurante) {
        System.out.println("\n--- GESTIÓN DE RESERVAS ---");
        System.out.println("1. Ver reservas | 2. Nueva reserva | 3. Buscar y cambiar estado");
        System.out.print("Seleccione: ");
        int op = leerEntero();

        switch (op) {
            case 1:
                if (restaurante.getReservas().getCabeza() != null) {
                    restaurante.getReservas().mostrarLista();
                } else {
                    System.out.println("⚠️ No hay reservas registradas.");
                }
                break;
            case 2:
                System.out.println("\n--- NUEVA RESERVA ---");
                System.out.print("Nombre Cliente: ");
                String nom = sc.nextLine();
                System.out.print("Teléfono de contacto: ");
                String tel = sc.nextLine();
                System.out.print("Cantidad de Personas: ");
                int cant = leerEntero();
                System.out.print("Día: ");
                int dia = leerEntero();
                System.out.print("Mes (1-12): ");
                int mes = leerEntero();
                System.out.print("Hora (0-23): ");
                int hora = leerEntero();
                System.out.print("Minuto (0-59): ");
                int min = leerEntero();
                System.out.print("Observaciones (Enter para omitir): ");
                String obs = sc.nextLine();

                if (obs.trim().isEmpty()) obs = "Ninguna";

                try {
                    int anioActual = java.time.LocalDateTime.now().getYear();
                    //int mesActual = java.time.LocalDateTime.now().getMonthValue();
                    java.time.LocalDateTime fecha = java.time.LocalDateTime.of(anioActual, mes, dia, hora, min);
                    if (fecha.isBefore(java.time.LocalDateTime.now())) {
                        System.out.println("❌ ERROR: ¡No puedes hacer una reserva en el pasado!");
                    }else {
                        String idReserva = restaurante.generarIdReserva(fecha);
                        restaurante.agregarReserva(new Reserva(idReserva, nom, tel, cant, fecha, obs));
                        System.out.println("✅ Reserva registrada exitosamente. (Estado: Pendiente | Mesa: Sin asignar)");
                        System.out.println("📌 CÓDIGO DE RESERVA: " + idReserva);
                    }
                } catch (Exception e) {
                    System.out.println("❌ Error: Fecha u hora inválida. Intente de nuevo.");
                }
                break;
            case 3:
                System.out.println("\n--- BUSCAR RESERVA ---");
                System.out.print("Ingrese el nombre del cliente o el ID de reserva: ");
                String busqueda = sc.nextLine().toLowerCase();

                boolean encontrado = false;
                Reserva reservaSeleccionada = null;
                NodoDE<Reserva> actual = restaurante.getReservas().getCabeza();

                while (actual != null) {
                    Reserva res = actual.getDato();
                    if (res.getNombreCliente().toLowerCase().contains(busqueda) ||
                            res.getId().toLowerCase().contains(busqueda)) {

                        System.out.println("\nReserva encontrada:");
                        System.out.println(res.toString());
                        reservaSeleccionada = res;
                        encontrado = true;
                        break;
                    }
                    actual = actual.getSiguiente();
                }

                if (!encontrado) {
                    System.out.println("❌ No se encontraron reservas con ese nombre o ID.");
                    break;
                }

                // Si la encontramos, procedemos a interactuar con ella
                System.out.println("\n¿Qué desea hacer con esta reserva?");
                System.out.println("1. Llegó el cliente (Confirmar y asignar mesa)");
                System.out.println("2. Cancelar reserva (No vino)");
                System.out.println("3. Liberar (El cliente ya comió y se fue)");
                System.out.println("0. Volver atrás");
                System.out.print("Opción: ");

                int opEstado = leerEntero();

                switch (opEstado) {
                    case 1:
                        reservaSeleccionada.setEstado("Confirmada");
                        System.out.print("¿En qué número de mesa los va a sentar?: ");
                        int numMesa = leerEntero();
                        reservaSeleccionada.setNumeroMesa(numMesa);
                        System.out.println("✅ ¡Excelente! Cliente acomodado en la mesa " + numMesa + ".");
                        break;
                    case 2:
                        reservaSeleccionada.setEstado("Cancelada");
                        reservaSeleccionada.setNumeroMesa(0);
                        System.out.println("✅ Reserva marcada como cancelada.");
                        break;
                    case 3:
                        reservaSeleccionada.setEstado("Liberada");
                        reservaSeleccionada.setNumeroMesa(0);
                        System.out.println("✅ Mesa liberada. Lista para nuevos clientes.");
                        break;
                    case 0:
                        System.out.println("Operación cancelada.");
                        break;
                    default:
                        System.out.println("❌ Opción inválida.");
                }
                break;
            default:
                System.out.println("❌ Opción inválida.");
        }
    }

    // ==========================================
    //           GESTIÓN DE PRODUCTOS Y MENÚ
    // ==========================================
    private static void registrarProducto(Restaurante restaurante) {
        System.out.println("\n--- REGISTRAR PRODUCTO AL MENÚ ---");
        int idGenerado = 1;
        NodoDE<Producto> auxId = restaurante.getMenu().getCabeza();
        while(auxId != null){
            if (auxId.getDato().getId() >= idGenerado) {
                idGenerado = auxId.getDato().getId() + 1; // Buscamos el ID más alto y le sumamos 1
            }
            auxId = auxId.getSiguiente();
        }
        System.out.println("ID asignado automáticamente: " + idGenerado);
       //System.out.print("ID: "); int id = leerEntero();
        System.out.print("Nombre: "); String nom = sc.nextLine();
        ArrayList<String> categorias = new ArrayList<>();
        NodoDE<Producto> actualMenu = restaurante.getMenu().getCabeza();
        while(actualMenu != null){
            String catProd = actualMenu.getDato().getCategoria();
            boolean existe = false;
            for(String c : categorias){
                if(c.equalsIgnoreCase(catProd)){
                    existe = true;
                    break;
                }
            }
            if(!existe) categorias.add(catProd);
            actualMenu = actualMenu.getSiguiente();
        }
        System.out.println("\nSeleccione la categoría del producto:");
        for (int i = 0; i < categorias.size(); i++) {
            System.out.println("[" + (i + 1) + "] " + categorias.get(i));
        }
        System.out.println("[0] ✨ Crear NUEVA Categoría");
        System.out.print("Opción: ");
        int opCat = leerEntero();
        String cat = "";
        if(opCat == 0){
            System.out.print("Escriba el nombre de la nueva categoría (Ej: Postres): ");
            cat = sc.nextLine();
        }else if(opCat > 0 && opCat <= categorias.size()){
            cat = categorias.get(opCat - 1);
        }else{
            System.out.println("⚠️ Opción inválida. Se asignará a la categoría 'General'.");
            cat = "General";
        }
        System.out.print("Precio (Bs): "); double pre = leerDouble();
        if(pre > 0) {

            System.out.print("¿Es un producto que se prepara con Receta? (S/N): ");
            String resp = sc.nextLine().toUpperCase();

            Producto nuevoProducto = new Producto(idGenerado, nom, pre, cat, 0); // Stock inicial 0

            if (resp.equals("S")) {
                System.out.println("\n--- ARMADO DE RECETA ---");
                Receta receta = new Receta();
                boolean agregando = true;

                while (agregando) {
                    System.out.print("Código del insumo (ej. I001) o 'FIN' para terminar: ");
                    String cod = sc.nextLine();
                    if (cod.equalsIgnoreCase("FIN")) break;

                    Insumo ins = restaurante.getInventario().buscarInsumo(cod);
                    if (ins != null) {
                        System.out.print("Cantidad necesaria por porción en [" + ins.getUnidad() + "]: ");
                        double cantReq = leerDouble();
                        sc.nextLine();
                        if(cantReq <= 0){
                            System.out.println("⚠️ La cantidad debe ser mayor a 0. Intente nuevamente.");
                            continue;
                        }
                        receta.agregarIngrediente(cod, cantReq);
                        System.out.println("✅ Ingrediente añadido.");
                    } else {
                        System.out.println("❌ Insumo no encontrado en el almacén.");
                    }
                }
                nuevoProducto.setReceta(receta);
                System.out.println("✅ Receta guardada exitosamente.");
            } else {
                System.out.print("Stock inicial (unidades): ");
                double st = leerDouble();
                sc.nextLine();
                nuevoProducto.setStock(st);
            }

            restaurante.agregarProducto(nuevoProducto);
            System.out.println("✅ Producto registrado en el menú.");
        } else {
            System.out.println("❌ El precio debe ser mayor a 0. Producto no registrado.");
        }
    }

    /*private static void consultarPorCategoria(Restaurante restaurante) {
        System.out.print("\nIngrese categoría a buscar: ");
        String cat = sc.nextLine();
        ListaDE<Producto> lista = restaurante.buscarPorCategoria(cat);
        if (lista.getCabeza() != null) lista.mostrarLista();
        else System.out.println("❌ No hay productos en esa categoría.");
    }

    private static void editarProducto(Restaurante restaurante) {
        System.out.print("\nID del producto a editar: ");
        int id = leerEntero();
        Producto p = restaurante.buscarPorId(id);

        if (p != null) {
            System.out.println("\n📦 Editando: " + p.getNombre());
            System.out.println("1. Cambiar Precio de Venta (Actual: Bs. " + p.getPrecio() + ")");

            // Solo mostramos opciones de stock si es un producto cerrado (bebidas)
            if (!p.tieneReceta()) {
                System.out.println("2. ➕ Agregar stock (Llegó mercadería)");
                System.out.println("3. ➖ Registrar Merma/Ajuste (Botella rota, pérdida, etc.)");
            }

            System.out.print("Seleccione qué desea modificar: ");
            int opEditar = leerEntero();

            if (opEditar == 1) {
                System.out.print("Nuevo Precio: Bs. ");
                p.setPrecio(leerDouble());
                System.out.println("✅ Precio actualizado exitosamente.");

            } else if (!p.tieneReceta() && opEditar == 2) {
                System.out.println("Stock actual: " + p.getStock() + " und");
                System.out.print("Cantidad a SUMAR: ");
                double sumar = leerDouble();
                if (sumar > 0) {
                    double costoTotal = calcularCostoCompra(sumar);
                    restaurante.registerExpense(new Egreso("Compra Mercadería: " + p.getNombre() + " (" + sumar + " und)", costoTotal));
                    System.out.println("💸 Gasto de Bs. " + costoTotal + " registrado en los egresos.");
                    p.setStock(p.getStock() + sumar);
                    System.out.println("✅ Stock aumentado. Nuevo total: " + p.getStock() + " und");
                } else {
                    System.out.println("⚠️ La cantidad debe ser mayor a 0.");
                }

            } else if (!p.tieneReceta() && opEditar == 3) {
                System.out.println("Stock actual: " + p.getStock() + " und");
                System.out.print("Cantidad a RESTAR: ");
                double restar = leerDouble();

                if (restar > 0 && restar <= p.getStock()) {
                    System.out.print("Escriba el motivo de la baja (Ej: Producto dañado, consumo interno): ");
                    String motivo = sc.nextLine();

                    p.setStock(p.getStock() - restar);
                    System.out.println("✅ Ajuste realizado por: '" + motivo + "'. Nuevo total: " + p.getStock() + " und");
                } else {
                    System.out.println("⚠️ Cantidad inválida o supera el stock actual.");
                }
            } else {
                System.out.println("❌ Opción no válida.");
            }

        } else {
            System.out.println("❌ Producto no encontrado en el menú.");
        }
    }

    private static void eliminarProductoDelMenu(Restaurante restaurante) {
        System.out.print("\nID del producto a eliminar: ");
        if (restaurante.eliminarProducto(leerEntero())) System.out.println("✅ Eliminado.");
        else System.out.println("❌ No se encontró el ID.");
    }

    private static void menuReportes(Restaurante restaurante) {
        System.out.println("\n--- REPORTES FINANCIEROS ---");
        System.out.println("1. Reporte de Caja Diario (Hoy)");
        System.out.println("2. Reporte General Mensual (Ventas totales)");
        System.out.println("3. Ver Detalle de Gastos (Por Mes)"); // NUEVA OPCIÓN
        System.out.print("Seleccione: ");
        int op = leerEntero();

        if (op == 1) {
            Reporte.generarReporteDiario(restaurante);
        } else if (op == 2) {
            System.out.print("Ingrese el número de mes (1-12): ");
            int mes = leerEntero();
            System.out.print("Ingrese el año (Ej. " + LocalDate.now().getYear() + "): ");
            int anio = leerEntero();
            Reporte.generarReporteMensual(restaurante, mes, anio);
        } else if (op == 3) {
            System.out.print("Ingrese el número de mes (1-12): ");
            int mes = leerEntero();
            System.out.print("Ingrese el año (Ej. " + LocalDate.now().getYear() + "): ");
            int anio = leerEntero();
            Reporte.mostrarDetalleEgresosMensual(restaurante, mes, anio); // LLAMAMOS AL NUEVO MÉTODO
        } else {
            System.out.println("❌ Opción inválida.");
        }
    }

    // ==========================================
    //           MÉTODOS AUXILIARES
    // ==========================================
    private static int leerEntero() {
        try {
            int n = sc.nextInt();
            sc.nextLine();
            return n;
        } catch (Exception e) {
            sc.nextLine();
            System.out.println("⚠️ Ingrese un número válido.");
            return -1;
        }
    }
    private static double leerDouble() {
        while (true) {
            try {
                double n = sc.nextDouble();
                sc.nextLine();
                return n;
            } catch (Exception e) {
                sc.nextLine();
                System.out.print("⚠️ Entrada inválida. Por favor, ingrese un número decimal válido (ej. 150.50): ");
            }
        }
    }
    private static void gestionarEmpleados(Restaurante restaurante){
        boolean salir = false;
        while(!salir){
            System.out.println("\n--- GESTIÓN DE EMPLEADOS ---");
            System.out.println("[1] Ver planilla de empleados");
            System.out.println("[2] Cambiar cargo de un empleado");
            System.out.println("[3] Agregar nuevo empleado");
            System.out.println("[4] Eliminar Empleado (Despedir)");
            System.out.println("[5] Modificar sueldo");
            System.out.println("[0] Volver al menú principal");
            System.out.print("Seleccione: ");

            int opcion = leerEntero();
            switch (opcion){
                case 1:
                    restaurante.mostrarPlanillaEmpleados();
                    break;
                case 2:
                    System.out.print("Ingrese el id del empleado: ");
                    int idCargo = leerEntero();
                    cambiarCargoEmpleado(restaurante,idCargo);
                    break;
                case 3:
                    System.out.println("\n--- NUEVO EMPLEADO ---");
                    System.out.print("ID del empleado: "); int idEmp = leerEntero();
                    System.out.print("Nombre completo: "); String nom = sc.nextLine();
                    System.out.print("Sueldo (Bs): "); double sueldo = leerDouble();
                    if(sueldo <= 0){
                        System.out.println("El sueldo debe ser mayor a 0");
                    }else {
                        // 1. Pedimos el cargo PRIMERO
                        String cargo = seleccionarCargo();

                        Empleado nuevoEmp = null;


                        if (cargo.equals("Administrador") || cargo.equals("Cajero")) {
                            System.out.print("Nombre de usuario (login): ");
                            String usr = sc.nextLine();
                            System.out.print("Contraseña: ");
                            String pwd = sc.nextLine();

                            if (cargo.equals("Administrador")) {
                                nuevoEmp = new Admin(idEmp, nom, usr, pwd, sueldo);
                            } else {
                                nuevoEmp = new Cajero(idEmp, nom, sueldo, usr, pwd);
                            }
                        } else {
                            nuevoEmp = new Empleado(idEmp, nom, cargo, sueldo);
                        }

                        restaurante.agregarEmpleado(nuevoEmp);
                        System.out.println("✅ Empleado " + nom + " (" + cargo + ") agregado exitosamente.");
                    }
                    break;
                case 4:
                    System.out.print("Ingrese el ID del empleado a despedir: ");
                    int idDel = leerEntero();
                    if (restaurante.eliminarEmpleado(idDel)) {
                        System.out.println("✅ Empleado despedido y eliminado del sistema.");
                    } else {
                        System.out.println("❌ No se encontró un empleado con ese ID.");
                    }
                    break;
                case 5:
                    System.out.print("Ingrese el ID del empleado: ");
                    int idSueldo = leerEntero();
                    Empleado empSueldo = restaurante.buscarEmpleado(idSueldo);

                    if (empSueldo != null) {
                        System.out.println("Sueldo actual: " + empSueldo.getSueldo() + " Bs");
                        System.out.print("Nuevo sueldo (Bs): ");
                        double nuevoSueldo = leerDouble();

                        empSueldo.setSueldo(nuevoSueldo);
                        System.out.println("✅ Sueldo actualizado correctamente.");
                    } else {
                        System.out.println("❌ Empleado no encontrado.");
                    }
                    break;
                case 0:
                    salir = true;
                default:
                    System.out.println("⚠ Ingrese una opción válida.");
            }
        }
    }
    private static void cambiarCargoEmpleado(Restaurante restaurante, int idEmpleado) {
        Empleado empViejo = restaurante.buscarEmpleado(idEmpleado);

        if (empViejo == null) {
            System.out.println("❌ Empleado no encontrado.");
            return;
        }
        System.out.println("\nEmpleado: " + empViejo.getNombre());
        System.out.println("Cargo actual: " + empViejo.getCargo());
        String nuevoCargo = seleccionarCargo();

        if (empViejo.getCargo().equalsIgnoreCase(nuevoCargo)) {
            System.out.println("⚠️ El empleado ya tiene ese cargo.");
            return;
        }

        System.out.print("Ingrese el nuevo sueldo para el cargo de " + nuevoCargo + " (Bs): ");
        double nuevoSueldo = leerDouble();

        Empleado empNuevo = null;

        // Detectamos si el empleado actual ya tiene cuenta y si el nuevo cargo la necesita
        boolean viejoEsUsuario = empViejo instanceof Usuario;
        boolean nuevoEsUsuario = nuevoCargo.equals("Administrador") || nuevoCargo.equals("Cajero");

        String usr = "";
        String pwd = "";

        if (nuevoEsUsuario) {
            if (viejoEsUsuario) {
                // Mantenemos credenciales (solo copiamos id y nombre del viejo)
                Usuario userViejo = (Usuario) empViejo;
                usr = userViejo.getUsername();
                pwd = userViejo.getPassword();
                System.out.println("ℹ️ Se mantendrán el usuario y contraseña actuales.");
            } else {
                // Pide datos nuevos (solo copiamos id y nombre del viejo)
                System.out.println("El nuevo cargo requiere acceso al sistema.");
                System.out.print("Ingrese nuevo nombre de usuario (login): ");
                usr = sc.nextLine();
                System.out.print("Ingrese nueva contraseña: ");
                pwd = sc.nextLine();
            }

            if (nuevoCargo.equals("Administrador")) {
                empNuevo = new Admin(empViejo.getId(), empViejo.getNombre(), usr, pwd, nuevoSueldo);
            } else {
                empNuevo = new Cajero(empViejo.getId(), empViejo.getNombre(), nuevoSueldo,usr, pwd);
            }

        } else {
            // Pasa a un cargo sin sistema
            if (viejoEsUsuario) {
                System.out.println("⚠️ Nota: El empleado perderá su acceso al sistema.");
            }
            empNuevo = new Empleado(empViejo.getId(), empViejo.getNombre(), nuevoCargo, nuevoSueldo);
        }

        // Actualizamos en la base de datos (lista) del Restaurante
        restaurante.eliminarEmpleado(empViejo.getId());
        restaurante.agregarEmpleado(empNuevo);

        System.out.println("✅ Cargo actualizado exitosamente a " + nuevoCargo);
    }
    private static String seleccionarCargo() {
        String cargoSeleccionado = "";
        while (cargoSeleccionado.isEmpty()) {
            System.out.println("\nSeleccione el cargo del empleado:");
            System.out.println("1. Administrador");
            System.out.println("2. Cajero");
            System.out.println("3. Cocina");
            System.out.println("4. Mesero");
            System.out.println("5. Limpieza");
            System.out.print("Opción: ");

            int op = leerEntero();
            switch (op) {
                case 1: cargoSeleccionado = "Administrador"; break;
                case 2: cargoSeleccionado = "Cajero"; break;
                case 3: cargoSeleccionado = "Cocina"; break;
                case 4: cargoSeleccionado = "Mesero"; break;
                case 5: cargoSeleccionado = "Limpieza"; break;
                default: System.out.println("❌ Cargo no válido. Intente de nuevo.");
            }
        }
        return cargoSeleccionado;
    }
    private static double calcularCostoCompra(double cantidad) {
        int op = 0;
        while (op != 1 && op != 2) {
            System.out.println("\n💰 ¿Cómo desea registrar el costo de esta compra?");
            System.out.println("[1] Precio TOTAL (Ej: Pagué 100 Bs por toda la caja/lote)");
            System.out.println("[2] Precio UNITARIO (Ej: Cada unidad me costó 5 Bs)");
            System.out.print("Seleccione: ");

            op = leerEntero();

            if (op == 1) {
                System.out.print("Ingrese el precio TOTAL pagado (Bs): ");
                double pTotal = leerDouble();
                while(pTotal < 0){
                    System.out.print("⚠️ El precio no puede ser negativo. Intente de nuevo: Bs. ");
                    pTotal = leerDouble();
                }
                return pTotal;

            } else if (op == 2) {
                System.out.print("Ingrese el precio UNITARIO (Bs): ");
                double pUnitario = leerDouble();
                while(pUnitario < 0){
                    System.out.print("⚠️ El precio no puede ser negativo. Intente de nuevo: Bs. ");
                    pUnitario = leerDouble();
                }
                return pUnitario * cantidad;

            } else {
                System.out.println("❌ Opción inválida. Por favor, ingrese 1 o 2.");
            }
        }
        return 0;
    }
}
*/
