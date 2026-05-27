package co.edu.uptc.project2.ui.controller;

import java.util.Scanner;

import co.edu.uptc.project2.domain.InvoiceDetail;
import co.edu.uptc.project2.enums.ProductCategory;
import co.edu.uptc.project2.service.InvoiceDetailService;
import co.edu.uptc.project2.ui.view.InvoiceDetailView;

public class InvoiceDetailController {

    private InvoiceDetailService service;
    private InvoiceDetailView view;
    private Scanner scanner;

    public InvoiceDetailController(Scanner scanner) {
        this.service = new InvoiceDetailService();
        this.view = new InvoiceDetailView();
        this.scanner = scanner;
    }

    // Menú principal de detalles
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
                case 1 -> createDetail();
                case 2 -> listAllDetails();
                case 3 -> findDetailById();
                case 4 -> updateDetail();
                case 5 -> deleteDetail();
                case 0 -> view.showMessage("Volviendo al menú principal...");
                default -> view.showMessage("Opción no válida.");
            }
        } while (option != 0);
    }

    // Crear detalle
    private void createDetail() {
        System.out.print("Nombre del producto: ");
        String productName = scanner.nextLine().trim();

        System.out.print("Cantidad: ");
        int quantity = 0;
        try {
            quantity = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            view.showMessage("Cantidad inválida.");
            return;
        }

        System.out.print("Precio unitario: ");
        double unitPrice = 0;
        try {
            unitPrice = Double.parseDouble(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            view.showMessage("Precio inválido.");
            return;
        }

        ProductCategory category = selectCategory();

        InvoiceDetail detail = new InvoiceDetail(productName, quantity, unitPrice, category);
        boolean result = service.createDetail(detail);
        view.showMessage(result ? "Detalle creado exitosamente." : "Error: no se pudo crear el detalle (ya existe o datos inválidos).");
    }

    // Consultar todos
    private void listAllDetails() {
        view.showAllDetails(service.getAllDetails());
    }

    // Consultar por nombre de producto
    private void findDetailById() {
        System.out.print("Nombre del producto a buscar: ");
        String productName = scanner.nextLine().trim();
        view.showDetail(service.getDetailById(productName));
    }

    // Actualizar detalle
    private void updateDetail() {
        System.out.print("Nombre del producto a actualizar: ");
        String productName = scanner.nextLine().trim();

        InvoiceDetail existing = service.getDetailById(productName);
        if (existing == null) {
            view.showMessage("Detalle no encontrado.");
            return;
        }

        System.out.print("Nueva cantidad (actual: " + existing.getQuantity() + "): ");
        String qStr = scanner.nextLine().trim();
        int quantity = qStr.isBlank() ? existing.getQuantity() : Integer.parseInt(qStr);

        System.out.print("Nuevo precio unitario (actual: " + existing.getUnitPrice() + "): ");
        String pStr = scanner.nextLine().trim();
        double unitPrice = pStr.isBlank() ? existing.getUnitPrice() : Double.parseDouble(pStr);

        System.out.print("Nueva categoría (actual: " + existing.getCategory() + ", presione ENTER para mantener): ");
        String catStr = scanner.nextLine().trim();
        ProductCategory category = catStr.isBlank() ? existing.getCategory() : selectCategoryFromInput(catStr);

        InvoiceDetail updated = new InvoiceDetail(existing.getProductName(), quantity, unitPrice, category);
        boolean result = service.updateDetail(productName, updated);
        view.showMessage(result ? "Detalle actualizado exitosamente." : "Error al actualizar el detalle.");
    }

    // Eliminar detalle
    private void deleteDetail() {
        System.out.print("Nombre del producto a eliminar: ");
        String productName = scanner.nextLine().trim();
        boolean result = service.deleteDetail(productName);
        view.showMessage(result ? "Detalle eliminado exitosamente." : "Detalle no encontrado.");
    }

    // Seleccionar categoría interactivo
    private ProductCategory selectCategory() {
        System.out.println("Categoría:");
        System.out.println("  1. FOOD");
        System.out.println("  2. TECHNOLOGY");
        System.out.println("  3. CLOTHING");
        System.out.println("  4. HOME");
        System.out.println("  5. HEALTH");
        System.out.println("  6. OFFICE");
        System.out.println("  7. OTHER");
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

    // Seleccionar categoría desde texto
    private ProductCategory selectCategoryFromInput(String input) {
        try {
            return ProductCategory.valueOf(input.toUpperCase());
        } catch (IllegalArgumentException e) {
            return ProductCategory.OTHER;
        }
    }
}