package co.edu.uptc.project2.ui.view;

import java.util.List;

import co.edu.uptc.project2.domain.InvoiceDetail;

public class InvoiceDetailView {

    // Mostrar menú de detalle de factura
    public void showMenu() {
        System.out.println("\n========== GESTIÓN DE DETALLES DE FACTURA ==========");
        System.out.println("1. Crear detalle");
        System.out.println("2. Consultar todos los detalles");
        System.out.println("3. Consultar detalle por nombre de producto");
        System.out.println("4. Actualizar detalle");
        System.out.println("5. Eliminar detalle");
        System.out.println("0. Volver al menú principal");
        System.out.print("Seleccione una opción: ");
    }

    // Mostrar un detalle
    public void showDetail(InvoiceDetail detail) {
        if (detail == null) {
            System.out.println("Detalle no encontrado.");
            return;
        }
        System.out.println("\n--- Detalle de Factura ---");
        System.out.println("Producto:    " + detail.getProductName());
        System.out.println("Categoría:   " + detail.getCategory());
        System.out.println("Cantidad:    " + detail.getQuantity());
        System.out.println("Precio unit: $" + detail.getUnitPrice());
        System.out.println("Subtotal:    $" + detail.getSubtotal());
        System.out.println("-------------------------");
    }

    // Mostrar lista de detalles
    public void showAllDetails(List<InvoiceDetail> details) {
        if (details == null || details.isEmpty()) {
            System.out.println("No hay detalles registrados.");
            return;
        }
        System.out.println("\n========== LISTA DE DETALLES ==========");
        for (InvoiceDetail d : details) {
            showDetail(d);
        }
    }

    // Mensajes
    public void showMessage(String message) {
        System.out.println(message);
    }
}