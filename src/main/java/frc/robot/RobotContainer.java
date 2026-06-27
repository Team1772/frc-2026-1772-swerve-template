package frc.robot;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.trajectory.TrapezoidProfile.Constraints;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.StartEndCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.Constants.OperatorConstants;
import frc.robot.commands.swervedrive.auto.shooter.Shoot;
// import frc.robot.commands.swervedrive.auto.shooter.ShootAuto;
// import frc.robot.commands.swervedrive.auto.shooter.ShootAutoTimer;
// import frc.robot.subsystems.BufferSubsystem;
// import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.swervedrive.SwerveSubsystem;
import java.io.File;
import swervelib.SwerveInputStream;

public class RobotContainer
{
  final         CommandXboxController driverXbox = new CommandXboxController(0);
  final CommandXboxController copilotXbox = new CommandXboxController(1);
  private final SwerveSubsystem       drivebase  = new SwerveSubsystem(new File(Filesystem.getDeployDirectory(),
                                                                                "swerve/neo"));
  private final SendableChooser<Command> autoChooser;
  private final ShooterSubsystem shooterSubsystem;
  // private final IntakeSubsystem intakeSubsystem;
  // private final BufferSubsystem bufferSubsystem;

  /**
   * Converts driver input into a field-relative ChassisSpeeds that is controlled by angular velocity.
   */
  SwerveInputStream driveAngularVelocity = SwerveInputStream.of(drivebase.getSwerveDrive(),
                                                                () -> (driverXbox.getLeftY() * 0.6),
                                                                () -> (driverXbox.getLeftX() * 0.6))
                                                            .withControllerRotationAxis((() -> (driverXbox.getRightX() * -1)))
                                                            .deadband(OperatorConstants.DEADBAND)
                                                            .scaleTranslation(0.8)
                                                            .allianceRelativeControl(true);

  /**
   * Clone's the angular velocity input stream and converts it to a fieldRelative input stream.
   */
  SwerveInputStream driveDirectAngle = driveAngularVelocity.copy().withControllerHeadingAxis(driverXbox::getRightX,
                                                                                             driverXbox::getRightY)
                                                           .headingWhile(true);

  /**
   * Clone's the angular velocity input stream and converts it to a robotRelative input stream.
   */
  SwerveInputStream driveRobotOriented = driveAngularVelocity.copy().robotRelative(true)
                                                             .allianceRelativeControl(false);

  SwerveInputStream driveAngularVelocityKeyboard = SwerveInputStream.of(drivebase.getSwerveDrive(),
                                                                        () -> -driverXbox.getLeftY(),
                                                                        () -> -driverXbox.getLeftX())
                                                                    .withControllerRotationAxis(() -> driverXbox.getRawAxis(
                                                                        2))
                                                                    .deadband(OperatorConstants.DEADBAND)
                                                                    .scaleTranslation(0.8)
                                                                    .allianceRelativeControl(true);
  // Derive the heading axis with math!
  SwerveInputStream driveDirectAngleKeyboard     = driveAngularVelocityKeyboard.copy()
    .withControllerHeadingAxis(() ->
                                  Math.sin(
                                      driverXbox.getRawAxis(
                                          2) *
                                      Math.PI) *
                                  (Math.PI *
                                    2),
                              () ->
                                  Math.cos(
                                      driverXbox.getRawAxis(
                                          2) *
                                      Math.PI) *
                                  (Math.PI *
                                    2))
    .headingWhile(true)
    .translationHeadingOffset(true)
    .translationHeadingOffset(Rotation2d.fromDegrees(
        0));


  public RobotContainer()
  {
    
   
    // bufferSubsystem = new BufferSubsystem();
    shooterSubsystem = new ShooterSubsystem();
    // intakeSubsystem = new IntakeSubsystem();
    configureBindings();
    DriverStation.silenceJoystickConnectionWarning(true);
    
    //Create the NamedCommands that will be used in PathPlanner
    NamedCommands.registerCommand("test", Commands.print("I EXIST"));
    NamedCommands.registerCommand("Shoot", new Shoot(shooterSubsystem,73 ));

    //Have the autoChooser pull in all PathPlanner autos as options
    autoChooser = AutoBuilder.buildAutoChooser();

    //Set the default auto (do nothing) 
    autoChooser.setDefaultOption("Do Nothing", Commands.none());

    //Add a simple auto option to have the robot drive forward for 1 second then stop
    autoChooser.addOption("Drive Forward", drivebase.driveForward().withTimeout(1));
    
    //Put the autoChooser on the SmartDashboard
    SmartDashboard.putData("Auto Chooser", autoChooser);

  }

  /**
   * Use this method to define your trigger->command mappings. Triggers can be created via the
   * {@link Trigger#Trigger(java.util.function.BooleanSupplier)} constructor with an arbitrary predicate, or via the
   * named factories in {@link edu.wpi.first.wpilibj2.command.button.CommandGenericHID}'s subclasses for
   * {@link CommandXboxController Xbox}/{@link edu.wpi.first.wpilibj2.command.button.CommandPS4Controller PS4}
   * controllers or {@link edu.wpi.first.wpilibj2.command.button.CommandJoystick Flight joysticks}.
   */
  private void configureBindings()
  {
    //----------------------------------------------------------------------------------------
    //swerve buttons
    //----------------------------------------------------------------------------------------
    Command driveFieldOrientedDirectAngle      = drivebase.driveFieldOriented(driveDirectAngle);
    Command driveFieldOrientedAnglularVelocity = drivebase.driveFieldOriented(driveAngularVelocity);
    Command driveRobotOrientedAngularVelocity  = drivebase.driveFieldOriented(driveRobotOriented);
    Command driveSetpointGen = drivebase.driveWithSetpointGeneratorFieldRelative(
        driveDirectAngle);
    Command driveFieldOrientedDirectAngleKeyboard      = drivebase.driveFieldOriented(driveDirectAngleKeyboard);
    Command driveFieldOrientedAnglularVelocityKeyboard = drivebase.driveFieldOriented(driveAngularVelocityKeyboard);
    Command driveSetpointGenKeyboard = drivebase.driveWithSetpointGeneratorFieldRelative(
        driveDirectAngleKeyboard);

    if (RobotBase.isSimulation())
    {
      drivebase.setDefaultCommand(driveFieldOrientedDirectAngleKeyboard);
    } else
    {
      drivebase.setDefaultCommand(driveFieldOrientedAnglularVelocity);
    }

    if (Robot.isSimulation())
    {
      Pose2d target = new Pose2d(new Translation2d(1, 4),
                                 Rotation2d.fromDegrees(90));
      //drivebase.getSwerveDrive().field.getObject("targetPose").setPose(target);
      driveDirectAngleKeyboard.driveToPose(() -> target,
                                           new ProfiledPIDController(5,
                                                                     0,
                                                                     0,
                                                                     new Constraints(5, 2)),
                                           new ProfiledPIDController(5,
                                                                     0,
                                                                     0,
                                                                     new Constraints(Units.degreesToRadians(360),
                                                                                     Units.degreesToRadians(180))
                                           ));
      driverXbox.start().onTrue(Commands.runOnce(() -> drivebase.resetOdometry(new Pose2d(3, 3, new Rotation2d()))));
      driverXbox.button(1).whileTrue(drivebase.sysIdDriveMotorCommand());
      driverXbox.button(2).whileTrue(Commands.runEnd(() -> driveDirectAngleKeyboard.driveToPoseEnabled(true),
                                                     () -> driveDirectAngleKeyboard.driveToPoseEnabled(false)));


    }
    if (DriverStation.isTest())
    {
      drivebase.setDefaultCommand(driveFieldOrientedAnglularVelocity); 

      driverXbox.x().whileTrue(Commands.runOnce(drivebase::lock, drivebase).repeatedly());
      driverXbox.start().onTrue((Commands.runOnce(drivebase::zeroGyro)));
      driverXbox.back().whileTrue(drivebase.centerModulesCommand());
      driverXbox.leftBumper().onTrue(Commands.none());
      driverXbox.rightBumper().onTrue(Commands.none());
    } else
    {
      driverXbox.a().onTrue((Commands.runOnce(drivebase::zeroGyro)));
      driverXbox.x().onTrue(Commands.runOnce(drivebase::addFakeVisionReading));
      driverXbox.start().whileTrue(Commands.none());
      driverXbox.back().whileTrue(Commands.none());
      //driverXbox.leftBumper().whileTrue(Commands.runOnce(drivebase::lock, drivebase).repeatedly());
      //driverXbox.rightBumper().onTrue(Commands.none());
    }

    //----------------------------------------------------------------------------------------
    //end of swerve buttons
    //----------------------------------------------------------------------------------------
// COMENTADO PARA TESTE --===----==---=-=---=-=-=-=-=-=-=--=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=---=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=--=--=-=---=-=-=-=-==-=-=-
    // boolean isUsarUmControle = false;
    // //true = 1 controle
    // //false = 2 controle
    // double velocityRps = 73; //Shooter
    // double velocityBuffer = -0.6;
    // double velocityIntake = 0.6;

    // Command shooterCommand = Commands.startEnd(() -> shooterSubsystem.velocityOut(velocityRps), shooterSubsystem::stop, shooterSubsystem);
    //                     //.alongWith(Commands.startEnd(() -> intakeSubsystem.percentOut(velocityIntake), intakeSubsystem::stop, intakeSubsystem));

    // Command bufferShooterCommand = Commands.startEnd(() -> shooterSubsystem.velocityOut(30), shooterSubsystem::stop, shooterSubsystem)
    //                     .alongWith(Commands.startEnd(() -> bufferSubsystem.percentOut(-0.9), bufferSubsystem::stop, bufferSubsystem));

    // Command bufferShooterCommand2 = Commands.startEnd(() -> shooterSubsystem.velocityOut(45), shooterSubsystem::stop, shooterSubsystem)
    //                     .alongWith(Commands.startEnd(() -> bufferSubsystem.percentOut(-0.9), bufferSubsystem::stop, bufferSubsystem));

    // Command ShootAutoCommand = new ShootAuto(bufferSubsystem, shooterSubsystem, intakeSubsystem, velocityRps);

  //   if (isUsarUmControle) {
    
  //   driverXbox.rightBumper().whileTrue(ShootAutoCommand);
  //   driverXbox.y().whileTrue(Commands.startEnd(() -> bufferSubsystem.percentOutServo(-0.05), bufferSubsystem::stopServo, bufferSubsystem));
  //   driverXbox.a().whileTrue(Commands.startEnd(() -> bufferSubsystem.percentOutServo(0.05), bufferSubsystem::stopServo, bufferSubsystem));
  //   driverXbox.rightTrigger().whileTrue(shooterCommand);
  //   driverXbox.x().whileTrue(Commands.startEnd(() -> bufferSubsystem.percentOut2(velocityBuffer), bufferSubsystem::stop, bufferSubsystem));
  //   driverXbox.b().whileTrue(Commands.startEnd(() -> bufferSubsystem.percentOut2(-velocityBuffer), bufferSubsystem::stop, bufferSubsystem));
  //   driverXbox.leftTrigger().whileTrue(Commands.startEnd(() -> intakeSubsystem.percentOut(velocityIntake), intakeSubsystem::stop, intakeSubsystem));
  //   driverXbox.leftBumper().whileTrue(Commands.startEnd(() -> intakeSubsystem.percentOut(-velocityIntake), intakeSubsystem::stop, intakeSubsystem));
  //   driverXbox.povUp().whileTrue(bufferShooterCommand2);

  // } else {
  //   copilotXbox.rightBumper().whileTrue(ShootAutoCommand);
  //   copilotXbox.y().whileTrue(Commands.startEnd(() -> bufferSubsystem.percentOutServo(-0.05), bufferSubsystem::stopServo, bufferSubsystem));
  //   copilotXbox.a().whileTrue(Commands.startEnd(() -> bufferSubsystem.percentOutServo(0.05), bufferSubsystem::stopServo, bufferSubsystem));
  //   copilotXbox.rightTrigger().whileTrue(shooterCommand);
  //   copilotXbox.x().whileTrue(Commands.startEnd(() -> bufferSubsystem.percentOut2(velocityBuffer), bufferSubsystem::stop, bufferSubsystem));
  //   copilotXbox.b().whileTrue(Commands.startEnd(() -> bufferSubsystem.percentOut2(-velocityBuffer), bufferSubsystem::stop, bufferSubsystem));
  //   copilotXbox.leftBumper().whileTrue(Commands.startEnd(() -> intakeSubsystem.percentOut(velocityIntake), intakeSubsystem::stop, intakeSubsystem));
  //   copilotXbox.leftTrigger().whileTrue(Commands.startEnd(() -> intakeSubsystem.percentOut(-velocityIntake), intakeSubsystem::stop, intakeSubsystem));
  //   copilotXbox.povUp().whileTrue(bufferShooterCommand2);
  //   }
     }

  public Command getAutonomousCommand()  
  {
   return drivebase.getAutonomousCommand("percurso");

  }

  public void setMotorBrake(boolean brake)
  {
    drivebase.setMotorBrake(brake);
  }

  
}

