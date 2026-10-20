package com.nmims.lms.beans;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.util.Date;
import java.util.function.Consumer;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    @Column(updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedDate;

    @CreatedBy
    @Column(updatable = false)
    private String createdBy;

    @LastModifiedBy
    private String lastModifiedBy;

    @Transient
    private Boolean isErrorRecord = false;

    @Transient
    private String errorMessage;

    @Column(columnDefinition = "ENUM('Y','N') DEFAULT 'Y'")
    private  String active;

    protected String formatDate(String date) {
        if (null == date)
            return date;
        if (date.length() > 19) {
            return date.substring(0, 19).replace(' ', 'T');
        } else {
            return date.replace(' ', 'T');
        }
    }

    @PrePersist
    public void prePersist() {
        if (active == null) {
            active = "Y";
        }
    }
    protected String checkNullAndSet(String value, String defaultValue) {
        return null == value ? defaultValue : value;
    }

    public <T> void setIfNotNull(T value, Consumer<T> setter) {
        if (value != null) setter.accept(value);
    }

}
