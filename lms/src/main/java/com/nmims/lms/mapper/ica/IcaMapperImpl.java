package com.nmims.lms.mapper.ica;

import com.nmims.lms.beans.Ica.Ica;
import com.nmims.lms.daos.ica.IcaDAO;
import com.nmims.lms.dto.Ica.IcaCloPloMappingDto;
import com.nmims.lms.dto.Ica.IcaComponentDto;
import com.nmims.lms.dto.Ica.IcaRequest;
import com.nmims.lms.utilities.Utils;


import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class IcaMapperImpl implements IcaMapper {
    @Override
    public IcaRequest toDto(Ica entity) {
        if(entity==null)return  null;
        IcaRequest icaRequest =new IcaRequest();
        icaRequest.setIcaName(entity.getIcaName());
        icaRequest.setDivision(entity.getDivision());
        icaRequest.setAcadSession(entity.getAcadSession());
        icaRequest.setAcadYear(entity.getAcadYear());
        icaRequest.setDescription(entity.getIcaDesc());
        icaRequest.setInternalMarks(entity.getInternalMarks());
        icaRequest.setInternalPassMarks(entity.getInternalPassMarks());
        icaRequest.setStartDate(entity.getStartDate());
        icaRequest.setEndDate(entity.getEndDate());
        icaRequest.setIsSubmitted(entity.getIsSubmitted());
        icaRequest.setIsIcaDivisionWise(entity.getIsIcaDivisionWise());
        icaRequest.setModuleId(entity.getModuleId());
        icaRequest.setAssignedFaculty(entity.getAssignedFaculty());
        return  icaRequest;
    }

    @Override
    public Ica toEntity(IcaRequest request) {
        if(request==null) return null;
        Ica ica = new Ica();
        ica.setModuleId(request.getModuleId());
        ica.setAcadYear(request.getAcadYear());
        ica.setCampusId(request.getCampusId());
        ica.setIcaDesc(request.getDescription());
        ica.setIcaName(request.getIcaName());
        ica.setDivision(request.getDivision());
        ica.setEventId(request.getEventId());
        ica.setProgramId(request.getProgramId());
        ica.setStartDate(request.getStartDate());
        ica.setEndDate(request.getEndDate());
        ica.setInternalMarks(request.getInternalMarks());
        ica.setInternalPassMarks(request.getInternalPassMarks());
        ica.setExternalMarks(request.getExternalMarks());
        ica.setExternalPassMarks(request.getExternalPassingMarks());
        ica.setIsSubmitted(request.getIsSubmitted());
        ica.setIsPublishCompWise(request.getIsPublishedCompWise());
        ica.setIsCourseraIca(request.getIsCourseraIca());
        ica.setIsIcaDivisionWise(request.getIsIcaDivisionWise());
        ica.setIsNonEventModule(request.getIsNoEventModule());
        ica.setAcadSession(request.getAcadSession());
        ica.setAssignedFaculty(request.getAssignedFaculty());

        return  ica;
    }

    public IcaRequest toIcaRequest(List<Map<String, Object>> rows) {

        if (rows == null || rows.isEmpty()) {
            return null;
        }

        IcaRequest icaRequest = new IcaRequest();
        System.out.println("Mapping ICA from rows: " + rows); // Debug log to check the incoming data
        // Keep components unique
        Map<Long, IcaComponentDto> componentMap = new LinkedHashMap<>();

        for (Map<String, Object> row : rows) {

            /* ---------------- ICA ROOT (ONCE) ---------------- */
            if (icaRequest.getIcaId() == null) {
                mapIcaRoot(row, icaRequest);
            }

            /* ---------------- COMPONENT ---------------- */
            Long componentId = Utils.getLong(row, "componentId");
            if (componentId == null) continue;

            IcaComponentDto component =
                    componentMap.computeIfAbsent(componentId, id -> {
                        IcaComponentDto dto = new IcaComponentDto();
                        dto.setId(id);
                        dto.setComponentName( Utils.getString(row, "componentName"));
                        dto.setMarks(Utils.getDouble(row, "componentMarks"));
                        dto.setIcaCloPloMappingDto(new ArrayList<>());
                        return dto;
                    });

            /* ---------------- CLO–PLO MAPPING ---------------- */
            if (row.get("cloId") != null) {
                IcaCloPloMappingDto mapping = new IcaCloPloMappingDto();
                mapping.setCloId(Utils.getLong(row, "cloId"));
                mapping.setCloName(Utils.getString(row, "cloName"));
                mapping.setPloId(Utils.getLong(row, "ploId"));
                mapping.setPloName(Utils.getString(row, "ploName"));
                mapping.setMarks(Utils.getDouble(row, "cloPloMarks"));

                component.getIcaCloPloMappingDto().add(mapping);
            }
        }

        icaRequest.setIcaComponentList(
                new ArrayList<>(componentMap.values())
        );

        return icaRequest;
    }

    private void mapIcaRoot(Map<String, Object> entity, IcaRequest icaRequest) {

       System.out.println("Mapping ICA Root: " + entity); // Debug log to check the incoming data
        icaRequest.setIcaName(Utils.getString(entity, "icaName"));
        icaRequest.setDivision(Utils.getString(entity, "division"));
        icaRequest.setAcadSession(Utils.getString(entity, "acadSession"));
        icaRequest.setAcadYear(Utils.getInteger(entity, "acadYear"));
        icaRequest.setDescription(Utils.getString(entity, "icaDesc"));
        icaRequest.setInternalMarks(Utils.getLong(entity, "internalMarks"));
        icaRequest.setInternalPassMarks(Utils.getLong(entity, "internalPassMarks"));
        icaRequest.setStartDate(Utils.getDate(entity, "startDate"));
        icaRequest.setEndDate(Utils.getDate(entity, "endDate"));
        icaRequest.setIsSubmitted(Utils.getString(entity, "isSubmitted"));
        icaRequest.setIsIcaDivisionWise(Utils.getString(entity, "isIcaDivisionWise"));
        icaRequest.setModuleId(Utils.getString(entity, "moduleId"));
        icaRequest.setIcaId(Utils.getLong(entity, "id"));
        icaRequest.setIsPublishedCompWise(Utils.getString(entity,"isPublishCompWise"));
        icaRequest.setAssignedFaculty(Utils.getString(entity, "facultyName"));

    }

}
