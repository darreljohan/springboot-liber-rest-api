package com.iglo.exam.liber;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

import java.util.Scanner;

@SpringBootApplication
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
public class LiberApplication {

	public static void main(String[] args) {
		SpringApplication.run(LiberApplication.class, args);
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
	}

}
