package com.cryptocurrency.converter.repository;

import com.cryptocurrency.converter.entity.History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoryRepository extends CrudRepository<History, Long> {
    List<History> findByUserId(long userId);
}
