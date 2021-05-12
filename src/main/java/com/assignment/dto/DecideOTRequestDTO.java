/**
 * 
 */
package com.assignment.dto;

import javax.validation.constraints.NotNull;

import com.assignment.entity.OvertimeEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author baovd
 *
 */
@ApiModel(description = "Class reprensenting a decision of PM for OT request ")
@Getter
@Setter
@NoArgsConstructor
public class DecideOTRequestDTO {
	@ApiModelProperty(notes = "Unique identifier of the overtime request", required = true, example = "1")
	@NotNull
	private Integer id;
	@ApiModelProperty(notes = "Status of the ot request", required = true, example = "1")
	@NotNull
	private Integer status;

	public DecideOTRequestDTO(OvertimeEntity entity) {
		this.id = entity.getId();
		this.status = entity.getStatus();
	}
}
