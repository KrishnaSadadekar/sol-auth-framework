package com.nmims.lms.beans.Program;

import com.nmims.lms.beans.BaseBean;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "program_campus")
public class ProgramCampus extends BaseBean {

    @ManyToOne
    @JoinColumn(name = "programId")
    private Program program;
    private  String campusId;
    private  String campusAbbr;
    private  String campusName;
}
