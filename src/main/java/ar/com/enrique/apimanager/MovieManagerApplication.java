package ar.com.enrique.apimanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages="ar.com.enrique.apimanager.repository")
public class MovieManagerApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(MovieManagerApplication.class, args);
    }
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(MovieManagerApplication.class);
    }

}
