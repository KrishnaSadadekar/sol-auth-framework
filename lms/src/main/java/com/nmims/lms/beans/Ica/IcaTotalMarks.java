package com.nmims.lms.beans.Ica;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "ica_total_marks")
public class IcaTotalMarks  extends BaseIcaMarks{

    Double icaScaledMarks;
    @Transient
    private List<IcaComponentMarks> icaComponentMarks;

}
