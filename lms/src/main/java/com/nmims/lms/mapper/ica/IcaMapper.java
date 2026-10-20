package com.nmims.lms.mapper.ica;

import com.nmims.lms.beans.Ica.Ica;
import com.nmims.lms.dto.Ica.IcaRequest;
import com.nmims.lms.mapper.BaseMapper;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.Map;

@Mapper(componentModel = "spring")
public interface IcaMapper extends BaseMapper<Ica, IcaRequest> {
   IcaRequest toIcaRequest(List<Map<String, Object>> rows);

}
