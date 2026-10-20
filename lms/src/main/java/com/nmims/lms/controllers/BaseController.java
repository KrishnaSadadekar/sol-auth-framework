package com.nmims.lms.controllers;

import com.nmims.lms.services.CampusConfigService;
import com.nmims.lms.services.Program.ProgramCampusService;
import com.nmims.lms.services.Program.ProgramService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public  abstract  class BaseController {

    private final CampusConfigService campusConfigService;
    private final ProgramService programService;
    private final ProgramCampusService programCampusService;


}
