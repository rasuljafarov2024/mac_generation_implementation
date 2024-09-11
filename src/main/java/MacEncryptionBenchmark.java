import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import javax.swing.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MacEncryptionBenchmark extends JFrame {

    public MacEncryptionBenchmark(String title) {
        super(title);

        DefaultCategoryDataset dataset = createDataset();

        JFreeChart chart = ChartFactory.createLineChart(
                "MAC Encryption Benchmark",
                "Text Size",
                "Processing Time (ns)",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false);

        ChartPanel panel = new ChartPanel(chart);
        setContentPane(panel);
    }

    private DefaultCategoryDataset createDataset() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        List<Integer> textSizes = List.of(40_000, 50_000, 60_000, 70_000, 100_000, 1000_000);
        List<String> randomStrings = new ArrayList<>();
        List<Long> processingTimes = new ArrayList<>();

        // ########## Warm-up phase #########
        for (Integer size : textSizes) {
            String randomString = generateRandomString(size);
            generateMac(randomString);
            randomStrings.add(randomString);
        }

        // ########## Testing phase #########
        for (String randomString : randomStrings) {
            long startTime = System.nanoTime();
            String result = generateMac(randomString);
            long endTime = System.nanoTime();
            long processingTime = (endTime - startTime) / 1000000;
            processingTimes.add(processingTime);
            System.out.printf("result: %s, Processing Time: %s ns\n", result, processingTime);
        }

        // Add data to dataset
        for (int i = 0; i < textSizes.size(); i++) {
            dataset.addValue(processingTimes.get(i), "Processing Time", textSizes.get(i));
        }

        return dataset;
    }

    private static String generateMac(String text) {
        String hexKey = "0XA0799CCC0C5BC09879A1BFCF2C99555D912A718A5A";
        byte[] keyBytes = MacEncryption.hexStringToByteArray(hexKey);

        byte[] plaintextBytes = text.getBytes(StandardCharsets.UTF_8);

        byte[] macResult = MacEncryption.generateRetailMac(keyBytes, plaintextBytes);
        byte[] truncatedResult = Arrays.copyOf(macResult, 4);
        return MacEncryption.bytesToHex(truncatedResult);
    }

    private static String generateRandomString(int length) {
        String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

        StringBuilder randomString = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int randomIndex = (int) (Math.random() * characters.length());
            randomString.append(characters.charAt(randomIndex));
        }
        return randomString.toString();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MacEncryptionBenchmark example = new MacEncryptionBenchmark("MAC Encryption Benchmark");
            example.setSize(800, 600);
            example.setLocationRelativeTo(null);
            example.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            example.setVisible(true);
        });
    }
}
