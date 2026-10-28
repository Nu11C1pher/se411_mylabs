package edu.psu.se411.lab14.controllers;

import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;
import edu.psu.se411.lab14.model.Course;
import edu.psu.se411.lab14.services.CourseService;

@RestController
@RequestMapping("/api/v1/courses")
public class CourseController {
	private final CourseService courseService;

	public CourseController(CourseService courseService) {
		this.courseService = courseService;
	}

	@GetMapping
	public ResponseEntity<Iterable<Course>> findAll() {
		return ResponseEntity.ok(courseService.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Course> findById(@PathVariable("id") @PositiveOrZero Long id) {
		return ResponseEntity.ok(courseService.findById(id));
	}

	@PostMapping
	public ResponseEntity<Course> create(@Valid @RequestBody Course course) {
		Course created = courseService.create(course);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}").buildAndExpand(created.id()).toUri();
		return ResponseEntity.created(location).body(created);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Void> update(@PathVariable("id") @PositiveOrZero Long id,
			@Valid @RequestBody Course course) {
		courseService.update(id, course);
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable("id") @PositiveOrZero Long id) {
		courseService.delete(id);
		return ResponseEntity.noContent().build();
	}
}
