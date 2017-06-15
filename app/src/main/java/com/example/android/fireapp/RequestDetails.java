package com.example.android.fireapp;

/**
 * Created by Yash Chowdhary on 15-06-2017.
 */

public class RequestDetails {
    public String tournamentId;
    public String userId;

    public RequestDetails() {
        //empty constructor for data retrieval via dataSnapashot
    }

    public String getTournamentId() {
        return tournamentId;
    }

    public void setTournamentId(String tournamentId) {
        this.tournamentId = tournamentId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
