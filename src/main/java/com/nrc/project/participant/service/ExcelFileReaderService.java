package com.nrc.project.participant.service;

import com.nrc.project.participant.constants.AppConstants;
import com.nrc.project.participant.model.ProjectParticipant;
import com.nrc.project.participant.helper.ApplicationHelper;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ExcelFileReaderService {
    private static final Logger logger = LoggerFactory.getLogger(ExcelFileReaderService.class);

    private final RestTemplate restTemplate;
    private final ApplicationHelper applicationHelper;

    @Autowired
    public ExcelFileReaderService(RestTemplate restTemplate, ApplicationHelper applicationHelper) {
        this.restTemplate = restTemplate;
        this.applicationHelper = applicationHelper;
    }

    public List<ProjectParticipant> readAndFormatExcel(String filePath) throws IOException {
        logger.info("Reading input excel file .....!!!!");

        List<ProjectParticipant> projectParticipantList = new ArrayList<>();
        try(FileInputStream excelFile = new FileInputStream(filePath);
            Workbook workbook = new XSSFWorkbook(excelFile)){
            for(int sheetIndex =0; sheetIndex< workbook.getNumberOfSheets(); sheetIndex++) {
                Sheet sheet = workbook.getSheetAt(sheetIndex);
                int startRow = 1;

                for (int rowIndex = startRow; rowIndex < sheet.getPhysicalNumberOfRows(); rowIndex++) {
                    logger.info("Reading row : {}", rowIndex);
                    Row row = sheet.getRow(rowIndex);
                    if (rowHasContent(row)) {
                        ProjectParticipant projectParticipant = new ProjectParticipant();
                        projectParticipant.setName(row.getCell(0).getStringCellValue());

                        Cell dobCell = row.getCell(1);
                        DataFormatter dataFormatter = new DataFormatter();
                        // checking the cell type
                        if (dobCell.getCellType().equals(CellType.NUMERIC)) {
                            cellStyling(workbook, dobCell);
                            projectParticipant.setDate_of_birth(applicationHelper.ISO8601DateFormattingDDMMYYYY(dataFormatter.formatCellValue(dobCell)));
                           /* if (DateUtil.isCellDateFormatted(dobCell)) {
                                projectParticipant.setDate_of_birth(applicationHelper.ISO8601DateFormattingDDMMYYYY(dataFormatter.formatCellValue(dobCell)));
                            } else {
                                projectParticipant.setDate_of_birth(String.valueOf(dobCell.getNumericCellValue()));
                            }*/
                        } else if (dobCell.getCellType().equals(CellType.STRING)) {
                            String updatedCellDate = formatDate(dobCell);
                            projectParticipant.setDate_of_birth(applicationHelper.ISO8601DateFormattingMMDDYYYY(updatedCellDate));
                        }

                        projectParticipant.setAddress(row.getCell(2).getStringCellValue());
                        projectParticipant.setPhone_number(row.getCell(3).getStringCellValue());
                        projectParticipantList.add(projectParticipant);
                    }
                }
            }
        }
        for(ProjectParticipant projectParticipant : projectParticipantList) {
            logger.info("Participant detail :{}", projectParticipant.toString());
        }
        return projectParticipantList;
    }

    public ResponseEntity<String> makeAPICall(List<ProjectParticipant> projectParticipantList) {
        // Set the request header api-key , including the content type
        HttpHeaders headers = new HttpHeaders();
        headers.add("api-key", AppConstants.API_KEY_HEADER);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<List<ProjectParticipant>> requestEntity = new HttpEntity<>(projectParticipantList, headers);
        logger.info("Make Rest API call....!!!");

        ResponseEntity<String> response = restTemplate.postForEntity(AppConstants.API_URL+AppConstants.API_END_POINT, requestEntity, String.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            logger.info("Request was successful");
        } else {
            logger.error("Request failed with status code: " + response.getStatusCodeValue());
        }
        return response;
    }

    private String formatDate(Cell dobCell) {
        // date formats observed in the data sheet
        String[] dateFormats = {AppConstants.DATE_FORMAT_1, AppConstants.DATE_FORMAT_2};
        Date cellDate= null;
        SimpleDateFormat outputFormat = new SimpleDateFormat(AppConstants.DATE_FORMAT_3);
        for(String format : dateFormats) {
            try {
                SimpleDateFormat inputFormat = new SimpleDateFormat(format);
                cellDate = inputFormat.parse(dobCell.getStringCellValue());
                break;
            } catch (ParseException e) {
               logger.error("Parsing failed for current format :"+ format+" , trying the next one");
            }
        }
        return outputFormat.format(cellDate);
    }

    private void cellStyling(Workbook workbook, Cell dobCell) {
        CellStyle cellStyle = workbook.createCellStyle();
        CreationHelper createHelper = workbook.getCreationHelper();
        // to preserve the leading zeros in the date format
        cellStyle.setDataFormat(createHelper.createDataFormat().getFormat(AppConstants.DATE_FORMAT_4));
        dobCell.setCellStyle(cellStyle);
    }

    private boolean rowHasContent(Row row) {
        if(null != row) {
            for (Cell cell : row) {
                if (cell.getCellType() != CellType.BLANK) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }
}
