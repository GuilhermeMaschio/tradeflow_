package br.com.tradeflow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@EnableRetry
@SpringBootApplication
public class TradeflowApplication {

    public static void main(String[] args) {
        SpringApplication.run(TradeflowApplication.class, args);
    }

}
