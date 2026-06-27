// package frc.robot.subsystems;

// import com.revrobotics.PersistMode;
// import com.revrobotics.ResetMode;
// import com.revrobotics.spark.SparkMax;
// import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
// import com.revrobotics.spark.config.SparkMaxConfig;
// import com.revrobotics.spark.SparkLowLevel.MotorType;

// import edu.wpi.first.wpilibj2.command.SubsystemBase;

// public class IntakeSubsystem extends SubsystemBase {
//     private SparkMax motorVortex;

//     public IntakeSubsystem() {
//         motorVortex = new SparkMax(14, MotorType.kBrushless);

//         SparkMaxConfig config = new SparkMaxConfig();
//         config
//         .smartCurrentLimit(40)
//         .idleMode(IdleMode.kCoast)
//         .inverted(true);

//         motorVortex.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
//     }

//     public void percentOut(double speed) {
//       motorVortex.set(speed);
//     }

//     public void stop() {
//       motorVortex.stopMotor();
//     }
    
// }
