package com.example.android.fireapp;

/**
 * Created by Yash Chowdhary on 10-06-2017.
 */

public class TournamentInformation {
    public String tournamentName;

    public boolean badminton;
    public boolean basketball;
    public boolean contractBridge;
    public boolean dodgeball;
    public boolean dota2;
    public boolean floorball;
    public boolean handball;
    public boolean chess;
    public boolean netball;
    public boolean reversi;
    public boolean roadRelay;
    public boolean soccer;
    public boolean tableTennis;
    public boolean tchoukball;
    public boolean tennis;
    public boolean touchFootball;
    public boolean ultimate;
    public boolean volleyball;

    public TournamentInformation(){
        //blank constructor for datasnapshot retrieval purposes
    }

    public String getTournamentName() {
        return tournamentName;
    }

    public void setTournamentName(String tournamentName) {
        this.tournamentName = tournamentName;
    }

    public boolean isBadminton() {
        return badminton;
    }

    public void setBadminton(boolean badminton) {
        this.badminton = badminton;
    }

    public boolean isBasketball() {
        return basketball;
    }

    public void setBasketball(boolean basketball) {
        this.basketball = basketball;
    }

    public boolean isContractBridge() {
        return contractBridge;
    }

    public void setContractBridge(boolean contractBridge) {
        this.contractBridge = contractBridge;
    }

    public boolean isDodgeball() {
        return dodgeball;
    }

    public void setDodgeball(boolean dodgeball) {
        this.dodgeball = dodgeball;
    }

    public boolean isDota2() {
        return dota2;
    }

    public void setDota2(boolean dota2) {
        this.dota2 = dota2;
    }

    public boolean isFloorball() {
        return floorball;
    }

    public void setFloorball(boolean floorball) {
        this.floorball = floorball;
    }

    public boolean isHandball() {
        return handball;
    }

    public void setHandball(boolean handball) {
        this.handball = handball;
    }

    public boolean isChess() {
        return chess;
    }

    public void setChess(boolean chess) {
        this.chess = chess;
    }

    public boolean isNetball() {
        return netball;
    }

    public void setNetball(boolean netball) {
        this.netball = netball;
    }

    public boolean isReversi() {
        return reversi;
    }

    public void setReversi(boolean reversi) {
        this.reversi = reversi;
    }

    public boolean isRoadRelay() {
        return roadRelay;
    }

    public void setRoadRelay(boolean roadRelay) {
        this.roadRelay = roadRelay;
    }

    public boolean isSoccer() {
        return soccer;
    }

    public void setSoccer(boolean soccer) {
        this.soccer = soccer;
    }

    public boolean isTableTennis() {
        return tableTennis;
    }

    public void setTableTennis(boolean tableTennis) {
        this.tableTennis = tableTennis;
    }

    public boolean isTchoukball() {
        return tchoukball;
    }

    public void setTchoukball(boolean tchoukball) {
        this.tchoukball = tchoukball;
    }

    public boolean isTennis() {
        return tennis;
    }

    public void setTennis(boolean tennis) {
        this.tennis = tennis;
    }

    public boolean isTouchFootball() {
        return touchFootball;
    }

    public void setTouchFootball(boolean touchFootball) {
        this.touchFootball = touchFootball;
    }

    public boolean isUltimate() {
        return ultimate;
    }

    public void setUltimate(boolean ultimate) {
        this.ultimate = ultimate;
    }

    public boolean isVolleyball() {
        return volleyball;
    }

    public void setVolleyball(boolean volleyball) {
        this.volleyball = volleyball;
    }
}
