package edu.psu.se411.lab13;

import java.math.BigDecimal;
import java.net.URI;
import java.net.http.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import jakarta.validation.Validator;
import edu.psu.se411.lab13.model.Product;
import edu.psu.se411.lab13.repositories.ProductRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class Lab13SpringApplicationTests {
	@LocalServerPort int port;
	@Autowired ProductRepository repository;
	@Autowired Validator validator;

	private HttpResponse<String> get(String path) throws Exception {
		return HttpClient.newHttpClient().send(
				HttpRequest.newBuilder(URI.create("http://localhost:" + port + "/api/v1/products" + path)).GET().build(),
				HttpResponse.BodyHandlers.ofString());
	}

	@Test
	void findAllReturnsJsonAndAllSeedProducts() throws Exception {
		HttpResponse<String> response = get("");
		assertEquals(200, response.statusCode());
		assertTrue(response.headers().firstValue("content-type").orElse("").contains("application/json"));
		assertTrue(response.body().contains("3d printer"));
		assertTrue(response.body().contains("PC 08"));
		assertEquals(17, repository.count());
	}

	@Test
	void findByIdReturnsProductAndExactPrice() throws Exception {
		HttpResponse<String> response = get("/10");
		assertEquals(200, response.statusCode());
		assertTrue(response.body().contains("\"id\":10"));
		assertTrue(response.body().contains("1840.25"));
		assertEquals(new BigDecimal("1840.25"), repository.findById(10).orElseThrow().price());
	}

	@Test
	void invalidAndMissingIdsReturnExpectedStatus() throws Exception {
		assertEquals(400, get("/-1").statusCode());
		assertEquals(400, get("/0").statusCode());
		assertEquals(400, get("/abc").statusCode());
		assertEquals(400, get("/2147483648").statusCode());
		assertEquals(404, get("/9999").statusCode());
	}

	@Test
	void productValidationRejectsBadInput() {
		assertTrue(validator.validate(new Product(10, "PC", null, new BigDecimal("10.25"))).isEmpty());
		assertFalse(validator.validate(new Product(-1, "PC", null, BigDecimal.ONE)).isEmpty());
		assertFalse(validator.validate(new Product(10, " ", null, BigDecimal.ONE)).isEmpty());
		assertFalse(validator.validate(new Product(10, "x".repeat(101), null, BigDecimal.ONE)).isEmpty());
		assertFalse(validator.validate(new Product(10, "PC", "x".repeat(101), BigDecimal.ONE)).isEmpty());
		assertFalse(validator.validate(new Product(10, "PC", null, new BigDecimal("-1"))).isEmpty());
		assertFalse(validator.validate(new Product(10, "PC", null, null)).isEmpty());
		assertFalse(validator.validate(new Product(10, "PC", null, new BigDecimal("1.234"))).isEmpty());
		assertFalse(validator.validate(new Product(10, "PC", null, new BigDecimal("100000000"))).isEmpty());
	}

	@Test
	void repositorySupportsPaginationAndSorting() {
		var page = repository.findAll(PageRequest.of(0, 5, Sort.by("id").descending()));
		assertEquals(17, page.getTotalElements());
		assertEquals(5, page.getNumberOfElements());
		assertEquals(26, page.getContent().get(0).id());
	}
}
