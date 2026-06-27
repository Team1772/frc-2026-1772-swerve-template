package frc.robot.subsystems;

import com.ctre.phoenix6.configs.CurrentLimitsConfigs;
import com.ctre.phoenix6.configs.MotorOutputConfigs;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.SoftwareLimitSwitchConfigs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.configs.TalonFXConfigurator;
import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.controls.VelocityDutyCycle;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.MotorAlignmentValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ShooterSubsystem extends SubsystemBase{

    private final TalonFX rightMotor;
    private final TalonFX leftMotor;
    private final Follower follower;
    private final VelocityVoltage shooterpid = new VelocityVoltage(0).withSlot(0);

    private final DutyCycleOut percentOut = new DutyCycleOut(0);
    private final VelocityDutyCycle velocityOut = new VelocityDutyCycle(0);

      public ShooterSubsystem() {
        rightMotor = new TalonFX(10);
        leftMotor = new TalonFX(11);

        follower = new Follower(10, MotorAlignmentValue.Opposed);
        
         SparkMaxConfig config = new SparkMaxConfig();
        config
        .smartCurrentLimit(30)
        .idleMode(IdleMode.kCoast)
        .inverted(true);

        leftMotor.getConfigurator().apply(new TalonFXConfiguration());
        rightMotor.getConfigurator().apply(new TalonFXConfiguration());


        TalonFXConfigurator masterConfig = rightMotor.getConfigurator();
        TalonFXConfigurator slaveConfig = leftMotor.getConfigurator();


        Slot0Configs slot0Configs = new Slot0Configs();
        slot0Configs.kS = 0.1; // Add 0.1 V output to overcome static friction
        slot0Configs.kV = 0.1; // A velocity target of 1 rps results in 0.10 V output
        slot0Configs.kP = 0; // An error of 1 rps results in 0.11 V output
        slot0Configs.kI = 0; // no output for integrated error
        slot0Configs.kD = 0; // no output for error derivative
        masterConfig.apply(slot0Configs);
        // slaveConfig.apply(positionPIDConfigs);
    
        MotorOutputConfigs rightMotoroutputConfigs = new MotorOutputConfigs();
        rightMotoroutputConfigs.PeakForwardDutyCycle = 1;
        rightMotoroutputConfigs.PeakReverseDutyCycle = -1;
        rightMotoroutputConfigs.withNeutralMode(NeutralModeValue.Coast);
        rightMotoroutputConfigs.withInverted(InvertedValue.Clockwise_Positive);
        masterConfig.apply(rightMotoroutputConfigs);
        // slaveConfig.apply(rightMotoroutputConfigs);

        MotorOutputConfigs leftMotorOutputConfigs = new MotorOutputConfigs();
        leftMotorOutputConfigs.PeakForwardDutyCycle = 1;
        leftMotorOutputConfigs.PeakReverseDutyCycle = -1;
        leftMotorOutputConfigs.withInverted(InvertedValue.Clockwise_Positive);
        slaveConfig.apply(leftMotorOutputConfigs);

        SoftwareLimitSwitchConfigs limitSwitchConfigs = new SoftwareLimitSwitchConfigs();
        limitSwitchConfigs.ForwardSoftLimitEnable = false;
        limitSwitchConfigs.ReverseSoftLimitEnable = false;
        limitSwitchConfigs.ForwardSoftLimitThreshold = 0;
        limitSwitchConfigs.ReverseSoftLimitThreshold = 0;
        masterConfig.apply(limitSwitchConfigs);
        slaveConfig.apply(limitSwitchConfigs);

        CurrentLimitsConfigs currentLimitsConfigs = new CurrentLimitsConfigs();
        currentLimitsConfigs.StatorCurrentLimitEnable = false;
        currentLimitsConfigs.SupplyCurrentLimitEnable = true;
        currentLimitsConfigs.SupplyCurrentLimit = 35;
        currentLimitsConfigs.SupplyCurrentLowerLimit = 30;
        currentLimitsConfigs.SupplyCurrentLowerTime = 1;
        masterConfig.apply(currentLimitsConfigs);
        slaveConfig.apply(currentLimitsConfigs);

        }

     public void percentOut(double speed) {
        rightMotor.setControl(percentOut.withOutput(speed));
        leftMotor.setControl(follower);
    }
    
    public void velocityOut(double rps) {
        rightMotor.setControl(shooterpid.withVelocity(rps).withFeedForward(0.5));
        leftMotor.setControl(follower);
    }

    public void stop() {
        rightMotor.stopMotor();
        leftMotor.stopMotor();
    }


    public double getVelocity() {
        return rightMotor.getVelocity().getValueAsDouble();
    }

     @Override
    public void periodic() {
        SmartDashboard.putNumber("shooter velocity", rightMotor.getVelocity().getValueAsDouble());
    }
}
