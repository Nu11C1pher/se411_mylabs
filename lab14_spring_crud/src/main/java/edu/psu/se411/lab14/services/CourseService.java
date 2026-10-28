package edu.psu.se411.lab14.services;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import edu.psu.se411.lab14.model.Course;
import edu.psu.se411.lab14.repositories.CourseRepository;

@Service
public class CourseService {
	private final CourseRepository courseRepository;

	public CourseService(CourseRepository courseRepository) {
		this.courseRepository = courseRepository;
	}

	public Iterable<Course> findAll() {
		return courseRepository.findAll();
	}

	public Course findById(Long id) {
		return courseRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found"));
	}

	@Transactional
	public Course create(Course course) {
		try {
			courseRepository.insertCourse(course.id(), course.code(), course.name(),
					course.description(), course.credits());
		} catch(DuplicateKeyException e) {
			throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Course ID already exists");
		}
		return course;
	}

	@Transactional
	public void update(Long id, Course course) {
		if(!id.equals(course.id()))
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Body ID must match the route ID");
		int updated = courseRepository.updateCourse(id, course.code(), course.name(),
				course.description(), course.credits());
		if(updated == 0)
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found");
	}

	@Transactional
	public void delete(Long id) {
		if(courseRepository.deleteCourse(id) == 0)
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found");
	}
}
