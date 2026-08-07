package com.example.authserver.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "disease_detection")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DiseaseDetection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String disease;

    private Double confidence;

    @Column(nullable = false,length = 1000)
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    private LocalDateTime detectedAt;

}