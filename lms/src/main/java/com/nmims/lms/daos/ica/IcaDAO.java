package com.nmims.lms.daos.ica;

import com.nmims.lms.beans.Ica.Ica;
import com.nmims.lms.daos.BaseDAO;
import com.nmims.lms.dto.Ica.IcaRequest;
import com.nmims.lms.mapper.ica.IcaMapper;
import lombok.RequiredArgsConstructor;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Map;
@Repository("icaDAO")
@RequiredArgsConstructor
public class IcaDAO  extends BaseDAO<Ica> {


    private  final  JdbcTemplate jdbcTemplate;
    private  final IcaMapper mapper;
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



    public IcaRequest findByIcaWithComponents(Long id) {
        String sql = " SELECT "
                + "     i.*, CONCAT(u.firstname,' ',u.lastname ) as facultyName , "
                + "     pd.id AS preDefId, pd.componentName, "
                + "     ic.componentId AS componentId, ic.marks AS componentMarks, "
                + "     clo.id AS cloId, clo.name AS cloName, "
                + "     plo.id AS ploId, plo.name AS ploName, "
                + "     icm.marks AS cloPloMarks "
                + " FROM ica i "
                + " LEFT JOIN ica_components ic ON i.id = ic.icaId "
                + " LEFT JOIN pre_def_ica_components pd ON pd.id = ic.componentId "
                + " LEFT JOIN ica_clo_plo_mapping icm ON ic.componentId = icm.componentId AND icm.icaId = i.id "
                + " LEFT JOIN ica_clo_plo_master clo ON clo.id = icm.cloId "
                + " LEFT JOIN ica_clo_plo_master plo ON plo.id = icm.ploId "
                + " INNER JOIN users u ON u.username=i.assignedFaculty "
                + " WHERE i.active = 'Y' AND i.id = ? ";

        // 1. Get the flat result list from the database
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, id);
        System.out.println("Raw query result: " + rows); // Debug log to check the raw data from DB
        if (rows.isEmpty()) return null;
        return mapper.toIcaRequest(rows);
    }

}
