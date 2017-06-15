package com.example.android.fireapp;

/**
 * Created by Yash Chowdhary on 15-06-2017.
 */

class TournamentStatus {

    public boolean isOrganizing;
    public boolean isParticipating;

    public TournamentStatus() {
    }

    public TournamentStatus(String tournamentId, boolean isOrganizing, boolean isParticipating) {
        this.isOrganizing = isOrganizing;
        this.isParticipating = isParticipating;
    }


    public boolean isOrganizing() {
        return isOrganizing;
    }

    public void setOrganizing(boolean organizing) {
        isOrganizing = organizing;
    }

    public boolean isParticipating() {
        return isParticipating;
    }

    public void setParticipating(boolean participating) {
        isParticipating = participating;
    }
}
