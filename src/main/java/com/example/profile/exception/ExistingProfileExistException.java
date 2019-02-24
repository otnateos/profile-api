package com.example.profile.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @since 23/2/19.
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason="Existing profile exist for user")
public class ExistingProfileExistException extends Exception
{
}
