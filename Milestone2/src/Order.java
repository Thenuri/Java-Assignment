import java.io.Serializable;
import java.util.Map;

public class Order implements Serializable{
    private static final long serialVersionUID = 1L;
    private String customerName;
    private int customerAddress;
    private String customerPhone;
    private Map<String, Object> orderDetails;
    private int invoiceID;
    private int gridCoordinateX;
    private int gridCoordinateY;


    public Order(String customerName, int customerAddress, String customerPhone, Map<String, Object> orderDetails) {
        this.customerName = customerName;
        this.customerAddress = customerAddress;
        if (isValidPhoneNumber(customerPhone)) {
            this.customerPhone = customerPhone;
        } else {
            throw new IllegalArgumentException("Invalid phone number format. Phone number must have exactly 10 digits.");
        }
        this.orderDetails = orderDetails;
        
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        // Remove any non-digit characters and check if the length is exactly 10 digits
        String cleanedPhoneNumber = phoneNumber.replaceAll("[^0-9]", "");
        return cleanedPhoneNumber.length() == 10;
    }

    

    public String getCustomerName() {
        return customerName;
    }

    public int getCustomerAddress() {
        return customerAddress;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public Map<String, Object> getOrderDetails() {
        return orderDetails;
    }
    public int getInvoiceID() {
        return invoiceID;
    }
    public int getGridCoordinateX() {
        return gridCoordinateX;
    }
    public int getGridCoordinateY() {
        return gridCoordinateY;
    }
}
