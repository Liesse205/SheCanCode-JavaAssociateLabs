package com.shecan.lab22.scenario2_notification_payment.exercise24_builder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NotificationMessage {
    private final String recipient;
    private final String subject;
    private final String body;
    private final Priority priority;
    private final List<String> attachments;

    private NotificationMessage(Builder builder) {
        this.recipient = builder.recipient;
        this.subject = builder.subject;
        this.body = builder.body;
        this.priority = builder.priority;
        this.attachments = Collections.unmodifiableList(builder.attachments);
    }

    public String getRecipient() { return recipient; }
    public String getSubject() { return subject; }
    public String getBody() { return body; }
    public Priority getPriority() { return priority; }
    public List<String> getAttachments() { return attachments; }

    @Override
    public String toString() {
        return "NotificationMessage{" +
                "recipient='" + recipient + '\'' +
                ", subject='" + subject + '\'' +
                ", body='" + body + '\'' +
                ", priority=" + priority +
                ", attachments=" + attachments +
                '}';
    }

    // ===== Priority Enum =====
    public enum Priority {
        LOW, MEDIUM, HIGH
    }

    // ===== Builder =====
    public static class Builder {
        private String recipient;
        private String subject;
        private String body;
        private Priority priority = Priority.MEDIUM; // default
        private List<String> attachments = new ArrayList<>();

        public Builder to(String recipient) {
            this.recipient = recipient;
            return this;
        }

        public Builder subject(String subject) {
            this.subject = subject;
            return this;
        }

        public Builder body(String body) {
            this.body = body;
            return this;
        }

        public Builder priority(Priority priority) {
            this.priority = priority;
            return this;
        }

        public Builder attach(String attachment) {
            this.attachments.add(attachment);
            return this;
        }

        public NotificationMessage build() {
            if (recipient == null || recipient.trim().isEmpty()) {
                throw new IllegalStateException("Recipient cannot be null or empty");
            }
            if (body == null || body.trim().isEmpty()) {
                throw new IllegalStateException("Body cannot be null or empty");
            }
            return new NotificationMessage(this);
        }
    }
}