package com.nmims.lms.beans.Program;

import com.nmims.lms.beans.BaseBean;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "program")
public class Program extends BaseBean {

    private String programName;
    private String abbr;
    private String durationInMonths;
    private String maxDurationInMonths;
    private String sessionType;
    private String revisedFromMonth;
    private String revisedFromYear;
    private String acadMonth;
    private String acadYear;

    @OneToMany(mappedBy = "program")
    private List<ProgramCampus> programCampus;
}
