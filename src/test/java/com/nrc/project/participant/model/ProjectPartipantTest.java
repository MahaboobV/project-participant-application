package com.nrc.project.participant.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProjectPartipantTest {

    @Test
    public void testGetterAndSetter() {
      ProjectParticipant projectParticipant= new ProjectParticipant();
      projectParticipant.setName("John");
      projectParticipant.setDate_of_birth("01-01-2023");
      projectParticipant.setAddress("John Villa");
      projectParticipant.setPhone_number("+47 12345678");
      assertEquals("John", projectParticipant.getName());
      assertEquals("01-01-2023", projectParticipant.getDate_of_birth());
      assertEquals("John Villa", projectParticipant.getAddress());
      assertEquals("+47 12345678", projectParticipant.getPhone_number());
    }

    @Test
    public void testConstructor() {
        ProjectParticipant projectParticipant = new ProjectParticipant("John", "01-01-2023", "John Villa", "+47 12345678");
        assertEquals("John", projectParticipant.getName());
        assertEquals("01-01-2023", projectParticipant.getDate_of_birth());
    }

}
