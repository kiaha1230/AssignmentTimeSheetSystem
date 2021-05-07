/**
 * 
 */
package com.assignment.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.assignment.dto.AttendanceDTO;
import com.assignment.dto.EmployeeDTO;
import com.assignment.dto.MessageDTO;
import com.assignment.dto.statistics.AttendanceStatisticsDTO;
import com.assignment.entity.AttendanceEntity;
import com.assignment.entity.EmployeeEntity;
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
			LocalDateTime date = LocalDateTime.now();
			entity.setEmployeeId(employeeId);
			entity.setCheckInTime(date);
			String hql = "FROM AttendanceEntity a WHERE DATE(a.checkInTime) = Date(now()) AND a.employeeId = :id";
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

	@Transactional
	public MessageDTO checkOut(Integer employeeId) {
		MessageDTO message = new MessageDTO();
		try {
			LocalDateTime dateNow = LocalDateTime.now();
			String hql = "FROM AttendanceEntity a WHERE DATE(a.checkInTime) = Date(now()) AND a.employeeId = :id and a.checkOutTime is null";
			Query q = em.createQuery(hql);
			q.setParameter("id", employeeId);
			List<AttendanceEntity> ls = q.getResultList();
			if (ls.size() > 0) {
				AttendanceEntity entity = ls.get(0);
				if (entity.getCheckInTime().isBefore(dateNow)) {
					String hql2 = "UPDATE AttendanceEntity a SET checkOutTime = :date";
					Query q2 = em.createQuery(hql2);
					q2.setParameter("date", dateNow);
					q2.executeUpdate();
					message.setMessage("Check-Out Successfully");
				} else {
					message.setMessage("Check-Out Time Can Not Before Check-In Time");
				}
			} else {
				message.setMessage("You Haven't Check In Today");
			}
			return message;
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional
	public AttendanceStatisticsDTO personalAttendanceStatistics(int year, int month) {
		AttendanceStatisticsDTO returnDto = new AttendanceStatisticsDTO();
		// get user id in db with username
		SecurityContext securityContext = SecurityContextHolder.getContext();
		String username = securityContext.getAuthentication().getName();
		String hql = "FROM EmployeeEntity e WHERE e.username = :username";
		Query q = em.createQuery(hql);
		q.setParameter("username", username);
		List<EmployeeEntity> employeeLs = q.getResultList();
		EmployeeDTO employeeDto = new EmployeeDTO(employeeLs.get(0));
		// end

		// get leave days (1/5)
		Set<String> workingDaysList;
		LocalDate currentDate = LocalDate.now();
		if (currentDate.getMonthValue() == month && currentDate.getYear() == year) {
			workingDaysList = getMonthWorkingDays(currentDate);
		} else {
			workingDaysList = getMonthWorkingDays(year, month);
		}
		int workingDays = workingDaysList.size();
		List<AttendanceDTO> lsAttendanceDto = getRecordsInMonth(year, month, employeeDto.getId());
		int leaveDays = workingDays - lsAttendanceDto.size();
		returnDto.setLeaveDates(leaveDays);
		// get name (2/5)
		returnDto.setName(employeeDto.getFullName());

		// get list attendance (3/5)
		returnDto.setLsAttandance(lsAttendanceDto);

		// get late days (4/5)
		for (AttendanceDTO attendanceDTO : lsAttendanceDto) {
			if (attendanceDTO.getCheckInTime().isBefore(other))
		}
		
		// get leave early (5/5)

		return returnDto;
	}

	public List<AttendanceDTO> getRecordsInMonth(int year, int month, int employeeId) {
		List<AttendanceDTO> lsAttendanceDto = new ArrayList<AttendanceDTO>();
		String hql = "FROM AttendanceEntity a WHERE a.employeeId = :id AND YEAR(checkInTime) = :year AND MONTH(checkInTime)= :month";
		Query q = em.createQuery(hql);
		q.setParameter("id", employeeId);
		q.setParameter("year", year);
		q.setParameter("month", month);
		List<AttendanceEntity> lsAttendance = q.getResultList();
		lsAttendance.stream().forEach(a -> lsAttendanceDto.add(new AttendanceDTO(a)));
		return lsAttendanceDto;

	}

	public Set<String> getMonthWorkingDays(int year, int month) {
		LocalDate ld = LocalDate.of(year, month, 1);
		Month myMonth = ld.getMonth();
		Set<String> ls = new HashSet<>();
		while (ld.getMonth() == myMonth) {
			DayOfWeek dow = ld.getDayOfWeek();
			if ((dow.equals(DayOfWeek.SATURDAY)) || (dow.equals(DayOfWeek.SUNDAY))) {
			} else {
				ls.add(ld.toString());
			}
			ld = ld.plusDays(1);
		}
		return ls;
	}

	public Set<String> getMonthWorkingDays(LocalDate toDate) {
		Set<String> ls = new HashSet<>();
		if (toDate == null) {
			return ls;
		}
		LocalDate ld = LocalDate.of(toDate.getYear(), toDate.getMonth(), 1);
		LocalDate stopPoint = toDate;
		Month myMonth = ld.getMonth();
		while (ld.getMonth() == myMonth && ld.compareTo(stopPoint) < 0) {
			DayOfWeek dow = ld.getDayOfWeek();
			if ((dow.equals(DayOfWeek.SATURDAY)) || (dow.equals(DayOfWeek.SUNDAY))) {
			} else {
				ls.add(ld.toString());
			}
			ld = ld.plusDays(1);
		}
		return ls;
	}

}
