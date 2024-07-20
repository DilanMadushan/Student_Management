package org.example.studentmanagement.Dao.impl;

import org.example.studentmanagement.Dao.Data;
import org.example.studentmanagement.dto.StudentDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class StudentStudentDataProcess implements Data {

    public static String saveStu = "INSERT INTO student VALUES(?,?,?,?,?)";
    public static String updateStu = "UPDATE student SET name = ?, email = ?, city = ?, level = ? WHERE id = ?";
    static String GET_STUDENT = "SELECT * FROM student WHERE id=?";
    static String deleteStu = "DELETE FROM Student WHERE id = ?";

    @Override
    public StudentDTO getStudent(String studentId, Connection connection) throws SQLException {
        StudentDTO studentDTO = new StudentDTO();


        var ps = connection.prepareStatement(GET_STUDENT);
        ps.setString(1, studentId);
        var resultSet = ps.executeQuery();
        while (resultSet.next()){
            studentDTO.setId(resultSet.getString("id"));
            studentDTO.setName(resultSet.getString("name"));
            studentDTO.setCity(resultSet.getString("city"));
            studentDTO.setEmail(resultSet.getString("email"));
            studentDTO.setLevel(resultSet.getString("level"));
        }
        return studentDTO;
    }

    @Override
    public String saveStudent(StudentDTO studentDTO, Connection connection) throws SQLException {
        PreparedStatement pstm = connection.prepareStatement(saveStu);
        pstm.setString(1,studentDTO.getId());
        pstm.setString(2,studentDTO.getName());
        pstm.setString(3,studentDTO.getEmail());
        pstm.setString(4,studentDTO.getCity());
        pstm.setString(5,studentDTO.getLevel());

        if (pstm.executeUpdate() !=0) {
            return "Saved";
        }else {
            return "SAve Failed";
        }
    }

    @Override
    public boolean deleteStudent(String studentId, Connection connection) throws SQLException {
        PreparedStatement pstm = connection.prepareStatement(deleteStu);
        pstm.setString(1,studentId);

        if (pstm.executeUpdate() !=0) {
            return true;
        }else {
            return false;
        }

    }

    @Override
    public boolean updateStudent(String studentId, StudentDTO student, Connection connection) throws SQLException {
        var psvm = connection.prepareStatement(updateStu);

        psvm.setString(1,student.getName());
        psvm.setString(2,student.getEmail());
        psvm.setString(3,student.getCity());
        psvm.setString(4,student.getLevel());
        psvm.setString(5,studentId);

        if (psvm.executeUpdate() !=0) {
            return true;
        }else {
            return false;
        }


    }
}
