package com.mycompany.myapp.master.repository;

import com.mycompany.myapp.master.domain.TenancyData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface TenancyDataRepository extends JpaRepository<TenancyData, Long> {

}
