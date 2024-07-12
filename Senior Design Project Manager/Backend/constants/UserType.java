package com.iastate.project_matcher.constants;

import java.util.HashSet;

public enum UserType {
    Advisor, Board, Client, Instructor, Student;

    public static HashSet<UserType> parseFromRecord(String record) {
        String[] split = record.replaceAll("[\\[\\]]", "").split(", ");
        HashSet<UserType> result = new HashSet<>();
        for (String type : split) {
            result.add(UserType.valueOf(type));
        }
        return result;
    }
}
