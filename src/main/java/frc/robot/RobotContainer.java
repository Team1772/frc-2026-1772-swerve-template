package frc.robot;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;

import frc.robot.commands.swervedrive.drivebase.SwerveInputFactory;
import frc.robot.commands.swervedrive.auto.shooter.Shoot;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.swervedrive.SwerveSubsystem;

import java.io.File;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;

public class RobotContainer {

  final CommandXboxController driverXbox = new CommandXboxController(0);
  final CommandXboxController copilotXbox = new CommandXboxController(1);

  private final SwerveSubsystem drivebase =
      new SwerveSubsystem(new File(Filesystem.getDeployDirectory(), "swerve/neo"));

  private final ShooterSubsystem shooterSubsystem;

  private final SwerveInputFactory swerveInputStreams;

  private SendableChooser<Command> autoChooser;

  // =========================
  // CONSTRUCTOR
  // =========================

  public RobotContainer() {

    shooterSubsystem = new ShooterSubsystem();

    swerveInputStreams = new SwerveInputFactory(driverXbox, drivebase);

    configureBindings();
    configureAutonomus();
  
  }

  // =========================
  // BINDINGS
  // =========================

  private void configureBindings() {
    configureBindingsDrive();
    configureBindingsSimulation();
    configureBindingsTest();
  }

  private void configureBindingsDrive() {

    Command driveFieldOrientedAngularVelocity =
        drivebase.driveFieldOriented(swerveInputStreams.getDriveAngularVelocity());

    Command driveFieldOrientedDirectAngleKeyboard =
        drivebase.driveFieldOriented(swerveInputStreams.getDriveDirectAngleKeyboard());

    if (RobotBase.isSimulation()) {
      drivebase.setDefaultCommand(driveFieldOrientedDirectAngleKeyboard);
    } else {
      drivebase.setDefaultCommand(driveFieldOrientedAngularVelocity);
    }

    configureBindingsDriverUtilities();
  }

  private void configureBindingsSimulation() {

    if (Robot.isSimulation()) {

      driverXbox.start().onTrue(
          Commands.runOnce(
              () -> drivebase.resetOdometry(new Pose2d(3, 3, new Rotation2d()))
          )
      );

      driverXbox.button(1).whileTrue(
          drivebase.sysIdDriveMotorCommand()
      );

      driverXbox.button(2).whileTrue(
          Commands.runEnd(
              () -> swerveInputStreams.getDriveDirectAngleKeyboard().driveToPoseEnabled(true),
              () -> swerveInputStreams.getDriveDirectAngleKeyboard().driveToPoseEnabled(false)
          )
      );
    }
  }

  private void configureBindingsTest() {

    if (!DriverStation.isTest()) {
      return;
    }

    drivebase.setDefaultCommand(
        drivebase.driveFieldOriented(swerveInputStreams.getDriveAngularVelocity())
    );

    driverXbox.x().whileTrue(
        Commands.runOnce(drivebase::lock, drivebase).repeatedly()
    );

    driverXbox.start()
        .onTrue(Commands.runOnce(drivebase::zeroGyro));

    driverXbox.back()
        .whileTrue(drivebase.centerModulesCommand());
  }

  private void configureBindingsDriverUtilities() {

    if (DriverStation.isTest()) {
      return;
    }

    driverXbox.a().onTrue(
        Commands.runOnce(drivebase::zeroGyro));

    driverXbox.x().onTrue(
        Commands.runOnce(drivebase::addFakeVisionReading));
  }

  private void configureAutonomus() {
    DriverStation.silenceJoystickConnectionWarning(true);

    NamedCommands.registerCommand("test", Commands.print("I EXIST"));
    NamedCommands.registerCommand("Shoot", new Shoot(shooterSubsystem, 73));

    autoChooser = AutoBuilder.buildAutoChooser();

    autoChooser.setDefaultOption("Do Nothing", Commands.none());

    autoChooser.addOption(
        "Drive Forward",
        drivebase.driveForward().withTimeout(1)
    );

    SmartDashboard.putData("Auto Chooser", autoChooser);
  }

  // =========================
  // AUTONOMOUS
  // =========================

  public Command getAutonomousCommand() {
    return drivebase.getAutonomousCommand("percurso");
  }

  public void setMotorBrake(boolean brake) {
    drivebase.setMotorBrake(brake);
  }
}