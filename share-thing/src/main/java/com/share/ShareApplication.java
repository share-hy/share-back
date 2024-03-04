package com.share;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication(scanBasePackages = {"com.share"})
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableScheduling
@Slf4j
public class ShareApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ShareApplication.class, args);
	}

	
	@Override
	public void run(String... args) {
		log.info("thing is running...");
	}

	@RestController
	public static class CheckController {
        @GetMapping("/check.do")
        public ResponseEntity<String> hello() {
            return ResponseEntity.ok("Ok!");
        }
	}
}