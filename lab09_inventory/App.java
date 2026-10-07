import java.util.List;

public class App {
    public static void main(String[] args) {
        System.out.println("System started.");

        Inventory<Item> inventory = new Inventory<>();

        Book book1 = new Book(1, "Concurrency in Java", "Brian Goetz");
        Book book2 = new Book(2, "Effective Java", "Joshua Bloch");
        ElectronicDevice device1 = new ElectronicDevice(3, "MacBook Pro", "Laptop");
        ElectronicDevice device2 = new ElectronicDevice(4, "iPhone", "Smartphone");

        inventory.addItem(book1);
        inventory.addItem(book2);
        inventory.addItem(device1);
        inventory.addItem(device2);

        System.out.println("\nAll inventory items:");
        displayInventory(inventory);

        System.out.println("\nSearch by name: Concurrency in Java");
        printResults(inventory.findItems(new SearchByName("Concurrency in Java")));

        System.out.println("\nSearch by ID: 3");
        printResults(inventory.findItems(new SearchById(3)));

        System.out.println("\nSearch by author: Joshua Bloch");
        printResults(inventory.findItems(new SearchByAuthor("Joshua Bloch")));

        System.out.println("\nSearch by category: Smartphone");
        printResults(inventory.findItems(new SearchByCategory("Smartphone")));

        System.out.println("\nSearch for an electronic device by name: MacBook Pro");
        printResults(inventory.findItems(new SearchByName("MacBook Pro")));

        inventory.removeItem(device2);
        System.out.println("\nInventory after removing iPhone:");
        displayInventory(inventory);

        System.out.println("\nSystem stopped.");
    }

    public static void displayInventory(Inventory<?> inventory) {
        for (Object item : inventory.getAllItems()) {
            System.out.println(item);
        }
    }

    private static void printResults(List<Item> items) {
        if (items.isEmpty()) {
            System.out.println("No matching items found.");
        } else {
            for (Item item : items) {
                System.out.println(item);
            }
        }
    }
}