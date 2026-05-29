package co.edu.uptc.project2.service;

import java.util.List;

import co.edu.uptc.project2.domain.InvoiceDetail;
import co.edu.uptc.project2.repository.InvoiceDetailRepository;

public class InvoiceDetailService {

    private InvoiceDetailRepository detailRepository;

    public InvoiceDetailService() {
        detailRepository = new InvoiceDetailRepository();
    }

    // Crear detalle
    public boolean createDetail(InvoiceDetail detail) {
        if (detail == null) return false;
        if (detail.getProductName() == null || detail.getProductName().isBlank()) return false;
        if (detail.getQuantity() <= 0) return false;
        if (detail.getUnitPrice() < 0) return false;
        return detailRepository.add(detail);
    }

    // Consultar todos
    public List<InvoiceDetail> getAllDetails() {
        return detailRepository.findAll();
    }

    // Consultar por nombre de producto
    public InvoiceDetail getDetailById(String productName) {
        if (productName == null || productName.isBlank()) return null;
        return detailRepository.findById(productName);
    }

    // Actualizar detalle
    public boolean updateDetail(String productName, InvoiceDetail updated) {
        if (productName == null || updated == null) return false;
        return detailRepository.update(productName, updated);
    }

    // Eliminar detalle
    public boolean deleteDetail(String productName) {
        if (productName == null || productName.isBlank()) return false;
        return detailRepository.delete(productName);
    }
}