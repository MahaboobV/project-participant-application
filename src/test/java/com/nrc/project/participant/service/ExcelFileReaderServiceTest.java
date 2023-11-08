package com.nrc.project.participant.service;

import com.nrc.project.participant.constants.AppConstants;
import com.nrc.project.participant.helper.ApplicationHelper;
import com.nrc.project.participant.model.ProjectParticipant;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.eq;

@ExtendWith(MockitoExtension.class)
public class ExcelFileReaderServiceTest {

    @Mock
    private RestTemplate restTemplateMock;

    @Mock
    private ApplicationHelper applicationHelperMock;

    ExcelFileReaderService excelFileReaderService;

    List<ProjectParticipant> projectParticipantList;

    Path tempFilePath;

    @BeforeEach
    public void setUp() {
        excelFileReaderService = new ExcelFileReaderService(restTemplateMock, applicationHelperMock);
        createTempExcelFile();
        createProjectParticipants();
    }

    @Test
    public void testReadAndFormatExcel() throws Exception {

        when(applicationHelperMock.ISO8601DateFormattingMMDDYYYY(anyString())).thenReturn("2023-11-08");

        List<ProjectParticipant> resultList = excelFileReaderService.readAndFormatExcel(tempFilePath.toString());

        assertEquals(projectParticipantList.size(), resultList.size());
        assertEquals(projectParticipantList.get(0).getName(), resultList.get(0).getName());
    }

    @Test
    public void testMakeAPICall() {
        ResponseEntity<String> expectedResponse = ResponseEntity.ok("Mocked Response");
        doReturn(expectedResponse).when(restTemplateMock).postForEntity(any(String.class), any(), any());

        HttpHeaders headers = new HttpHeaders();
        headers.add("api-key", AppConstants.API_KEY_HEADER);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<List<ProjectParticipant>> requestEntity = new HttpEntity<>(projectParticipantList, headers);

        ResponseEntity<String> response = excelFileReaderService.makeAPICall(projectParticipantList);

        assertEquals(expectedResponse.getBody(), response.getBody());
        assertEquals(expectedResponse.getStatusCode(), response.getStatusCode());
        verify(restTemplateMock).postForEntity(eq(AppConstants.API_URL), eq(requestEntity), eq(String.class));

    }

    private void createTempExcelFile() {
        try {
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("SampleSheet");

            Row headerRow = sheet.createRow(0);
            Cell headerCell1 = headerRow.createCell(0);
            headerCell1.setCellValue("name");
            Cell headerCell2 = headerRow.createCell(1);
            headerCell2.setCellValue("date_of_birth");
            Cell headerCell3 = headerRow.createCell(2);
            headerCell3.setCellValue("address");
            Cell headerCell4 = headerRow.createCell(3);
            headerCell4.setCellValue("phone_number");

            Row valueRow = sheet.createRow(1);
            Cell valueCell1 = valueRow.createCell(0);
            valueCell1.setCellValue("Test name");
            Cell valueCell2 = valueRow.createCell(1);
            valueCell2.setCellValue("01/01/2023");
            Cell valueCell3 = valueRow.createCell(2);
            valueCell3.setCellValue("Test address");
            Cell valueCell4 = valueRow.createCell(3);
            valueCell4.setCellValue("12345678910");

            // Create a temporary file to store the Excel data
            Path tempDir = Files.createTempDirectory("test_excel");

            tempFilePath = tempDir.resolve("sample_excel.xlsx");
            FileOutputStream outputStream = new FileOutputStream(tempFilePath.toFile());
            workbook.write(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createProjectParticipants() {
        ProjectParticipant projectParticipant = new ProjectParticipant();
        projectParticipant.setName("Test name");
        projectParticipant.setDate_of_birth("01/01/2023");
        projectParticipant.setAddress("Test address");
        projectParticipant.setPhone_number("12345678910");
        projectParticipantList = new ArrayList<>();
        projectParticipantList.add(projectParticipant);
    }
}
