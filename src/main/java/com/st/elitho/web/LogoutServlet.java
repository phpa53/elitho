package com.st.elitho.web;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;

import com.st.elitho.uti.AppProperties;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/app-logout")
public class LogoutServlet extends HttpServlet {

    private static final long serialVersionUID = -3192314119027579320L;
	// Adjust these two values
    private static final String KEYCLOAK_LOGOUT_URL =
            AppProperties.KEYCLOAK_URL.replace("/keycloak", "") + "/realms/deeplitho/protocol/openid-connect/logout";

    @Override
	protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {

        if (request.getSession(false) != null) {
        	request.getSession(false).invalidate();
        }

        final var postLogoutRedirect = String.format("%s://%s:%d%s/",
        	request.getScheme(), request.getServerName(), request.getServerPort(), request.getContextPath());
        final var stb = new StringBuilder(KEYCLOAK_LOGOUT_URL);
        String idToken;

        if (request.getUserPrincipal() instanceof KeycloakPrincipal<?>) {
            final KeycloakSecurityContext ksc =
                    ((KeycloakPrincipal<?>) request.getUserPrincipal()).getKeycloakSecurityContext();
            idToken = ksc.getIdTokenString();
        } else {
        	idToken = "";
        }

        stb.append("?");

        if (Optional.ofNullable(idToken).orElse("").isEmpty()) {
            stb.append("client_id=").append(URLEncoder.encode("elitho", StandardCharsets.UTF_8));
        } else {
            stb.append("id_token_hint=").append(URLEncoder.encode(idToken, StandardCharsets.UTF_8));
        }

        stb.append("&post_logout_redirect_uri=").append(URLEncoder.encode(postLogoutRedirect, StandardCharsets.UTF_8));

        response.sendRedirect(stb.toString());

    }

}
