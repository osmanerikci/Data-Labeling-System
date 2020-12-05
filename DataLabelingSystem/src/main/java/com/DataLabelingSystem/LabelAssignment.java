package com.DataLabelingSystem;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

@JsonPropertyOrder({"instance id", "class label ids", "user id", "datetime"})
public class LabelAssignment {

    @JsonIgnore
    private Instance instance;
    @JsonIgnore
    private Label[] labels;
    @JsonIgnore
    private User user;
    private Date datetime;

    protected LabelAssignment(User user, Instance instance, Label[] labels) {
        this.user = user;
        this.instance = instance;
        this.labels = labels;
        this.datetime = new Date();
    }

    public Instance getInstance() {
        return instance;
    }

    public Label[] getLabels() {
        return labels;
    }

    public User getUser() {
        return user;
    }

    public Date getDatetime() {
        return datetime;
    }

    @JsonGetter("instance id")
    public int getInstanceId() {
        return this.getInstance().getId();
    }

    @JsonGetter("class label ids")
    public ArrayList<Integer> getClassLabelIds() {
        ArrayList<Integer> classLabelIds = new ArrayList<>();

        for (Label label : getLabels()) {
            classLabelIds.add(label.getId());
        }

        return classLabelIds;
    }

    @JsonGetter("user id")
    public int getUserId() {
        return this.getUser().getId();
    }

    @JsonGetter("datetime")
    public String getDatetimeString() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return dateFormat.format(this.datetime);
    }
}