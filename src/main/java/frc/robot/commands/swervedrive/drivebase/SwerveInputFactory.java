package frc.robot.commands.swervedrive.drivebase;

import edu.wpi.first.math.geometry.Rotation2d;
import frc.robot.Constants.OperatorConstants;
import frc.robot.subsystems.swervedrive.SwerveSubsystem;
import swervelib.SwerveInputStream;

import edu.wpi.first.wpilibj2.command.button.CommandXboxController;

public class SwerveInputFactory {

  private final CommandXboxController driverXbox;
  private final SwerveSubsystem drivebase;

  public SwerveInputFactory(
      CommandXboxController driverXbox,
      SwerveSubsystem drivebase) {

    this.driverXbox = driverXbox;
    this.drivebase = drivebase;
  }

  // =========================
  // BASE DRIVE
  // =========================

  public SwerveInputStream getDriveAngularVelocity() {
    return SwerveInputStream.of(
            drivebase.getSwerveDrive(),
            () -> driverXbox.getLeftY() * 0.6,
            () -> driverXbox.getLeftX() * 0.6)
        .withControllerRotationAxis(() -> -driverXbox.getRightX())
        .deadband(OperatorConstants.DEADBAND)
        .scaleTranslation(0.8)
        .allianceRelativeControl(true);
  }

  public SwerveInputStream getDriveDirectAngle() {
    return getDriveAngularVelocity().copy()
        .withControllerHeadingAxis(
            driverXbox::getRightX,
            driverXbox::getRightY)
        .headingWhile(true);
  }

  public SwerveInputStream getDriveRobotOriented() {
    return getDriveAngularVelocity().copy()
        .robotRelative(true)
        .allianceRelativeControl(false);
  }

  // =========================
  // KEYBOARD MODE
  // =========================

  public SwerveInputStream getDriveAngularVelocityKeyboard() {
    return SwerveInputStream.of(
            drivebase.getSwerveDrive(),
            () -> -driverXbox.getLeftY(),
            () -> -driverXbox.getLeftX())
        .withControllerRotationAxis(() -> driverXbox.getRawAxis(2))
        .deadband(OperatorConstants.DEADBAND)
        .scaleTranslation(0.8)
        .allianceRelativeControl(true);
  }

  public SwerveInputStream getDriveDirectAngleKeyboard() {
    return getDriveAngularVelocityKeyboard().copy()
        .withControllerHeadingAxis(
            () -> Math.sin(driverXbox.getRawAxis(2) * Math.PI) * (Math.PI * 2),
            () -> Math.cos(driverXbox.getRawAxis(2) * Math.PI) * (Math.PI * 2))
        .headingWhile(true)
        .translationHeadingOffset(true)
        .translationHeadingOffset(Rotation2d.fromDegrees(0));
  }
}