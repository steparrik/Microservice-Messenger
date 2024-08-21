package steparrik;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class
ChatsMicroserviceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ChatsMicroserviceApplication.class, args);
    }

}
