package com.example.authserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.authserver.entity.DiseaseDetection;

public interface DiseaseDetectionRepository extends JpaRepository<DiseaseDetection, Long>{

}
