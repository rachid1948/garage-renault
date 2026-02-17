package com.renault.garage.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
@Entity
@Table(name = "garages")
public class GarageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String telephone;

    @Column(nullable = false)
    private String email;

    @OneToMany(mappedBy = "garage", cascade = CascadeType.ALL ,orphanRemoval = true)
    @Builder.Default
    private List<VehicleEntity> vehicles = new ArrayList<>();

    @OneToMany(mappedBy = "garage", cascade = CascadeType.ALL ,orphanRemoval = true)
    @Builder.Default
    private List<OpeningTimeEntity> openingTimes = new ArrayList<>();




}
