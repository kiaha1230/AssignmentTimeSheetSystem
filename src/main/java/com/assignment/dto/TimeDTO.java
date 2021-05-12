/**
 * 
 */
package com.assignment.dto;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author baovd
 *
 */
@ApiModel(description = "Class reprensenting a Time object include year and month")
@Getter
@Setter
@NoArgsConstructor
public class TimeDTO {
	@NotNull(message = "error.blank")
	@ApiModelProperty(notes = "integer year", required = true, example = "2021")
	private Integer year;

	@NotNull(message = "error.blank")
	@ApiModelProperty(notes = "integer month of year", required = true, example = "5")
	private Integer month;

}
