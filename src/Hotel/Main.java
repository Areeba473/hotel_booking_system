package Hotel;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // ✅ Initialize the database and create table if not exists
        DatabaseConnection.initializeDatabase();

        Scanner scanner = new Scanner(System.in);
        Hotel hotel = new Hotel(5); // Create hotel with 5 rooms

        while (true) {
            System.out.println("\n--- Hotel Booking System ---");
            System.out.println("1. Show Available Rooms");
            System.out.println("2. Book a Room");
            System.out.println("3. Cancel Booking");
            System.out.println("4. Check Out");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");

            if (!scanner.hasNextInt()) {
                System.out.println("❌ Please enter a valid number.");
                scanner.next(); // discard invalid input
                continue;
            }

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            int roomNumber;

            switch (choice) {
                case 1:
                    hotel.showAvailableRooms();
                    break;

                case 2:
                    System.out.print("Enter room number to book: ");
                    roomNumber = scanner.nextInt();
                    scanner.nextLine(); // Consume newline

                    System.out.print("Enter customer name: ");
                    String customerName = scanner.nextLine();

                    hotel.bookRoom(roomNumber, customerName);
                    break;

                case 3:
                    System.out.print("Enter room number to cancel booking: ");
                    roomNumber = scanner.nextInt();
                    scanner.nextLine();
                    hotel.cancelBooking(roomNumber);
                    break;

                case 4:
                    System.out.print("Enter room number to check out: ");
                    roomNumber = scanner.nextInt();
                    scanner.nextLine();
                    hotel.checkOut(roomNumber);
                    break;

                case 5:
                    System.out.println("Thank you! Goodbye.");
                    scanner.close();
                    return;

                default:
                    System.out.println("❌ Invalid option. Try again.");
            }
        }
    }
}
