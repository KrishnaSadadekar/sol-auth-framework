package com.nmims.lms.services;

import com.nmims.lms.beans.Course.Course;
import com.nmims.lms.daos.BaseDAO;
import com.nmims.lms.daos.Course.CourseDAO;
import com.nmims.lms.repositories.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseService extends BaseService<Course> {

    private final CourseRepository repository;
    private final CourseDAO courseDAO;

    @Override
    protected BaseDAO getDAO() {
        return courseDAO;
    }

    public List<String> findAcademicSessionsByAcademicYearAndCampus(Long academicYear,
                                                                    Long campusId) {
        return courseDAO.findAcademicSessionsByAcademicYearAndCampus(academicYear, campusId);
    }

    public List<Course> findSubjectByAcademicYearAndCampus(Long academicYear, Long campusId) {
        return courseDAO.findSubjectByAcademicYearAndCampus(academicYear, campusId);
    }

    public List<Course> findSubjectByAcademicSessionAndAcademicYearAndCampus(String academicSession, Long academicYear, Long campusId) {
        return courseDAO.findSubjectByAcademicSessionAndAcademicYearAndCampus(academicSession, academicYear, campusId);
    }

    public List<String> findAllAcademicYears() {
        return courseDAO.findAllAcademicYears();
    }
}
