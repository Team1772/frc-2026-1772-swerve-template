// package frc.robot.commands.swervedrive.auto.shooter;

// import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
// import edu.wpi.first.wpilibj2.command.WaitCommand;
// import frc.robot.subsystems.BufferSubsystem;
// import frc.robot.subsystems.IntakeSubsystem;
// import frc.robot.subsystems.ShooterSubsystem;
// import frc.robot.subsystems.swervedrive.SwerveSubsystem;

// public class AutoShootAndBack extends SequentialCommandGroup {

//   public AutoShootAndBack(SwerveSubsystem drive,
//                           BufferSubsystem buffer,
//                           ShooterSubsystem shooter,
//                           IntakeSubsystem intake) {

//     addCommands(

//       // 1️⃣ anda para trás
//       drive.driveCommand(() -> -0.4, () -> 0.0, () -> 0.0)
//           .withTimeout(1.5),

//       // pequena pausa
//       new WaitCommand(1.0),

//       // 2️⃣ executa o shooter
//       new ShootAuto(buffer, shooter, intake, 73).withTimeout(3)

//     );
//   }
// }
