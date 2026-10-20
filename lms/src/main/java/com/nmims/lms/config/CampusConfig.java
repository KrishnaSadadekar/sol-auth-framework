package com.nmims.lms.config;

import com.nmims.lms.beans.BaseBean;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Data
@Table(name = "campus_config")
public class CampusConfig extends BaseBean {
    @NotBlank(message = "Campus name cannot be blank")
    @Column(name = "campus_code",length = 100,nullable = false)
    private String campusCode;

    @NotBlank(message = "School name cannot be blank")
    @Column(name = "school" ,length = 100 ,nullable = false)
    private String school;


    @Column(name = "support_clo_plo",columnDefinition = "ENUM('Y','N') DEFAULT 'N' ")
    private String supportsCloPlo;
    @Column(name = "special_strategy")
    private String specialStrategy;


}
