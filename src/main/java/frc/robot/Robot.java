// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.util.sendable.SendableRegistry;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.drive.MecanumDrive;

/** This is a demo program showing how to use Mecanum control with the MecanumDrive class. */
public class Robot extends TimedRobot {
  private static final int kFrontLeftChannel = 4;
  private static final int kRearLeftChannel = 3;
  private static final int kFrontRightChannel = 1;
  private static final int kRearRightChannel = 2;

  private static final int kJoystickChannel = 0;

  private MecanumDrive m_robotDrive;
  private XboxController m_stick;

  @Override
  public void robotInit() {
    SparkMax frontLeft = new SparkMax(kFrontLeftChannel, MotorType.kBrushless);
    SparkMax rearLeft = new SparkMax(kRearLeftChannel, MotorType.kBrushless);
    SparkMax frontRight = new SparkMax(kFrontRightChannel, MotorType.kBrushless);
    SparkMax rearRight = new SparkMax(kRearRightChannel, MotorType.kBrushless);                                                                                                                                                                          

    SendableRegistry.addChild(m_robotDrive, frontLeft);
    SendableRegistry.addChild(m_robotDrive, rearLeft);
    SendableRegistry.addChild(m_robotDrive, frontRight);
    SendableRegistry.addChild(m_robotDrive, rearRight);

    // Invert the right side motors.
    // You may need to change or remove this to match your robot.
    SparkMaxConfig config = new SparkMaxConfig();

    config
        .inverted(true)
        .idleMode(IdleMode.kBrake);

    frontRight.configure(config,ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    rearRight.configure(config,ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    rearLeft.configure(config,ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

    m_robotDrive = new MecanumDrive(frontLeft::set, rearLeft::set, frontRight::set, rearRight::set);

    m_stick = new XboxController(kJoystickChannel);

    UsbCamera camera = CameraServer.startAutomaticCapture();
    camera.setResolution(640, 480);
    camera.setFPS(30);
  }

  @Override
  public void teleopPeriodic() {
    // Use the joystick Y axis for forward movement, X axis for lateral
    // movement, and Z axis for rotation.
    m_robotDrive.driveCartesian(-m_stick.getRightX(), m_stick.getRightY(), m_stick.getLeftX());
  }
}
