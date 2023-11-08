package com.nrc.project.participant.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProjectParticipant {
    String name;
    String date_of_birth;
    String address;
    String phone_number;
}
