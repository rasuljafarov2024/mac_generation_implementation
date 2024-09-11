**MAC Encryption and Benchmarking Tool**

This repository contains two Java classes:

* **MacEncryption:** This class implements a Retail MAC (Message Authentication Code) generation algorithm using the DES encryption standard. It takes a secret key in hexadecimal format and a plaintext message as input, and generates a truncated MAC value.

* **MacEncryptionBenchmark:** This class is a Swing application that benchmarks the performance of the MacEncryption class for different message sizes. It generates random strings of varying lengths, calculates the MAC for each string, and measures the processing time. The results are then visualized in a line chart.

**How to Use**

1. **MacEncryption:**
   * Clone the repository:
   ```bash
   git clone <repository_url>
   cd <repository_folder>
   ```
   * Compile the Java program:
   ```bash
   javac MacEncryption.java
   ```
   * Run the program

   ```bash
    java MacEncryption
   ```

2. **MacEncryptionBenchmark:**
   * Compile the code using Maven:
     ```bash
     mvn compile
     ```
   * Run the benchmark application:
     ```bash
     mvn exec:java -Dexec.mainClass="MacEncryptionBenchmark"
     ```
   * This will launch a window displaying a line chart that shows the processing time for different message sizes.

**Dependencies**

This project uses the following external library:

* **JFreeChart:** A Java charting library used for visualization in the benchmark application.

The dependency is included in the pom.xml file.
