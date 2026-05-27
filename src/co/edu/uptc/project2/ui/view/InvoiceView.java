package co.edu.uptc.project2.ui.view;

import java.util.List;

import co.edu.uptc.project2.domain.Invoice;
import co.edu.uptc.project2.domain.InvoiceDetail;

public class InvoiceView {

    // Mostrar menú de factura
    public void showMenu() {
        System.out.println("\n========== GESTIÓN DE FACTURAS ==========");
        System.out.println("1. Crear factura");
        System.out.println("2. Consultar todas las facturas");
        System.out.println("3. Consultar factura por ID");
        System.out.println("4. Actualizar factura");
        System.out.println("5. Eliminar factura");
        System.out.println("0. Volver al menú principal");
        System.out.print("Seleccione una opción: ");
    }

    // Mostrar una factura
    public void showInvoice(Invoice invoice) {
        if (invoice == null) {
            System.out.println("Factura no encontrada.");
            return;
        }
        System.out.println("\n--- Factura ---");
        System.out.println("ID:             " + invoice.getId());
        System.out.println("Código:         " + invoice.getCode());
        System.out.println("Fecha:          " + invoice.getDate());
        System.out.println("Estado:         " + invoice.getInvoiceStatus());
        System.out.println("Método de pago: " + invoice.getPaymentMethod());
        System.out.println("Pagada:         " + (invoice.isPaid() ? "Sí" : "No"));
        System.out.println("Total:          $" + invoice.getTotal());

        if (invoice.getCustomer() != null) {
            System.out.println("Cliente:        " + invoice.getCustomer().getName()
                    + " " + invoice.getCustomer().getLastName()
                    + " (" + invoice.getCustomer().getDocumentNumber() + ")");
        }

        System.out.println("Detalles:");
        if (invoice.getDetails() == null || invoice.getDetails().isEmpty()) {
            System.out.println("  (sin detalles)");
        } else {
            for (InvoiceDetail d : invoice.getDetails()) {
                System.out.println("  - " + d.getProductName()
                        + " | Cant: " + d.getQuantity()
                        + " | P.Unit: $" + d.getUnitPrice()
                        + " | Subtotal: $" + d.getSubtotal());
            }
        }
        System.out.println("-------------------------");
    }

    // Mostrar lista de facturas
    public void showAllInvoices(List<Invoice> invoices) {
        if (invoices == null || invoices.isEmpty()) {
            System.out.println("No hay facturas registradas.");
            return;
        }
        System.out.println("\n========== LISTA DE FACTURAS ==========");
        for (Invoice inv : invoices) {
            showInvoice(inv);
        }
    }

    // Mensajes
    public void showMessage(String message) {
        System.out.println(message);
    }
}