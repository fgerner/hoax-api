package com.example.hoxify;

import com.example.hoxify.error.ApiError;
import com.example.hoxify.user.User;
import com.example.hoxify.user.UserRepository;
import com.example.hoxify.user.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class LoginControllerTest {
    private static final String API_1_0_LOGIN = "/api/1.0/login";

    @Autowired
    TestRestTemplate testRestTemplate;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @Before
    public void cleanup(){
        userRepository.deleteAll();
        testRestTemplate.getRestTemplate().getInterceptors().clear();
    }

    @Test
    public void postLogin_withoutUserCredenials_receiveUnauthorised(){
        ResponseEntity<Object> response = login(Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void postLogin_withIncorrectUserCredenials_receiveUnauthorised() {
        authenticate();
        ResponseEntity<Object> response = login(Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void postLogin_withoutUserCredenials_receiveApiError(){
        ResponseEntity<ApiError> response = login(ApiError.class);
        assertThat(response.getBody().getUrl()).isEqualTo(API_1_0_LOGIN);
    }

    @Test
    public void postLogin_withoutUserCredenials_receiveApiErrorWithoutValidationErrors() {
        ResponseEntity<String> response = login(String.class);
        assertThat(response.getBody().contains("validationErrors")).isFalse();
    }
    @Test
    public void postLogin_withIncorrectUserCredenials_receiveUnauthorisedWithoutWwwAuthenticateHeader() {
        authenticate();
        ResponseEntity<Object> response = login(Object.class);
        assertThat(response.getHeaders().containsKey("WWW-Authenticate")).isFalse();
    }

    @Test
    public void postLogin_withValidUserCredetials_receiveOk(){
        userService.save(TestUtil.createValidUser());
        authenticate();
        ResponseEntity<Object> response = login(Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void postLogin_withValidUserCredetials_receiveLogedinUserId() {
        User inDb = userService.save(TestUtil.createValidUser());
        authenticate();
        ResponseEntity<Map<String, Object>> response = login(new ParameterizedTypeReference<Map<String, Object>>() {});
        Map<String, Object> body = response.getBody();
        Integer id = (Integer) body.get("id");
        assertThat(id).isEqualTo(inDb.getId());
    }

    private void authenticate() {
        testRestTemplate.getRestTemplate()
                .getInterceptors().add(new BasicAuthenticationInterceptor("test-user", "P4ssword"));
    }

    public <T> ResponseEntity<T> login(Class<T> responseType){
        return testRestTemplate.postForEntity(API_1_0_LOGIN, null, responseType);
    }
    public <T> ResponseEntity<T> login(ParameterizedTypeReference<T> responseType){
        return testRestTemplate.exchange(API_1_0_LOGIN, HttpMethod.POST, null, responseType);
    }
}
