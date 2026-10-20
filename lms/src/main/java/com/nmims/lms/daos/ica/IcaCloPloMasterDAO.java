package com.nmims.lms.daos.ica;

import com.nmims.lms.beans.Ica.IcaCloPloMaster;
import com.nmims.lms.daos.BaseDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository("icaCloPloMasterDAO")
@RequiredArgsConstructor
public class IcaCloPloMasterDAO extends BaseDAO<IcaCloPloMaster> {
    @Override
    protected String getTableName() {
        return "clo_plo_master";
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


    List<IcaCloPloMaster> getAllPloList()
    {
        String sq="SELECT * from clo_plo_master where parent_id is null and type='PLO' ";
        return  findAllSQL(sq,new Object[]{});
    }
}
