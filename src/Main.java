import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    static class Room {
        int roomNumber;
        String roomType;
        double price;
        boolean isOccupied;

        public Room(int roomNumber, String roomType, double price) {
            this.roomNumber = roomNumber;
            this.roomType = roomType;
            this.price = price;
            this.isOccupied = false;
        }
    }

    static class Reservation {
        int reservationNumber;
        Room room;
        String guestName;
        String checkInDate;
        String checkOutDate;

        public Reservation(int reservationNumber, Room room, String guestName, String checkInDate, String checkOutDate) {
            this.reservationNumber = reservationNumber;
            this.room = room;
            this.guestName = guestName;
            this.checkInDate = checkInDate;
            this.checkOutDate = checkOutDate;
        }
    }
    public static void main(String[] args) {
        Logger logger = new Logger();
        List<Room> rooms = loadRoomsFromFile(logger);
        List<Reservation> reservations = loadReservationsFromFile(rooms, logger);
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Hotel Management System");
            System.out.println("1. Manage Rooms");
            System.out.println("2. Manage Reservations");
            System.out.println("3. Check-Out");
            System.out.println("4. List Available Rooms");
            System.out.println("5. Save & Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    manageRooms(rooms, scanner, logger);
                    break;
                case 2:
                    manageReservations(rooms, reservations, scanner, logger);
                    break;
                case 3:
                    performCheckOut(reservations, rooms, logger, scanner);
                    break;
                case 4:
                    listAvailableRooms(rooms, logger);
                    break;
                case 5:
                    saveRoomsToFile(rooms, logger);
                    saveReservationsToFile(reservations, logger);
                    logger.Info("Data saved. Goodbye!");
                    System.exit(0);
                default:
                    logger.Error("Invalid choice. Please try again.");
            }
        }
    }

    public static void listAvailableRooms(List<Room> rooms, Logger logger) {
        logger.Info("Available Rooms:");
        for (Room room : rooms) {
            if (!room.isOccupied) {
                System.out.println("Room Number: " + room.roomNumber);
                System.out.println("Room Type: " + room.roomType);
                System.out.println("Price: " + room.price);
                System.out.println("Occupied: No");
                System.out.println();
            }
        }
    }

    public static void performCheckOut(List<Reservation> reservations, List<Room> rooms, Logger logger, Scanner scanner) {
        logger.Info("Check-Out");
        logger.Info("Enter the reservation number for check-out: ");
        int reservationNumber = scanner.nextInt();

        Reservation reservation = findReservationByNumber(reservations, reservationNumber);

        if (reservation != null) {
            Room room = reservation.room;
            room.isOccupied = false; // Mark the room as unoccupied
            reservations.remove(reservation); // Remove the reservation

            logger.Info("Check-out completed. Room #" + room.roomNumber + " is now available.");
        } else {
            logger.Error("Reservation not found. Please enter a valid reservation number.");
        }
    }

    public static Reservation findReservationByNumber(List<Reservation> reservations, int reservationNumber) {
        for (Reservation reservation : reservations) {
            if (reservation.reservationNumber == reservationNumber) {
                return reservation;
            }
        }
        return null; // Return null if no matching reservation is found
    }

    public static void manageRooms(List<Room> rooms, Scanner scanner, Logger logger) {
        while (true) {
            System.out.println("Room Management");
            System.out.println("1. Add Room");
            System.out.println("2. List Rooms");
            System.out.println("3. Back to Main Menu");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Enter room number: ");
                    int roomNumber = scanner.nextInt();
                    System.out.print("Enter room type: ");
                    String roomType = scanner.next();
                    System.out.print("Enter room price: ");
                    double roomPrice = scanner.nextDouble();

                    Room room = new Room(roomNumber, roomType, roomPrice);
                    rooms.add(room);
                    logger.Info("Room added successfully.");
                    break;
                case 2:
                    System.out.println("List of Rooms:");
                    for (Room r : rooms) {
                        System.out.println("Room Number: " + r.roomNumber);
                        System.out.println("Room Type: " + r.roomType);
                        System.out.println("Price: " + r.price);
                        System.out.println("Occupied: " + (r.isOccupied ? "Yes" : "No"));
                    }
                    break;
                case 3:
                    return;
                default:
                    logger.Error("Invalid choice. Please try again.");
            }
        }
    }

    public static void manageReservations(List<Room> rooms, List<Reservation> reservations, Scanner scanner, Logger logger) {
        while (true) {
            System.out.println("Reservation Management");
            System.out.println("1. Make Reservation");
            System.out.println("2. List Reservations");
            System.out.println("3. Back to Main Menu");
            System.out.println("Enter your choice: ");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Enter guest name: ");
                    String guestName = scanner.next();
                    System.out.print("Enter room number: ");
                    int roomNumber = scanner.nextInt();
                    System.out.print("Enter check-in date: ");
                    String checkInDate = scanner.next();
                    System.out.print("Enter check-out date: ");
                    String checkOutDate = scanner.next();

                    Room room = findRoomByNumber(rooms, roomNumber);

                    if (room != null && !room.isOccupied) {
                        Reservation reservation = new Reservation(reservations.size() + 1, room, guestName, checkInDate, checkOutDate);
                        reservations.add(reservation);
                        room.isOccupied = true;
                        logger.Info("Reservation made successfully.");
                    } else {
                        logger.Error("Invalid room number or room is already occupied.");
                    }
                    break;
                case 2:
                    System.out.println("List of Reservations:");
                    for (Reservation r : reservations) {
                        System.out.println("Reservation Number: " + r.reservationNumber);
                        System.out.println("Guest Name: " + r.guestName);
                        System.out.println("Room Number: " + r.room.roomNumber);
                        System.out.println("Check-in Date: " + r.checkInDate);
                        System.out.println("Check-out Date: " + r.checkOutDate);
                    }
                    break;
                case 3:
                    return;
                default:
                    logger.Error("Invalid choice. Please try again.");
            }
        }
    }

    public static Room findRoomByNumber(List<Room> rooms, int roomNumber) {
        for (Room room : rooms) {
            if (room.roomNumber == roomNumber) {
                return room;
            }
        }
        return null;
    }

    public static List<Room> loadRoomsFromFile(Logger logger) {
        List<Room> rooms = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("rooms.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                int roomNumber = Integer.parseInt(parts[0]);
                String roomType = parts[1];
                double price = Double.parseDouble(parts[2]);
                Room room = new Room(roomNumber, roomType, price);
                room.isOccupied = Boolean.parseBoolean(parts[3]);
                rooms.add(room);
            }
        } catch (IOException e) {
            logger.Error("Error loading room data from file.");
        }
        return rooms;
    }

    public static void saveRoomsToFile(List<Room> rooms, Logger logger) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("rooms.txt"))) {
            for (Room room : rooms) {
                writer.write(room.roomNumber + "," + room.roomType + "," + room.price + "," + room.isOccupied);
                writer.newLine();
            }
        } catch (IOException e) {
            logger.Error("Error saving room data to file.");
        }
    }

    public static List<Reservation> loadReservationsFromFile(List<Room> rooms, Logger logger) {
        List<Reservation> reservations = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("reservations.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                int reservationNumber = Integer.parseInt(parts[0]);
                int roomNumber = Integer.parseInt(parts[1]);
                String guestName = parts[2];
                String checkInDate = parts[3];
                String checkOutDate = parts[4];

                Room room = findRoomByNumber(rooms, roomNumber);

                if (room != null) {
                    Reservation reservation = new Reservation(reservationNumber, room, guestName, checkInDate, checkOutDate);
                    reservations.add(reservation);
                    room.isOccupied = true;
                } else {
                    logger.Error("Invalid room number in reservation data: " + roomNumber);
                }
            }
        } catch (IOException e) {
            logger.Error("Error loading reservation data from file.");
        }
        return reservations;
    }

    public static void saveReservationsToFile(List<Reservation> reservations, Logger logger) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("reservations.txt"))) {
            for (Reservation reservation : reservations) {
                writer.write(reservation.reservationNumber + "," + reservation.room.roomNumber + "," + reservation.guestName
                        + "," + reservation.checkInDate + "," + reservation.checkOutDate);
                writer.newLine();
            }
        } catch (IOException e) {
            logger.Error("Error saving reservation data to file.");
        }
    }
}
