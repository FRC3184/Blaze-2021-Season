// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;


import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.SpeedControllerGroup;

//import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;


import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DigitalInput;
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
  private XboxController m_rightStick;
  private SpeedControllerGroup m_leftMotors;
  private SpeedControllerGroup m_rightMotors;
  private static final int leftDeviceID1 = 8;
  private static final int rightDeviceID1 = 3;
  private static final int leftDeviceID2 = 4;
  private static final int rightDeviceID2 = 2;
  private CANSparkMax m_leftMotor1;
  private CANSparkMax m_rightMotor1;
  private CANSparkMax m_leftMotor2;
  private CANSparkMax m_rightMotor2;
  private CANSparkMax m_winch;
  private CANSparkMax m_shooter;
  private CANEncoder shooter_e;
  private CANEncoder leftEncoder1;
  private CANEncoder leftEncoder2;
  private CANEncoder rightEncoder1;
  private CANEncoder rightEncoder2;
  private WPI_TalonSRX m_feeder;
  private WPI_TalonSRX m_intake;
  private WPI_TalonSRX m_arm;
  private WPI_TalonSRX m_turret;
  private WPI_VictorSPX m_door;
  private WPI_VictorSPX m_patrick;
  private DigitalInput Toplimitswitch;
  private DigitalInput bottomlimitswitch;
  private DigitalInput Leftlimitswitch;
  private DigitalInput Rightlimitswitch;
  private static int AUTONOMOUS_STATE = 0;
  NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
  NetworkTableEntry tx = table.getEntry("tx");
  NetworkTableEntry ty = table.getEntry("ty");
  NetworkTableEntry ta = table.getEntry("ta");
  NetworkTableEntry Lights = table.getEntry("ledMode");
  NetworkTableEntry tv = table.getEntry("tv");
  double rotate = .2;
  double starttime;
  double Ft2Ticks = 2607.6;
  
    double deadzone = 2.5;
    double dr;
  
  //private WPI_VictorSPX m_rightdoor;

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
    m_shooter = new CANSparkMax(30, MotorType.kBrushless);
    m_intake = new WPI_TalonSRX(9);
    m_arm = new WPI_TalonSRX(10);
    m_turret = new WPI_TalonSRX(14);
    shooter_e = new CANEncoder(m_shooter);
    leftEncoder1 = new CANEncoder(m_leftMotor1);
    leftEncoder2 = new CANEncoder(m_leftMotor2);
    rightEncoder1 = new CANEncoder(m_rightMotor1); // inverted
    rightEncoder2 = new CANEncoder(m_rightMotor2); // inverted
    // rightEncoder2.setInverted(true);
    Toplimitswitch = new DigitalInput(0);
    bottomlimitswitch = new DigitalInput(1);
    m_door = new WPI_VictorSPX(7);
    m_patrick = new WPI_VictorSPX(6);
    m_feeder = new WPI_TalonSRX(4);
    Leftlimitswitch = new DigitalInput(2);
    Rightlimitswitch = new DigitalInput(3);
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
    m_rightStick = new XboxController(1);
    
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
    System.out.printf("RE2 Position (init 1): %s\n", rightEncoder2.getPosition());
    Lights.setNumber(3);
    m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);
    starttime = Timer.getFPGATimestamp();
    leftEncoder1.setPosition(0);
    leftEncoder2.setPosition(0);
    rightEncoder1.setPosition(0);
    rightEncoder2.setPosition(0);
    AUTONOMOUS_STATE = 0;
    System.out.printf("RE2 Position (init 2): %s\n", rightEncoder2.getPosition());
  }

  /**
   * 0 = move 5 feet
   * 1 = tracking
   * 2 = shooting
   */
  

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
    SmartDashboard.putNumber("autostep", AUTONOMOUS_STATE);
    
    double x = tx.getDouble(0.0);
    double y = ty.getDouble(0.0);
    double Target = tv.getDouble(0.0);
    double area = ta.getDouble(0.0);
    switch (m_autoSelected) {
      case kCustomAuto:
        // Put custom auto code here
        break;
      case kDefaultAuto:
      default:
        // Put default auto code here
        

        // do stuff depending on the state of the autonomous
        // encoder controls each side of motors to hopefully reduce drifting
        // 30 is roughly 5 feet? but drifts to 33 when ran.
        switch (AUTONOMOUS_STATE) {
          case 0:
            if (rightEncoder2.getPosition() > -30) {
              m_rightMotor1.set(-.25);
              m_rightMotor2.set(-.25);
            } else {
              m_rightMotor1.set(0);
              m_rightMotor2.set(0);
            }
    
            if (leftEncoder2.getPosition() < 30) {
              m_leftMotor1.set(.25);
              m_leftMotor2.set(.25);
            } else {
              m_leftMotor1.set(0);
              m_leftMotor2.set(0);
            }
    
            // When the robot's moved roughly 5 feet away.
            if (leftEncoder2.getPosition() > 30 && rightEncoder2.getPosition() < -30) {
              AUTONOMOUS_STATE = 1; // start tracking
            }
          break;

          // aim the turret using BallUtility
          case 1: 
            //double target = BallUtility.aimTurret(tx, ty, tv, ta, Leftlimitswitch, Rightlimitswitch, m_turret);
            if (!Leftlimitswitch.get() && m_turret.get() > 0){
              m_turret.set(0);
          } else if (!Rightlimitswitch.get() && m_turret.get() < 0){
              m_turret.set(0);
          }
  
          if (Target != 1){
              m_turret.set(rotate);
              if (!Leftlimitswitch.get()){
                  rotate = -.1;
              } else if (!Leftlimitswitch.get()){
                  rotate = .1;
              }
          } else if (Target > 0 && Leftlimitswitch.get() && Rightlimitswitch.get()){
              dr = x;
              if (Math.abs(dr) < deadzone){
                  dr=0;
              }
              
              m_turret.set(-dr*.015);
          }
            // this means the turret should be aimed (hopefully?)
            if (Math.abs(dr) < deadzone) {
              AUTONOMOUS_STATE = 2; // shoot ball
            }
            break;

          // shoot balls. turret should already be aimed (I think?).
          case 2:
          double time = Timer.getFPGATimestamp();
          SmartDashboard.putNumber("time", time - starttime);
          if (time - starttime < 10){
            m_shooter.set(1);
            
          } else {
            m_shooter.set(0);
          }
          if (time - starttime < 10 && time - starttime > 3){
          m_feeder.set(-.9);
            m_patrick.set(-1);
          } else {
            m_patrick.set(0);
            m_feeder.set(0);
          }
            break;
        }

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
    double dy = m_leftStick.getY(GenericHID.Hand.kLeft);
    dx= dx/2.0;
    dy= dy/4.0;
    if (Math.abs(dx)<.1)
         dx = 0;
    if (Math.abs(dy)<.1)
         dy = 0;

    // switch (m_rightStick.getPOV()) {
    //   // Right controller DPAD Up
    //   case DPadPovPositions.UP:
    //     break;
    // }

    m_myRobot.arcadeDrive(dy*-2.5, Math.pow(dx,1));
    if (m_leftStick.getBumper(GenericHID.Hand.kRight)) {
      m_winch.set(.5);  
    } else if (m_leftStick.getBumper(GenericHID.Hand.kLeft)) {
      m_winch.set(-.5);  
    } else {
      m_winch.set(0);
    }

    if (m_rightStick.getAButton())
    {
      m_arm.set(-.5);
    } else if (m_rightStick.getYButton()){
      m_arm.set(.5);
    } else {
      m_arm.set(0);
    }
    
    if (m_rightStick.getTriggerAxis(GenericHID.Hand.kLeft) >= .1) {
      m_shooter.set(1);
    } else if (m_rightStick.getTriggerAxis(GenericHID.Hand.kRight) >= .1) {
      m_shooter.set(-1);
    } else {
      m_shooter.set(0);
    }

    if (m_rightStick.getXButton())
    {
      m_door.set(-1);
    } else if (m_rightStick.getBButton()){
      m_door.set(1);  
    } else {
      m_door.set(0);
    } 
    
    if (m_door.get() < 0){ 
      if (!Toplimitswitch.get()){
        m_door.set(0);
      } else {
        m_door.set(-1);
      }
    } else if (m_door.get() > 0){
      if (!bottomlimitswitch.get()){
        m_door.set(0);
      } else {
        m_door.set(1);
      }
    }
    
    if (m_leftStick.getTriggerAxis(GenericHID.Hand.kRight) >= .1)
    {
      m_intake.set(.4);
    } else if (m_leftStick.getTriggerAxis(GenericHID.Hand.kLeft) >= .1) {
      m_intake.set(-.4);
    } else {
      m_intake.set(0); 
    }

    if (m_rightStick.getStickButton(Hand.kRight)) {
      m_feeder.set(-.9);
    } else {
      m_feeder.set(0);
    }

    if (m_rightStick.getBumper(Hand.kLeft)){
      m_turret.set(.4);
    } else if (m_rightStick.getBumper(Hand.kRight)){
      m_turret.set(-.4);
    } else {
      m_turret.set(0);
    }

    /*if (m_rightStick.getBumper(GenericHID.Hand.kRight)) {
      m_patrick.set(1);
    }*/

    if (m_rightStick.getStickButton(Hand.kRight)){
      m_patrick.set(-1);
    } else {
      m_patrick.set(0);
    }

    if (m_rightStick.getStickButton(Hand.kLeft)) {
      Lights.setNumber(3);
    } else {
      Lights.setNumber(1);
    }
    double x = tx.getDouble(0.0);
    double y = ty.getDouble(0.0);
    double Target = tv.getDouble(0.0);
    double area = ta.getDouble(0.0);
    /*if (m_rightStick.getStickButton(Hand.kRight)){
      dr= x;
      if (Math.abs(dr) < deadzone){
        dr=0;
      } 
      m_turret.set(-dr*.015);
    }*/
    if (!Leftlimitswitch.get() && m_turret.get() > 0){
      m_turret.set(0);
    } else if (!Rightlimitswitch.get() && m_turret.get() < 0){
      m_turret.set(0);
    }

    if (m_rightStick.getStickButton(Hand.kLeft)){
      if (Target != 1){
          m_turret.set(rotate);
          if (!Leftlimitswitch.get()){
            rotate = -.2;
          } else if (!Rightlimitswitch.get()){
            rotate = .2;
          }
      } else if (Target > 0 && Leftlimitswitch.get() && Rightlimitswitch.get()){
        dr = x;
        if (Math.abs(dr) < deadzone){
          dr=0;
        }
        
        m_turret.set(-dr*.015);
      }
    } 

    SmartDashboard.putNumber("Speed", shooter_e.getVelocity());
    SmartDashboard.putBoolean("Topswitch", Toplimitswitch.get());
    SmartDashboard.putNumber("LimelightX", x);
    SmartDashboard.putNumber("LimelightY", y);
    SmartDashboard.putNumber("LimelightArea", area);
    SmartDashboard.putNumber("Right POV", m_rightStick.getPOV());
    SmartDashboard.putNumber("Left POV", m_leftStick.getPOV());
    SmartDashboard.putNumber("shooter", shooter_e.getPosition());
    
    
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
