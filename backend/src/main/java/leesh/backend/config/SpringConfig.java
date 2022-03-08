package leesh.backend.config;

import leesh.backend.common.AppProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(value = {
        AppProperties.class
})
public class SpringConfig {
}
