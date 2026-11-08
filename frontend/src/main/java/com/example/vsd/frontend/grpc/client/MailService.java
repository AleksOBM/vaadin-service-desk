package com.example.vsd.frontend.grpc.client;

import com.example.vsd.grpc.messages.UpdateConfigRequest;
import com.example.vsd.grpc.services.admin.AdminMailControllerGrpc.AdminMailControllerBlockingStub;
import com.google.protobuf.Empty;
import io.grpc.StatusRuntimeException;
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

	public String getNotificationRecipient() {
		try {
			return adminMailStub.getMailConfig(Empty.getDefaultInstance()).getNotificationRecipient();
		} catch (StatusRuntimeException e) {
			log.error("Error while loading mail configuration", e);
			throw e;
		}
	}

	public boolean updateConfig(String email, String password, String notificationRecipient) {
		try {
			var response = adminMailStub.updateConfig(UpdateConfigRequest.newBuilder()
					.setEmail(email)
					.setPassword(password)
					.setNotificationRecipient(notificationRecipient)
					.build());
			return response.getSuccess();
		} catch (StatusRuntimeException e) {
			log.error("Error while updating mail configuration", e);
			throw e;
		}
	}
}
