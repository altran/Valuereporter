package org.valuereporter.gui;

/**
 * Created by baardl on 2017-01-16.
 */

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.UNPROCESSABLE_ENTITY, reason="No input parameters may contain javascript nor HTML.")
public class IllegalInputException extends RuntimeException {
}
