import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class Deserialization {
    public static void main(String[] args) {
        // Deserialize an Order object from a file
        try (FileInputStream fileInputStream = new FileInputStream("order.ser");
             ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {

            Order order = (Order) objectInputStream.readObject();

            // Now, you can use the deserialized Order object
            System.out.println("Deserialized Order Object:");
            System.out.println("Customer Name: " + order.getCustomerName());
            System.out.println("Customer Address: " + order.getCustomerAddress());
            System.out.println("Customer Phone: " + order.getCustomerPhone());
            System.out.println("Order Details: " + order.getOrderDetails());
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
