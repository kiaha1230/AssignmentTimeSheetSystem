/**
 * 
 */
package com.assignment.dto;

import javax.validation.constraints.NotNull;

import com.assignment.entity.OvertimeEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author baovd
 *
 */
@Getter
@Setter
@NoArgsConstructor
public class DecideOTRequestDTO {
	@NotNull
	private Integer id;
	@NotNull
	private Integer status;

	public DecideOTRequestDTO(OvertimeEntity entity) {
		this.id = entity.getId();
		this.status = entity.getStatus();
	}
}
