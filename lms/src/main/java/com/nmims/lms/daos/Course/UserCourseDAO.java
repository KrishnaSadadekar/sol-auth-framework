package com.nmims.lms.daos.Course;

import com.nmims.lms.beans.Course.UserCourse;
import com.nmims.lms.daos.BaseDAO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("UserCourseDAO")
public class UserCourseDAO  extends BaseDAO<UserCourse> {
    @Override
    protected String getTableName() {
        return null;
    }

    @Override
    protected String getInsertSql() {
        return null;
    }

    @Override
    protected String getUpdateSql() {
        return null;
    }

    @Override
    protected String getUpsertSql() {
        return null;
    }

    public List<UserCourse> findAllFaculties() {
        String sql ="SELECT * FROM usercourse uc ";
        return  findAllSQL(sql ,new Object[]{});
    }

    public List<UserCourse> findFacultiesBySubjectAndAcademicYear(
            Long academicYear, Long moduleId) {

        String sql =
                " SELECT DISTINCT " +
                        "   uc.username, " +
                        "   CONCAT(u.firstName, ' ', u.lastName) AS facultyName " +
                        " FROM user_course uc " +
                        " JOIN course c ON uc.courseId = c.id " +
                        " JOIN users u ON u.username = uc.username " +
                        " WHERE uc.role = 'ROLE_FACULTY' " +
                        "   AND uc.active = 'Y' " +
                        "   AND c.active = 'Y' " +
                        "   AND c.moduleId = ? " +
                        "   AND c.acadYearCode = ?";

        return findAllSQL(
                sql,
                new Object[]{
                        moduleId,
                       academicYear
                }
        );
    }
}
