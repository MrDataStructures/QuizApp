package com.beaconfire.quizapp.model;

public class Choice {

    private int choice_id;
    private int question_id;
    private String description;
    private boolean is_correct;
    private boolean is_selected;

    public boolean getIs_selected() {
        return is_selected;
    }

    public void setIs_selected(boolean is_selected) {
        this.is_selected = is_selected;
    }

    public int getChoice_id() {
        return choice_id;
    }

    public void setChoice_id(int choice_id) {
        this.choice_id = choice_id;
    }

    public int getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(int question_id) {
        this.question_id = question_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isIs_correct() {
        return is_correct;
    }

    public void setIs_correct(boolean is_correct) {
        this.is_correct = is_correct;
    }
}
