package com.gmail.erofeev.st.alexei.onlinemarket.service.model;

public class ReviewHideFieldState {
    private Long id;
    private Boolean hided = false;

    public ReviewHideFieldState() {
    }

    public ReviewHideFieldState(Long id, Boolean hided) {
        this.id = id;
        this.hided = hided;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getHided() {
        return hided;
    }

    public void setHided(Boolean hided) {
        this.hided = hided;
    }

    @Override
    public String toString() {
        return "ReviewHideFieldState{" +
                "id=" + id +
                ", hided=" + hided +
                '}';
    }
}
