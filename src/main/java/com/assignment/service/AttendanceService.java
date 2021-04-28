/**
 * 
 */
package com.assignment.service;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.assignment.dto.AttendanceDTO;
import com.assignment.dto.MessageDTO;
import com.assignment.entity.AttendanceEntity;
import com.assignment.repository.AttendanceRepository;

/**
 * @author baovd
 *
 */
@Service
public class AttendanceService {
	@Autowired
	private AttendanceRepository attendanceRepository;
	@PersistenceContext
	private EntityManager em;

	public MessageDTO checkIn(Integer employeeId) {
		MessageDTO message = new MessageDTO();
		AttendanceEntity entity = new AttendanceEntity();
		try {
			Date date = new Date();
			entity.setEmployeeId(employeeId);
			entity.setCheckInTime(date);
			String hql = "FROM AttendanceEntity a WHERE DATE(a.checkInTime) = Date(now()) AND a.employeeId = :id  ";
			Query q = em.createQuery(hql);
			q.setParameter("id", employeeId);
			List<AttendanceEntity> ls = q.getResultList();
			if (ls.size() > 0) {
				message.setMessage("Employee Has Already Checked In Today");
				return message;
			}
			attendanceRepository.save(entity);
			message.setMessage("Check-In Successfully");
			return message;
		} catch (ConstraintViolationException contrainException) {
			message.setMessage("Employee Did Not Exist");
			return message;
		} catch (Exception e) {
			throw e;
		}
	}

	public void checkOut(Integer employeeId) {
//		try {
//			Date date = new Date();
//			attendanceRepository.checkOut(date, employeeId);
//		} catch (Exception e) {
//			throw e;
//		}
	}
}
