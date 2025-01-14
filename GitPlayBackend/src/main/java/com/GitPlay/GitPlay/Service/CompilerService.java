package com.GitPlay.GitPlay.Service;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

@Service
public class CompilerService {

    public String executeCode(String language, String code) throws Exception {
        String filename = "";
        String containerName = "multi-lang-container"; // Container name defined in docker-compose.yml
        String command;

        // Create a temp file in the host system
        File tempFile = File.createTempFile("temp_script", getFileExtension(language));
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            writer.write(code);
        }

        // Copy the file into the Docker container
        executeCommand("docker cp " + tempFile.getAbsolutePath() + " " + containerName + ":/workspace/" + tempFile.getName());

        // Determine the execution command based on the language
        switch (language) {
            case "python":
                command = "python3 /workspace/" + tempFile.getName();
                break;
            case "javascript":
                command = "node /workspace/" + tempFile.getName();
                break;
            case "java":
                command = "javac /workspace/" + tempFile.getName() + " && java -cp /workspace/ " + tempFile.getName().replace(".java", "");
                break;
            case "cpp":
                command = "g++ /workspace/" + tempFile.getName() + " -o /workspace/temp && /workspace/temp";
                break;
            default:
                throw new IllegalArgumentException("Unsupported language: " + language);
        }

        // Execute the command inside the container
        String output = executeCommand("docker exec " + containerName + " bash -c \"" + command + "\"");

        // Delete the temporary file after execution
        Files.deleteIfExists(Paths.get(tempFile.getAbsolutePath()));

        return output;
    }

    private String executeCommand(String command) throws IOException, InterruptedException {
        Process process = new ProcessBuilder(command.split(" ")).start();
        StringBuilder output = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
        }

        // Wait for the process to complete
        if (!process.waitFor(30, TimeUnit.SECONDS)) {
            process.destroy();
            throw new RuntimeException("Command timeout");
        }

        if (process.exitValue() != 0) {
            throw new RuntimeException("Command failed: " + output.toString());
        }

        return output.toString();
    }

    private String getFileExtension(String language) {
        switch (language.toLowerCase()) {
            case "python":
                return ".py";
            case "javascript":
                return ".js";
            case "java":
                return ".java";
            case "cpp":
                return ".cpp";
            default:
                throw new IllegalArgumentException("Unsupported language: " + language);
        }
    }
}
