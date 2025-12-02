package com.techbarn.webapp.model;

import java.time.LocalDateTime;

public class Question {
    private int questionId;
    private String title;
    private String contents;
    private String status;
    private LocalDateTime dateAsked;
    private int userId;
    private String reply;
    private Integer repId;

    // getters / setters
    public int getQuestionId() { return questionId; }
    public void setQuestionId(int questionId) { this.questionId = questionId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getContents() { return contents; }
    public void setContents(String contents) { this.contents = contents; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getDateAsked() { return dateAsked; }
    public void setDateAsked(LocalDateTime dateAsked) { this.dateAsked = dateAsked; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getReply() { return reply; }
    public void setReply(String reply) { this.reply = reply; }
    public Integer getRepId() { return repId; }
    public void setRepId(Integer repId) { this.repId = repId; }
}
