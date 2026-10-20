package com.nmims.lms.beans.Course;

import com.nmims.lms.beans.BaseBean;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "user_course")
public class UserCourse extends BaseBean {

    private static final long serialVersionUID = 5541433915016984117L;

    // 🔑 FK column
    @Column(name = "username", nullable = false)
    private String username;


    @Column(name = "acadMonth")
    private String acadMonth;

    @Column(name = "acadYear")
    private Integer acadYear;

    @Column(name = "acadYearCode")
    private Integer acadYearCode;

    @Column(name = "acadSession")
    private String acadSession;

    @Column(name = "role", nullable = false)
    private String role; // STUDENT / FACULTY

    // ✅ COURSE RELATIONSHIP
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "courseId",
            referencedColumnName = "id",
            insertable = false,
            updatable = false
    )
    private Course course;

    @Transient
    private  String facultyName;

}
