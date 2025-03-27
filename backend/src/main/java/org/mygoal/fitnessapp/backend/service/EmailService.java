package org.mygoal.fitnessapp.backend.service;

import lombok.RequiredArgsConstructor;
import org.mygoal.fitnessapp.backend.exceptions.AppException;
import org.mygoal.fitnessapp.backend.model.BodyMeasurements;
import org.mygoal.fitnessapp.backend.model.Athlete;
import org.mygoal.fitnessapp.backend.model.Training;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;


@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender emailSender;
    private final Environment environment;

    @Async
    public void sendEmailParams(Athlete athlete) throws MailException {
        LocalDateTime moscowTime = LocalDateTime.now(ZoneId.of("Europe/Moscow"));
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);
        String formattedDate = moscowTime.format(formatter);

        BodyMeasurements measurements = athlete.getBodyMeasurements();
        if (measurements == null) {
            throw new AppException("Body measurements not found for athlete", HttpStatus.BAD_REQUEST);
        }

        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setFrom(environment.getProperty("spring.mail.username"));
        mail.setTo(athlete.getEmail());
        mail.setSubject("Body Parameters (" + formattedDate + ")");

        mail.setText(String.format(
                """
                        Your body parameters as of %s:
                        
                        Height: %.1f cm
                        
                        Weight: %.1f kg
                        
                        Fat: %.1f%%
                        
                        Shoulder Width: %.1f cm
                        
                        Shoulder Circumference: %.1f cm
                        
                        Chest Circumference: %.1f cm
                        
                        Waist Circumference: %.1f cm
                        
                        Hip Circumference: %.1f cm
                        
                        Calf Circumference: %.1f cm
                        """,
                formattedDate,
                measurements.getHeight(),
                measurements.getWeight(),
                measurements.getFat(),
                measurements.getShoulderWidth(),
                measurements.getShoulderCircumference(),
                measurements.getChestCircumference(),
                measurements.getWaistCircumference(),
                measurements.getHipCircumference(),
                measurements.getCalfCircumference()
        ));

        emailSender.send(mail);
    }

    @Async
    public void sendTrainingCancellationEmail(Athlete athlete, Training training) {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setFrom(environment.getProperty("spring.mail.username"));
        mail.setTo(athlete.getEmail());
        mail.setSubject("Training Enrollment Canceled");

        mail.setText(String.format(
                        """
                                Hello %s,
                                
                                You have been removed from the training session:
                                
                                Training: %s
                                Date: %s
                                
                                If you have any questions, please contact your coach.
                                """,
                        athlete.getFirstName() + " " + athlete.getLastName(),
                        training.getName(),
                        (
                                training.getStart().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)) + " - " +
                                        training.getEnd().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT))
                        )
                )
        );

        emailSender.send(mail);
    }

    @Async
    public void sendTrainingStatusUpdateEmail(Athlete athlete, Training training, String newStatus) {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setFrom(environment.getProperty("spring.mail.username"));
        mail.setTo(athlete.getEmail());
        mail.setSubject("Training Status Update");

        mail.setText(String.format(
                """
                        Hello %s,
                        
                        The status of your training session has been updated:
                        
                        Training: %s
                        Date: %s
                        New Status: %s
                        
                        Please check the training schedule for further updates.
                        """,
                athlete.getFirstName() + " " + athlete.getLastName(),
                training.getName(),
                (
                        training.getStart().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)) + " - " +
                                training.getEnd().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT))
                ),
                newStatus
        ));

        emailSender.send(mail);
    }
}
