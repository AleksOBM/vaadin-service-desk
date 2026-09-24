package com.example.vsd.manager.repository;

import com.example.vsd.manager.enity.Agent;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AgentRepository extends
		JpaRepository<@NonNull Agent, @NonNull Long>,
		JpaSpecificationExecutor<@NonNull Agent> {

	@Query("""
			select a from Agent a
			where a.deleted = false
			and(
			lower(a.serviceDeskNumber) like lower(concat('%', :text, '%')) or
			lower(a.name) like lower(concat('%', :text, '%')))
			""")
	List<Agent> findAgents(String text);

	List<Agent> findAllByDeletedTrue();

	List<Agent> findAllByDeletedFalse();

	Optional<Agent> findByName(String name);

	boolean existsById(@NonNull Long id);

	@Query("""
			select a.deleted from Agent a
			where a.id = :id
			""")
	boolean checkDeleted(@NonNull Long id);

	boolean existsByName(String name);
}
