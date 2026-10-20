package com.nmims.lms.beans.Course;

import com.nmims.lms.beans.BaseBean;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "course")
public class Course extends BaseBean {

    private String abbr;
    private String courseName;

    private String property1;
    private String property2;
    private String property3;

    private Integer programId;
    private Short acadYear;
    private String acadMonth;
    private String acadYearCode;

    private String eventName;
    private String acadSession;

    private String moduleId;
    private String moduleName;

    private String campusId;

    private String moduleAbbr;

    private String deptCode;
    private String dept;

    private String moduleCategoryCode;
    private String moduleCategoryName;


}

