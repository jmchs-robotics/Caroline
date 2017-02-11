// RobotBuilder Version: 2.0
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.

package org.usfirst.frc5933.Caroline.subsystems;

import org.usfirst.frc5933.Caroline.Robot;
import org.usfirst.frc5933.Caroline.RobotMap;
import org.usfirst.frc5933.Caroline.commands.DefaultTeleopCommand;

import com.ctre.CANTalon;
import com.ctre.CANTalon.TalonControlMode;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class DriveTrainSystem extends Subsystem {
	public final static float kNominalVoltage = 0;
	public final static float kPeakVoltage = 12;

	private boolean in_low_gear_ = false;
	private boolean is_shifting_ = false;

	public final static double kLowGearMin = 0;
	public final static double kLowGearMax = 5;

	public final static double kHighGearMin = 165;
	public final static double kHighGearMax = 170;

	private final CANTalon leftSlave1Motor = RobotMap.driveTrainSystemLeftSlaveMotor1;
	private final CANTalon leftSlave2Motor = RobotMap.driveTrainSystemLeftSlaveMotor2;

	private final CANTalon rightSlave1Motor = RobotMap.driveTrainSystemRightSlaveMotor1;
	private final CANTalon rightSlave2Motor = RobotMap.driveTrainSystemRightSlaveMotor2;
	
	private final static double kMaximumMagnitudePercentVBusShudder = 0.9;
	private final static double kMinimumMagnitudePercentVBusShudder = 0.2;
	
	private double shudderMagnitude = 0.5;

	public DriveTrainSystem() {
		super();

	}

	// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANTS

	// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANTS

	// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
	private final CANTalon leftMasterMotor = RobotMap.driveTrainSystemLeftMasterMotor;
	private final CANTalon rightMasterMotor = RobotMap.driveTrainSystemRightMasterMotor;
	private final RobotDrive robotDrive = RobotMap.driveTrainSystemRobotDrive;
	private final Servo leftShifter = RobotMap.driveTrainSystemLeftShifter;
	private final Servo rightShifter = RobotMap.driveTrainSystemRightShifter;

	// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS

	// Put methods for controlling this subsystem
	// here. Call these from Commands.

	public void initDefaultCommand() {
		// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND

		setDefaultCommand(new DefaultTeleopCommand());

		// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND

		// Set the default command for a subsystem here.
		// setDefaultCommand(new MySpecialCommand());
	}

	public void robotInit() {
		configVoltages(kNominalVoltage, kPeakVoltage);
		configFollower();
	}

	public void teleopInit() {
		SmartDashboard.putNumber("Shudder Magnitude:", shudderMagnitude);
		enableBrakeMode(false);
	}

	public void autonomousInit() {
		enableBrakeMode(true);
	}

	public void set(double left, double right) {
		leftMasterMotor.set(left);
		rightMasterMotor.set(right);
	}

	public void stop() {
		robotDrive.stopMotor();
	}

	public void arcadeDrive(GenericHID joystick) {
		robotDrive.arcadeDrive(joystick);
	}

	public void arcadeDrive(double moveValue, double rotateValue) {
		robotDrive.arcadeDrive(moveValue, rotateValue);
	}

	public void tankDrive(double leftValue, double rightValue) {
		robotDrive.tankDrive(leftValue, rightValue);
	}

	private void enableBrakeMode(boolean enable) {
		leftMasterMotor.enableBrakeMode(enable);
		rightMasterMotor.enableBrakeMode(enable);

		if (!Robot.is_sonny) {
			leftSlave1Motor.enableBrakeMode(enable);
			leftSlave2Motor.enableBrakeMode(enable);
			rightSlave1Motor.enableBrakeMode(enable);
			rightSlave2Motor.enableBrakeMode(enable);
		}
	}

	private void configVoltages(float nominal, double peak) {
		leftMasterMotor.configNominalOutputVoltage(nominal, -nominal);
		leftMasterMotor.configPeakOutputVoltage(peak, -peak);
		rightMasterMotor.configNominalOutputVoltage(nominal, -nominal);
		rightMasterMotor.configPeakOutputVoltage(peak, -peak);

		if (!Robot.is_sonny) {
			leftSlave1Motor.configNominalOutputVoltage(nominal, -nominal);
			leftSlave1Motor.configPeakOutputVoltage(peak, -peak);
			leftSlave2Motor.configNominalOutputVoltage(nominal, -nominal);
			leftSlave2Motor.configPeakOutputVoltage(peak, -peak);
			rightSlave1Motor.configNominalOutputVoltage(nominal, -nominal);
			rightSlave1Motor.configPeakOutputVoltage(peak, -peak);
			rightSlave2Motor.configNominalOutputVoltage(nominal, -nominal);
			rightSlave2Motor.configPeakOutputVoltage(peak, -peak);
		}
	}

	private void configFollower() {
		if (!Robot.is_sonny) {
			leftSlave1Motor.changeControlMode(TalonControlMode.Follower);
			leftSlave1Motor.set(leftMasterMotor.getDeviceID()); // tells the
			// follower
			// which master
			// to follow.
			leftSlave2Motor.changeControlMode(TalonControlMode.Follower);
			leftSlave2Motor.set(leftMasterMotor.getDeviceID()); // tells the
			// follower
			// which master
			// to follow.
			rightSlave1Motor.changeControlMode(TalonControlMode.Follower);
			rightSlave1Motor.set(rightMasterMotor.getDeviceID()); // tells the
			// follower
			// which
			// master to
			// follow.
			rightSlave2Motor.changeControlMode(TalonControlMode.Follower);
			rightSlave2Motor.set(rightMasterMotor.getDeviceID()); // tells the
			// follower
			// which
			// master to
			// follow.
		}
	}


	private void adjustGearing() { //low goes to high and high goes to low. Automagically.
		boolean left_is_done = false;
		boolean right_is_done = false;
		if (inLowGear()) { //always maintains a gear during match
			double leftAngle = leftShifter.getAngle(); 
			if (!(leftAngle >= kLowGearMin) && !(leftAngle <= kLowGearMax)) {
				leftShifter.setAngle((kLowGearMax - kLowGearMin) / 2);
			} else {
				left_is_done = true;
			}

			double rightAngle = rightShifter.getAngle(); 
			if (!(rightAngle >= kLowGearMin) && !(rightAngle <= kLowGearMax)) {
				rightShifter.setAngle((kLowGearMax - kLowGearMin) / 2);
			} else {
				right_is_done = true;
			}
			is_shifting_ = !(left_is_done && right_is_done);
		} else {
			double leftAngle = leftShifter.getAngle(); 
			if (!(leftAngle >= kHighGearMin) && !(leftAngle <= kHighGearMax)) {
				leftShifter.setAngle((kHighGearMax - kHighGearMin) / 2);
			} else {
				left_is_done = true;
			}

			double rightAngle = rightShifter.getAngle(); 
			if (!(rightAngle >= kHighGearMin) && !(rightAngle <= kHighGearMax)) {
				rightShifter.setAngle((kHighGearMax - kHighGearMin) / 2);
			} else {
				right_is_done = true;
			}
			is_shifting_ = !(left_is_done && right_is_done);
		}
	}


	public void teleopPeriodic() {
		adjustGearing();
	}

	public void autonomousPeriodic() {
		adjustGearing();
	}

	public boolean inLowGear() {
		return in_low_gear_;
	}

	public void shift() {
		if (is_shifting_)
			return;

		is_shifting_ = true;
		in_low_gear_ = !in_low_gear_;
	}
	// TODO: Test out GearShifting and make sure that the angles for the servos work. 


	public void shudder_left(){
		set(-shudderMagnitude,shudderMagnitude);
	}

	public void shudder_right(){
		set(shudderMagnitude,-shudderMagnitude);
	}
	
	public void incrementShudder(){
		if(shudderMagnitude > kMaximumMagnitudePercentVBusShudder){
			shudderMagnitude = kMaximumMagnitudePercentVBusShudder;
		}else{
			shudderMagnitude ++;
		}
		SmartDashboard.putNumber("Shudder Magnitude:", shudderMagnitude);
	}
	
	public void decrementShudder(){
		if(shudderMagnitude < kMinimumMagnitudePercentVBusShudder){
			shudderMagnitude = kMinimumMagnitudePercentVBusShudder;
		}else{
			shudderMagnitude --;
		}
		SmartDashboard.putNumber("Shudder Magnitude:", shudderMagnitude);
	}
}
