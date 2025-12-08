package com.enotes.Enotes_INDUS.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI(){
        OpenAPI openApi=new OpenAPI();
        Info info=new Info();
        info.setTitle("ENOTES INDUS APIS");
        info.setDescription("LIST OF ALL THE API CALLS PRESENT IN THE ENOTES APP");
        info.setVersion("1.0.0");
        info.setTermsOfService("http://enotesIndus.com");
        info.setContact(
                new Contact().email("chaseaccel2@gmail.com").name("Chase").url("http://enotesIndus.com")
        );
        info.setLicense(new License().name("Enotes 1.1").url("http://enotesIndus.com"));

        List<Server> serversList =List.of(new Server().description("DEV").url("http://localhost:8080/enotes"),
        new Server().description("TEST").url("http://localhost:8080/enotes"),
        new Server().description("PROD").url("http://localhost:8080/enotes"));

        SecurityScheme securityScheme=new SecurityScheme().name("Authorization")
                        .scheme("bearer").type(SecurityScheme.Type.HTTP)
                        .bearerFormat("JWT").in(SecurityScheme.In.HEADER);

        Components components=new Components();
        components.addSecuritySchemes("JwtToken",securityScheme);
        openApi.setComponents(components);
        openApi.setInfo(info);
        openApi.setServers(serversList);
        openApi.setSecurity(List.of(new SecurityRequirement().addList("JwtToken")));
        return openApi;
    }
}
