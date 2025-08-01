package org.matcha.springbackend.repository;

import org.matcha.springbackend.entities.SubredditEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SubredditRepository extends JpaRepository<SubredditEntity, UUID> {
    Optional<SubredditEntity> findByName(String name);
}
