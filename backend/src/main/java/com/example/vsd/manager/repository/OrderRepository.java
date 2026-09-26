package com.example.vsd.manager.repository;

import com.example.vsd.manager.enity.Order;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends
		JpaRepository<@NonNull Order, @NonNull Long>,
		JpaSpecificationExecutor<@NonNull Order> {

	@Query("""
			select o from Order o
			where o.deleted = false
			and(
			lower(o.serviceDeskNumber) like lower(concat('%', :text, '%')) or
			lower(o.name) like lower(concat('%', :text, '%')))
			""")
	List<Order> findOrders(String text);

	List<Order> findAllByDeletedTrue();

	List<Order> findAllByDeletedFalse();
}
