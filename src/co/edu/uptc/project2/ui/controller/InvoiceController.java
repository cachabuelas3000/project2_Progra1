package co.edu.uptc.project2.ui.controller;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import co.edu.uptc.project2.domain.Customer;
import co.edu.uptc.project2.domain.Invoice;
import co.edu.uptc.project2.domain.InvoiceDetail;
import co.edu.uptc.project2.enums.InvoiceStatus;
import co.edu.uptc.project2.enums.PaymentMethod;
import co.edu.uptc.project2.enums.ProductCategory;
import co.edu.uptc.project2.service.CustomerService;
import co.edu.uptc.project2.service.InvoiceService;
import co.edu.uptc.project2.ui.view.InvoiceView;

public class InvoiceController {

    private InvoiceService service;
    private CustomerService customerService;
    private InvoiceView view;
    private Scanner scanner;

    // Contador simple para generar IDs
    private static int idCounter = 1;

    public InvoiceController(Scanner scanner, CustomerService customerService) {
        this.service = new InvoiceService();
        this.customerService = customerService;
        this.view = new InvoiceView();
        this.scanner = scanner;
    }

    // Menú principal de facturas
    public void showMenu() {
        int option = -1;
        do {
            view.showMenu();
            try {
                option = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                view.showMessage("Opción inválida.");
                continue;
            }

            switch (option) {
                case 1 -> createInvoice();
                case 2 -> listAllInvoices();
                case 3 -> findInvoiceById();
                case 4 -> updateInvoice();
                case 5 -> deleteInvoice();
                case 0 -> view.showMessage("Volviendo al menú principal...");
                default -> view.showMessage("Opción no válida.");
            }
        } while (option != 0);
    }

    // Crear factura
    private void createInvoice() {
        System.out.print("Código de factura (ej: FAC-001): ");
        String code = scanner.nextLine().trim();

        System.out.print("Fecha (ej: 2026-05-27): ");
        String date = scanner.nextLine().trim();

        PaymentMethod paymentMethod = selectPaymentMethod();
        InvoiceStatus status = selectInvoiceStatus();

        System.out.print("¿Está pagada? (s/n): ");
        boolean paid = scanner.nextLine().trim().equalsIgnoreCase("s");

        System.out.print("Número de documento del cliente: ");
        String docNumber = scanner.nextLine().trim();
        Customer customer = customerService.getCustomerById(docNumber);
        if (customer == null) {
            view.showMessage("Cliente no encontrado. Cree el cliente primero.");
            return;
        }

        // Agregar detalles a la factura
        Set<InvoiceDetail> details = new HashSet<>();
        double total = 0;

        System.out.print("¿Cuántos productos desea agregar? ");
        int numProducts = 0;
        try {
            numProducts = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            view.showMessage("Número inválido de productos.");
            return;
        }

        for (int i = 0; i < numProducts; i++) {
            System.out.println("\n--- Producto " + (i + 1) + " ---");
            System.out.print("Nombre del producto: ");
            String productName = scanner.nextLine().trim();

            System.out.print("Cantidad: ");
            int quantity = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Precio unitario: ");
            double unitPrice = Double.parseDouble(scanner.nextLine().trim());

            ProductCategory category = selectCategory();

            InvoiceDetail detail = new InvoiceDetail(productName, quantity, unitPrice, category);
            details.add(detail);
            total += detail.getSubtotal();
        }

        Invoice invoice = new Invoice(idCounter++, code, date, total, paid, paymentMethod, status, customer, details);
        boolean result = service.createInvoice(invoice);
        view.showMessage(result ? "Factura creada exitosamente. Total: $" + total
                : "Error: no se pudo crear la factura.");
    }

    // Consultar todas
    private void listAllInvoices() {
        view.showAllInvoices(service.getAllInvoices());
    }

    // Consultar por ID
    private void findInvoiceById() {
        System.out.print("ID de la factura: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            view.showInvoice(service.getInvoiceById(id));
        } catch (NumberFormatException e) {
            view.showMessage("ID inválido.");
        }
    }

    // Actualizar factura
    private void updateInvoice() {
        System.out.print("ID de la factura a actualizar: ");
        int id;
        try {
            id = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            view.showMessage("ID inválido.");
            return;
        }

        Invoice existing = service.getInvoiceById(id);
        if (existing == null) {
            view.showMessage("Factura no encontrada.");
            return;
        }

        System.out.print("Nueva fecha (actual: " + existing.getDate() + ", ENTER para mantener): ");
        String date = scanner.nextLine().trim();
        if (date.isBlank()) date = existing.getDate();

        System.out.print("Nuevo método de pago (actual: " + existing.getPaymentMethod() + ", ENTER para mantener): ");
        String pmStr = scanner.nextLine().trim();
        PaymentMethod paymentMethod = pmStr.isBlank() ? existing.getPaymentMethod() : selectPaymentMethodFromInput(pmStr);

        System.out.print("Nuevo estado (actual: " + existing.getInvoiceStatus() + ", ENTER para mantener): ");
        String stStr = scanner.nextLine().trim();
        InvoiceStatus status = stStr.isBlank() ? existing.getInvoiceStatus() : selectStatusFromInput(stStr);

        System.out.print("¿Está pagada? (s/n, actual: " + (existing.isPaid() ? "s" : "n") + "): ");
        String paidStr = scanner.nextLine().trim();
        boolean paid = paidStr.isBlank() ? existing.isPaid() : paidStr.equalsIgnoreCase("s");

        Invoice updated = new Invoice(id, existing.getCode(), date, existing.getTotal(),
                paid, paymentMethod, status, existing.getCustomer(), existing.getDetails());

        boolean result = service.updateInvoice(id, updated);
        view.showMessage(result ? "Factura actualizada exitosamente." : "Error al actualizar la factura.");
    }

    // Eliminar factura
    private void deleteInvoice() {
        System.out.print("ID de la factura a eliminar: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            boolean result = service.deleteInvoice(id);
            view.showMessage(result ? "Factura eliminada exitosamente." : "Factura no encontrada.");
        } catch (NumberFormatException e) {
            view.showMessage("ID inválido.");
        }
    }

    // Selección de método de pago interactivo
    private PaymentMethod selectPaymentMethod() {
        System.out.println("Método de pago:");
        System.out.println("  1. CASH (Efectivo)");
        System.out.println("  2. CREDIT_CARD (Tarjeta de crédito)");
        System.out.println("  3. TRANSFER (Transferencia)");
        System.out.print("Seleccione: ");
        String opt = scanner.nextLine().trim();
        return switch (opt) {
            case "1" -> PaymentMethod.CASH;
            case "2" -> PaymentMethod.CREDIT_CARD;
            case "3" -> PaymentMethod.TRANSFER;
            default -> PaymentMethod.CASH;
        };
    }

    private PaymentMethod selectPaymentMethodFromInput(String input) {
        try {
            return PaymentMethod.valueOf(input.toUpperCase());
        } catch (IllegalArgumentException e) {
            return PaymentMethod.CASH;
        }
    }

    // Selección de estado de factura
    private InvoiceStatus selectInvoiceStatus() {
        System.out.println("Estado de la factura:");
        System.out.println("  1. PENDING (Pendiente)");
        System.out.println("  2. PAID (Pagada)");
        System.out.println("  3. CANCELLED (Cancelada)");
        System.out.print("Seleccione: ");
        String opt = scanner.nextLine().trim();
        return switch (opt) {
            case "1" -> InvoiceStatus.PENDING;
            case "2" -> InvoiceStatus.PAID;
            case "3" -> InvoiceStatus.CANCELLED;
            default -> InvoiceStatus.PENDING;
        };
    }

    private InvoiceStatus selectStatusFromInput(String input) {
        try {
            return InvoiceStatus.valueOf(input.toUpperCase());
        } catch (IllegalArgumentException e) {
            return InvoiceStatus.PENDING;
        }
    }

    // Seleccionar categoría de producto
    private ProductCategory selectCategory() {
        System.out.println("Categoría:");
        System.out.println("  1. FOOD  2. TECHNOLOGY  3. CLOTHING  4. HOME  5. HEALTH  6. OFFICE  7. OTHER");
        System.out.print("Seleccione: ");
        String opt = scanner.nextLine().trim();
        return switch (opt) {
            case "1" -> ProductCategory.FOOD;
            case "2" -> ProductCategory.TECHNOLOGY;
            case "3" -> ProductCategory.CLOTHING;
            case "4" -> ProductCategory.HOME;
            case "5" -> ProductCategory.HEALTH;
            case "6" -> ProductCategory.OFFICE;
            default -> ProductCategory.OTHER;
        };
    }
}