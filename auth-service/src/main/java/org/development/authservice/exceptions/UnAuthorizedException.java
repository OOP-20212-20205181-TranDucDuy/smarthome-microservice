package org.development.authservice.exceptions;

public class UnAuthorizedException extends RuntimeException {

    public UnAuthorizedException() {
        super("Unauthorized");
    }
}
