package co.edu.uptc.project2.ui;

import java.util.Scanner;

import co.edu.uptc.project2.ui.controller.CustomerController;
import co.edu.uptc.project2.ui.controller.InvoiceController;
import co.edu.uptc.project2.ui.controller.InvoiceDetailController;


public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        // CustomerController se construye primero y comparte su servicio con InvoiceController
        // a través de un método getter — así Main no necesita importar ningun Service
        CustomerController customerController = new CustomerController(scanner);
        InvoiceDetailController detailController = new InvoiceDetailController(scanner);
        InvoiceController invoiceController = new InvoiceController(scanner, customerController.getService());

        int option = -1;

        System.out.println("============================================");
        System.out.println("   SISTEMA DE GESTIÓN DE FACTURAS   ");
        System.out.println("============================================");

        do {
            System.out.println("\n========== MENÚ PRINCIPAL ==========");
            System.out.println("1. Gestión de Clientes");
            System.out.println("2. Gestión de Detalles de Factura");
            System.out.println("3. Gestión de Facturas");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");

            try {
                option = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Opción inválida. Intente de nuevo.");
                continue;
            }

            switch (option) {
                case 1 -> customerController.showMenu();
                case 2 -> detailController.showMenu();
                case 3 -> invoiceController.showMenu();
                case 0 -> System.out.println("¡Hasta luego!");
                default -> System.out.println("Opción no válida.");
            }

        } while (option != 0);

        scanner.close();
    }
}
