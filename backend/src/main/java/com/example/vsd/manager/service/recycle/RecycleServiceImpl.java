package com.example.vsd.manager.service.recycle;

import com.example.vsd.grpc.messages.BaseEntityProto;
import com.example.vsd.manager.enity.Agent;
import com.example.vsd.manager.enity.BaseEntity;
import com.example.vsd.manager.enity.Client;
import com.example.vsd.manager.enity.Order;
import com.example.vsd.manager.mapper.RecycleMapper;
import com.example.vsd.manager.model.TypedEntity;
import com.example.vsd.manager.repository.AgentRepository;
import com.example.vsd.manager.repository.ClientRepository;
import com.example.vsd.manager.repository.OrderRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RecycleServiceImpl implements RecycleService {

	OrderRepository orderRepository;
	ClientRepository clientRepository;
	AgentRepository agentRepository;

	@Override
	public List<BaseEntityProto> apiFindAll() {
		return Stream.of(orderRepository.findAllByDeletedTrue(),
						clientRepository.findAllByDeletedTrue(),
						agentRepository.findAllByDeletedTrue())
				.flatMap(List::stream)
				.map(entity -> (BaseEntity) entity)
				.sorted(Comparator.comparing(BaseEntity::getLastUpdated).reversed()
						.thenComparing(e -> ((TypedEntity) e).getType(),
								Comparator.nullsLast(Comparator.naturalOrder()))
						.thenComparing(BaseEntity::getId,
								Comparator.nullsLast(Comparator.naturalOrder()))
				)
				.map(RecycleMapper::toProto)
				.toList();
	}

	@Override
	public List<BaseEntityProto> findRecycle(String text) {
		var entities = getBaseEntities(text);
		return entities.stream()
				.sorted(Comparator.comparing(BaseEntity::getLastUpdated).reversed()
						.thenComparing(e -> ((TypedEntity) e).getType(),
								Comparator.nullsLast(Comparator.naturalOrder()))
						.thenComparing(BaseEntity::getId,
								Comparator.nullsLast(Comparator.naturalOrder()))
				)
				.map(RecycleMapper::toProto)
				.toList();
	}

	private @NonNull List<BaseEntity> getBaseEntities(@NonNull String text) {
		String lower = text.toLowerCase();
		return Stream.of(orderRepository.findAllByDeletedTrue(),
						clientRepository.findAllByDeletedTrue(),
						agentRepository.findAllByDeletedTrue())
				.flatMap(List::stream)
				.map(entity -> (BaseEntity) entity)
				.filter(entity -> entity.getServiceDeskNumber().toLowerCase().contains(lower) ||
						((TypedEntity) entity).getName().toLowerCase().contains(lower))
				.toList();
	}

	private void saveEntity(@NonNull BaseEntity entity) {
		switch (entity) {
			case Order e -> orderRepository.save(e);
			case Client e -> clientRepository.save(e);
			case Agent e -> agentRepository.save(e);
			default -> throw new IllegalStateException("Unexpected value: " + entity);
		}
	}
}
