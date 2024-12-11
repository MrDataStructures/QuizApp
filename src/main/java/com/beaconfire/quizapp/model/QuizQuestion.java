package com.beaconfire.quizapp.model;

public class QuizQuestion {

    private int qq_id;
    private int quiz_id;
    private int question_id;
    private int user_choice_id;

    public int getQq_id() {
        return qq_id;
    }

    public void setQq_id(int qq_id) {
        this.qq_id = qq_id;
    }

    public int getQuiz_id() {
        return quiz_id;
    }

    public void setQuiz_id(int quiz_id) {
        this.quiz_id = quiz_id;
    }

    public int getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(int question_id) {
        this.question_id = question_id;
    }

    public int getUser_choice_id() {
        return user_choice_id;
    }

    public void setUser_choice_id(int user_choice_id) {
        this.user_choice_id = user_choice_id;
    }
}
