package com.springjpa.repo;

import com.springjpa.model.RequestLine;
import org.springframework.data.repository.CrudRepository;

public interface RequestLineRepository extends CrudRepository<RequestLine, Long> {
}
