package com.pfc;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI thindeskOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                      .title("ThinDesk API")
                      .description("API REST para abertura, acompanhamento e resolução de chamados técnicos")
                      .version("1.0"))
                .addSecurityItem(new SecurityRequirement().addList("basicAuth"))
                .components(new Components().addSecuritySchemes("basicAuth", new SecurityScheme()
                      .type(SecurityScheme.Type.HTTP)
                      .scheme("basic")));
    }
}
