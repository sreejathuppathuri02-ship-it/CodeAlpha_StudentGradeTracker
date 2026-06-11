import java.util.ArrayList;
import java.util.Scanner;

public class StudentGradeTracker {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<Double> grades = new ArrayList<>();
        
        System.out.println("=== CodeAlpha Student Grade Tracker ===");
        System.out.println("Enter student grades one by one. Type -1 when you are finished:");
        
        // 1. Input loop for entering grades
        while (true) {
            System.out.print("Enter grade: ");
            if (scanner.hasNextDouble()) {
                double grade = scanner.nextDouble();
                
                // Exit condition
                if (grade == -1) {
                    break;
                }
                
                // Data validation
                if (grade < 0 || grade > 100) {
                    System.out.println("Invalid grade! Please enter a value between 0 and 100.");
                } else {
                    grades.add(grade);
                }
            } else {
                System.out.println("Invalid input! Please enter a valid number.");
                scanner.next(); // Clear the invalid input
            }
        }
        
        // 2. Calculations section
        if (grades.isEmpty()) {
            System.out.println("\nNo grades were entered. System exiting.");
        } else {
            double sum = 0;
            double highest = grades.get(0);
            double lowest = grades.get(0);
            
            for (double grade : grades) {
                sum += grade;
                if (grade > highest) {
                    highest = grade;
                }
                if (grade < lowest) {
                    lowest = grade;
                }
            }
            
            double average = sum / grades.size();
            
            // 3. Display summary report
            System.out.println("\n=================================");
            System.out.println("     STUDENT GRADE SUMMARY       ");
            System.out.println("=================================");
            System.out.println("Total Students Processed : " + grades.size());
            System.out.printf("Average Grade            : %.2f\n", average);
            System.out.println("Highest Grade            : " + highest);
            System.out.println("Lowest Grade             : " + lowest);
            System.out.println("=================================");
        }
        
        scanner.close();
    }
}
