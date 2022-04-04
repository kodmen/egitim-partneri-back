package com.hanrideb.service.dto;

import java.util.List;

public class TestAnswerDto {

    private Long testId;
    private List<String> answers;

    public Long getTestId() {
        return testId;
    }

    public void setTestId(Long testId) {
        this.testId = testId;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }
}
