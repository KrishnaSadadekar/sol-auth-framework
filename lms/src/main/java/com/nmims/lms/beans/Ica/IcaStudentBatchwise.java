package com.nmims.lms.beans.Ica;

import com.nmims.lms.beans.BaseBean;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;

@Entity
@Data
@Table(name = "ica_student_batchwise")
public class IcaStudentBatchwise extends BaseBean {

    private Long icaId;
    private String facultyId;
    private String username;
    @Transient
    private String studentName;
    @Transient
    private String facultyName;
    @Transient
    private String rollNo;


}
