package com.iro.labs.banking.processor;

import com.iro.labs.banking.model.ParseError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class CsvTransactionProcessor {
    private static final Logger log = LoggerFactory.getLogger(CsvTransactionProcessor.class);
    private final List<ParseError> errors = new ArrayList<>();
    private int processedCount = 0;
    
    public void processFile(String inputFilePath, String outputErrorFilePath) {
        Path inputPath = Paths.get(inputFilePath);
        
        if (!Files.exists(inputPath)) {
            log.error("Input file not found: {}", inputFilePath);
            return;
        }
        
        try (BufferedReader reader = Files.newBufferedReader(inputPath)) {
            String line;
            int lineNumber = 0;
            
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                processLine(line, lineNumber);
            }
            
            logSummary();
            writeErrorsToFile(outputErrorFilePath);
            
        } catch (IOException e) {
            log.error("Failed to process file: {}", inputFilePath, e);
        }
    }
    
    private void processLine(String line, int lineNumber) {
        String trimmedLine = line.trim();
        
        if (trimmedLine.isEmpty()) {
            errors.add(new ParseError(lineNumber, line, "Empty line"));
            return;
        }
        
        String[] parts = trimmedLine.split(",");
        
        if (parts.length != 3) {
            errors.add(new ParseError(lineNumber, line, "Invalid format. Expected 3 fields: accountId,amount,type"));
            return;
        }
        
        try {
            String accountId = parts[0].trim();
            double amount = Double.parseDouble(parts[1].trim());
            String type = parts[2].trim();
            
            if (accountId.isEmpty()) {
                errors.add(new ParseError(lineNumber, line, "Account ID cannot be empty"));
                return;
            }
            
            if (!type.equals("DEBIT") && !type.equals("CREDIT")) {
                errors.add(new ParseError(lineNumber, line, "Type must be DEBIT or CREDIT"));
                return;
            }
            
            processedCount++;
            log.debug("Processed line {}: account={}, amount={}, type={}", 
                      lineNumber, accountId, amount, type);
            
        } catch (NumberFormatException e) {
            errors.add(new ParseError(lineNumber, line, "Invalid amount format: " + parts[1]));
        }
    }
    
    private void logSummary() {
        log.info("Processed: {}, Failed: {}", processedCount, errors.size());
    }
    
    private void writeErrorsToFile(String outputFilePath) {
        if (errors.isEmpty()) {
            log.info("No errors to write");
            return;
        }
        
        Path outputPath = Paths.get(outputFilePath);
        
        try (BufferedWriter writer = Files.newBufferedWriter(outputPath)) {
            writer.write("Line Number,Error Message,Raw Line");
            writer.newLine();
            
            for (ParseError error : errors) {
                writer.write(String.format("%d,\"%s\",\"%s\"", 
                           error.getLineNumber(), 
                           error.getErrorMessage(),
                           error.getRawLine()));
                writer.newLine();
            }
            
            log.info("Wrote {} errors to: {}", errors.size(), outputFilePath);
            
        } catch (IOException e) {
            log.error("Failed to write errors to file: {}", outputFilePath, e);
        }
    }
}
