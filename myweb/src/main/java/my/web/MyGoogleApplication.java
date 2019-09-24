package my.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@EntityScan({"my"})
@ComponentScan({"my"})
@SpringBootApplication
public class MyGoogleApplication {
    public static void main(String[] args) {
        SpringApplication.run(MyGoogleApplication.class, args);
    }
}
