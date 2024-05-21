package Pegas.controller;

import Pegas.presentation.Presentation;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.Objects;


@Controller
public class WebController {
    private final RestClient restClient;

    private final OAuth2AuthorizedClientManager authorizedClientManager;

    public WebController(ClientRegistrationRepository clientRegistrationRepository,
                         OAuth2AuthorizedClientRepository authorizedClientRepository) {
        this.authorizedClientManager = new DefaultOAuth2AuthorizedClientManager(
                clientRegistrationRepository, authorizedClientRepository);
        this.restClient = RestClient.builder()
                .baseUrl("http://localhost:8080")
                .requestInterceptor((request, body, execution) -> {
                    if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                        var token = Objects.requireNonNull(this.authorizedClientManager.authorize(
                                        OAuth2AuthorizeRequest.withClientRegistrationId("sand-image-app-authorization-code")
                                                .principal(SecurityContextHolder.getContext().getAuthentication())
                                                .build()))
                                .getAccessToken().getTokenValue();
                        request.getHeaders().setBearerAuth(token);
                    }

                    return execution.execute(request, body);
                })
                .build();
    }


    @GetMapping("/users/")
    public String get(Model model) throws NoSuchFieldException, IllegalAccessException {
        byte[] arr = this.restClient.get()
                .uri("/api/v1/image")
                .retrieve()
                .body(byte[].class);
        String base64Image = Base64.getEncoder().encodeToString(arr);
        model.addAttribute("cat",base64Image);
        return "image";
    }
}
