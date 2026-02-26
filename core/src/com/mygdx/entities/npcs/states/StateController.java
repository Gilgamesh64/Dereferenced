package com.mygdx.entities.npcs.states;

public class StateController {
    public enum MovementState {
        FOLLOW_PLAYER,
        FLEE,
        CIRLE_AROUND,
        WANDER,
        STILL
    }

    public enum CombatState{
        SHOOTING,
        STILL
    }

    private MovementState movementState;
    private MovementState prevMovementState;

    private CombatState combatState;
    private CombatState prevCombatState;


    public MovementState getMovState() {
        return movementState;
    }

    public MovementState getPrevMovState() {
        return prevMovementState;
    }

    public CombatState getCombatState() {
        return combatState;
    }
    public CombatState getPrevCombatState() {
        return prevCombatState;
    }

    public void setMovementState(MovementState movState) {
        this.prevMovementState = this.movementState;
        this.movementState = movState;
    }

    public void setCombatState(CombatState combatState) {
        this.prevCombatState = this.combatState;
        this.combatState = combatState;
    }
}
