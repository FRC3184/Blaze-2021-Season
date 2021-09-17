package frc.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import frc.robot.BotMap.CAN;

public class Drive extends Subsystem {

    private CANSparkMax leftMotor1 = new CANSparkMax(CAN.FRONT_LEFT, MotorType.kBrushless);
    private CANSparkMax rightMotor1 = new CANSparkMax(CAN.FRONT_RIGHT, MotorType.kBrushless);
    private CANSparkMax leftMotor2 = new CANSparkMax(CAN.BACK_LEFT, MotorType.kBrushless);
    private CANSparkMax rightMotor2 = new CANSparkMax(CAN.BACK_RIGHT, MotorType.kBrushless);

    private SpeedControllerGroup leftMotors = new SpeedControllerGroup(leftMotor1, leftMotor2);;
    private SpeedControllerGroup rightMotors = new SpeedControllerGroup(rightMotor1, rightMotor2);;
  
    DifferentialDrive myRobot = new DifferentialDrive(leftMotors, rightMotors);

    public Drive() {

    }

    public void initDefaultCommand() {
        /**
         * The RestoreFactoryDefaults method can be used to reset the configuration
         * parameters in the SPARK MAX to their factory default state. If no argument is
         * passed, these parameters will not persist between power cycles
         **/
        leftMotor1.restoreFactoryDefaults();
        rightMotor1.restoreFactoryDefaults();
        leftMotor2.restoreFactoryDefaults();
        rightMotor2.restoreFactoryDefaults();

        setDefaultCommand(xboxDrive());

    }

    public Command xboxDrive() {
        return new SubsystemCommand("XBox Drive", this) {
            @Override
            public void execute() {
                double forward = Robot.m_oi.driver.getTriggerAxis(Hand.kRight);
                double reverse = Robot.m_oi.driver.getTriggerAxis(Hand.kLeft);
                double rotation = Robot.m_oi.driver.getX(Hand.kLeft);
                controller.arcadeDrive(forward - reverse, rotation);
            }

            @Override
            public boolean isFinished() {
                return false;
            }
        };
    }
    
}
