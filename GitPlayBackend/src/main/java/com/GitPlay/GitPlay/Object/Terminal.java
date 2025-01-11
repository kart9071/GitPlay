package com.GitPlay.GitPlay.Object;

import org.springframework.stereotype.Component;

import java.io.*;

@Component
public class Terminal implements TerminalInterface {

    @Override
    public void createTerminal() {
        System.out.println("No implementation needed for createTerminal in this approach.");
    }


    @Override
    public void executeSingleCommand(String command) {
        Process process = null;
        try {
            String os = System.getProperty("os.name").toLowerCase();
            String[] finalCommand;
            if (os.contains("win")) {
                finalCommand = new String[]{"cmd.exe", "/c", command};
            } else {
                finalCommand = new String[]{"bash", "-c", command};
            }

            process = Runtime.getRuntime().exec(finalCommand);

            // Create threads to consume stdout and stderr
            Process finalProcess = process;
            Thread outputReader = new Thread(() -> {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(finalProcess.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.out.println("Command Output: " + line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            Process finalProcess1 = process;
            Thread errorReader = new Thread(() -> {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(finalProcess1.getErrorStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.err.println("Command Error: " + line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            outputReader.start();
            errorReader.start();

            // Wait for the process to finish
            int exitCode = process.waitFor();
            outputReader.join();
            errorReader.join();

            System.out.println("Command executed with exit code: " + exitCode);

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Failed to execute command: " + command, e);
        } finally {
            if (process != null) {
                process.destroy();
            }
        }
    }
}
