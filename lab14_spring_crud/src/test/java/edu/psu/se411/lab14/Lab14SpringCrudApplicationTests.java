package edu.psu.se411.lab14;

import java.net.URI;
import java.net.http.*;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class Lab14SpringCrudApplicationTests {
	@LocalServerPort int port;
	private final HttpClient client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build();

	private HttpResponse<String> request(String method, String path, String json) throws Exception {
		HttpRequest.Builder builder = HttpRequest.newBuilder(
				URI.create("http://localhost:" + port + "/api/v1/courses" + path))
				.timeout(Duration.ofSeconds(15));
		if(json == null) builder.method(method, HttpRequest.BodyPublishers.noBody());
		else builder.header("Content-Type", "application/json").method(method, HttpRequest.BodyPublishers.ofString(json));
		return client.send(builder.build(), HttpResponse.BodyHandlers.ofString());
	}

	private String course(long id, String name, int credits) {
		return "{\"id\":" + id + ",\"code\":\"SE411\",\"name\":\"" + name
				+ "\",\"description\":\"Lab course\",\"credits\":" + credits + "}";
	}

	@Test
	void allCoursesReturnJson() throws Exception {
		var response = request("GET", "", null);
		assertEquals(200, response.statusCode());
		assertTrue(response.headers().firstValue("content-type").orElse("").contains("application/json"));
		assertTrue(response.body().startsWith("["));
		assertTrue(response.body().contains("Software Construction"));
	}

	@Test
	void seededCourseCanBeRetrieved() throws Exception {
		var response = request("GET", "/1", null);
		assertEquals(200, response.statusCode());
		assertTrue(response.body().contains("\"id\":1"));
		assertTrue(response.body().contains("\"code\":\"SE411\""));
	}

	@Test
	void completeCrudLifecycle() throws Exception {
		var created = request("POST", "", course(901, "Spring CRUD", 3));
		assertEquals(201, created.statusCode());
		assertTrue(created.headers().firstValue("location").orElseThrow().endsWith("/api/v1/courses/901"));
		assertTrue(created.body().contains("\"id\":901"));
		assertEquals(200, request("GET", "/901", null).statusCode());
		var updated = request("PUT", "/901", course(901, "Updated course", 4));
		assertEquals(204, updated.statusCode());
		assertEquals("", updated.body());
		var read = request("GET", "/901", null);
		assertTrue(read.body().contains("Updated course"));
		assertTrue(read.body().contains("\"credits\":4"));
		var deleted = request("DELETE", "/901", null);
		assertEquals(204, deleted.statusCode());
		assertEquals("", deleted.body());
		assertEquals(404, request("GET", "/901", null).statusCode());
	}

	@Test
	void duplicateIdReturns422WithoutOverwriting() throws Exception {
		var response = request("POST", "", course(1, "Replacement", 5));
		assertEquals(422, response.statusCode());
		assertTrue(request("GET", "/1", null).body().contains("Software Construction"));
	}

	@Test
	void absentResourcesReturn404() throws Exception {
		assertEquals(404, request("GET", "/999999", null).statusCode());
		assertEquals(404, request("PUT", "/999999", course(999999, "Missing", 3)).statusCode());
		assertEquals(404, request("DELETE", "/999999", null).statusCode());
		assertEquals(404, request("GET", "/999999", null).statusCode());
	}

	@Test
	void postValidationRejectsInvalidFields() throws Exception {
		String valid = course(902, "Valid course", 3);
		String[] invalid = {
			valid.replace("\"id\":902", "\"id\":-1"),
			valid.replace("\"id\":902", "\"id\":null"),
			valid.replace("\"code\":\"SE411\"", "\"code\":\"\""),
			valid.replace("\"code\":\"SE411\"", "\"code\":\"   \""),
			valid.replace("\"code\":\"SE411\"", "\"code\":null"),
			valid.replace("Valid course", "A"),
			valid.replace("Valid course", "  "),
			valid.replace("\"name\":\"Valid course\"", "\"name\":null"),
			course(902, "Valid course", 0),
			course(902, "Valid course", 11),
			valid.replace("\"credits\":3", "\"credits\":null"),
			valid.replace("Valid course", "x".repeat(101)),
			valid.replace("\"code\":\"SE411\"", "\"code\":\"" + "x".repeat(21) + "\""),
			valid.replace("Lab course", "x".repeat(1001))
		};
		for(String body : invalid) assertEquals(400, request("POST", "", body).statusCode(), body);
		assertEquals(404, request("GET", "/902", null).statusCode());
	}

	@Test
	void zeroIdAndCreditBoundariesAreValid() throws Exception {
		assertEquals(201, request("POST", "", course(0, "Minimum", 1)).statusCode());
		assertEquals(200, request("GET", "/0", null).statusCode());
		assertEquals(204, request("PUT", "/0", course(0, "Maximum", 10)).statusCode());
		assertTrue(request("GET", "/0", null).body().contains("\"credits\":10"));
		assertEquals(204, request("DELETE", "/0", null).statusCode());
	}

	@Test
	void updateValidationDoesNotChangeTheExistingCourse() throws Exception {
		assertEquals(400, request("PUT", "/1", course(1, "Invalid credits", 11)).statusCode());
		assertEquals(400, request("PUT", "/1", course(2, "Wrong ID", 3)).statusCode());
		assertTrue(request("GET", "/1", null).body().contains("Software Construction"));
	}

	@Test
	void invalidRouteIdsReturn400() throws Exception {
		for(String id : new String[]{"-1", "abc", "9223372036854775808"}) {
			assertEquals(400, request("GET", "/" + id, null).statusCode());
			assertEquals(400, request("DELETE", "/" + id, null).statusCode());
			assertEquals(400, request("PUT", "/" + id, course(1, "Valid", 3)).statusCode());
		}
	}

	@Test
	void malformedOrMissingJsonReturns400() throws Exception {
		assertEquals(400, request("POST", "", "{bad json").statusCode());
		assertEquals(400, request("POST", "", "").statusCode());
		assertEquals(400, request("PUT", "/1", "").statusCode());
	}

	@Test
	void nullableDescriptionIsAccepted() throws Exception {
		String body = course(903, "No description", 3).replace("\"Lab course\"", "null");
		assertEquals(201, request("POST", "", body).statusCode());
		assertEquals(204, request("DELETE", "/903", null).statusCode());
	}

	@Test
	void concurrentDuplicateCreatesDoNotOverwrite() throws Exception {
		var first = CompletableFuture.supplyAsync(() -> createConcurrent("First version"));
		var second = CompletableFuture.supplyAsync(() -> createConcurrent("Second version"));
		int a = first.get(), b = second.get();
		assertTrue((a == 201 && b == 422) || (a == 422 && b == 201));
		assertEquals(200, request("GET", "/904", null).statusCode());
		assertEquals(204, request("DELETE", "/904", null).statusCode());
	}

	private int createConcurrent(String name) {
		try { return request("POST", "", course(904, name, 3)).statusCode(); }
		catch(Exception e) { throw new RuntimeException(e); }
	}
}
