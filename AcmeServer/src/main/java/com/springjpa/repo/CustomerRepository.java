package com.springjpa.repo;

import org.springframework.data.repository.CrudRepository;

import com.springjpa.model.Customer;

public interface CustomerRepository extends CrudRepository<Customer, Long> {
	Customer findByEmail(String email);
}
