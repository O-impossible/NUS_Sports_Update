package com.example.android.fireapp;

/**
 * Created by Yash Chowdhary on 15-06-2017.
 */

public class RequestDetails {
    public String tournamentId;
    public String userId;
    public boolean isOCRequest;
    public boolean isParticipantRequest;
    public String sport;

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

    public boolean isOCRequest() {
        return isOCRequest;
    }

    public void setOCRequest(boolean OCRequest) {
        isOCRequest = OCRequest;
    }

    public boolean isParticipantRequest() {
        return isParticipantRequest;
    }

    public void setParticipantRequest(boolean participantRequest) {
        isParticipantRequest = participantRequest;
    }

    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof RequestDetails){
            RequestDetails requestDetails = (RequestDetails) obj;
            return ((RequestDetails) obj).getUserId().equals(this.getUserId()) &&
                    ((RequestDetails) obj).getTournamentId().equals(this.getTournamentId()) &&
                    ((RequestDetails) obj).getSport().equals(this.getSport()) &&
                    ((RequestDetails) obj).isOCRequest() == this.isOCRequest() &&
                    ((RequestDetails) obj).isParticipantRequest() == this.isParticipantRequest();
        }
        return  false;
    }
}
