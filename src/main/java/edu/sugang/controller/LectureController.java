package edu.sugang.controller;

import edu.sugang.dto.LectureDTO;
import edu.sugang.service.LectureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/lectures")
public class LectureController {
    @Autowired
    private LectureService lectureService;

    @GetMapping("/{subjectId}/{targetGrade}")
    public List<LectureDTO> getLecturesBySubjectIdAndTargetGrade(@PathVariable Integer subjectId, @PathVariable String targetGrade) {
        return lectureService.getLecturesBySubjectIdAndTargetGrade(subjectId, targetGrade);
    }
}
