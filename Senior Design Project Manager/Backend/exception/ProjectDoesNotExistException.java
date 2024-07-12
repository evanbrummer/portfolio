package com.iastate.project_matcher.exception;

public class ProjectDoesNotExistException extends Exception{
    public ProjectDoesNotExistException(String message) {
        super(message);
    }
}
