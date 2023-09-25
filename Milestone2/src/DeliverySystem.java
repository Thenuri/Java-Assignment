// import java.io.FileOutputStream;
import java.util.*;
// import java.util.stream.Collectors;

public class DeliverySystem {
    private int[][] distanceMatrix;
    private List<Order> orders;

    public DeliverySystem(int[][] distanceMatrix, List<Order> orders) {
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
                if (order.getCustomerAddress() == node) {
                    loadingStack.push(order);
                    break;
                }
            }
        }

        return loadingStack;
    }
    public List<Map<String, Object>> generateInvoices(List<Integer> reversedPath) {
        List<Map<String, Object>> invoices = new ArrayList<>();
        int  InvoiceID = 1;
        for (int node: reversedPath) {
            for (Order order : orders) {
                if (order.getCustomerAddress() == node) {
                    Map<String, Object> invoice = new HashMap<>();
                    invoice.put("InvoiceID", InvoiceID);
                    invoice.put("CustomerName", order.getCustomerName());
                    invoice.put("CustomerAddress", order.getCustomerAddress());
                    invoice.put("CustomerPhone", order.getCustomerPhone());
                    invoice.put("OrderDetails", order.getOrderDetails());
                    invoices.add(invoice);
                    InvoiceID++;
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
}
