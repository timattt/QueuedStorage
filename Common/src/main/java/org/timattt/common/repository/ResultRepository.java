package org.timattt.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.timattt.common.model.Result;

@Repository
public interface ResultRepository extends JpaRepository<Result, Integer> {
}
