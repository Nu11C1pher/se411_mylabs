package edu.psu.se411.lab14.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import edu.psu.se411.lab14.model.Course;

@Repository
public interface CourseRepository extends CrudRepository<Course, Long> {
	// An explicit INSERT keeps a new record with a supplied ID from becoming an UPDATE.
	@Modifying
	@Query("INSERT INTO course (id, code, name, description, credits) VALUES (:id, :code, :name, :description, :credits)")
	int insertCourse(@Param("id") Long id, @Param("code") String code,
			@Param("name") String name, @Param("description") String description,
			@Param("credits") Integer credits);

	@Modifying
	@Query("UPDATE course SET code = :code, name = :name, description = :description, credits = :credits WHERE id = :id")
	int updateCourse(@Param("id") Long id, @Param("code") String code,
			@Param("name") String name, @Param("description") String description,
			@Param("credits") Integer credits);

	@Modifying
	@Query("DELETE FROM course WHERE id = :id")
	int deleteCourse(@Param("id") Long id);
}
