package com.example.todolist;

public class item_model {
    private String disease;
    private boolean check;




    public item_model(String m_disease,Boolean m_check) {
        this.disease = m_disease;
        this.check=m_check;
    }

    public String getDisease() {
        return disease;
    }
    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }
    public boolean getCheck() {
        return check;
    }

    public void setDisease(String m_disease) {
        this.disease = m_disease;
    }



}
