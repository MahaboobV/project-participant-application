package com.nrc.project.participant.component;

import com.nrc.project.participant.model.ProjectParticipant;
import com.nrc.project.participant.service.ExcelFileReaderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class ExcelFileProcessor implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(ExcelFileProcessor.class);

    private final ExcelFileReaderService excelFileReaderService;

    @Autowired
    public ExcelFileProcessor(ExcelFileReaderService excelFileReaderService) {
        this.excelFileReaderService = excelFileReaderService;
    }

    @Override
    public void run(String... args) throws Exception {
        if (args.length < 1) {
            logger.error("Please provide the path to the Excel file.");
            System.exit(1);
        }
        String filePath = args[0];
        try {
            List<ProjectParticipant> projectParticipantList = excelFileReaderService.readAndFormatExcel(filePath);
            ResponseEntity<String> response = excelFileReaderService.makeAPICall(projectParticipantList);
            logger.info(response.toString());
        }catch (IOException e) {
            logger.error("Error reading the Excel file: " + e.getMessage());
            System.exit(1);
        }
    }
}
