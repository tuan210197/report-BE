package com.foxconn.EmployeeManagerment.repository;

import com.foxconn.EmployeeManagerment.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CateRepository extends JpaRepository<Category, String> {

    @Query(value = "SELECT c FROM Category c WHERE c.categoryId = :id")
    Category getOneCategory(@Param("id") String id);

    @Query("SELECT c FROM Category c WHERE c.categoryId = :cateId")
    Category findByCategory(@Param("cateId") String cateId);
}
