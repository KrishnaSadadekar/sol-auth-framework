package com.nmims.lms.daos;

import com.nmims.lms.beans.Program.Program;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository("programDAO")
public class ProgramDAO extends  BaseDAO<Program> {


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

    public List<Program> findProgramsByAcademicSessionAndAcademicYearAndSubject(
            String academicSession, Long academicYear, Long moduleId) {

        Map<String, Object> params = new HashMap<>();
        params.put("acadSession", academicSession);
        params.put("acadYear", academicYear);
        params.put("moduleId", moduleId);

        String sql =
                " SELECT DISTINCT p.* " +
                        " FROM program p " +
                        " JOIN course c ON p.id = c.programId " +
                        " WHERE c.acadSession = :acadSession " +
                        "   AND c.acadYearCode = :acadYear " +
                        "   AND c.moduleId = :moduleId ";

        return getNamedParameterJdbcTemplate()
                .query(sql, params, BeanPropertyRowMapper.newInstance(Program.class));
    }
}
