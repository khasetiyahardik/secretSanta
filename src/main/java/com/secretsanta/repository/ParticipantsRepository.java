package com.secretsanta.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.secretsanta.model.Participants;

@Repository
public interface ParticipantsRepository
		extends JpaRepository<Participants, Integer>, JpaSpecificationExecutor<Participants> {

	@Query("SELECT p FROM Participants p WHERE p.emailAddress=:email")
	Optional<Participants> findOneByEmailIgnoreCase(String email);
}
