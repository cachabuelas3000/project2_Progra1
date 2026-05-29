package co.edu.uptc.project2.repository;

import java.util.ArrayList;
import java.util.List;

import co.edu.uptc.project2.domain.Invoice;

public class InvoiceRepository {

    private List<Invoice> invoices;

    public InvoiceRepository() {
        invoices = new ArrayList<>();
    }

    // Crear
    public boolean add(Invoice invoice) {
        if (invoice == null) return false;
        for (Invoice inv : invoices) {
            if (inv.equals(invoice)) return false;
        }
        invoices.add(invoice);
        return true;
    }

    // Consultar todos
    public List<Invoice> findAll() {
        return new ArrayList<>(invoices);
    }

    // Consultar por ID
    public Invoice findById(int id) {
        for (Invoice inv : invoices) {
            if (inv.getId() == id) {
                return inv;
            }
        }
        return null;
    }

    // Actualizar
    public boolean update(int id, Invoice updated) {
        for (int i = 0; i < invoices.size(); i++) {
            if (invoices.get(i).getId() == id) {
                invoices.set(i, updated);
                return true;
            }
        }
        return false;
    }

    // Eliminar
    public boolean delete(int id) {
        return invoices.removeIf(inv -> inv.getId() == id);
    }
}