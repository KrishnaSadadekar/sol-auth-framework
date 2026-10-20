package com.nmims.lms.dto.Ica;

import lombok.Data;

import java.util.List;
import java.util.Objects;

@Data
public class IcaComponentDto  {

    private Long id;

    private String componentName;

    private Double marks;

    private Long icaId;

    private Long icaComponentId;
    private Integer sequenceNo;

    private List<IcaCloPloMappingDto> icaCloPloMappingDto;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IcaComponentDto that = (IcaComponentDto) o;
        return Objects.equals(id, that.id) ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, marks);
    }
}
