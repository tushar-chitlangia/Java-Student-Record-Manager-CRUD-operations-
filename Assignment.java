import java.io.*;
import java.util.*;

public class Assignment {
    private static final String CSV_FILE = "Students.csv";

    public static void main(String[] args) {
        try {
            System.out.println("--- Current File Content (Initial) ---");
            displayFile();

            // 1. Add 3 more rows with marks4 and marks5 as zero initially
            appendNewStudents();
            System.out.println("\n--- Added 3 New Rows (CRUD: Create) ---");
            displayFile();

            // 2. Update all rows with correct marks and calculate percentage
            updateMarksAndCalculate();
            System.out.println("\n--- Updated Marks and Percentages (CRUD: Update) ---");
            displayFile();

            // 3. Delete a row from the file
            deleteStudent("102");
            System.out.println("\n--- Deleted Student ID 102 (CRUD: Delete) ---");
            displayFile();

            // 4. Show exception condition in output
            System.out.println("\n--- Testing Exception Handling ---");
            triggerIOException();

        } 
        catch (IOException e) {
            System.out.println("Handled Exception: " + e.getMessage());
        }
    }

    private static void appendNewStudents() throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CSV_FILE, true))) {
            bw.write("104,Alice,CS,78,82,88,0,0,0\n");
            bw.write("105,Bob,IT,65,70,72,0,0,0\n");
            bw.write("106,Charlie,EC,90,85,92,0,0,0\n");
        }
    }

    private static void updateMarksAndCalculate() throws IOException {
        List<String> updatedLines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE))) {
            String header = br.readLine();
            updatedLines.add(header);
            
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                
                // Update marks4 and marks5 if they are zero
                if (data[6].equals("0")) data[6] = "75";
                if (data[7].equals("0")) data[7] = "80";

                // Calculate percentage
                double total = 0;
                for (int i = 3; i <= 7; i++) {
                    total += Double.parseDouble(data[i]);
                }
                double percentage = total / 5.0;
                data[8] = String.format("%.2f", percentage);
                
                updatedLines.add(String.join(",", data));
            }
        }
        rewriteFile(updatedLines);
    }

    private static void deleteStudent(String studentId) throws IOException {
        List<String> remainingLines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.startsWith(studentId + ",")) {
                    remainingLines.add(line);
                }
            }
        }
        rewriteFile(remainingLines);
    }

    private static void rewriteFile(List<String> lines) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CSV_FILE))) {
            for (String l : lines) {
                bw.write(l + "\n");
            }
        }
    }

    private static void displayFile() throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        }
    }

    private static void triggerIOException() throws IOException {
        // Purposely looking for a file that doesn't exist
        new FileReader("invalid_file_path.csv").read();
    }
}
