import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
public class HotelReservationSystem {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        HotelManagementSystem hotel = new HotelManagementSystem();

        System.out.println("=============================================");
        System.out.println("   WELCOME TO THE CODEALPHA HOTEL SYSTEM     ");
        System.out.println("=============================================");

        while (true) {
            System.out.println("\n[1] Search Available Rooms  [2] Make a Reservation  [3] View My Bookings  [4] Exit");
            System.out.print("Choose an option: ");
            
            int choice;
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
            } else {
                System.out.println("Invalid option! Please enter a number.");
                scanner.next(); // Clear invalid token
                continue;
            }

            if (choice == 4) {
                System.out.println("\nThank you for using the CodeAlpha Reservation System. Goodbye!");
                break;
            }

            switch (choice) {
                case 1: // SEARCH
                    hotel.displayAvailableRooms();
                    break;

                case 2: // BOOK
                    hotel.displayAvailableRooms();
                    System.out.print("\nEnter the exact Room Number you wish to book: ");
                    int roomNum = scanner.nextInt();
                    System.out.print("Enter Guest Full Name: ");
                    scanner.nextLine(); // Clear buffer
                    String guestName = scanner.nextLine();
                    hotel.bookRoom(roomNum, guestName);
                    break;

                case 3: // VIEW BOOKINGS
                    hotel.displayCurrentBookings();
                    break;

                default:
                    System.out.println("Invalid choice! Please select an option from 1 to 4.");
            }
            
            System.out.println("\n--- Process Completed ---");
        }
        scanner.close();
    }
}

// Class representing an individual Hotel Room
class Room {
    private int roomNumber;
    private String category; // e.g., Standard, Deluxe, Suite
    private double pricePerNight;
    private boolean isAvailable;

    public Room(int roomNumber, String category, double pricePerNight) {
        this.roomNumber = roomNumber;
        this.category = category;
        this.pricePerNight = pricePerNight;
        this.isAvailable = true; // Rooms are open by default
    }

    public int getRoomNumber() { return roomNumber; }
    public String getCategory() { return category; }
    public double getPricePerNight() { return pricePerNight; }
    public boolean isAvailable() { return isAvailable; }

    public void setAvailability(boolean availability) {
        this.isAvailable = availability;
    }
}

// Class representing a completed Booking transaction
class Booking {
    private String guestName;
    private Room bookedRoom;
    private double totalPaid;

    public Booking(String guestName, Room bookedRoom) {
        this.guestName = guestName;
        this.bookedRoom = bookedRoom;
        this.totalPaid = bookedRoom.getPricePerNight(); // Base cost for a single night simulation
    }

    public void displayBookingDetails() {
        System.out.printf("Guest: %-15s | Room: %-4d | Category: %-10s | Paid: $%.2f\n", 
            guestName, bookedRoom.getRoomNumber(), bookedRoom.getCategory(), totalPaid);
    }
}

// System controller managing active states and collections
class HotelManagementSystem {
    private List<Room> rooms;
    private List<Booking> bookings;

    public HotelManagementSystem() {
        rooms = new ArrayList<>();
        bookings = new ArrayList<>();
        
        // Populate hotel inventory floors
        rooms.add(new Room(101, "Standard", 85.00));
        rooms.add(new Room(102, "Standard", 85.00));
        rooms.add(new Room(201, "Deluxe", 150.00));
        rooms.add(new Room(202, "Deluxe", 150.00));
        rooms.add(new Room(301, "VIP Suite", 320.00));
    }

    public void displayAvailableRooms() {
        System.out.println("\n--- CURRENT ROOM AVAILABILITY ---");
        System.out.printf("%-10s %-12s %-15s\n", "Room Num", "Category", "Price per Night");
        System.out.println("----------------------------------------");
        
        boolean found = false;
        for (Room room : rooms) {
            if (room.isAvailable()) {
                System.out.printf("%-10d %-12s $%-15.2f\n", room.getRoomNumber(), room.getCategory(), room.getPricePerNight());
                found = true;
            }
        }
        if (!found) {
            System.out.println("Notice: No rooms are currently vacant.");
        }
    }

    public void bookRoom(int roomNumber, String guestName) {
        for (Room room : rooms) {
            if (room.getRoomNumber() == roomNumber) {
                if (!room.isAvailable()) {
                    System.out.println("Booking Error: That specific room is already occupied!");
                    return;
                }
                
                // Process reservation
                room.setAvailability(false);
                Booking newBooking = new Booking(guestName, room);
                bookings.add(newBooking);
                System.out.printf("\nSuccess! Room %d has been securely reserved for %s.\n", roomNumber, guestName);
                return;
            }
        }
        System.out.println("System Error: Room number not recognized in our database.");
    }

    public void displayCurrentBookings() {
        System.out.println("\n=============================================");
        System.out.println("          ACTIVE RESERVATION LEDGER          ");
        System.out.println("=============================================");
        
        if (bookings.isEmpty()) {
            System.out.println("Ledger Status: No active reservations found.");
            return;
        }

        for (Booking booking : bookings) {
            booking.displayBookingDetails();
        }
    }
}
