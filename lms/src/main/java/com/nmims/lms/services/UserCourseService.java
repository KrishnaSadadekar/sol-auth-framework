package com.nmims.lms.services;

import com.nmims.lms.beans.Course.UserCourse;
import com.nmims.lms.daos.BaseDAO;
import com.nmims.lms.daos.Course.UserCourseDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserCourseService  extends  BaseService<UserCourse>{
    private final UserCourseDAO userCourseDAO;

    @Override
    protected BaseDAO<UserCourse> getDAO() {
        return userCourseDAO;
    }

    public List<UserCourse> findAllFaculties() {
        return  userCourseDAO.findAllFaculties();
    }

    public List<UserCourse> findFacultiesBySubjectAndAcademicYear(Long academicYear, Long moduleId) {
        return userCourseDAO.findFacultiesBySubjectAndAcademicYear(academicYear, moduleId);
    }
}
