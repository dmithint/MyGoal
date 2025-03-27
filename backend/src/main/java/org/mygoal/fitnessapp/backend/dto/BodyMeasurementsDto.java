package org.mygoal.fitnessapp.backend.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) representing an athlete's body measurements in the FitnessApp.
 * <p>
 * This class encapsulates various physical measurements such as height, weight, body fat percentage,
 * and circumferences of different body parts, all measured in metric units (e.g., centimeters for length, kilograms for weight).
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "DTO representing an athlete's body measurements, including height, weight, and various circumferences")
public class BodyMeasurementsDto {

    /**
     * The height of the athlete in centimeters.
     */
    @Schema(description = "Height of the athlete in centimeters", example = "175.5")
    private Double height;

    /**
     * The weight of the athlete in kilograms.
     */
    @Schema(description = "Weight of the athlete in kilograms", example = "70.2")
    private Double weight;

    /**
     * The body fat percentage of the athlete.
     */
    @Schema(description = "Body fat percentage of the athlete", example = "15.0")
    private Double fat;

    /**
     * The width of the athlete's shoulders in centimeters.
     */
    @Schema(description = "Shoulder width of the athlete in centimeters", example = "45.0")
    private Double shoulderWidth;

    /**
     * The circumference of the athlete's shoulders in centimeters.
     */
    @Schema(description = "Shoulder circumference of the athlete in centimeters", example = "120.0")
    private Double shoulderCircumference;

    /**
     * The circumference of the athlete's chest in centimeters.
     */
    @Schema(description = "Chest circumference of the athlete in centimeters", example = "95.0")
    private Double chestCircumference;

    /**
     * The circumference of the athlete's waist in centimeters.
     */
    @Schema(description = "Waist circumference of the athlete in centimeters", example = "80.0")
    private Double waistCircumference;

    /**
     * The circumference of the athlete's hips in centimeters.
     */
    @Schema(description = "Hip circumference of the athlete in centimeters", example = "90.0")
    private Double hipCircumference;

    /**
     * The circumference of the athlete's calf in centimeters.
     */
    @Schema(description = "Calf circumference of the athlete in centimeters", example = "38.0")
    private Double calfCircumference;
}