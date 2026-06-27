// package frc.robot.commands.swervedrive.auto.shooter;

// import edu.wpi.first.wpilibj.Timer;
// import edu.wpi.first.wpilibj2.command.Command;
// import frc.robot.subsystems.BufferSubsystem;
// import frc.robot.subsystems.IntakeSubsystem;
// import frc.robot.subsystems.ShooterSubsystem;

// public class ShootAuto extends Command {
//   private final BufferSubsystem buffer;
//   private final ShooterSubsystem shooter;
//   private final IntakeSubsystem intake;
//   private final double rps;
//   private final int errorLimit = 5;

//   public ShootAuto(BufferSubsystem buffer, ShooterSubsystem shooter, IntakeSubsystem intake, double rps) {
//     this.buffer = buffer;
//     this.shooter = shooter;
//     this.intake = intake;
//     this.rps = rps;
//     addRequirements(this.buffer, this.intake);
//   }

//   @Override
//   public void initialize() {

//   }

//   @Override
//   public void execute() {

//     this.intake.percentOut(0.7);

//     boolean isbufferenable = (Math.abs(rps - this.shooter.getVelocity())) <= Math.abs(errorLimit);

//     if (isbufferenable) {
//       this.buffer.percentOut(-0.6);
//     }
//   }

//   @Override
//   public void end(boolean isInterrupted) {
//     this.buffer.stop();
//     this.intake.stop();
//   }
// }