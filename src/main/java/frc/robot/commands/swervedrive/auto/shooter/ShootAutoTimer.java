// package frc.robot.commands.swervedrive.auto.shooter;

// import edu.wpi.first.wpilibj.Timer;
// import edu.wpi.first.wpilibj2.command.Command;
// import frc.robot.subsystems.BufferSubsystem;
// import frc.robot.subsystems.ShooterSubsystem;

// public class ShootAutoTimer extends Command {
//   public final BufferSubsystem buffer;
//   private final BufferSubsystem servo;
//   private final ShooterSubsystem shooter;
//   private final Timer timer;
//   private double secondsEnabled;

//   public ShootAutoTimer(BufferSubsystem intake, ShooterSubsystem shooter, BufferSubsystem servoBuffer, double secondsEnabled) {
//     this.servo = servoBuffer;
//     this.buffer = intake;
//     this.shooter = shooter;
//     this.secondsEnabled = secondsEnabled;

//     this.timer = new Timer();
//     addRequirements(this.servo, this.buffer, this.shooter);
//   }

//   @Override
//   public void initialize() {
//     this.timer.reset();
//     this.timer.start();
//   }

//   @Override
//   public void execute() {

//     this.servo.percentOutServo(0.05);
//     this.shooter.velocityOut(73);

//     boolean isBufferEnable = (Math.abs(73 - this.shooter.getVelocity())) <= Math.abs(5);

//     if (isBufferEnable) {
//       if (this.buffer.motorVortex.getOutputCurrent() >= 25 && this.buffer.motorVortex.getEncoder().getVelocity() <= 100){
//         //this.buffer.rollbackBuffer(0.6);
//         this.buffer.percentOut(-0.8, 0.05);
//         //System.out.println("rollback");
//       } 
//       else{
//         if (this.timer.hasElapsed(6)) {
//           this.buffer.percentOut(-0.6, 0.05);
//           //System.out.println("percentout");
//         }
//         else{
//           this.buffer.percentOut(-0.6, -0.05);
//           //System.out.println("percentout3");
//         }
//       }
//     }
//   }

//   @Override
//   public boolean isFinished() {
//     return this.timer.hasElapsed(this.secondsEnabled);
//   }

//   @Override
//   public void end(boolean isInterrupted) {
//     this.buffer.stop();
//     this.shooter.stop();
//     this.timer.stop();
//   }
// }