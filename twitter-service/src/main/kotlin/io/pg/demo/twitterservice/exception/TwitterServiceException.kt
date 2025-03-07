package io.pg.demo.twitterservice.exception

class TwitterServiceException : RuntimeException {
    constructor() : super()
    constructor(message: String) : super(message)
    constructor(message: String, cause: Throwable) : super(message, cause)
}