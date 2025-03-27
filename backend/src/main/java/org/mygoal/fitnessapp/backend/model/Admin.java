package org.mygoal.fitnessapp.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "admins")
@PrimaryKeyJoinColumn(name = "user_id")
@Getter
@Setter
public class Admin extends User {
}
