package org.development.userservice.exceptions;

public class UnAuthorizedException extends RuntimeException {

    public UnAuthorizedException() {
        super("Unauthorized");
    }
}
