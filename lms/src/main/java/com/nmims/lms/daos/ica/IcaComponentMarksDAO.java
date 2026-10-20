package com.nmims.lms.daos.ica;

import com.nmims.lms.beans.Ica.IcaComponentMarks;
import com.nmims.lms.daos.BaseDAO;

public class IcaComponentMarksDAO extends BaseDAO<IcaComponentMarks> {
    @Override
    protected String getTableName() {
        return "ica_component_marks";
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



}
