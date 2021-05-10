/**
 * 
 */
package com.assignment.dto;

import javax.validation.constraints.NotNull;

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
public class TimeDTO {
	@NotNull(message ="error.blank")
	private Integer year;

	@NotNull(message ="error.blank")
	private Integer month;

}
