package com.batch.SpringBatchExmaple.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.batch.SpringBatchExmaple.entity.Car;
import com.batch.SpringBatchExmaple.entity.CarPK;

@Repository
public interface CarRepo extends JpaRepository<Car, CarPK> {

    /** 查詢全部，依PK欄位排序 */
//    Page<Car> findAll(Pageable pageable);

}
