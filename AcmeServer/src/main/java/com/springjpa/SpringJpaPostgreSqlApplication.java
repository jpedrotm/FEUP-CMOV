package com.springjpa;

import com.springjpa.utils.MetadataManager;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.*;


@SpringBootApplication
public class SpringJpaPostgreSqlApplication implements CommandLineRunner{
	
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		SpringApplication.run(SpringJpaPostgreSqlApplication.class, args);
	}

	@Override
	public void run(String... arg0) throws Exception {
		// clear all record if existed before do the tutorial with new data
	}
}
