package main.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@Data
public class MvcConfig implements WebMvcConfigurer {
  @Value("${file.upload_directory}")
  private String pathLoad;

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler(pathLoad + "/**").addResourceLocations("file:" + pathLoad + "/");
  }
}
