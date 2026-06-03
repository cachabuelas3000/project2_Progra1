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
import co.edu.uptc.project2.util.Validator;

public class InvoiceController {

    private InvoiceService service;
    private CustomerService customerService;
    private InvoiceView view;
    private Scanner scanner;
    private int idCounter = 1;

    public InvoiceController(Scanner scanner, CustomerService customerService) {
        this.service = new InvoiceService();
        this.customerService = customerService;
        this.view = new InvoiceView();
        this.scanner = scanner;
    }

    // ── Menú ─────────────────────────────────────────────────────────────────

    public void showMenu() {
        String input;
        int option = -1;
        do {
            view.showMenu();
            input = scanner.nextLine().trim();

            if (!Validator.isValidMenuOption(input)) {
                view.showMessage(Validator.MSG_MENU_OPTION);
                continue;
            }

            option = Integer.parseInt(input);
            switch (option) {
                case 1 -> createInvoice();
                case 2 -> listAllInvoices();
                case 3 -> findInvoiceById();
                case 4 -> updateInvoice();
                case 5 -> deleteInvoice();
                case 0 -> view.showMessage("Volviendo al menú principal...");
                default -> view.showMessage("Opción no válida. Elija entre 0 y 5.");
            }
        } while (option != 0);
    }

    // ── Crear factura ─────────────────────────────────────────────────────────

    private void createInvoice() {
        String code = readInvoiceCode();
        String date = readDate();

        PaymentMethod paymentMethod = selectPaymentMethod();
        InvoiceStatus status = selectInvoiceStatus();

        System.out.print("¿Está pagada? (s/n): ");
        boolean paid = scanner.nextLine().trim().equalsIgnoreCase("s");

        // Número de documento del cliente — solo dígitos
        String docNumber = readDocumentNumber();
        Customer customer = customerService.getCustomerById(docNumber);
        if (customer == null) {
            view.showMessage("✗ Cliente no encontrado. Cree el cliente primero.");
            return;
        }

        Set<InvoiceDetail> details = new HashSet<>();
        double total = 0;

        // Cantidad de productos — entero positivo
        int numProducts = readPositiveInteger("¿Cuántos productos desea agregar?");

        for (int i = 0; i < numProducts; i++) {
            System.out.println("\n--- Producto " + (i + 1) + " ---");

            System.out.print("Nombre del producto: ");
            String productName = scanner.nextLine().trim();
            if (productName.isBlank()) {
                view.showMessage("  ✗ El nombre no puede estar vacío. Producto omitido.");
                continue;
            }

            int quantity   = readPositiveInteger("Cantidad");
            double unitPrice = readPositiveDecimal("Precio unitario");
            ProductCategory category = selectCategory();

            InvoiceDetail detail = new InvoiceDetail(productName, quantity, unitPrice, category);
            details.add(detail);
            total += detail.getSubtotal();
        }

        Invoice invoice = new Invoice(idCounter++, code, date, total, paid,
                paymentMethod, status, customer, details);
        boolean result = service.createInvoice(invoice);
        view.showMessage(result
                ? "✓ Factura creada. Total: $" + String.format("%.2f", total)
                : "✗ Error: no se pudo crear la factura.");
    }

    // ── Listar facturas ───────────────────────────────────────────────────────

    private void listAllInvoices() {
        view.showAllInvoices(service.getAllInvoices());
    }

    // ── Buscar factura ────────────────────────────────────────────────────────

    private void findInvoiceById() {
        int id = readInvoiceId();
        view.showInvoice(service.getInvoiceById(id));
    }

    // ── Actualizar factura ────────────────────────────────────────────────────

    private void updateInvoice() {
        int id = readInvoiceId();
        Invoice existing = service.getInvoiceById(id);
        if (existing == null) {
            view.showMessage("✗ Factura no encontrada.");
            return;
        }

        String date = readOptionalDate(existing.getDate());
        PaymentMethod paymentMethod = readOptionalPaymentMethod(existing.getPaymentMethod());
        InvoiceStatus status = readOptionalStatus(existing.getInvoiceStatus());

        System.out.print("¿Está pagada? (s/n, actual: " + (existing.isPaid() ? "s" : "n") + ", ENTER para mantener): ");
        String paidStr = scanner.nextLine().trim();
        boolean paid = paidStr.isBlank() ? existing.isPaid() : paidStr.equalsIgnoreCase("s");

        Invoice updated = new Invoice(id, existing.getCode(), date, existing.getTotal(),
                paid, paymentMethod, status, existing.getCustomer(), existing.getDetails());

        boolean result = service.updateInvoice(id, updated);
        view.showMessage(result ? "✓ Factura actualizada." : "✗ Error al actualizar.");
    }

    // ── Eliminar factura ──────────────────────────────────────────────────────

    private void deleteInvoice() {
        int id = readInvoiceId();
        boolean result = service.deleteInvoice(id);
        view.showMessage(result ? "✓ Factura eliminada." : "✗ Factura no encontrada.");
    }

    // ── Helpers de lectura con validación ────────────────────────────────────

    /** Lee código de factura: letras, números y guiones. */
    private String readInvoiceCode() {
        String value;
        do {
            System.out.print("Código de factura (ej: FAC-001): ");
            value = scanner.nextLine().trim();
            if (!Validator.isValidInvoiceCode(value)) {
                view.showMessage(Validator.MSG_INVOICE_CODE);
            }
        } while (!Validator.isValidInvoiceCode(value));
        return value;
    }

    /** Lee fecha en formato YYYY-MM-DD. */
    private String readDate() {
        String value;
        do {
            System.out.print("Fecha (YYYY-MM-DD, ej: 2026-05-27): ");
            value = scanner.nextLine().trim();
            if (!Validator.isValidDate(value)) {
                view.showMessage(Validator.MSG_DATE);
            }
        } while (!Validator.isValidDate(value));
        return value;
    }

    private String readOptionalDate(String current) {
        String value;
        do {
            System.out.print("Nueva fecha (actual: " + current + ", ENTER para mantener): ");
            value = scanner.nextLine().trim();
            if (value.isBlank()) {
            	return current;
            }
            if (!Validator.isValidDate(value)) {
                view.showMessage(Validator.MSG_DATE);
            }
        } while (!Validator.isValidDate(value));
        return value;
    }

    /** Lee número de documento del cliente — solo dígitos. */
    private String readDocumentNumber() {
        String value;
        do {
            System.out.print("Número de documento del cliente (solo números, 6-12 dígitos): ");
            value = scanner.nextLine().trim();
            if (!Validator.isValidDocumentNumber(value)) {
                view.showMessage(Validator.MSG_DOCUMENT_NUMBER);
            }
        } while (!Validator.isValidDocumentNumber(value));
        return value;
    }

    /** Lee un entero positivo con un label dado. */
    private int readPositiveInteger(String label) {
        String value;
        do {
            System.out.print(label + " (número entero > 0): ");
            value = scanner.nextLine().trim();
            if (!Validator.isValidPositiveInteger(value)) {
                view.showMessage(Validator.MSG_POSITIVE_INTEGER);
            }
        } while (!Validator.isValidPositiveInteger(value));
        return Integer.parseInt(value);
    }

    /** Lee un decimal positivo con un label dado. */
    private double readPositiveDecimal(String label) {
        String value;
        do {
            System.out.print(label + " (número positivo, ej: 1500 o 1500.50): ");
            value = scanner.nextLine().trim();
            if (!Validator.isValidPositiveDecimal(value)) {
                view.showMessage(Validator.MSG_POSITIVE_DECIMAL);
            }
        } while (!Validator.isValidPositiveDecimal(value));
        return Double.parseDouble(value);
    }

    /** Lee un ID de factura — entero positivo. */
    private int readInvoiceId() {
        String value;
        do {
            System.out.print("ID de la factura (número entero > 0): ");
            value = scanner.nextLine().trim();
            if (!Validator.isValidPositiveInteger(value)) {
                view.showMessage(Validator.MSG_POSITIVE_INTEGER);
            }
        } while (!Validator.isValidPositiveInteger(value));
        return Integer.parseInt(value);
    }

    // ── Selección de enums con validación regex ───────────────────────────────

    /** Selección de método de pago: solo acepta 1, 2 o 3. */
    private PaymentMethod selectPaymentMethod() {
        String opt;
        String validOptions = "^[1-3]$";
        do {
            System.out.println("Método de pago:");
            System.out.println("  1. CASH          (Efectivo)");
            System.out.println("  2. CREDIT_CARD   (Tarjeta de crédito)");
            System.out.println("  3. TRANSFER      (Transferencia)");
            System.out.print("Seleccione [1-3]: ");
            opt = scanner.nextLine().trim();
            if (!opt.matches(validOptions)) {
                view.showMessage("  ✗ Opción inválida. Ingrese 1, 2 o 3.");
            }
        } while (!opt.matches(validOptions));
        return switch (opt) {
            case "1" -> PaymentMethod.CASH;
            case "2" -> PaymentMethod.CREDIT_CARD;
            default  -> PaymentMethod.TRANSFER;
        };
    }

    private PaymentMethod readOptionalPaymentMethod(PaymentMethod current) {
        System.out.print("Nuevo método de pago (actual: " + current + ", ENTER para mantener): ");
        String input = scanner.nextLine().trim();
        if (input.isBlank()) {
        	return current;
        }
        try { return PaymentMethod.valueOf(input.toUpperCase()); }
        catch (IllegalArgumentException e) { return current; }
    }

    /** Selección de estado de factura: solo acepta 1, 2 o 3. */
    private InvoiceStatus selectInvoiceStatus() {
        String opt;
        String validOptions = "^[1-3]$";
        do {
            System.out.println("Estado de la factura:");
            System.out.println("  1. PENDING    (Pendiente)");
            System.out.println("  2. PAID       (Pagada)");
            System.out.println("  3. CANCELLED  (Cancelada)");
            System.out.print("Seleccione [1-3]: ");
            opt = scanner.nextLine().trim();
            if (!opt.matches(validOptions)) {
                view.showMessage("  ✗ Opción inválida. Ingrese 1, 2 o 3.");
            }
        } while (!opt.matches(validOptions));
        return switch (opt) {
            case "1" -> InvoiceStatus.PENDING;
            case "2" -> InvoiceStatus.PAID;
            default  -> InvoiceStatus.CANCELLED;
        };
    }

    private InvoiceStatus readOptionalStatus(InvoiceStatus current) {
        System.out.print("Nuevo estado (actual: " + current + ", ENTER para mantener): ");
        String input = scanner.nextLine().trim();
        if (input.isBlank()) {
        	return current;
        }
        try { return InvoiceStatus.valueOf(input.toUpperCase()); }
        catch (IllegalArgumentException e) { return current; }
    }

    /** Selección de categoría: solo acepta 1 al 7. */
    private ProductCategory selectCategory() {
        String opt;
        String validOptions = "^[1-7]$";
        do {
            System.out.println("Categoría:");
            System.out.println("  1. FOOD       2. TECHNOLOGY  3. CLOTHING  4. HOME");
            System.out.println("  5. HEALTH     6. OFFICE      7. OTHER");
            System.out.print("Seleccione [1-7]: ");
            opt = scanner.nextLine().trim();
            if (!opt.matches(validOptions)) {
                view.showMessage("  ✗ Opción inválida. Ingrese un número entre 1 y 7.");
            }
        } while (!opt.matches(validOptions));
        return switch (opt) {
            case "1" -> ProductCategory.FOOD;
            case "2" -> ProductCategory.TECHNOLOGY;
            case "3" -> ProductCategory.CLOTHING;
            case "4" -> ProductCategory.HOME;
            case "5" -> ProductCategory.HEALTH;
            case "6" -> ProductCategory.OFFICE;
            default  -> ProductCategory.OTHER;
        };
    }
}
