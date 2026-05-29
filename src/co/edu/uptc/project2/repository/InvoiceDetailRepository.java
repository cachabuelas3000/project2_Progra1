package co.edu.uptc.project2.repository;

import java.util.ArrayList;
import java.util.List;

import co.edu.uptc.project2.domain.InvoiceDetail;

public class InvoiceDetailRepository {

    private List<InvoiceDetail> details;

    public InvoiceDetailRepository() {
        details = new ArrayList<>();
    }

    // Crear
    public boolean add(InvoiceDetail detail) {
        if (detail == null) return false;
        // Evitar duplicados por nombre de producto
        for (InvoiceDetail d : details) {
            if (d.equals(detail)) return false;
        }
        details.add(detail);
        return true;
    }

    // Consultar todos
    public List<InvoiceDetail> findAll() {
        return new ArrayList<>(details);
    }

    // Consultar por nombre de producto (ID)
    public InvoiceDetail findById(String productName) {
        for (InvoiceDetail d : details) {
            if (d.getProductName().equalsIgnoreCase(productName)) {
                return d;
            }
        }
        return null;
    }

    // Actualizar
    public boolean update(String productName, InvoiceDetail updated) {
        for (int i = 0; i < details.size(); i++) {
            if (details.get(i).getProductName().equalsIgnoreCase(productName)) {
                details.set(i, updated);
                return true;
            }
        }
        return false;
    }

    // Eliminar
    public boolean delete(String productName) {
        return details.removeIf(d -> d.getProductName().equalsIgnoreCase(productName));
    }
}