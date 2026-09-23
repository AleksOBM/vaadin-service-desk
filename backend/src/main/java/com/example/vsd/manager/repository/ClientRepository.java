package com.example.vsd.manager.repository;

import com.example.vsd.manager.enity.Client;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepository extends
		JpaRepository<@NonNull Client, @NonNull Long>,
		JpaSpecificationExecutor<@NonNull Client> {

	@Query("""
			select c from Client c
			where c.deleted = false
			and(
			lower(c.serviceDeskNumber) like lower(concat('%', :text, '%')) or
			lower(c.name) like lower(concat('%', :text, '%')))
			""")
	List<Client> findClients(String text);

	List<Client> findAllByDeletedTrue();

	List<Client> findAllByDeletedFalse();

	boolean existsById(@NonNull Long id);

	@Query("""
			select c.deleted from Client c
			where c.id = :id
			""")
	boolean checkDeleted(@NonNull Long id);
}
