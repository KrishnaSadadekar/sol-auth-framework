package com.nmims.lms.services;

import com.nmims.lms.campus.CampusStrategy;
import com.nmims.lms.campus.CampusStrategyFactory;
import com.nmims.lms.config.CampusConfig;
import com.nmims.lms.context.SchoolContext;
import com.nmims.lms.context.SchoolContextHolder;
import com.nmims.lms.daos.BaseDAO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
public abstract class BaseService<T> {

    protected abstract BaseDAO<T> getDAO();

    @Value("${school}")
    protected String school;

    @Value("${campusCode}")
    protected String campusCode;

    @Autowired
    protected CampusStrategyFactory strategyFactory;

    protected CampusStrategy strategy;
    protected CampusConfig config;

    @PersistenceContext
    protected EntityManager entityManager;

    /**
     * Resolves the effective SchoolContext: request-scoped first, property fallback second.
     */
    protected SchoolContext resolveSchoolContext() {
        SchoolContext ctx = SchoolContextHolder.get();
        if (ctx != null) {
            return ctx;
        }
        return SchoolContext.builder()
                .campusCode(campusCode)
                .school(school)
                .build();
    }

    public int insert(T bean) {
        return getDAO().insert(bean);
    }

    @Transactional
    public void init() {
        SchoolContext ctx = resolveSchoolContext();
        log.info("Inside BaseService.init() — campusCode: {}, school: {}", ctx.getCampusCode(), ctx.getSchool());
        strategy = strategyFactory.getStrategy(ctx.getCampusCode(), ctx.getSchool());
        config = strategyFactory.getConfig(ctx.getCampusCode(), ctx.getSchool());

        if (config != null) {
            log.info("Campus supports CLO/PLO: {}", config.getSupportsCloPlo());
        }
    }
}