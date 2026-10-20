package com.nmims.lms.daos.ica;

import com.nmims.lms.beans.Ica.IcaComponent;
import com.nmims.lms.daos.BaseDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("icaComponentDAO")
@RequiredArgsConstructor
public class IcaComponentDAO extends BaseDAO<IcaComponent> {



    @Override
    protected String getTableName() {
        return "ica_components";
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


    public List<IcaComponent> findAllComponents() {
        String sql ="SELECT id ,componentName from pre_def_ica_components where active='Y' ";
        return findAllSQL(sql,new Object[]{});
    }

}
