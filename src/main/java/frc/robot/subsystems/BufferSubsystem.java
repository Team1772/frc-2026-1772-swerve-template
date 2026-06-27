// package frc.robot.subsystems;




// import com.revrobotics.spark.SparkMax;
// import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
// import com.revrobotics.spark.config.SparkMaxConfig;
// import com.revrobotics.PersistMode;
// import com.revrobotics.ResetMode;
// import com.revrobotics.spark.SparkLowLevel.MotorType;
// import com.ctre.phoenix6.configs.TalonFXConfiguration;
// import com.ctre.phoenix6.hardware.TalonFX;

// import edu.wpi.first.wpilibj.Servo;
// import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
// import edu.wpi.first.wpilibj2.command.SubsystemBase;

// public class BufferSubsystem extends SubsystemBase {
// public SparkMax motorVortex;
// public TalonFX servoBuffer;

//   public BufferSubsystem() {
//     this.motorVortex = new SparkMax(12, MotorType.kBrushless);
    
//     //servoBuffer = new Servo(1);

//     servoBuffer = new TalonFX(16);
//     servoBuffer.getConfigurator().apply(new TalonFXConfiguration());

//     SparkMaxConfig config = new SparkMaxConfig();
//     config
//         .smartCurrentLimit(40)
//         .idleMode(IdleMode.kCoast)
//         .inverted(true);

//     // Persist parameters to retain configuration in the event of a power cycle
//     motorVortex.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    
//     // this.motorVortex.follow(this.motorLeftFront);

//     // this.drive = new DifferentialDrive(this.motorRightFront, this.motorLeftFront);

//   }

//     public void percentOut(double speed) {
//       motorVortex.set(speed);

//       servoBuffer.set(0.05);
//     }

//     public void percentOut2(double speed) {
//       motorVortex.set(speed);
//     }

//     public void percentOut3(double speed) {
//       motorVortex.set(speed);

//       servoBuffer.set(-0.05);
//     }

//     public void percentOutReverse(double speed) {
//       motorVortex.set(speed);

//       servoBuffer.set(0.05);
//     }
//     public void rollbackBuffer(double speed) {
//       motorVortex.set(-speed);

//       servoBuffer.set(0.05);
//     }
//     public void percentOutServo(double speedServo) {
//       servoBuffer.set(speedServo);
//     }
//     public void stopServo() {
//       servoBuffer.set(0);
//     }

//     public void percentOut(double bufferSpeed, double indexerSpeed)
//     {
//       motorVortex.set(bufferSpeed);
//       servoBuffer.set(indexerSpeed);
//     }

    
//     public void stop() {
//       motorVortex.stopMotor();

//       servoBuffer.set(0);
//     }

//     @Override
//     public void periodic()
//     {
//     SmartDashboard.putNumber("corrente:", motorVortex.getOutputCurrent());
//     }
// }