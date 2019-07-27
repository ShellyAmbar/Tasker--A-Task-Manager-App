package com.shellyAmbar.ambar.workoutplanner.Models;

public class ModelTask {
    private String taskName;
    private String taskBody;
    private String alertDate;
    private String taskAlerted;
    private String important;
    private String deleted;
    private String category;
    private String keyId;
    private String alarm_id;


    public ModelTask(String taskName, String taskBody, String alertDate, String taskAlerted, String important, String deleted, String category, String keyId, String alarm_id) {
        this.taskName = taskName;
        this.taskBody = taskBody;
        this.alertDate = alertDate;
        this.taskAlerted = taskAlerted;
        this.important = important;
        this.deleted = deleted;
        this.category = category;
        this.keyId = keyId;
        this.alarm_id = alarm_id;
    }

    public ModelTask() {
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getKeyId() {
        return keyId;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }



    public String getImportant() {
        return important;
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }

    public void setImportant(String important) {
        this.important = important;
    }

    public String getAlertDate() {
        return alertDate;
    }

    public void setAlertDate(String alertDate) {
        this.alertDate = alertDate;
    }

    public String getTaskAlerted() {
        return taskAlerted;
    }

    public void setTaskAlerted(String taskAlerted) {
        this.taskAlerted = taskAlerted;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskBody() {
        return taskBody;
    }

    public void setTaskBody(String taskBody) {
        this.taskBody = taskBody;
    }

    public String getAlarm_id() {
        return alarm_id;
    }

    public void setAlarm_id(String alarm_id) {
        this.alarm_id = alarm_id;
    }
}
