package com.example.vsd.frontend.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MailEntity {
    Long id;
    String targetMail;
    String appMail;
    String appMailHost;
    String appMailPort;
    String appMailPassword;
    Boolean mailSmtpAuth;
    Boolean mailSmtpStartTls;
}
