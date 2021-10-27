package frc.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.DigitalInput;

/**
 * Utility for loading / sending balls. Used in autonomous.
 */
public final class BallUtility {
    /**
     * This should find the target and aim the turret. This is ran in autonomous.
     * I don't know if this actually works. Code ripped from Will.
     * 
     * @param tx
     * @param ty
     * @param tv
     * @param ta
     * @param leftLimitSwitch
     * @param rightLimitSwitch
     * @param turret
     */
    public static double aimTurret(
        NetworkTableEntry tx, 
        NetworkTableEntry ty,
        NetworkTableEntry tv,
        NetworkTableEntry ta,
        DigitalInput leftLimitSwitch,
        DigitalInput rightLimitSwitch,
        WPI_TalonSRX turret,
        NetworkTableEntry lights
    ) {
        lights.setNumber(3);

        double x = tx.getDouble(0.0);
        double y = ty.getDouble(0.0);
        double target = tv.getDouble(0.0);
        double area = ta.getDouble(0.0);
        double deadzone = 2.5;
        double dr;

        double rotate = .1; 

        // if either limit switch is hit, the turret is moving, and it's rotating in
        // it's direction, then stop. (i'm assuming if rotate is in the negatives, it's right and vice versa)
        if (!leftLimitSwitch.get() && turret.get() > 0 && 0 < rotate) {
            turret.set(0);
        } else if (!rightLimitSwitch.get() && turret.get() < 0 && 0 > rotate) {
            turret.set(0);
        }

        if (target != 1){
            turret.set(rotate);

            if (!leftLimitSwitch.get()){
                rotate = -.1;
            } else if (!leftLimitSwitch.get()){
                rotate = .1;
            }
        } else if (target > 0 && leftLimitSwitch.get() && rightLimitSwitch.get()){
            dr = x;
            if (Math.abs(dr) < deadzone){
                dr=0;
            }
            
            turret.set(-dr*.015);
        }

        return target; // this function gets stopped when target equals 1 (in autonomous)
    }
}
