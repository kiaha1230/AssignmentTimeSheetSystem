package com.assignment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.assignment.dto.DecideOTRequestDTO;
import com.assignment.entity.OvertimeEntity;

public interface OvertimeRepository extends JpaRepository<OvertimeEntity, Integer> {
	@Transactional
	@Modifying
	@Query("update OvertimeEntity o set o.status = :#{#dto.status} where o.id = :#{#dto.id}")
	void confirmOrRejectOT(@Param("dto") DecideOTRequestDTO dto);

}
