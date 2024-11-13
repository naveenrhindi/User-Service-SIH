package com.security.controller;

import com.security.model.Student;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class StudentController {

    List<Student> list = new ArrayList<>(
            List.of(
                    new Student(1,"Naveen",98),
                    new Student(2,"Mark",89),
                    new Student(3,"Allan",67)
            )
    );

    @GetMapping("/student")
    public List<Student> getList() {
        return list;
    }

    @PostMapping("/add")
    public Student register(@RequestBody Student student) {
        list.add(student);
        return student;
    }

    @GetMapping("/csrf")
    public CsrfToken getCsrfToken(HttpServletRequest request) {
        return (CsrfToken) request.getAttribute("_csrf");
    }

}
