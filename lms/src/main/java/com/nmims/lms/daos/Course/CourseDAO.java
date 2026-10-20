package com.nmims.lms.daos.Course;

import com.nmims.lms.beans.Course.Course;
import com.nmims.lms.daos.BaseDAO;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository("courseDAO")
public class CourseDAO extends BaseDAO<Course> {
    @Override
    protected String getTableName() {
        return "course";
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

    public List<Course> findSubjectByAcademicYearAndCampus(Long academicYear, Long campusId) {
        String sql = " SELECT CONCAT(m.module_description, ' (', m.module_abbr, ')') AS moduleName  , m.module_id AS moduleId FROM module m " +
                " INNER JOIN program_campus pc ON pc.programId=m.program_id WHERE  m.acadyear= ? AND pc.campusId= ? ";

        return findAllSQL(sql, new Object[]{academicYear, campusId});
    }


    public List<Course> findSubjectByAcademicSessionAndAcademicYearAndCampus(
            String academicSession, Long academicYear, Long campusId) {

        StringBuilder sql = new StringBuilder();
        List<Object> params = new ArrayList<>();

        sql.append(
                " SELECT DISTINCT " +
                        "   m.module_id AS moduleId, " +
                        "   m.module_description AS moduleName, " +
                        "   m.module_abbr AS moduleAbbr, " +
                        "   sm.sapSessionText AS acadSession, " +
                        "   p.programName "
        );

        if (campusId != null) {
            sql.append(", pc.campusName ");
        }

        sql.append(
                " FROM module m " +
                        " JOIN session_master sm ON m.session_code = sm.sapSessionCode " +
                        " JOIN program p ON m.program_id = p.id "
        );

        if (campusId != null) {
            sql.append(" JOIN program_campus pc ON p.id = pc.programId ");
        }

        sql.append(
                " WHERE sm.sapSessionText = ? " +
                        "   AND m.acadYear = ? " +
                        "   AND sm.active = 'Y' "
        );

        params.add(academicSession);
        params.add(academicYear);

        if (campusId != null) {
            sql.append(" AND pc.campusId = ? ");
            params.add(campusId);
        }


        return findAllSQL(sql.toString(), params.toArray());
    }

    public List<String> findAcademicSessionsByAcademicYearAndCampus(
            Long academicYear, Long campusId) {

        String sql =
                " SELECT DISTINCT acadSession " +
                        " FROM " + getTableName() +
                        " WHERE acadYearCode = ? " +
                        "   AND active = 'Y' " +
                        "   AND ( ? IS NULL OR campusId = ? )";

        return listOfStringParameter(
                sql,
                new Object[]{ academicYear, campusId, campusId }
        );
    }

    public List<String> findAllAcademicYears() {

        String sql =
                " SELECT DISTINCT acadYearCode FROM course ";

       return listOfStringParameter(sql, new Object[] {});
    }

}
