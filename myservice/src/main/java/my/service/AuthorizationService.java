package my.service;

import com.google.api.client.auth.oauth2.Credential;

import java.io.IOException;

public interface AuthorizationService {

    boolean isUserAuthenticated() throws Exception;

    Credential getCredentials() throws IOException;

    String authenticateUserViaGoogle() throws Exception;

    void exchangeCodeForTokens(String code) throws Exception;
}
