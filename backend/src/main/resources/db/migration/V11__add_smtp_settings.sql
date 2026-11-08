ALTER TABLE mail_config
    ADD COLUMN smtp_host varchar(255) NOT NULL DEFAULT 'localhost',
    ADD COLUMN smtp_port integer NOT NULL DEFAULT 1025,
    ADD COLUMN smtp_auth boolean NOT NULL DEFAULT false,
    ADD COLUMN smtp_ssl boolean NOT NULL DEFAULT false;
