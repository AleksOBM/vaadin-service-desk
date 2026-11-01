package com.example.vsd.frontend.config;

import com.example.vsd.frontend.security.JwtClientInterceptor;
import com.example.vsd.grpc.services.admin.AdminAgentControllerGrpc;
import com.example.vsd.grpc.services.admin.AdminClientControllerGrpc;
import com.example.vsd.grpc.services.admin.AdminOrderControllerGrpc;
import com.example.vsd.grpc.services.admin.AdminRecycleControllerGrpc;
import com.example.vsd.grpc.services.free.FreeControllerGrpc;
import com.example.vsd.grpc.services.user.UserControllerGrpc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.grpc.client.ChannelBuilderOptions;
import org.springframework.grpc.client.GrpcChannelFactory;

import java.util.List;

@Configuration
//@ImportGrpcClients(target = "backend-service", basePackages = "com.example.vsd.grpc.services")
public class GrpcClientConfig {

	private static final String CHANNEL = "backend-service";

	@Bean
	public AdminAgentControllerGrpc.AdminAgentControllerBlockingStub adminAgentStub(
			GrpcChannelFactory channelFactory,
			JwtClientInterceptor interceptor) {
		return AdminAgentControllerGrpc.newBlockingStub(
				channelFactory.createChannel(CHANNEL, options(interceptor)));
	}

	@Bean
	public AdminClientControllerGrpc.AdminClientControllerBlockingStub adminClientStub(
			GrpcChannelFactory channelFactory,
			JwtClientInterceptor interceptor) {
		return AdminClientControllerGrpc.newBlockingStub(
				channelFactory.createChannel(CHANNEL, options(interceptor)));
	}

	@Bean
	public AdminOrderControllerGrpc.AdminOrderControllerBlockingStub adminOrderStub(
			GrpcChannelFactory channelFactory,
			JwtClientInterceptor interceptor) {
		return AdminOrderControllerGrpc.newBlockingStub(
				channelFactory.createChannel(CHANNEL, options(interceptor)));
	}

	@Bean
	public AdminRecycleControllerGrpc.AdminRecycleControllerBlockingStub adminRecycleStub(
			GrpcChannelFactory channelFactory,
			JwtClientInterceptor interceptor) {
		return AdminRecycleControllerGrpc.newBlockingStub(
				channelFactory.createChannel(CHANNEL, options(interceptor)));
	}

	@Bean
	public UserControllerGrpc.UserControllerBlockingStub userStub(
			GrpcChannelFactory channelFactory,
			JwtClientInterceptor interceptor) {
		return UserControllerGrpc.newBlockingStub(
				channelFactory.createChannel(CHANNEL, options(interceptor)));
	}

	@Bean
	public FreeControllerGrpc.FreeControllerBlockingStub freeStub(
			GrpcChannelFactory channelFactory,
			JwtClientInterceptor interceptor) {
		return FreeControllerGrpc.newBlockingStub(
				channelFactory.createChannel(CHANNEL, options(interceptor)));
	}

	private static ChannelBuilderOptions options(JwtClientInterceptor interceptor) {
		return ChannelBuilderOptions.defaults()
				.withInterceptors(List.of(interceptor));
	}

}