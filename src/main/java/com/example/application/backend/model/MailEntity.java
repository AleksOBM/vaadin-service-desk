package com.example.application.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Entity
@Table(name = "mail_data")
@EqualsAndHashCode(of = "id")
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MailEntity {

    @Id
    Long id;
    String targetMail;
    String appMail;
    String appMailHost;
    String appMailPort;
    String appMailPassword;
    boolean mailSmtpAuth;
    boolean mailSmtpStartTls;
}
