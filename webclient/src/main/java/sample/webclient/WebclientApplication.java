package sample.webclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.client.endpoint.OAuth2ClientCredentialsGrantRequest;
import org.springframework.security.oauth2.client.endpoint.ReactiveOAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.server.UnAuthenticatedServerOAuth2AuthorizedClientRepository;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URL;

@SpringBootApplication
public class WebclientApplication {
	@Bean
	UnAuthenticatedServerOAuth2AuthorizedClientRepository authorizedClientRepository() {
		return new UnAuthenticatedServerOAuth2AuthorizedClientRepository();
	}

	@Bean
	WebClient webClient(ReactiveClientRegistrationRepository clientRegistrationRepository,
			ServerOAuth2AuthorizedClientRepository authorizedClientRepository) {
		ServerOAuth2AuthorizedClientExchangeFilterFunction oauth =
				new ServerOAuth2AuthorizedClientExchangeFilterFunction(clientRegistrationRepository, authorizedClientRepository);

		oauth.setClientCredentialsTokenResponseClient(client());
		return WebClient.builder()
				.filter(oauth)
				.build();
	}

	WebClientReactiveClientCredentialsTokenResponseClient client() {
		WebClient client = WebClient.builder()
				.filter((r, n) -> {
					URI uri = UriComponentsBuilder.fromUri(r.url())
							.queryParam("token_format", "opaque")
							.build()
							.toUri();
					r = ClientRequest.from(r)
						.url(uri)
						.build();
					return n.exchange(r);
				})
				.build();
		WebClientReactiveClientCredentialsTokenResponseClient result =
			new WebClientReactiveClientCredentialsTokenResponseClient();
		result.setWebClient(client);
		return result;
	}

	public static void main(String[] args) {
		SpringApplication.run(WebclientApplication.class, args);
	}
}
