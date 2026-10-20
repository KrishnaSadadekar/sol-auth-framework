package com.nmims.lms.daos;

import com.nmims.lms.beans.Program.ProgramCampus;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository("programCampusDAO")
public class ProgramCampusDAO  extends BaseDAO<ProgramCampus>{
    @Override
    protected String getTableName() {
        return "program_campus";
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

    public List<ProgramCampus> findAllCampuses()
    {
        String sql ="SELECT distinct campusName, campusId ,campusAbbr  FROM program_campus";
        return  findAllSQL(sql,new Object[]{});
    }
}
