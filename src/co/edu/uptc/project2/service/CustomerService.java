package co.edu.uptc.project2.service;

import java.util.List;

import co.edu.uptc.project2.domain.Customer;
import co.edu.uptc.project2.repository.CustomerRepository;

public class CustomerService {

    private CustomerRepository customerRepository;

    public CustomerService() {
        customerRepository = new CustomerRepository();
    }

    // Crear cliente
    public boolean createCustomer(Customer customer) {
        if (customer == null) {
        	return false;
        }
        if (customer.getName() == null || customer.getName().isBlank()) {
        	return false;
        }
        if (customer.getDocumentNumber() == null || customer.getDocumentNumber().isBlank()) {
        	return false;
        }
        return customerRepository.add(customer);
    }

    // Consultar todos
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    // Consultar por número de documento
    public Customer getCustomerById(String documentNumber) {
        if (documentNumber == null || documentNumber.isBlank()) {
        	return null;
        }
        return customerRepository.findById(documentNumber);
    }

    // Actualizar cliente
    public boolean updateCustomer(String documentNumber, Customer updated) {
        if (documentNumber == null || updated == null) {
        	return false;
        }
        return customerRepository.update(documentNumber, updated);
    }

    // Eliminar cliente
    public boolean deleteCustomer(String documentNumber) {
        if (documentNumber == null || documentNumber.isBlank()) {
        	return false;
        }
        return customerRepository.delete(documentNumber);
    }
}