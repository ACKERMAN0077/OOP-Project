package mainpkg.ecoresort.Restaurant;

import java.io.Serializable;
import java.util.ArrayList;

public class Order implements Serializable {
    private int orderId;
    private ArrayList<MenuItem> items;
    private String status;
    private double subtotal;
    private double tax;
    private double discount;
    private double grandTotal;
    private String paymentMethod;
    private int tableNumber;
    private int roomNumber;

    public Order() {
        this.items = new ArrayList<>();
    }

    public Order(int orderId, int tableNumber) {
        this.orderId = orderId;
        this.tableNumber = tableNumber;
        this.items = new ArrayList<>();
        this.status = "Pending";
        this.subtotal = 0;
        this.tax = 0;
        this.discount = 0;
        this.grandTotal = 0;
        this.paymentMethod = "";
        this.roomNumber = 0;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public ArrayList<MenuItem> getItems() {
        return items;
    }

    public void setItems(ArrayList<MenuItem> items) {
        this.items = items;
    }

    public void addItem(MenuItem item) {
        this.items.add(item);
        calculateTotals();
    }

    public void removeItem(MenuItem item) {
        this.items.remove(item);
        calculateTotals();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getTax() {
        return tax;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
        calculateTotals();
    }

    public double getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(double grandTotal) {
        this.grandTotal = grandTotal;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public int getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(int tableNumber) {
        this.tableNumber = tableNumber;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public void calculateTotals() {
        this.subtotal = 0;
        for (MenuItem item : items) {
            this.subtotal += item.getPrice();
        }
        this.tax = this.subtotal * 0.05;
        this.grandTotal = this.subtotal + this.tax - this.discount;
    }
}
