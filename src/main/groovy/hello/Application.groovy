package hello

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class Application {

    static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args)
    }
}