package frc.robot.commands.swervedrive.auto.shooter;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ShooterSubsystem;

public class Shoot extends Command {
  private final ShooterSubsystem m_shooter;
  private final double rps;

  public Shoot(ShooterSubsystem shooter, double rps) {
    m_shooter = shooter;
    this.rps = rps;
  }

  @Override
  public void initialize() {

  }

  @Override
  public void execute() {
    m_shooter.velocityOut(rps);
  }

  

  @Override
  public void end(boolean isInterrupted) {
   m_shooter.stop();
  }
}