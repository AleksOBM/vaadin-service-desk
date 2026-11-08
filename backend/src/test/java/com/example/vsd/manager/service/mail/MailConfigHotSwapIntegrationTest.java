package com.example.vsd.manager.service.mail;

import com.example.vsd.grpc.messages.OrderProto;
import com.example.vsd.grpc.messages.UpdateConfigRequest;
import com.example.vsd.grpc.services.admin.AdminMailControllerGrpc;
import com.example.vsd.grpc.services.user.UserControllerGrpc;
import com.example.vsd.manager.model.MailConfigUpdateData;
import com.example.vsd.manager.model.MailCredentials;
import com.example.vsd.manager.model.MailMessage;
import com.example.vsd.manager.repository.MailConfigRepository;
import com.google.protobuf.Empty;
import com.google.type.Date;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Metadata;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.MetadataUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.grpc.test.autoconfigure.LocalGrpcServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
class MailConfigHotSwapIntegrationTest {

	private static final String OLD_EMAIL = "old@example.com";
	private static final String OLD_PASSWORD = "old-app-password";
	private static final String OLD_NOTIFICATION_RECIPIENT = "old-notifications@example.com";
	private static final String NEW_EMAIL = "new@example.com";
	private static final String NEW_PASSWORD = "new-app-password";
	private static final String NEW_NOTIFICATION_RECIPIENT = "new-notifications@example.com";

	@LocalGrpcServerPort
	private int grpcPort;

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private MailConfigRepository mailConfigRepository;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private MailConfigService mailConfigService;

	@Autowired
	private JwtEncoder jwtEncoder;

	@MockitoBean
	private MailSenderService mailSenderService;

	private ManagedChannel channel;
	private ManagedChannel adminChannel;
	private UserControllerGrpc.UserControllerBlockingStub stub;
	private AdminMailControllerGrpc.AdminMailControllerBlockingStub adminMailStub;
	private AdminMailControllerGrpc.AdminMailControllerBlockingStub agentMailStub;

	@BeforeEach
	void setUp() {
		mailConfigRepository.deleteAllInBatch();
		mailConfigService.load();

		Metadata headers = new Metadata();
		Metadata.Key<String> authorization =
				Metadata.Key.of("Authorization", Metadata.ASCII_STRING_MARSHALLER);
		headers.put(authorization, "Bearer " + createToken("ROLE_AGENT"));

		channel = ManagedChannelBuilder.forAddress("localhost", grpcPort)
				.usePlaintext()
				.intercept(MetadataUtils.newAttachHeadersInterceptor(headers))
				.build();
		stub = UserControllerGrpc.newBlockingStub(channel);
		agentMailStub = AdminMailControllerGrpc.newBlockingStub(channel);

		Metadata adminHeaders = new Metadata();
		adminHeaders.put(authorization, "Bearer " + createToken("ROLE_ADMIN"));
		adminChannel = ManagedChannelBuilder.forAddress("localhost", grpcPort)
				.usePlaintext()
				.intercept(MetadataUtils.newAttachHeadersInterceptor(adminHeaders))
				.build();
		adminMailStub = AdminMailControllerGrpc.newBlockingStub(adminChannel);
	}

	@AfterEach
	void tearDown() {
		if (channel != null) {
			channel.shutdownNow();
		}
		if (adminChannel != null) {
			adminChannel.shutdownNow();
		}
		mailConfigRepository.deleteAllInBatch();
		mailConfigService.load();
	}

	@Test
	void changingSenderEmailKeepsExistingPasswordWhenMaskIsSubmitted() {
		mailConfigService.update(MailConfigUpdateData.builder()
				.email(OLD_EMAIL)
				.password(OLD_PASSWORD)
				.notificationRecipient(OLD_NOTIFICATION_RECIPIENT)
				.resetPassword(false)
				.build());

		mailConfigService.update(MailConfigUpdateData.builder()
				.email(NEW_EMAIL)
				.password("********")
				.notificationRecipient(NEW_NOTIFICATION_RECIPIENT)
				.smtpHost("localhost")
				.smtpPort(1025)
				.smtpAuth(false)
				.smtpSsl(false)
				.resetPassword(false)
				.build());

		assertEquals(MailCredentials.builder()
						.email(NEW_EMAIL)
						.password(OLD_PASSWORD)
						.notificationRecipient(NEW_NOTIFICATION_RECIPIENT)
						.smtpHost("localhost")
						.smtpPort(1025)
						.smtpAuth(false)
						.smtpSsl(false)
						.build(),
				mailConfigService.get());
	}

	@Test
	void updatesMailCredentialsWithoutRestartingApplicationContext() {
		assertEquals(0, mailConfigRepository.count());
		var emptyConfig = adminMailStub.getMailConfig(Empty.getDefaultInstance());
		assertEquals("", emptyConfig.getEmail());
		assertFalse(emptyConfig.getPasswordConfigured());

		mailConfigService.update(MailConfigUpdateData.builder()
				.email(OLD_EMAIL)
				.password(OLD_PASSWORD)
				.notificationRecipient(OLD_NOTIFICATION_RECIPIENT)
				.resetPassword(false)
				.build());
		var configuredMail = adminMailStub.getMailConfig(Empty.getDefaultInstance());
		assertEquals(OLD_EMAIL, configuredMail.getEmail());
		assertTrue(configuredMail.getPasswordConfigured());

		String rawPasswordBefore = rawStoredPassword();
		List<MailCredentials> credentialsUsedForSending = new ArrayList<>();
		doAnswer(_ -> {
			credentialsUsedForSending.add(mailConfigService.get());
			return null;
		}).when(mailSenderService).send(any(MailMessage.class));

		ApplicationContext contextBefore = applicationContext;

		stub.saveOrder(createOrder("before update"));

		verify(mailSenderService).send(argThat(message ->
				OLD_NOTIFICATION_RECIPIENT.equals(message.to())
						&& message.subject().matches("SD-\\d{8}")
						&& message.text().contains("Создатель: test-agent")));

		assertEquals(MailCredentials.builder()
						.email(OLD_EMAIL)
						.password(OLD_PASSWORD)
						.notificationRecipient(OLD_NOTIFICATION_RECIPIENT)
						.smtpHost("localhost")
						.smtpPort(1025)
						.smtpAuth(false)
						.smtpSsl(false)
						.build(),
				credentialsUsedForSending.getFirst());

		assertThrows(StatusRuntimeException.class,
				() -> agentMailStub.getMailConfig(Empty.getDefaultInstance()));
		assertThrows(StatusRuntimeException.class, () -> agentMailStub.updateConfig(
				UpdateConfigRequest.newBuilder()
						.setEmail("agent@example.com")
						.setPassword("agent-password")
						.setNotificationRecipient("agent-notifications@example.com")
						.build()));

		assertThrows(StatusRuntimeException.class, () -> adminMailStub.updateConfig(
				UpdateConfigRequest.newBuilder()
						.setEmail(NEW_EMAIL)
						.setNotificationRecipient(NEW_NOTIFICATION_RECIPIENT)
						.build()));
		assertThrows(StatusRuntimeException.class, () -> adminMailStub.updateConfig(
				UpdateConfigRequest.newBuilder()
						.setEmail(NEW_EMAIL)
						.setNotificationRecipient(NEW_NOTIFICATION_RECIPIENT)
						.setResetPassword(true)
						.build()));
		var updateResponse = adminMailStub.updateConfig(UpdateConfigRequest.newBuilder()
				.setEmail(NEW_EMAIL)
				.setPassword(NEW_PASSWORD)
				.setNotificationRecipient(NEW_NOTIFICATION_RECIPIENT)
				.setResetPassword(true)
				.setSmtpHost("smtp.example.com")
				.setSmtpPort(2525)
				.setSmtpAuth(true)
				.setSmtpSsl(false)
				.build());
		assertTrue(updateResponse.getSuccess());
		var updatedConfig = adminMailStub.getMailConfig(Empty.getDefaultInstance());
		assertEquals(NEW_NOTIFICATION_RECIPIENT, updatedConfig.getNotificationRecipient());
		assertEquals(2525, updatedConfig.getSmtpPort());
		assertEquals("smtp.example.com", updatedConfig.getSmtpHost());
		assertEquals(2525, mailConfigService.get().smtpPort());

		String rawPasswordAfter = rawStoredPassword();
		assertNotEquals(rawPasswordBefore, rawPasswordAfter);
		assertEquals(1, mailConfigRepository.count());

		stub.saveOrder(createOrder("after update"));

		verify(mailSenderService).send(argThat(message ->
				NEW_NOTIFICATION_RECIPIENT.equals(message.to())
						&& message.subject().matches("SD-\\d{8}")
						&& message.text().contains("Создатель: test-agent")));

		assertEquals(List.of(
						MailCredentials.builder()
								.email(OLD_EMAIL)
								.password(OLD_PASSWORD)
								.notificationRecipient(OLD_NOTIFICATION_RECIPIENT)
								.smtpHost("localhost")
								.smtpPort(1025)
								.smtpAuth(false)
								.smtpSsl(false)
								.build(),
						MailCredentials.builder()
								.email(NEW_EMAIL)
								.password(NEW_PASSWORD)
								.notificationRecipient(NEW_NOTIFICATION_RECIPIENT)
								.smtpHost("smtp.example.com")
								.smtpPort(2525)
								.smtpAuth(true)
								.smtpSsl(false)
								.build()),
				credentialsUsedForSending);

		adminMailStub.updateConfig(UpdateConfigRequest.newBuilder()
				.setEmail(OLD_EMAIL)
				.setPassword("replacement-app-password")
				.setNotificationRecipient(OLD_NOTIFICATION_RECIPIENT)
				.setResetPassword(true)
				.setSmtpHost("smtp.example.com")
				.setSmtpPort(2525)
				.setSmtpAuth(true)
				.setSmtpSsl(false)
				.build());
		assertEquals(MailCredentials.builder()
						.email(OLD_EMAIL)
						.password("replacement-app-password")
						.notificationRecipient(OLD_NOTIFICATION_RECIPIENT)
						.smtpHost("smtp.example.com")
						.smtpPort(2525)
						.smtpAuth(true)
						.smtpSsl(false)
						.build(),
				mailConfigService.get());

		Long firstOrderId = jdbcTemplate.queryForObject(
				"select min(id) from sd_mail_test.orders", Long.class);
		stub.saveOrder(OrderProto.newBuilder()
				.setId(firstOrderId)
				.setName("updated existing order")
				.build());
		verifyNoMoreInteractions(mailSenderService);
		assertSame(contextBefore, applicationContext);
	}

	@Test
	void encryptsDemoMailSeedWhenLoadingConfiguration() {
		jdbcTemplate.update("""
						insert into sd_mail_test.mail_config
							(email_encrypted, password_encrypted, notification_recipient)
						values (?, ?, ?)
						""",
				"demo-seed:sender@example.com",
				"demo-seed:test-password-123",
				"demo-seed:recipient@example.com");

		mailConfigService.load();

		assertEquals(MailCredentials.builder()
						.email("sender@example.com")
						.password("test-password-123")
						.notificationRecipient("recipient@example.com")
						.smtpHost("localhost")
						.smtpPort(1025)
						.smtpAuth(false)
						.smtpSsl(false)
						.build(),
				mailConfigService.get());
		assertNotEquals("test-password-123", rawStoredPassword());
		assertFalse(rawStoredPassword().startsWith("demo-seed:"));
	}

	private OrderProto createOrder(String name) {
		LocalDate start = LocalDate.now().plusDays(1);
		LocalDate deadline = start.plusDays(30);
		Long clientId = jdbcTemplate.queryForObject(
				"select min(id) from sd_mail_test.clients", Long.class);
		Long agentId = jdbcTemplate.queryForObject(
				"select min(id) from sd_mail_test.agents", Long.class);
		return OrderProto.newBuilder()
				.setName(name)
				.setDescription("Created through gRPC")
				.setStartLine(toProtoDate(start))
				.setDeadLine(toProtoDate(deadline))
				.setClient(com.example.vsd.grpc.messages.ClientProto.newBuilder()
						.setId(clientId)
						.setName("Client"))
				.setAgent(com.example.vsd.grpc.messages.AgentProto.newBuilder()
						.setId(agentId)
						.setName("Agent"))
				.build();
	}

	private Date toProtoDate(LocalDate date) {
		return Date.newBuilder()
				.setYear(date.getYear())
				.setMonth(date.getMonthValue())
				.setDay(date.getDayOfMonth())
				.build();
	}

	private String createToken(String role) {
		Instant now = Instant.now();
		JwtClaimsSet claims = JwtClaimsSet.builder()
				.issuer("mail-config-integration-test")
				.issuedAt(now)
				.expiresAt(now.plusSeconds(300))
				.subject("test-agent")
				.claim("authorities", List.of(role))
				.build();
		JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();
		return jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
	}

	private String rawStoredPassword() {
		return jdbcTemplate.queryForObject(
				"select password_encrypted from sd_mail_test.mail_config", String.class);
	}
}
