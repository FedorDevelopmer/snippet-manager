package eu.ibagroup.snippet_manager.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
       info = @Info(title = "Snippet manager API",
       description = "Provided endpoints for interaction with main object entities of code snippet manager - Language Tags "
               + "and Code Snippets.",
       version = "1.0"),
        servers = @Server(
            description = "Local environment",
            url = "http://localhost:9091"
        )
)

public class SwaggerConfig {
}
