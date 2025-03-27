package org.mygoal.fitnessapp.backend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "trainings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Training {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TrainingStatus status = TrainingStatus.SCHEDULED;

    private Integer maxAthletes;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 4000)
    private String description;

    @Column(nullable = false)
    private LocalDateTime start;

    @Column(nullable = false)
    private LocalDateTime end;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coach_id")
    @JsonBackReference("coach-trainings")
    private Coach coach;

    @ManyToMany
    @JoinTable(
            name = "training_athletes",
            joinColumns = @JoinColumn(name = "training_id"),
            inverseJoinColumns = @JoinColumn(name = "athlete_id")
    )
    private List<Athlete> athletes = new ArrayList<>();

    @OneToMany(mappedBy = "training", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TrainingFeedback> feedbacks = new ArrayList<>();

    public double getAverageRating() {
        return feedbacks.isEmpty() ? 0.0 :
                feedbacks.stream().mapToInt(TrainingFeedback::getRating).average().orElse(0.0);
    }
}
