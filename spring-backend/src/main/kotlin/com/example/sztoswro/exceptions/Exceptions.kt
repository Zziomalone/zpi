package com.example.sztoswro.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import javax.management.OperationsException
import javax.security.auth.message.AuthException

//204
@ResponseStatus(HttpStatus.NO_CONTENT)
class NoContentException(message:String) : RuntimeException(message, null, true, false)

//400
@ResponseStatus(HttpStatus.BAD_REQUEST)
class BadRequestException(message: String): RuntimeException(message, null, true, false)

//401
@ResponseStatus(HttpStatus.UNAUTHORIZED)
class NoAuthorizationException(message:String) : AuthException(message)

//403
@ResponseStatus(HttpStatus.FORBIDDEN)
class ForbiddenException(message: String) : OperationsException(message)

//500
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
class InternalServerException(message:String) : Exception(message, null, true, false)

@ResponseStatus(HttpStatus.BAD_REQUEST)
class UserAlreadyExists(message: String): Exception(message, null, true, false)

