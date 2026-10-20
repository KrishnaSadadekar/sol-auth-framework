package com.nmims.lms.beans.Course;

import com.nmims.lms.beans.BaseBean;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "module")
public class Module extends BaseBean {

    @Column(name = "module_id")
    private Integer moduleId;

    @Column(name = "acadyear")
    private Integer acadYear;

    @Column(name = "start_date")
    private Integer startDate;

    @Column(name = "end_date")
    private Integer endDate;

    @Column(name = "sem_total", length = 50)
    private String semTotal;

    @Column(name = "oral_total", length = 50)
    private String oralTotal;

    @Column(name = "prac_total", length = 50)
    private String pracTotal;

    @Column(name = "internal_total", length = 50)
    private String internalTotal;

    @Column(name = "oral_prac_total", length = 50)
    private String oralPracTotal;

    @Column(name = "term_work_total", length = 50)
    private String termWorkTotal;

    @Column(name = "ica_weightage", length = 50)
    private String icaWeightage;

    @Column(name = "term_end_weightage", length = 50)
    private String termEndWeightage;

    @Column(name = "module_credit", length = 50)
    private String moduleCredit;

    @Column(name = "module_object_id", length = 50)
    private String moduleObjectId;

    @Column(name = "module_abbr", length = 50)
    private String moduleAbbr;

    @Column(name = "module_description", length = 2500)
    private String moduleDescription;

    @Column(name = "session_code", length = 50)
    private String sessionCode;

    @Column(name = "program_id")
    private Integer programId;

    @Column(name = "module_descipline_code", length = 1500)
    private String moduleDisciplineCode;

    @Column(name = "module_descipline_name", length = 1500)
    private String moduleDisciplineName;

    @Column(name = "module_category_code", length = 1500)
    private String moduleCategoryCode;

    @Column(name = "module_category_name", length = 1500)
    private String moduleCategoryName;


}
