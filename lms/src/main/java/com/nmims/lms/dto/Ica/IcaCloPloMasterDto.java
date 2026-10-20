package com.nmims.lms.dto.Ica;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class IcaCloPloMasterDto {
    @NotBlank(message = "Name cannot be blank")
    private String name;

    private String type;

    private Long parentId;

    private String description;

    private  Long id;

    private String active;

}
