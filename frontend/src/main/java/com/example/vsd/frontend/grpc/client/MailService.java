package com.example.vsd.frontend.grpc.client;

import com.example.vsd.frontend.model.MailConfigData;
import com.example.vsd.grpc.messages.GetMailConfigResponse;
import com.example.vsd.grpc.messages.UpdateConfigRequest;
import com.example.vsd.grpc.services.admin.AdminMailControllerGrpc.AdminMailControllerBlockingStub;
import com.google.protobuf.Empty;
import io.grpc.StatusRuntimeException;
import jakarta.annotation.Nonnull;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MailService {

	AdminMailControllerBlockingStub adminMailStub;

	public GetMailConfigResponse getMailConfig() {
		try {
			return adminMailStub.getMailConfig(Empty.getDefaultInstance());
		} catch (StatusRuntimeException e) {
			log.error("Error while loading mail configuration", e);
			throw e;
		}
	}

	public boolean updateConfig(@Nonnull MailConfigData mailConfigData) {
		try {
			var response = adminMailStub.updateConfig(UpdateConfigRequest.newBuilder()
					.setEmail(mailConfigData.email())
					.setPassword(mailConfigData.password())
					.setNotificationRecipient(mailConfigData.notificationRecipient())
					.setResetPassword(mailConfigData.resetPassword())
					.setSmtpHost(mailConfigData.smtpHost())
					.setSmtpPort(mailConfigData.smtpPort())
					.setSmtpAuth(mailConfigData.smtpAuth())
					.setSmtpSsl(mailConfigData.smtpSsl())
					.build());
			return response.getSuccess();
		} catch (StatusRuntimeException e) {
			log.error("Error while updating mail configuration", e);
			throw e;
		}
	}
}
