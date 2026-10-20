package com.nmims.lms.daos.ica;

import com.nmims.lms.beans.Ica.IcaTotalMarks;
import com.nmims.lms.daos.BaseDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository("icaTotalMarksDAO")
public class IcaTotalMarksDAO extends BaseDAO<IcaTotalMarks> {


    @Override
    protected String getTableName() {
       return  "ica_total_marks";
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

    public List<IcaTotalMarks> getIcaQueryRaisedStudents(Long icaId) {

        String sql = "select itm.username,itm.isQueryApproved,itm.remarks from ica_total_marks itm , ica_queries iq where itm.icaId=iq.icaId "
                + "and itm.icaId=? and itm.isQueryRaise='Y' and iq.isApproved='Y' and (iq.isReEvaluated <> 'Y' or iq.isReEvaluated is null)";
        return findAllSQL(sql, new Object[] { icaId });
    }

    public int getFinalSubmittedStudentCount(Long icaId) {
        String sql = "SELECT COUNT(*) FROM " +getTableName() + " WHERE icaId = ? AND finalSubmit = 'Y';";
        return getJdbcTemplate().queryForObject(sql, Integer.class, icaId);
    }

    public int getBatchWiseStudentCount(Long icaId) {
        try {

            String sql = "SELECT COUNT(*) FROM ica_student_batchwise WHERE icaId = ?";
            return getJdbcTemplate().queryForObject(sql, Integer.class, icaId);
        } catch (Exception e) {
            return 0;
        }
    }


    public int getTotalMarksSubmittedCount(Long icaId) {
        try {

            String sql = "SELECT COUNT(*) FROM "+ getTableName()+ " WHERE icaId = ? and finalSubmit='Y' ";
            return getJdbcTemplate().queryForObject(sql, Integer.class, icaId);
        } catch (Exception e) {
            return 0;
        }
    }

    public List<IcaTotalMarks> getStudentsForReevaluation(Long icaId) {

        String sql = "SELECT itm.username,itm.isQueryApproved,itm.remarks from ica_total_marks itm , ica_queries iq where itm.icaId=iq.icaId "
                + "and itm.icaId=? and itm.isQueryRaise='Y' and iq.isApproved='Y' and (iq.isReEvaluated <> 'Y' or iq.isReEvaluated is null)";
        return findAllSQL(sql, new Object[] { icaId });
    }


}
