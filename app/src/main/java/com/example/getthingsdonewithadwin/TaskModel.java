package com.example.getthingsdonewithadwin;

public class TaskModel {
    String taskTitle;
    String taskDesc;
    String taskDate;
    String taskTime;

    public TaskModel(){}

    public TaskModel(String taskTitle, String taskDate, String taskTime, String taskDesc) {
        this.taskTitle = taskTitle;
        this.taskDesc= taskDesc;
        this.taskDate = taskDate;
        this.taskTime = taskTime;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public String getTaskDesc() {
        return taskDesc;
    }

    public void setTaskDesc(String taskDesc) {
        this.taskDesc = taskDesc;
    }

    public String getTaskDate() {
        return taskDate;
    }

    public void setTaskDate(String taskDate) {
        this.taskDate = taskDate;
    }

    public String getTaskTime() {
        return taskTime;
    }

    public void setTaskTime(String taskTime) {
        this.taskTime = taskTime;
    }
}
