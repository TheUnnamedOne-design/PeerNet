import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class CSVWriter100x100 {
    public static void main(String[] args) {
        String fileName = "matrix_mult_2.csv";
        Random random = new Random();
        int n=10;
        try (FileWriter writer = new FileWriter(fileName)) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    int value = random.nextInt(n) + 1; // Generates number from 1 to 100
                    writer.append(String.valueOf(value));
                    if (j < n-1) {
                        writer.append(",");
                    }
                }
                writer.append("\n");
            }
            System.out.println("CSV file with random values created: " + fileName);
        } catch (IOException e) {
            System.out.println("An error occurred while writing the file.");
            e.printStackTrace();
        }
    }
}
