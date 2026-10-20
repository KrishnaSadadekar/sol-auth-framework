package com.nmims.lms.beans.Ica;

import com.nmims.lms.beans.BaseBean;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Data
@Table(name = "ica_clo_plo_master")
public class IcaCloPloMaster extends BaseBean {

    @NotBlank(message = "Name cannot be blank")
    @Column(nullable = false)
    private String name;

    private String type;          // optional, can be null

    @Column(name = "parent_id")
    private Long parentId;

    private String description;

}

