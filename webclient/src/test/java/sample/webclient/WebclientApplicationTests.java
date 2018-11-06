package sample.webclient;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.reactive.function.client.WebClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction.clientRegistrationId;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WebclientApplicationTests {
	@Autowired
	WebClient webClient;

	@Test
	public void helloBoot() {
		String body = this.webClient.get()
			.uri("http://localhost:8080/message")
			.attributes(clientRegistrationId("uaa"))
			.retrieve()
			.bodyToMono(String.class)
			.block();
		assertThat(body).isEqualTo("Hello Boot!");
	}

}
