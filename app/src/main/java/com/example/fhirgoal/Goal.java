package com.example.fhirgoal;

import android.util.Range;

import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;

public class Goal {

    private String language;
    private String text;
    private List<String> identifier;
    private String lifecycleStatus;
    private List<String> achievementStatus;
    private List<String> category;
    private List<String> priority;
    private String description;
    private String subject;
    private LocalDateTime startDate;


    private String startCodeableConcept;
    private List<String> target;
    private List<String> measure;
    private String detailQuantity;

    private Range detailRange;
    private String detailCodeableConcept;
    private String detailString;
    private Boolean detailBoolean;
    private Integer detailInteger;
    private String detailRatio;
    private LocalDateTime dueDate;
    private String dueDuration;
    private LocalDateTime statusDate;
    private String statusReason;
    private List<String> note;
    private String expressedBy;
    private String addresses;
    private String outcomeCode;
    private List<String> outcomeReference;

    public Goal() {
    }


}
