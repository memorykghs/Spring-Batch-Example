package com.batch.SpringBatchExample.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.batch.SpringBatchExample.entity.Car;
import com.batch.SpringBatchExample.entity.CarPK;


@Repository
public interface CarRepo extends JpaRepository<Car, CarPK> {

    /** 查詢全部，依PK欄位排序 */
    Page<Car> findAll(Pageable pageable);

}
