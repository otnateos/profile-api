package com.example.profile.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @since 23/2/19.
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason="Profile does not exist for user")
public class ProfileNotFoundException extends Exception
{
}
