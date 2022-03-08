package leesh.backend.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "app")
@ConstructorBinding
public class AppProperties {

    private final String url;
    private final String docsUrl;

    @Getter
    @RequiredArgsConstructor
    public static final class Cors {
        private final String[] allowedOrigins;
    }

}
