package com.nmims.lms.services.Ica;

import com.nmims.lms.beans.Ica.IcaTotalMarks;
import com.nmims.lms.daos.BaseDAO;
import com.nmims.lms.daos.ica.IcaTotalMarksDAO;
import com.nmims.lms.repositories.IcaTotalMarksRepository;
import com.nmims.lms.services.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service("icaTotalMarksService")
public class IcaTotalMarksService  extends BaseService<IcaTotalMarks> {

    @Autowired
    IcaTotalMarksDAO icaTotalMarksDAO;
    @Autowired
    private  IcaTotalMarksRepository icaTotalMarksRepository;
    @Override
    protected BaseDAO<IcaTotalMarks> getDAO() {
        return icaTotalMarksDAO;
    }

    public List<IcaTotalMarks> getIcaQueryRaisedStudents(Long icaId) {
        return icaTotalMarksDAO.getIcaQueryRaisedStudents(icaId);
    }

    public int getBatchWiseStudentCount(Long icaId) {
        return  icaTotalMarksDAO.getBatchWiseStudentCount(icaId);
    }

    public int getFinalSubmittedStudentCount(Long icaId) {
        return icaTotalMarksDAO.getFinalSubmittedStudentCount(icaId);
    }

    public int getTotalMarksSubmittedCount(Long icaId) {
        return icaTotalMarksDAO.getTotalMarksSubmittedCount(icaId);
    }

    public List<IcaTotalMarks> getStudentsForReevaluation(Long icaId)
    {
        return icaTotalMarksDAO.getStudentsForReevaluation(icaId);
    }

    public IcaTotalMarks save( IcaTotalMarks total)
    {
        return  icaTotalMarksRepository.save(total);
    }
}
