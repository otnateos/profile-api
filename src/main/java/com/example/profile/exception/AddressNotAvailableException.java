package com.example.profile.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @since 23/2/19.
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason="Address not available")
public class AddressNotAvailableException extends Exception
{
}
