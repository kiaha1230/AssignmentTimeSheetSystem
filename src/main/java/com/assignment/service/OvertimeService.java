/**
 * 
 */
package com.assignment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.assignment.dto.DecideOTRequestDTO;
import com.assignment.dto.OvertimeDTO;
import com.assignment.entity.OvertimeEntity;
import com.assignment.repository.OvertimeRepository;

/**
 * @author baovd
 *
 */
@Service
public class OvertimeService {
	@Autowired
	private OvertimeRepository overtimeRepository;

	public void createOTRequest(OvertimeDTO dto) {
		try {
			OvertimeEntity entity = new OvertimeEntity(dto);
			entity.setStatus(0);
			overtimeRepository.save(entity);
		} catch (Exception e) {
			throw e;
		}
	}

	public void confirmOrRejectOT(DecideOTRequestDTO dto) {
		try {
			overtimeRepository.confirmOrRejectOT(dto);
		} catch (Exception e) {
			throw e;
		}

	}

}
