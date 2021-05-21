/**
 * 
 */
package com.assignment.service;

import java.io.FileOutputStream;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
import com.assignment.enumm.GlobalValue;
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
		} catch (Exception e) {
			Throwable t = e.getCause();
			if (t instanceof ConstraintViolationException) {
				message.setMessage("Employee Did Not Exist");
				return message;
			} else {
				throw e;
			}
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

		// get user id in db with username from security context
		SecurityContext securityContext = SecurityContextHolder.getContext();
		String username = securityContext.getAuthentication().getName();
		String hql = "FROM EmployeeEntity e WHERE e.username = :username";
		Query q = em.createQuery(hql);
		q.setParameter("username", username);

		@SuppressWarnings("unchecked")
		List<EmployeeEntity> employeeLs = q.getResultList();
		EmployeeDTO employeeDto = new EmployeeDTO(employeeLs.get(0));
		// end

		List<AttendanceDTO> lsAttendanceDto = getRecordsInMonth(year, month, employeeDto.getId());

		// get name (2/5)
		returnDto.setName(employeeDto.getFullName());

		// get list attendance (3/5)
		returnDto.setLsAttandance(lsAttendanceDto);

		// get leave days (1/5) get late days (4/5) and leave early (5/5)
		Set<String> workingDaysList;
		LocalDate currentDate = LocalDate.now();
		if (currentDate.getMonthValue() == month && currentDate.getYear() == year) {
			workingDaysList = getMonthWorkingDays(currentDate);
		} else {
			workingDaysList = getMonthWorkingDays(year, month);
		}
		int workingDaysInMonth = workingDaysList.size();
		int realWorkingday = 0;

		int lateDays = 0;
		int leaveEarly = 0;

		for (AttendanceDTO attendanceDTO : lsAttendanceDto) {
			if (attendanceDTO.isLate() == true) {
				lateDays++;
			}
			if (attendanceDTO.isLeaveEarly() == true) {
				leaveEarly++;
			}
			if (attendanceDTO.getCheckInTime() != null && attendanceDTO.getCheckOutTime() != null) {
				realWorkingday++;
			}
		}
		int leaveDays = workingDaysInMonth - realWorkingday;

		returnDto.setLateDays(lateDays);
		returnDto.setLeaveEarlyDays(leaveEarly);
		returnDto.setLeaveDays(leaveDays);
		returnDto.setMonth(month);
		returnDto.setYear(year);
		return returnDto;
	}

	@Transactional
	public List<AttendanceStatisticsDTO> allAttendanceStatistics(int year, int month) {
		List<AttendanceStatisticsDTO> returnLs = new ArrayList<AttendanceStatisticsDTO>();

		// get list employee of company
		String hql = "FROM EmployeeEntity e where e.active = 1";
		Query q = em.createQuery(hql);

		@SuppressWarnings("unchecked")
		List<EmployeeEntity> lsEmployee = q.getResultList();
		List<EmployeeDTO> lsEmployeeDTO = new ArrayList<EmployeeDTO>();
		lsEmployee.stream().forEach(e -> lsEmployeeDTO.add(new EmployeeDTO(e)));
		// end
		for (EmployeeDTO employeeDto : lsEmployeeDTO) {
			AttendanceStatisticsDTO attendanceStatisticsDTO = new AttendanceStatisticsDTO();

			List<AttendanceDTO> lsAttendanceDto = getRecordsInMonth(year, month, employeeDto.getId());

			// get name (2/5)
			attendanceStatisticsDTO.setName(employeeDto.getFullName());

			// get list attendance (3/5)
			attendanceStatisticsDTO.setLsAttandance(lsAttendanceDto);

			// get leave days (1/5) get late days (4/5) and leave early (5/5)
			Set<String> workingDaysList;
			LocalDate currentDate = LocalDate.now();
			if (currentDate.getMonthValue() == month && currentDate.getYear() == year) {
				workingDaysList = getMonthWorkingDays(currentDate);
			} else {
				workingDaysList = getMonthWorkingDays(year, month);
			}
			int workingDaysInMonth = workingDaysList.size();
			int realWorkingday = 0;

			int lateDays = 0;
			int leaveEarly = 0;

			for (AttendanceDTO attendanceDTO : lsAttendanceDto) {
				if (attendanceDTO.isLate() == true) {
					lateDays++;
				}
				if (attendanceDTO.isLeaveEarly() == true) {
					leaveEarly++;
				}
				if (attendanceDTO.getCheckInTime() != null && attendanceDTO.getCheckOutTime() != null) {
					realWorkingday++;
				}
			}
			int leaveDays = workingDaysInMonth - realWorkingday;

			attendanceStatisticsDTO.setLateDays(lateDays);
			attendanceStatisticsDTO.setLeaveEarlyDays(leaveEarly);
			attendanceStatisticsDTO.setLeaveDays(leaveDays);
			attendanceStatisticsDTO.setMonth(month);
			attendanceStatisticsDTO.setYear(year);
			returnLs.add(attendanceStatisticsDTO);
		}
		return returnLs;
	}

	@SuppressWarnings("unchecked")
	public List<AttendanceDTO> getRecordsInMonth(int year, int month, int employeeId) {
		List<AttendanceDTO> lsAttendanceDto = new ArrayList<AttendanceDTO>();
		String hql = "FROM AttendanceEntity a WHERE a.employeeId = :id AND YEAR(checkInTime) = :year AND MONTH(checkInTime)= :month";
		Query q = em.createQuery(hql);
		q.setParameter("id", employeeId);
		q.setParameter("year", year);
		q.setParameter("month", month);
		List<AttendanceEntity> lsAttendance = new ArrayList<>();
		lsAttendance = q.getResultList();
		lsAttendance.stream().forEach(a -> lsAttendanceDto.add(new AttendanceDTO(a)));

		// check late or leave soon
		for (AttendanceDTO attendanceDTO : lsAttendanceDto) {
			if (attendanceDTO.getCheckInTime().toLocalTime().isAfter(GlobalValue.startWorkingTime)) {
				attendanceDTO.setLate(true);
			}
			if (attendanceDTO.getCheckOutTime().toLocalTime().isBefore(GlobalValue.leaveTime)) {
				attendanceDTO.setLeaveEarly(true);
			}
		}

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

	// get month working day but from the first day of that month to that day
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

	public void exportExcel(int year, int month) {
		AttendanceStatisticsDTO dto = personalAttendanceStatistics(year, month);

		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet();

		// title font
		Font titleFont = workbook.createFont();
		titleFont.setFontName("Times New Roman");
		titleFont.setBold(true);
		titleFont.setFontHeightInPoints((short) 20);
		// end
		// common font
		Font commonFont = workbook.createFont();
		commonFont.setFontName("Times New Roman");
		commonFont.setFontHeightInPoints((short) 11);
		// end
		// header font
		Font headerfont = workbook.createFont();
		headerfont.setFontName("Times New Roman");
		headerfont.setBold(true);
		headerfont.setFontHeightInPoints((short) 14);
		// end
		// title cell style
		CellStyle titleStyle = workbook.createCellStyle();
		titleStyle.setAlignment(HorizontalAlignment.CENTER);
		titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		titleStyle.setFont(titleFont);
		// end
		// header cell style
		CellStyle headerStyle = workbook.createCellStyle();
		headerStyle.setAlignment(HorizontalAlignment.CENTER);
		headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		headerStyle.setFont(headerfont);
		// data cell style
		CellStyle dataStyle = workbook.createCellStyle();
		dataStyle.setAlignment(HorizontalAlignment.CENTER);
		dataStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		dataStyle.setFont(commonFont);
		// end
		// data name cell style
		CellStyle dataNameStyle = workbook.createCellStyle();
		dataNameStyle.setAlignment(HorizontalAlignment.LEFT);
		dataNameStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		dataNameStyle.setFont(commonFont);
		// end
		// set title
		Row titleRow = sheet.createRow(0);
		titleRow.setHeight((short) 700);
		Cell titleCell = titleRow.createCell(0);
		titleCell.setCellValue("BẢNG CHẤM CÔNG THÁNG " + dto.getMonth() + " NĂM " + dto.getYear());
		titleCell.setCellStyle(titleStyle);
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 4));
		// end
		// set header
		Row headerRow = sheet.createRow(1);
		Cell sttCell = headerRow.createCell(0);
		sttCell.setCellValue("STT");
		sttCell.setCellStyle(headerStyle);
		Cell nameCell = headerRow.createCell(1);
		nameCell.setCellValue("Họ Tên");
		nameCell.setCellStyle(headerStyle);
		Cell dateCell = headerRow.createCell(2);
		dateCell.setCellValue("Ngày");
		dateCell.setCellStyle(headerStyle);
		Cell checkInCell = headerRow.createCell(3);
		checkInCell.setCellValue("Chấm Công Vào");
		checkInCell.setCellStyle(headerStyle);
		Cell checkOutCell = headerRow.createCell(4);
		checkOutCell.setCellValue("Chấm Công Ra");
		checkOutCell.setCellStyle(headerStyle);
		// end
		int stt = 1;
		for (AttendanceDTO AttendanceDto : dto.getLsAttandance()) {
			Row dataRow = sheet.createRow(stt + 1);
			Cell sttDataCell = dataRow.createCell(0);
			sttDataCell.setCellStyle(dataStyle);
			sttDataCell.setCellValue(stt);

			Cell nameDataCell = dataRow.createCell(1);
			nameDataCell.setCellStyle(dataNameStyle);
			nameDataCell.setCellValue(dto.getName());

			Cell dateDataCell = dataRow.createCell(2);
			dateDataCell.setCellStyle(dataStyle);
			dateDataCell.setCellValue(AttendanceDto.getCheckInTime().toLocalDate().toString());

			Cell checkInDataCell = dataRow.createCell(3);
			checkInDataCell.setCellStyle(dataStyle);
			checkInDataCell.setCellValue(AttendanceDto.getCheckInTime().toLocalTime().toString());

			Cell checkOutDataCell = dataRow.createCell(4);
			checkOutDataCell.setCellStyle(dataStyle);
			checkOutDataCell.setCellValue(AttendanceDto.getCheckOutTime().toLocalTime().toString());

			stt++;
		}

		Row workingDaysRow = sheet.createRow(stt + 1);
		Cell workingDaysCell = workingDaysRow.createCell(1);
		workingDaysCell.setCellValue("Công Chuẩn Trong Tháng");
		workingDaysCell.setCellStyle(headerStyle);

		Cell workingDaysNumberCell = workingDaysRow.createCell(2);
		workingDaysNumberCell.setCellValue(getMonthWorkingDays(year, month).size());
		workingDaysNumberCell.setCellStyle(headerStyle);

		// last config
		sheet.setColumnWidth(0, 1700);
		sheet.setColumnWidth(1, 11000);
		sheet.setColumnWidth(2, 7000);
		sheet.setColumnWidth(3, 7000);
		sheet.setColumnWidth(4, 7000);
		//
		// set data

		// end
		try (FileOutputStream out = new FileOutputStream("C:\\Users\\Administrator\\Desktop\\test.xlsx")) {
			workbook.write(out);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		int rowCount = 0;

	}

}
