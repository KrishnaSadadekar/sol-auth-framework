package com.nmims.lms.campus;

import com.nmims.lms.config.CampusConfig;
import com.nmims.lms.dto.Ica.IcaComponentDto;

import java.util.List;

public interface CampusStrategy {
    String supportsCloPlo();
    List<IcaComponentDto> processComponents(Long icaId, CampusConfig config);



}
