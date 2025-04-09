package edu.sugang.controller;

import edu.sugang.dto.CourseDTO;
import edu.sugang.dto.StudentStatusDTO;
import edu.sugang.service.MyPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mypage")
public class MyPageController {

    @Autowired
    private MyPageService myPageService;

    @GetMapping("/{studentId}")
    public List<StudentStatusDTO> getStudentStatusByStudentId(@PathVariable Integer studentId, @RequestParam Integer departmentId) {
        return myPageService.getStudentStatusByStudentId(studentId, departmentId);
    }

    @GetMapping("/enrollment")
    public List<CourseDTO> getCourseBySemester(@RequestParam Integer semester) {
        return myPageService.getCourseBySemester(semester);
    }
}
