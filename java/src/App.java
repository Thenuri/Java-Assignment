import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;
// import java.util.stream.Collectors;

class Order {
    String customerName;
    int customerAddress;
    String customerPhone;
    Map<String, Object> orderDetails;

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
}

public class App {

    private int[][] distanceMatrix;
    private List<Order> orders;

    public App(int[][] distanceMatrix, List<Order> orders) {
        this.distanceMatrix = distanceMatrix;
        this.orders = orders;
    }

    public List<Integer> shortestPath(int startPoint) {
        List<Integer> path = new ArrayList<>();
        boolean[] visited = new boolean[distanceMatrix.length];
        Arrays.fill(visited, false);

        path.add(startPoint);
        visited[startPoint] = true;

        while (true) {
            int currentNode = path.get(path.size() - 1);
            int nextNode = -1;
            int minDistance = Integer.MAX_VALUE;

            for (int neighbor = 0; neighbor < distanceMatrix[currentNode].length; neighbor++) {
                if (!visited[neighbor] && distanceMatrix[currentNode][neighbor] < minDistance) {
                    nextNode = neighbor;
                    minDistance = distanceMatrix[currentNode][neighbor];
                }
            }

            if (nextNode == -1) {
                // Backtrack when no unvisited neighbors are found
                // path.remove(path.size() - 1);
                break;

            } else {
                // If "nextNode" is found, add it to "path" and mark it as visited
                path.add(nextNode);
                visited[nextNode] = true;
            }

            if (nextNode == -1 && path.size() == 1) {
                break; // No more paths to explore
            }
        }

        return path;
    }

    public Stack<Order> loadOrder(List<Integer> path) {

        Stack<Order> loadingStack = new Stack<>();

        for (int node : path) {
            for (Order order : orders) {
                if (order.customerAddress == node) {
                    loadingStack.push(order);
                    break;
                }
            }
        }

        return loadingStack;
    }

    public List<Map<String, Object>> generateInvoices(List<Integer> reversedPath) {
        List<Map<String, Object>> invoices = new ArrayList<>();
        int invoiceID = 1;
        for (int node: reversedPath) {
            for (Order order : orders) {
                if (order.customerAddress == node) {
                    Map<String, Object> invoice = new HashMap<>();
                    invoice.put("InvoiceID", invoiceID);
                    invoice.put("CustomerName", order.customerName);
                    invoice.put("CustomerAddress", order.customerAddress);
                    invoice.put("CustomerPhone", order.customerPhone);
                    invoice.put("OrderDetails", order.orderDetails);
                    invoices.add(invoice);
                    invoiceID++;
                    break;
                }
            }
        }

        return invoices;
    }

    public List<Map<String, Object>> generateFile (List<Map<String, Object>> invoices ){
        List<Map<String, Object>> file = new ArrayList<>();
        Collections.reverse(invoices);

        int previousLocationIndex = 0; // 0 is the starting point
        for (Map<String, Object> invoice : invoices) {
            Map<String, Object> fileInvoice = new HashMap<>();
            fileInvoice.put("InvoiceID", invoice.get("InvoiceID"));
            file.add(fileInvoice);

            int distance = distanceMatrix[previousLocationIndex][(int) invoice.get("CustomerAddress")];
            fileInvoice.put("Distance", distance);

            int[] coordinates = {previousLocationIndex, (int) invoice.get("CustomerAddress")};
            String coordinatesString = "[" + coordinates[0] + "][" + coordinates[1] + "]";
            fileInvoice.put("Coordinates", coordinatesString);

            // Update previousLocationIndex with the current CustomerAddress
            previousLocationIndex = (int) invoice.get("CustomerAddress");

           
        }
        return file;

        
    }

    public static void main(String[] args) {
        int[][] distanceMatrix = {
                { 0, 18, 25, 1, 8 },
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
        Order order2 = new Order("Nimashi", 2, "0712345674", order2Details);

        Map<String, Object> order3Details = new HashMap<>();
        order3Details.put("item", "NailPolish");
        order3Details.put("quantity", 2);
        order3Details.put("price", 100);
        Order order3 = new Order("sanduni", 3, "0771234567", order3Details);

        Map<String, Object> order4Details = new HashMap<>();
        order4Details.put("item", "Foundation");
        order4Details.put("quantity", 2);
        order4Details.put("price", 100);
        Order order4 = new Order("Chamodi", 4, "0771234564", order4Details);

        orders.add(order1);
        orders.add(order2);
        orders.add(order3);
        orders.add(order4);

        for (Order order : orders) {
            int customerAddress = order.customerAddress;
            if (customerAddress < 0 || customerAddress >= distanceMatrix.length) {
                throw new IllegalArgumentException("Invalid customer address: " + customerAddress);
            }
        }


        App deliverySystem = new App(distanceMatrix, orders);

        int startPoint = 0; // Define your starting point
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
            System.out.println("Customer Name: " + order.customerName);
            System.out.println("Customer Address: " + order.customerAddress);
            System.out.println("Customer Phone: " + order.customerPhone);
            System.out.println("Order Details: " + order.orderDetails);
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

        //Serialize the backupfile objects and save them to files

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
