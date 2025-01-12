package com.GitPlay.GitPlay.Service;

import org.springframework.stereotype.Service;

import java.io.*;

@Service
public class CompilerService {

    public String executeCode(String language, String code) throws Exception {
        String command="";
        String filename = "";
        String tempDir = System.getProperty("java.io.tmpdir");
        switch (language) {
            case "python":
                command=python_compiler(filename,tempDir,command,code);
                break;
            case "javascript":
                command = "node -e \"" + code.replace("\"", "\\\"") + "\"";
                break;
            case "java":
                // Save code to a temporary file and compile it
                command = "java -cp /tmp/ YourClassName";
                break;
            case "cpp":
                // Save code to a temporary file and compile it
                command = "g++ /tmp/temp.cpp -o /tmp/temp && /tmp/temp";
                break;
            default:
                throw new IllegalArgumentException("Unsupported language: " + language);
        }
        Process process = Runtime.getRuntime().exec(command);
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        StringBuilder output = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            output.append(line).append("\n");
        }

        // Capture errors (stderr)
        BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        StringBuilder errorOutput = new StringBuilder();
        String errorLine;
        while ((errorLine = errorReader.readLine()) != null) {
            errorOutput.append(errorLine).append("\n");
        }

        // Wait for the process to finish
        int exitCode = process.waitFor();
        if (exitCode != 0) {
            // If there's an error in execution, throw an exception
            throw new RuntimeException("Execution failed. Error: " + errorOutput.toString());
        }

        // If there is error output, throw it as an exception
        if (errorOutput.length() > 0) {
            throw new RuntimeException("Execution error: " + errorOutput.toString());
        }

        return output.toString();
    }

    private String python_compiler(String filename,String tempDir,String command,String code) throws IOException {
        filename = tempDir + "temp_script.py";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write(code);
        }
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            command = "python " + filename;
        } else {
            command = "python3 " + filename;
        }
        return command;
    }



}
