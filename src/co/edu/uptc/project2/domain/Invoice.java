package co.edu.uptc.project2.domain;

import java.util.HashSet;
import java.util.Set;

import co.edu.uptc.project2.enums.InvoiceStatus;
import co.edu.uptc.project2.enums.PaymentMethod;

public class Invoice {

    // Entidad principal: Factura

    private int id;
    private String code;
    private String date;
    private double total;
    private boolean paid;

    private PaymentMethod paymentMethod;
    private InvoiceStatus invoiceStatus;

    // Relaciones
    private Customer customer;
    private Set<InvoiceDetail> details;
    
   
    public Invoice() {
        details = new HashSet<>();
    }
    
    

    public Invoice(int id, String code, String date, double total, boolean paid,
            PaymentMethod paymentMethod, InvoiceStatus invoiceStatus,
            Customer customer, Set<InvoiceDetail> details) {

        this.id = id;
        this.code = code;
        this.date = date;
        this.total = total;
        this.paid = paid;
        this.paymentMethod = paymentMethod;
        this.invoiceStatus = invoiceStatus;
        this.customer = customer;
        this.details = details;
    }

	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getCode() {
		return code;
	}


	public void setCode(String code) {
		this.code = code;
	}


	public String getDate() {
		return date;
	}


	public void setDate(String date) {
		this.date = date;
	}


	public double getTotal() {
		return total;
	}


	public void setTotal(double total) {
		this.total = total;
	}


	public boolean isPaid() {
		return paid;
	}


	public void setPaid(boolean paid) {
		this.paid = paid;
	}


	public PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}


	public void setPaymentMethod(PaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}


	public InvoiceStatus getInvoiceStatus() {
		return invoiceStatus;
	}


	public void setInvoiceStatus(InvoiceStatus invoiceStatus) {
		this.invoiceStatus = invoiceStatus;
	}


	public Customer getCustomer() {
		return customer;
	}


	public void setCustomer(Customer customer) {
		this.customer = customer;
	}


	public Set<InvoiceDetail> getDetails() {
	    return details;
	}


	public void setDetails(Set<InvoiceDetail> details) {
	    this.details = details;
	}



	@Override
	public String toString() {
		return "Invoice [id=" + id + ", code=" + code + ", date=" + date + ", total=" + total + ", paid=" + paid
				+ ", paymentMethod=" + paymentMethod + ", invoiceStatus=" + invoiceStatus + ", customer=" + customer
				+ ", details=" + details + "]";
	}
	

	

    
}