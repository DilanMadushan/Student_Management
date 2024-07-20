package org.example.studentmanagement.Controller;

import jakarta.json.*;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebInitParam;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.studentmanagement.Dao.Data;
import org.example.studentmanagement.Dao.impl.StudentStudentDataProcess;
import org.example.studentmanagement.dto.StudentDTO;

//import javax.json.bind.Jsonb;
//import javax.json.bind.JsonbBuilder;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@WebServlet(urlPatterns = "/student",
        initParams = {
                @WebInitParam(name = "driver-class",value = "com.mysql.cj.jdbc.Driver"),
                @WebInitParam(name = "dbURL",value = "jdbc:mysql://localhost:3306/aad67JavaEE?createDatabaseIfNotExist=true"),
                @WebInitParam(name = "dbUserName",value = "root"),
                @WebInitParam(name = "dbPassword",value = "Dilan123!"),
        }
)
public class StudentController extends HttpServlet {

    Connection connection;
    Data data = new StudentStudentDataProcess();



    @Override
    public void init() throws ServletException {

        System.out.println("Called");

        try {
            var driverClass = getServletContext().getInitParameter("driver-class");
            var dbUrl = getServletContext().getInitParameter("dbURL");
            var userName = getServletContext().getInitParameter("dbUserName");
            var password = getServletContext().getInitParameter("dbPassword");
            Class.forName(driverClass);

            this.connection = DriverManager.getConnection(dbUrl,userName,password);

        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }



    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        if (!req.getContentType().toLowerCase().endsWith("application/json") || req.getContentType() == null){

            resp.sendError(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);

        }


        Jsonb jsonb = JsonbBuilder.create();
        StudentDTO dto = jsonb.fromJson(req.getReader(), StudentDTO.class);
        System.out.println(dto.getCity());

        String id = UUID.randomUUID().toString();
        dto.setId(id);

        try(PrintWriter printWriter = resp.getWriter()) {


            String save = data.saveStudent(dto,connection);

            printWriter.println(save);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        if (!req.getContentType().toLowerCase().endsWith("application/json") || req.getContentType() == null){

            resp.sendError(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);

        }

        var StudentID = req.getParameter("stu_id");

        try {

            Jsonb jsonb = JsonbBuilder.create();
            var updateStu = jsonb.fromJson(req.getReader(), StudentDTO.class);

            boolean isUpdated = data.updateStudent(StudentID,updateStu,connection);

            if (isUpdated) {
                resp.setStatus(HttpServletResponse.SC_CREATED);
                resp.getWriter().write("Updated");
            }else{
                resp.getWriter().write("Update Failed");
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }




    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String id = req.getParameter("stu_id");

        try {
            boolean isDelete = data.deleteStudent(id,connection);

            if (isDelete) {
                resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
            }else{
                resp.getWriter().write("Delete Failed");
            }

        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            throw new RuntimeException(e);
        }

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        var studentId = req.getParameter("id");

        try (var writer = resp.getWriter()){


            StudentDTO dto = data.getStudent(studentId,connection);

            var jsonb = JsonbBuilder.create();
            jsonb.toJson(dto,writer);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


