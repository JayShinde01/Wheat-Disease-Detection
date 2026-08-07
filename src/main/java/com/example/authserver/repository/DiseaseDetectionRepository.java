package com.example.authserver.repository;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.authserver.entity.DiseaseDetection;

public interface DiseaseDetectionRepository extends JpaRepository<DiseaseDetection, Long>{

	List<DiseaseDetection> findByUserId(Integer userId);
}
