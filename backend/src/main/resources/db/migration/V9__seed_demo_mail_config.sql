INSERT INTO mail_config (email_encrypted, password_encrypted, notification_recipient, updated_at)
SELECT 'demo-seed:sender@example.com',
       'demo-seed:test-password-123',
       'demo-seed:recipient@example.com',
       CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM mail_config);
