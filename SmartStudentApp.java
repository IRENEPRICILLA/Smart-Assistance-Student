import java.sql.*;
import java.util.Scanner;

public class SmartStudentApp {

    // MySQL Connection
    static String url = "jdbc:mysql://localhost:3306/";
    static String user = "root";
    static String password = ""; // default empty for XAMPP
    static Scanner sc = new Scanner(System.in);

    // Predict performance based on marks
    public static String performanceCategory(int marks) {
        if (marks >= 90) return "Excellent";
        else if (marks >= 75) return "Very Good";
        else if (marks >= 50) return "Average";
        else return "Needs Improvement";
    }

    // Create Database if not exists
    public static void createDatabase(Connection con) throws SQLException {
        Statement st = con.createStatement();
        st.executeUpdate("CREATE DATABASE IF NOT EXISTS smart_student_db");
        System.out.println("✔ Database checked/created.");
    }

    // Create Table if not exists
    public static void createTable(Connection con) throws SQLException {
        Statement st = con.createStatement();
        st.executeUpdate(
            "CREATE TABLE IF NOT EXISTS smart_student_db.students (" +
            "id INT AUTO_INCREMENT PRIMARY KEY," +
            "name VARCHAR(100)," +
            "marks INT," +
            "performance VARCHAR(50)" +
            ")"
        );
        System.out.println("✔ Table checked/created.");
    }

    // Add student
    public static void addStudent(Connection con) throws SQLException {
        System.out.print("Enter student name: ");
        String name = sc.nextLine();

        System.out.print("Enter marks (0-100): ");
        int marks = sc.nextInt();
        sc.nextLine();

        String performance = performanceCategory(marks);

        PreparedStatement ps = con.prepareStatement(
            "INSERT INTO smart_student_db.students (name, marks, performance) VALUES (?, ?, ?)"
        );
        ps.setString(1, name);
        ps.setInt(2, marks);
        ps.setString(3, performance);
        ps.executeUpdate();

        System.out.println("\n✔ Student added successfully!");
        System.out.println("🎯 Predicted Performance: " + performance + "\n");
    }

    // View all students
    public static void viewStudents(Connection con) throws SQLException {
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery("SELECT * FROM smart_student_db.students");

        System.out.println("\n------ Student Records ------");
        while (rs.next()) {
            System.out.println(
                "ID: " + rs.getInt("id") +
                ", Name: " + rs.getString("name") +
                ", Marks: " + rs.getInt("marks") +
                ", Performance: " + rs.getString("performance")
            );
        }
        System.out.println("------------------------------\n");
    }

    // Smart motivational message
    public static void smartMessage() {
        System.out.println("\n✨ SMART TIP OF THE DAY ✨");
        String tips[] = {
            "Believe in yourself and your abilities!",
            "Every day is a new chance to improve!",
            "Hard work always beats talent!",
            "Stay consistent, success will follow!",
            "Small progress is still progress!"
        };
        int rand = (int) (Math.random() * tips.length);
        System.out.println("💡 " + tips[rand] + "\n");
    }

    public static void main(String[] args) {
        try {
            // Connect without specifying DB first (to create DB if needed)
            Connection con = DriverManager.getConnection(url, user, password);
            createDatabase(con);
            createTable(con);

            while (true) {
                System.out.println("====== SMART STUDENT ASSISTANT ======");
                System.out.println("1. Add Student");
                System.out.println("2. View All Students");
                System.out.println("3. Smart Motivational Message");
                System.out.println("4. Exit");
                System.out.print("Choose option: ");

                int choice = sc.nextInt();
                sc.nextLine();

                switch (choice) {
                    case 1: addStudent(con); break;
                    case 2: viewStudents(con); break;
                    case 3: smartMessage(); break;
                    case 4:
                        System.out.println("Goodbye! 👋");
                        con.close();
                        return;
                    default:
                        System.out.println("❌ Invalid choice!\n");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
