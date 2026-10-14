CREATE TABLE mail_data (
    id bigint NOT NULL,
    app_mail varchar(255),
    app_mail_host varchar(255),
    app_mail_password varchar(255),
    app_mail_port varchar(255),
    target_mail varchar(255),
    mail_smtp_auth boolean NOT NULL,
    mail_smtp_start_tls boolean NOT NULL,
    CONSTRAINT pk_mail_data PRIMARY KEY (id)
);