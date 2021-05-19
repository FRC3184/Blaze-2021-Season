// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.SpeedControllerGroup;

//import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import edu.wpi.first.wpilibj.GenericHID;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();
  private DifferentialDrive m_myRobot;
  private XboxController m_leftStick;
  private SpeedControllerGroup m_leftMotors;
  private SpeedControllerGroup m_rightMotors;
  private static final int leftDeviceID1 = 1;
  private static final int rightDeviceID1 = 3;
  private static final int leftDeviceID2 = 4;
  private static final int rightDeviceID2 = 2;
  private CANSparkMax m_leftMotor1;
  private CANSparkMax m_rightMotor1;
  private CANSparkMax m_leftMotor2;
  private CANSparkMax m_rightMotor2;
  private CANSparkMax m_winch;
  private WPI_TalonSRX m_intake;
  private WPI_TalonSRX m_arm;
  private WPI_VictorSPX m_leftdoor;
  private WPI_VictorSPX m_rightdoor;

  /**
   * This function is run when the robot is first started up and should be used
   * for any initialization code.
   */
  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);
    m_leftMotor1 = new CANSparkMax(leftDeviceID1, MotorType.kBrushless);
    m_rightMotor1 = new CANSparkMax(rightDeviceID1, MotorType.kBrushless);
    m_leftMotor2 = new CANSparkMax(leftDeviceID2, MotorType.kBrushless);
    m_rightMotor2 = new CANSparkMax(rightDeviceID2, MotorType.kBrushless);
    m_winch = new CANSparkMax(5, MotorType.kBrushless);

    m_intake = new WPI_TalonSRX(9);
    m_arm = new WPI_TalonSRX(10);

    m_leftdoor = new WPI_VictorSPX(6);
    m_rightdoor = new WPI_VictorSPX(7);

    /**
     * The RestoreFactoryDefaults method can be used to reset the configuration
     * parameters in the SPARK MAX to their factory default state. If no argument is
     * passed, these parameters will not persist between power cycles
     */
    m_leftMotor1.restoreFactoryDefaults();
    m_rightMotor1.restoreFactoryDefaults();
    m_leftMotor2.restoreFactoryDefaults();
    m_rightMotor2.restoreFactoryDefaults();
    m_winch.restoreFactoryDefaults();
  
    

    m_leftMotors = new SpeedControllerGroup(m_leftMotor1, m_leftMotor2);
    m_rightMotors = new SpeedControllerGroup(m_rightMotor1, m_rightMotor2);

    m_myRobot = new DifferentialDrive(m_leftMotors, m_rightMotors);

    m_leftStick = new XboxController(0);
  }

  /**
   * This function is called every robot packet, no matter the mode. Use this for
   * items like diagnostics that you want ran during disabled, autonomous,
   * teleoperated and test.
   *
   * <p>
   * This runs after the mode specific periodic functions, but before LiveWindow
   * and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select between different
   * autonomous modes using the dashboard. The sendable chooser code works with the Java
   * SmartDashboard. If you prefer the LabVIEW Dashboard, remove all of the chooser code and
   * uncomment the getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to the switch structure
   * below with additional strings. If using the SendableChooser make sure to add them to the
   * chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
      case kCustomAuto:
        // Put custom auto code here
        break;
      case kDefaultAuto:
      default:
        // Put default auto code here
        break;
    }
  }

  /** This function is called once when teleop is enabled. */
  @Override
  public void teleopInit() {}

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {
    double dx = m_leftStick.getX(GenericHID.Hand.kRight);
    double dy = m_leftStick.getY(GenericHID.Hand.kRight);
    if (Math.abs(dx)<.1)
         dx = 0;
    if (Math.abs(dy)<.1)
         dy = 0;

    m_myRobot.arcadeDrive(dy*-1, Math.pow(dx,3));
    if (m_leftStick.getTriggerAxis(GenericHID.Hand.kRight) >= .1) {
      m_winch.set(.5);  
    } else if (m_leftStick.getTriggerAxis(GenericHID.Hand.kLeft) >= .1) {
      m_winch.set(-.5);  
      
    } else {
      m_winch.set(0);
    }

    if (m_leftStick.getAButton())
    {
      m_arm.set(-.4);
    } else if (m_leftStick.getYButton()){
      m_arm.set(.4);
    } else {
      m_arm.set(0);
    }

    if (m_leftStick.getXButton())
    {
      //m_leftdoor.set(-.25);
      //m_rightdoor.set(-.25);
    } else if (m_leftStick.getBButton())
    {
      //m_leftdoor.set(.25);
      //m_rightdoor.set(.25);  
    } else {
      //m_leftdoor.set(0);
      //m_rightdoor.set(0);
    } 

    if (m_leftStick.getBumper(GenericHID.Hand.kRight))
    {
      m_intake.set(.4);
    } else if (m_leftStick.getBumper(GenericHID.Hand.kLeft)) {
      m_intake.set(-.4);
    } else {
      m_intake.set(0);
    }
  }

  /** This function is called once when the robot is disabled. */
  @Override
  public void disabledInit() {}

  /** This function is called periodically when disabled. */
  @Override
  public void disabledPeriodic() {}

  /** This function is called once when test mode is enabled. */
  @Override
  public void testInit() {}

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}
}
