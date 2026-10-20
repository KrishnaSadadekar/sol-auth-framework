package com.nmims.lms.services.Ica;


import com.nmims.lms.beans.Ica.PredefinedIcaComponent;
import com.nmims.lms.daos.BaseDAO;
import com.nmims.lms.daos.ica.PredefinedIcaComponentDAO;
import com.nmims.lms.services.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("predefinedIcaComponentService")
public class PredefinedIcaComponentService  extends BaseService<PredefinedIcaComponent> {

    @Autowired
    PredefinedIcaComponentDAO predefinedIcaComponentDAO;
    @Override
    protected BaseDAO<PredefinedIcaComponent> getDAO() {
        return predefinedIcaComponentDAO ;
    }





}
