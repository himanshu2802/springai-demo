package com.spring.ai.firstproject.first_project.enums;

public enum ExamType {
    MHT_CET("MHT-CET"),
    NEET_UG_AI("NEET UG All India"),
    NEET_UG_STATE("NEET UG State");

    private final String label;

    ExamType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return label;
    }
}