package com.springjpa.repo;

import com.springjpa.model.Voucher;
import org.springframework.data.repository.CrudRepository;

public interface VoucherRepository extends CrudRepository<Voucher, Long>{
}
