package frc.robot;

/**
 * Used to handle the 6 ball auto
 */
public class Autonomous {
    /**
     * 0 = Move back two feet.
     * 1 = Attempt to aim turret and shoot 
     * 2 = Bring down cage & begin collecting.
     * 3 = Go back forward back to shooting position.
     * 4 = Attempt to aim turret and shoot the three collected balls.
     */
    private int autoState = 0;

    /**
     * Reset state back to zero.
     */
    public void resetState() {
        autoState = 0;
    }
    
    public Autonomous() {
    }

    /**
     * Ran constantly in autonomousPeriodic in Robot.java
     * 
     * Runs the phase that needs to be ran. Each phase function should move itself on
     * after completion. ex: after moved two feet, phaseZero() will set autoState itself.
     * 
     * @see Robot.java
     * @return
     */
    public int runAuto() {
        switch (autoState) {
            case 0:
                phaseZero();
                break;
            case 1:
                phaseOne();
                break;
            case 2:
                phaseTwo();
                break;
            case 3:
                phaseThree();
                break;
            case 4:
                phaseFour();
                break;
        }
        return autoState;
    }

    /**
     * The initial moving backwards of two feet.
     * @see autoState
     */
    public void phaseZero() {
    }

    /**
     * Aiming the turret after moving, and then shooting the intial three balls.
     * @see autoState
     */
    public void phaseOne() {
    }

    /**
     * Bring down the cage, and go collect the three balls.
     * @see autoState
     */
    public void phaseTwo() {
    }

    /**
     * After collecting balls, return back to original shooting position.
     * @see autoState
     */
    public void phaseThree() {
    }

    /**
     * Attempt to aim and shoot the three collected balls.
     * @see autoState
     */
    public void phaseFour() {
    }

    /**
     * Aim the turret. Used in phase one and four.
     */
    public void aimTurret() {
    }

    /**
     * Shoot either the inital three balls in phase zero, or shoot the picked up balls in phase 4.
     */
    public void shootBalls() { 
    }
}
