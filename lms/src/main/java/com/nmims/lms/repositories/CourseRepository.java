package com.nmims.lms.repositories;

import com.nmims.lms.beans.Course.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository  extends JpaRepository<Course, Long> {
}
