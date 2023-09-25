import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;

public class App {


    public static void main(String[] args) {
        int[][] distanceMatrix = {
                { 0, 18, 25, 12, 8 },
                { 18, 0, 15, 8, 2 },
                { 25, 15, 0, 1, 28 },
                { 12, 8, 5, 0, 12 },
                { 8, 2, 28, 12, 0 }
        };

         // Check for negative distances in the distance matrix
         for (int i = 0; i < distanceMatrix.length; i++) {
            for (int j = 0; j < distanceMatrix[i].length; j++) {
                if (distanceMatrix[i][j] < 0) {
                    // Handle the error, for example, by throwing an exception
                    throw new IllegalArgumentException("Negative distance found at (" + i + ", " + j + ")");
                }
            }
        }
        //check if the distance between two points is zero
        for (int i = 0; i < distanceMatrix.length; i++) {
            for (int j = i + 1; j < distanceMatrix[i].length; j++) {
                if (distanceMatrix[i][j] == 0) {
                    throw new IllegalArgumentException("Distance between points  " + i + " and " + j + "  canot be  zero.");
                }
            }
        }

        List<Order> orders = new ArrayList<>();
        Map<String, Object> order1Details = new HashMap<>();
        order1Details.put("item", "RedLipstick");
        order1Details.put("quantity", 2);
        order1Details.put("price", 100);
        Order order1 = new Order("Sachith", 1, "0771234567", order1Details);

        Map<String, Object> order2Details = new HashMap<>();
        order2Details.put("item", "EyeLash");
        order2Details.put("quantity", 2);
        order2Details.put("price", 100);
        Order order2 = new Order("Nimashi", 2, "0771234567", order2Details);

        Map<String, Object> order3Details = new HashMap<>();
        order3Details.put("item", "NailPolish");
        order3Details.put("quantity", 2);
        order3Details.put("price", 100);
        Order order3 = new Order("sanduni", 3, "0771234567", order3Details);

        Map<String, Object> order4Details = new HashMap<>();
        order4Details.put("item", "Foundation");
        order4Details.put("quantity", 2);
        order4Details.put("price", 100);
        Order order4 = new Order("Chamodi", 4, "0771245671", order4Details);
        
        orders.add(order1);
        orders.add(order2);
        orders.add(order3);
        orders.add(order4);

        for (Order order : orders) {
            int customerAddress = order.getCustomerAddress();
            if (customerAddress < 0 || customerAddress >= distanceMatrix.length) {
                throw new IllegalArgumentException("Invalid customer address: " + customerAddress);
            }
        }

        DeliverySystem deliverySystem = new DeliverySystem(distanceMatrix, orders);

        int startPoint = 0;
        List<Integer> path = deliverySystem.shortestPath(startPoint);
        List<Integer> reversedPath = new ArrayList<>(path);
        Collections.reverse(reversedPath);

        Stack<Order> loadingStack = deliverySystem.loadOrder(reversedPath);
        List<Map<String, Object>> invoices = deliverySystem.generateInvoices(reversedPath);

        // Print shortest path
        System.out.println("Shortest Path:");
        System.out.println(path);
        System.out.println("Reversed Path:");
        System.out.println(reversedPath);

        // Print loading stack
        System.out.println("Package Order (Orders):");
        for (Order order : loadingStack) {
            System.out.println("Customer Name: " + order.getCustomerName());
            System.out.println("Customer Address: " + order.getCustomerAddress());
            System.out.println("Customer Phone: " + order.getCustomerPhone());
            System.out.println("Order Details: " + order.getOrderDetails());
            System.out.println();
        }

        // Print invoices
        System.out.println("Invoices:");
        for (Map<String, Object> invoice : invoices) {
            System.out.println("Invoice ID: " + invoice.get("InvoiceID"));
            System.out.println("Customer Name: " + invoice.get("CustomerName"));
            System.out.println("Customer Address: " + invoice.get("CustomerAddress"));
            System.out.println("Customer Phone: " + invoice.get("CustomerPhone"));
            System.out.println("Order Details: " + invoice.get("OrderDetails"));
            System.out.println();
        }


         
        List<Map<String, Object>> file = new ArrayList<>();
        file = deliverySystem.generateFile(invoices);

        //Serialize the order objects and save them to files

        try (FileOutputStream fileOutputStream = new FileOutputStream("backupfile.ser");
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {

            objectOutputStream.writeObject(file);
            System.out.println("Order object has been serialized and saved to backupfile.ser");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Deserialize the order objects from files

        try (FileInputStream fileInputStream = new FileInputStream("backupfile.ser");
             ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {

            List<Map<String, Object>> filebackup = (List<Map<String, Object>>) objectInputStream.readObject();

            // Now, you can use the deserialized Order object
            System.out.println("Deserialized filebackup of infomation");
            for (Map<String, Object> invoice : filebackup) {
                System.out.println("Invoice ID: " + invoice.get("InvoiceID"));
                System.out.println("distance to location: " + invoice.get("Distance"));
                System.out.println("Gride Codinates: " + invoice.get("Coordinates"));
                System.out.println();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
