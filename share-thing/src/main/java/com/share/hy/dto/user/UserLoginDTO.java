package com.share.hy.dto.user;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
public class UserLoginDTO implements Serializable {
	private static final long serialVersionUID = -12318872209795986L;

	@NotEmpty(message = "account can not null")
	private String userName;

	/**
	 * md5
	 */
	@NotEmpty(message = "password can not null")
	private String password;

}
