package com.example.vsd.manager.grpc.admin;

import com.example.vsd.grpc.messages.GetMailConfigResponse;
import com.example.vsd.grpc.messages.UpdateConfigRequest;
import com.example.vsd.grpc.messages.UpdateConfigResponse;
import com.example.vsd.grpc.services.admin.AdminMailControllerGrpc.AdminMailControllerImplBase;
import com.example.vsd.manager.model.MailConfigUpdateData;
import com.example.vsd.manager.security.AdminOnly;
import com.example.vsd.manager.service.mail.MailConfigService;
import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jspecify.annotations.NonNull;
import org.springframework.grpc.server.service.GrpcService;

@GrpcService
@AdminOnly
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminMailGrpcService extends AdminMailControllerImplBase {

	MailConfigService mailConfigService;

	@Override
	public void getMailConfig(@NonNull Empty request,
	                          StreamObserver<GetMailConfigResponse> responseObserver) {
		responseObserver.onNext(GetMailConfigResponse.newBuilder()
				.setNotificationRecipient(mailConfigService.getNotificationRecipient())
				.setEmail(mailConfigService.getEmail())
				.setPasswordConfigured(mailConfigService.isPasswordConfigured())
				.setSmtpHost(mailConfigService.getSmtpHost())
				.setSmtpPort(mailConfigService.getSmtpPort())
				.setSmtpAuth(mailConfigService.isSmtpAuth())
				.setSmtpSsl(mailConfigService.isSmtpSsl())
				.build());
		responseObserver.onCompleted();
	}

	@Override
	public void updateConfig(@NonNull UpdateConfigRequest request,
	                         StreamObserver<UpdateConfigResponse> responseObserver) {
		mailConfigService.update(MailConfigUpdateData.builder()
				.email(request.getEmail())
				.password(request.getPassword())
				.notificationRecipient(request.getNotificationRecipient())
				.resetPassword(request.getResetPassword())
				.smtpHost(request.getSmtpHost())
				.smtpPort(request.getSmtpPort())
				.smtpAuth(request.getSmtpAuth())
				.smtpSsl(request.getSmtpSsl())
				.build());
		responseObserver.onNext(UpdateConfigResponse.newBuilder()
				.setSuccess(true)
				.build());
		responseObserver.onCompleted();
	}
}
