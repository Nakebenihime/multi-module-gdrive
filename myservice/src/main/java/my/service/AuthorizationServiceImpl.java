package my.service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.util.store.DataStoreFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import my.config.ApplicationConfig;
import my.context.ApplicationConstantVariables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStreamReader;

@Service
public class AuthorizationServiceImpl implements AuthorizationService {

    private ApplicationConfig applicationConfig;

    @Autowired
    public void setApplicationConfig(ApplicationConfig applicationConfig){this.applicationConfig = applicationConfig;}

    private GoogleAuthorizationCodeFlow flow;

    @PostConstruct
    public void init() throws IOException {

        InputStreamReader reader = new InputStreamReader(applicationConfig.getGdSecretKeys().getInputStream());
        DataStoreFactory dataStoreFactory = new FileDataStoreFactory(applicationConfig.getCredentialsFolder().getFile());

        GoogleClientSecrets secrets = GoogleClientSecrets.load(ApplicationConstantVariables.JSON_FACTORY, reader);
        flow = new GoogleAuthorizationCodeFlow.Builder(ApplicationConstantVariables.HTTP_TRANSPORT, ApplicationConstantVariables.JSON_FACTORY, secrets, ApplicationConstantVariables.SCOPES)
                .setDataStoreFactory(dataStoreFactory).build();

    }

    @Override
    public boolean isUserAuthenticated() throws Exception {
        Credential credential = getCredentials();
        if (credential != null) {
            boolean isTokenValid = credential.refreshToken();
            return isTokenValid;
        }
        return false;
    }

    @Override
    public Credential getCredentials() throws IOException {
        return flow.loadCredential(ApplicationConstantVariables.USER_IDENTIFIER_KEY);
    }

    @Override
    public String authenticateUserViaGoogle() throws Exception {
        GoogleAuthorizationCodeRequestUrl url = flow.newAuthorizationUrl();
        String redirectURL = url.setRedirectUri(applicationConfig.getCALLBACK_URI()).setAccessType("offline").build();
        return redirectURL;
    }

    @Override
    public void exchangeCodeForTokens(String code) throws Exception {
        GoogleTokenResponse response = flow.newTokenRequest(code).setRedirectUri(applicationConfig.getCALLBACK_URI()).execute();
        flow.createAndStoreCredential(response, ApplicationConstantVariables.USER_IDENTIFIER_KEY);

    }
}
