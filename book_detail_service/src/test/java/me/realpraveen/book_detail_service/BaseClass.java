package me.realpraveen.book_detail_service;

import java.util.List;
import java.util.Optional;

import org.mockito.Mockito;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.context.WebApplicationContext;

import io.restassured.RestAssured;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.webtestclient.RestAssuredWebTestClient;
import me.realpraveen.book_detail_service.Controller.BookContoller;
import me.realpraveen.book_detail_service.Model.Book;
import me.realpraveen.book_detail_service.Model.Category;
import me.realpraveen.book_detail_service.Repository.BookRepository;

@DirtiesContext
@AutoConfigureWebTestClient
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class BaseClass {

	@MockBean
	private BookRepository bookRepo;

	@LocalServerPort
	private int port;

	@Autowired
	private WebTestClient testClient;

	@Autowired
	private WebApplicationContext context;

	@Autowired
	private BookContoller contoller;

	static {
        System.setProperty("eureka.client.enabled", "false");
        System.setProperty("eureka.client.register-with-eureka", "false");
        System.setProperty("spring.cloud.gateway.discovery.locator.enabled", "false");
    }

	@BeforeEach
	public void before() throws Exception {

		RestAssured.baseURI = "http://localhost:" + port;
		RestAssuredWebTestClient.webTestClient(this.testClient);
		RestAssuredMockMvc.webAppContextSetup(context);

		Book b1 = new Book(12L, 45L, "Random fandom", "Jimmy", 8422, Category.SCIENCE);
		Book b2 = new Book(10L, 675L, "awesome title", "cow", 2345, Category.UNLISTED);
		Book b3 = new Book(11L, 675L, "some book", "alex", 9876, Category.SCIENCE);

		Mockito.when(bookRepo.findAll()).thenReturn(List.of(b1, b2));
		Mockito.when(bookRepo.findById(12L)).thenReturn(Optional.of(b1));
		Mockito.when(bookRepo.findByUserId(675L)).thenReturn(List.of(b2, b3));

	}


	@Test
	void postBookTest()  {

		testClient = WebTestClient.bindToController(contoller).build();

		Book bookBody = new Book(849L, 4523L, "Random fandom", "Jimmy", 8422, Category.SCIENCE);

		testClient.post()
		          .uri("/book-service")
				  .header(MediaType.APPLICATION_JSON_VALUE)
				  .bodyValue(bookBody)
				  .exchange()
				  .expectStatus().isCreated();
	}

}
