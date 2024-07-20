package org.example.studentmanagement.Dao;

import org.example.studentmanagement.dto.StudentDTO;

import java.sql.Connection;
import java.sql.SQLException;

public interface Data {
    StudentDTO getStudent(String studentId, Connection connection) throws SQLException;
    String saveStudent(StudentDTO studentDTO,Connection connection) throws SQLException;
    boolean deleteStudent(String studentId,Connection connection) throws SQLException;
    boolean updateStudent(String studentId,StudentDTO student,Connection connection) throws SQLException;
}
