package co.edu.uptc.project2.ui.controller;

import java.util.Scanner;

import co.edu.uptc.project2.domain.InvoiceDetail;
import co.edu.uptc.project2.enums.ProductCategory;
import co.edu.uptc.project2.service.InvoiceDetailService;
import co.edu.uptc.project2.ui.view.InvoiceDetailView;
import co.edu.uptc.project2.util.Validator;

public class InvoiceDetailController {

    private InvoiceDetailService service;
    private InvoiceDetailView view;
    private Scanner scanner;

    public InvoiceDetailController(Scanner scanner) {
        this.service = new InvoiceDetailService();
        this.view = new InvoiceDetailView();
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
                case 1 -> createDetail();
                case 2 -> listAllDetails();
                case 3 -> findDetailById();
                case 4 -> updateDetail();
                case 5 -> deleteDetail();
                case 0 -> view.showMessage("Volviendo al menú principal...");
                default -> view.showMessage("Opción no válida. Elija entre 0 y 5.");
            }
        } while (option != 0);
    }

    // ── Crear detalle ─────────────────────────────────────────────────────────

    private void createDetail() {
        String productName = readProductName();
        int quantity       = readPositiveInteger("Cantidad");
        double unitPrice   = readPositiveDecimal("Precio unitario");
        ProductCategory category = selectCategory();

        InvoiceDetail detail = new InvoiceDetail(productName, quantity, unitPrice, category);
        boolean result = service.createDetail(detail);
        view.showMessage(result
                ? "✓ Detalle creado exitosamente."
                : "✗ Error: ya existe un detalle con ese nombre de producto.");
    }

    // ── Listar detalles ───────────────────────────────────────────────────────

    private void listAllDetails() {
        view.showAllDetails(service.getAllDetails());
    }

    // ── Buscar detalle ────────────────────────────────────────────────────────

    private void findDetailById() {
        System.out.print("Nombre del producto a buscar: ");
        String productName = scanner.nextLine().trim();
        view.showDetail(service.getDetailById(productName));
    }

    // ── Actualizar detalle ────────────────────────────────────────────────────

    private void updateDetail() {
        System.out.print("Nombre del producto a actualizar: ");
        String productName = scanner.nextLine().trim();

        InvoiceDetail existing = service.getDetailById(productName);
        if (existing == null) {
            view.showMessage("✗ Detalle no encontrado.");
            return;
        }

        int quantity     = readOptionalPositiveInteger("Nueva cantidad", existing.getQuantity());
        double unitPrice = readOptionalPositiveDecimal("Nuevo precio unitario", existing.getUnitPrice());

        System.out.print("Nueva categoría (actual: " + existing.getCategory() + ", ENTER para mantener): ");
        String catStr = scanner.nextLine().trim();
        ProductCategory category = catStr.isBlank() ? existing.getCategory() : selectCategoryFromInput(catStr);

        InvoiceDetail updated = new InvoiceDetail(existing.getProductName(), quantity, unitPrice, category);
        boolean result = service.updateDetail(productName, updated);
        view.showMessage(result ? "✓ Detalle actualizado." : "✗ Error al actualizar.");
    }

    // ── Eliminar detalle ──────────────────────────────────────────────────────

    private void deleteDetail() {
        System.out.print("Nombre del producto a eliminar: ");
        String productName = scanner.nextLine().trim();
        boolean result = service.deleteDetail(productName);
        view.showMessage(result ? "✓ Detalle eliminado." : "✗ Detalle no encontrado.");
    }

    // ── Helpers de lectura con validación ────────────────────────────────────

    /** Lee nombre de producto: no puede estar vacío. */
    private String readProductName() {
        String value;
        do {
            System.out.print("Nombre del producto (no puede estar vacío): ");
            value = scanner.nextLine().trim();
            if (value.isBlank()) {
                view.showMessage("  ✗ El nombre del producto no puede estar vacío.");
            }
        } while (value.isBlank());
        return value;
    }

    /** Lee entero positivo con label. Repite hasta obtener valor válido. */
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

    /** Lee entero positivo opcional: ENTER conserva el valor actual. */
    private int readOptionalPositiveInteger(String label, int current) {
        String value;
        do {
            System.out.print(label + " (actual: " + current + ", ENTER para mantener): ");
            value = scanner.nextLine().trim();
            if (value.isBlank()) {
            	return current;
            }
            if (!Validator.isValidPositiveInteger(value)) {
                view.showMessage(Validator.MSG_POSITIVE_INTEGER);
            }
        } while (!Validator.isValidPositiveInteger(value));
        return Integer.parseInt(value);
    }

    /** Lee decimal positivo con label. Repite hasta obtener valor válido. */
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

    /** Lee decimal positivo opcional: ENTER conserva el valor actual. */
    private double readOptionalPositiveDecimal(String label, double current) {
        String value;
        do {
            System.out.print(label + " (actual: " + current + ", ENTER para mantener): ");
            value = scanner.nextLine().trim();
            if (value.isBlank()) {
            	return current;
            }
            if (!Validator.isValidPositiveDecimal(value)) {
                view.showMessage(Validator.MSG_POSITIVE_DECIMAL);
            }
        } while (!Validator.isValidPositiveDecimal(value));
        return Double.parseDouble(value);
    }

    // ── Selección de categoría con validación regex ───────────────────────────

    /** Selección de categoría: solo acepta 1 al 7. */
    private ProductCategory selectCategory() {
        String opt;
        String validOptions = "^[1-7]$";
        do {
            System.out.println("Categoría:");
            System.out.println("  1. FOOD    2. TECHNOLOGY  3. CLOTHING  4. HOME");
            System.out.println("  5. HEALTH  6. OFFICE      7. OTHER");
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

    private ProductCategory selectCategoryFromInput(String input) {
        try { return ProductCategory.valueOf(input.toUpperCase()); }
        catch (IllegalArgumentException e) { return ProductCategory.OTHER; }
    }
}
