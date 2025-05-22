package com.travelq.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
class OpenAPITest {

    private final OpenAPIConfig openAPIConfig = new OpenAPIConfig();

    @Test
    void testMyOpenAPI_NotNull() {
        OpenAPI openAPI = openAPIConfig.myOpenAPI();
        assertNotNull(openAPI, "OpenAPI bean should not be null");
    }

    @Test
    void testMyOpenAPI_InfoFields() {
        OpenAPI openAPI = openAPIConfig.myOpenAPI();
        Info info = openAPI.getInfo();

        assertNotNull(info, "Info section should not be null");
        assertEquals("TravelQ API", info.getTitle());
        assertEquals("1.0", info.getVersion());
        assertEquals("This API exposes endpoints to manage travel reservations, flight searches, and user accounts.", info.getDescription());

        License license = info.getLicense();
        assertNotNull(license, "License should not be null");
        assertEquals("MIT License", license.getName());
        assertEquals("https://choosealicense.com/licenses/mit/", license.getUrl());

        Contact contact = info.getContact();
        assertNotNull(contact, "Contact should not be null");
        assertEquals("TravelQ Team", contact.getName());
        assertEquals("support@travelq.com", contact.getEmail());
    }

    @Test
    void testMyOpenAPI_ServerConfig() {
        OpenAPI openAPI = openAPIConfig.myOpenAPI();
        List<Server> servers = openAPI.getServers();

        assertNotNull(servers, "Servers list should not be null");
        assertEquals(1, servers.size());

        Server server = servers.get(0);
        assertEquals("http://localhost:8080", server.getUrl());
        assertEquals("Server URL in Development environment", server.getDescription());
    }
}

