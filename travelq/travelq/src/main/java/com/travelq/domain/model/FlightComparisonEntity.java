package com.travelq.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "flight_comparisons")
public class FlightComparisonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comparison_id")
    private Long id;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "flight_comparison_items",
            joinColumns = @JoinColumn(name = "comparison_id"),
            inverseJoinColumns = @JoinColumn(name = "flight_id")
    )
    private List<FlightEntity> flights;
}
