package org.mygoal.fitnessapp.backend.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "coaches")
@PrimaryKeyJoinColumn(name = "user_id")
@Getter @Setter
public class Coach extends User {

    private String specialization;
    private Integer experience;

    @OneToMany(mappedBy = "coach", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("coach-trainings")
    private List<Training> trainings = new ArrayList<>();


    public double getAverageRating() {
        return trainings.stream()
                .filter(t -> !t.getFeedbacks().isEmpty())
                .mapToDouble(Training::getAverageRating)
                .average()
                .orElse(0.0);
    }
}
