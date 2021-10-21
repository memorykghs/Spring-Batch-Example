package com.batch.SpringBatchExmaple.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.batch.SpringBatchExmaple.entity.Cars;
import com.batch.SpringBatchExmaple.entity.CarsPK;

@Repository
public interface CarsRepo extends JpaRepository<Cars, CarsPK> {

    /** 查詢全部，依PK欄位排序 */
//    Page<Car> findAll(Pageable pageable);

}
