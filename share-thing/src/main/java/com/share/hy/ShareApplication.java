package com.share.hy;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication(scanBasePackages = {"com.share.hy"})
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableScheduling
@MapperScan({"com.share.hy.mapper"})
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