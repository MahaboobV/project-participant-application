package com.nrc.project.participant.component;

import com.nrc.project.participant.model.ProjectParticipant;
import com.nrc.project.participant.service.ExcelFileReaderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import java.util.List;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ExcelFileProcessorTest {

    @Mock
    private ExcelFileReaderService excelFileReaderServiceMock;

    @InjectMocks
    private ExcelFileProcessor excelFileProcessor;

    private List<ProjectParticipant> projectParticipantList;

    @BeforeEach
    void setUp() {
        ProjectParticipant projectParticipant = new ProjectParticipant();
        projectParticipant.setName("Test name");
        projectParticipant.setAddress("Test Address");
        projectParticipant.setDate_of_birth("01-01-2023");
        projectParticipant.setPhone_number("12345678910");
        projectParticipantList = List.of(projectParticipant);
    }

    @Test
    void testRun() throws Exception{

        ResponseEntity<String> response= ResponseEntity.ok("Success");

        when(excelFileReaderServiceMock.readAndFormatExcel(anyString())).thenReturn(projectParticipantList);
        when(excelFileReaderServiceMock.makeAPICall(anyList())).thenReturn(response);

        String[] testArgs = new String[]{"arg1"};
        excelFileProcessor.run(testArgs);

        verify(excelFileReaderServiceMock).readAndFormatExcel(testArgs[0]);
        verify(excelFileReaderServiceMock).makeAPICall(projectParticipantList);
    }
}
