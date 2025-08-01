package com.dhanashri.Notification_Service;

import com.dhanashri.common.events.QuizNotificationEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class QuizNotificationListener {

    @KafkaListener(topics = "${quiz.notification.topic}", groupId = "notification-group")
    public void consumeQuizNotification(QuizNotificationEvent event) {
        // Logic to send email to students
        System.out.println("New quiz created: " + event.getQuizName());
        // sendEmailToStudents(event);
    }
}
