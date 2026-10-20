package com.nmims.lms.daos.ica;

import com.nmims.lms.beans.Ica.PredefinedIcaComponent;
import com.nmims.lms.daos.BaseDAO;
import org.springframework.stereotype.Repository;

@Repository("predefinedIcaComponentDAO")
public class PredefinedIcaComponentDAO extends BaseDAO<PredefinedIcaComponent> {
    @Override
    protected String getTableName() {
        return null;
    }

    @Override
    protected String getInsertSql() {
        return "INSERT INTO pre_def_ica_components(componentName,active) values (:componentName,:active) ";
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
