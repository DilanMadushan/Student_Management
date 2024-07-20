package org.example.studentmanagement.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StudentDTO implements Serializable {
    private String id;
    private String name;
    private String email;
    private String city;
    private String level;

}
