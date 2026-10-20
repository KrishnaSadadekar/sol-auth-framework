package com.nmims.lms.daos;

import com.nmims.lms.Exceptions.CustomValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.GenericTypeResolver;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import javax.sql.DataSource;
import java.util.List;

public abstract class BaseDAO<T > extends NamedParameterJdbcDaoSupport {

    protected Class<T> genericType;


    @SuppressWarnings("unchecked")
    public BaseDAO() {
        super();
        // Get the generic type class
        this.genericType = (Class<T>) GenericTypeResolver.resolveTypeArgument(
                getClass(), BaseDAO.class);
    }


    public void deleteSoftById(String id) {
    }

    protected abstract String getTableName();

    protected abstract String getInsertSql();

    protected abstract String getUpdateSql();

    protected abstract String getUpsertSql();


    public int insert(final T bean) {

        return executeSQL(bean, getInsertSql());
    }

    @Autowired
    public void setDS(DataSource dataSource) {
        setDataSource(dataSource);
    }


    private int executeSQL(final T bean, final String sql) {
        try
        {
            SqlParameterSource parameterSource = getParameterSource(bean);
            return getNamedParameterJdbcTemplate().update(sql,
                    parameterSource);

        }catch (Exception ex)
        {
            throw new  CustomValidationException(ex.getMessage(),HttpStatus.BAD_REQUEST);
        }


    }

    protected BeanPropertySqlParameterSource getParameterSource(T bean) {
        return new BeanPropertySqlParameterSource(
                bean);
    }

    public List<T> findAllSQL(final String sql, final Object[] parameters) {
        if (null == parameters) {
            return getJdbcTemplate().query(sql,
                    BeanPropertyRowMapper.newInstance(genericType));
        }
        return getJdbcTemplate().query(sql,
                BeanPropertyRowMapper.newInstance(genericType), parameters);
    }


    public List<String> listOfStringParameter(final String sql,
                                              Object[] parameters) {
        return getJdbcTemplate().query(sql, parameters,
                new SingleColumnRowMapper<String>(String.class));

    }

}
