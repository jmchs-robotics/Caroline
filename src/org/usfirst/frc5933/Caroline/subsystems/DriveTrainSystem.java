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
	/*
	 * the kRobot_* constants are all robot dimensions. Is it worth it to make these into either an ArrayList or a class?
	 * I only need these for now, but decided it was important to establish this convention now.
	 */
	private final static double kRobot_CenterTurningRadius = 14.25;	//NEEDS TO BE PRECISELY MEASURED
	private final static double kRobot_CenterTurningCircumference = 2 * kRobot_CenterTurningRadius * Math.PI; //approx 89.53539062730911
	private static final double kRobot_EncoderToOutputProportion = 15.833626974;	//this should be (but isn't) a 'tested' coefficient,
	// a good guess is:													//between what we tell the robot to do and what it actually does.
	/* drive gear (from the cim): 14 teeth
	 * driven gear (where the encoder is): 50 teeth
	 * ratio from CIM to Encoder = 50/14 = 3.57142857 : 1
	 * 
	 * Total high gear ratio: 4.5 : 1
	 * where the drive gear is the gear on the Encoder shaft
	 * and the driven gear is on the output shaft...
	 * so driven gear / drive gear = 4.5 : 1 = 9/2 from the CIM to the output.
	 * 
	 * Therefore, the ratio from the encoder gear to the output gear
	 * is 4.5 / 3.5714285 = 1.26
	 * 
	 * circumference of 4-inch drive wheels: 12.56 inches
	 * Since the encoder shaft turns 1.26 times for every rotation of the
	 * output shaft, and the output shaft turns once for every revolution of
	 * the wheels, every time the encoder completes one full revolution, the
	 * robot moves 1.26 (encoder to shaft) * 1 (pulley to pulley) * 12.56637061436 (circumference) = 15.833626974 inches
	 * 
	 * Therefore, the 1 inch to encoder rotation ratio is 1 / 15.833626974 = 0.063156723449
	 * 
	 * So, you can now hopefully put exact degrees and exact inches into any of the driveStraight
	 * code that uses inches instead of timing (so motion magic or PID stuff) and it will actually move
	 * that number of inches
	 * 
	 http://www.studica.com/ca/en/andymark/super-sonic-shifter.html
	 */

	public enum DriveTrainConfigurations {
		Auto_5F1_RightLead, Auto_5F1_LeftLead, Teleop_2F1x2, Auto_2F1x2
	};
	
	public enum Direction {
		Left, Right, Forwards, Backwards
	};
	
	public static final double kLeftCoefficient = 1.05;
	
	public final static float kNominalVoltage = 0;
	public final static float kPeakVoltage = 12;

	private boolean in_low_gear_ = false;

	private final static boolean leftSideInverted = false;
	private final static boolean rightSideInverted = false;

	// These are tested values. the set() uses a 0.0 - 1.0 range
	public final static double kLowGearMin = 0.6;

	// these are intermediate guestimated values. is more like maxLooseness
	public final static double kLowGearMax = 0.525;

	// believe me, these values work
	public final static double kHighGearMin = 0.05;

	// DON'T CHANGE (Upon penalty of death)
	public final static double kHighGearMax = 0.085;

	// the slave and master motors have a new terminology:
	// X follow Y by Z
	// this means that X motors are in follower mode, following
	// Y motors who act as 'master' motors, by Z 'modules'.
	// For example, Rosie's drivetrain ran as a 1 follow 1 by 2 setup
	// with both the left and right sides (the two 'modules')
	// consisting of 1 'master' motor and
	// 1 follower motor, hence the 1f1x2 setup (abbreviated)

	private final CANTalon leftSlave1Motor = RobotMap.driveTrainSystemLeftSlaveMotor1;
	private final CANTalon leftSlave2Motor = RobotMap.driveTrainSystemLeftSlaveMotor2;
	private final CANTalon rightSlave1Motor = RobotMap.driveTrainSystemRightSlaveMotor1;
	private final CANTalon rightSlave2Motor = RobotMap.driveTrainSystemRightSlaveMotor2;

	private final static double kMaximumMagnitudePercentVBusShudder = 0.90;
	private final static double kMinimumMagnitudePercentVBusShudder = 0.20;

	// the incrementing step. generally 0.1
	private final static double kVBusShudderIncrement = 0.1;
	private double shudderMagnitude = 0.5;

	/*
	 * from example at
	 * https://github.com/CrossTheRoadElec/FRC-Examples/blob/master/
	 * JAVA_VelocityClosedLoop/src/org/usfirst/frc/team469/robot/Robot.java
	 */
	// use codes per revolution unless otherwise specified
	private static final int kEncoderPerRev_ = 360;
	// Pulses Per Revolution: 1440
	// Cycles per revolution: 360
	// the native units are calculated by (for quadrature encoders) 4*(codes per
	// revolution)
	// so we have 4 * 360 = 1440 native units per rotation

	// Encoder info:
	// Cycles per Revolution: 360
	// Pulses per Revolution: 1440
	/*
	 * http://www.andymark.com/E4T-OEM-Miniature-Optical-Encoder-Kit-p/am-3132.
	 * htm
	 */

	// stupid sonic shifter gear ratios:
	// Low Ratio: 11.4:1 //going to test, don't need these ratios. Leaving for
	// anyone looking to do math
	// High Ratio: 4.5:1
	/* http://www.andymark.com/super-sonic-2-speed-gearbox-p/am-3039_45.htm */
	
	// Calculate native units / 100 ms (the velocity calculation is perfomed
	// every 100 ms)
	// target velocity (as rotations/min) * (1 min/60sec) * (1 sec/10ms) * 1440
	// nu/100ms = (3000 / 60 / 10) * 1440
	// calculate f gain (feed-forward) so 100% motor output is 3000 rpm
	// (setpoint in code)
	// f = 100% * (full forward output) / (native units per 100 ms)
	// f = 100% * 3000 / ((3000 * 360 / 60 / 10) * 1440) IS TECHNICALLY
	// INDEPENDENT FROM SYSTEM

	// feed-forward gain
	private static final double kFGain_R = 0.4;	//needs to be experimentally tuned
	private static final double kFGain_L = 0.4;	//needs to be experimentally tuned
	
	// calculated p gain = (percentThrottleToFixError *
	// fullForwardOutput)/(maximumError)
	// double until motor oscillates (too much p) or is adequate for sys	tem.
	// ONLY TEST WITH SYSTEM DRAG ON MOTOR

	// p gain
	private static final double kPGain_R = 0.2;
	private static final double kPGain_L = 0.2;
	
	// smoothes motion from error to setpoint.
	// Start with 10 * pgain

	// d gain
	private static final double kDGain_R = 0.0;
	private static final double kDGain_L = 0.0;

	// If dgain doesn't quite get to setpoint, add igain
	// start with 1/100 * pgain

	// i gain
	private static final double kIGain_R = 0.000;
	private static final double kIGain_L = 0.000;

	
	//these are static placeholders. Don't delete.
	private static double leftArcLength = 0;
	private static double rightArcLength = 0;
	
	
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

	private void configLeftFeedback() {
		leftMasterMotor.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);

		leftMasterMotor.reverseSensor(false);

		leftMasterMotor.configEncoderCodesPerRev(kEncoderPerRev_);

		leftMasterMotor.setEncPosition(0);
	}

	private void configRightFeedback() {
		rightMasterMotor.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);

		rightMasterMotor.reverseSensor(false);

		rightMasterMotor.configEncoderCodesPerRev(kEncoderPerRev_);

		rightMasterMotor.setEncPosition(0);
	}

	public void setVBusMode() {
		leftMasterMotor.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
		rightMasterMotor.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
	}

	private void configMotionMagic(DriveTrainConfigurations config) {
		// Totally "not" arbitrary choice
		configFollower(config);

		// TODO: config a 2 follow 1 by 2 with flexible error ceilings (new
		// method?)

		switch (config) {
		case Auto_5F1_LeftLead:
			// set control mode to motionMagic
			leftMasterMotor.changeControlMode(CANTalon.TalonControlMode.MotionMagic);
			// configure the feedback device
			configLeftFeedback();
			// configure the PID loop constants
			configLeftPID(0, kFGain_L, kPGain_L, kIGain_L, kDGain_L);
			
			leftMasterMotor.setMotionMagicAcceleration(2200);
			leftMasterMotor.setMotionMagicCruiseVelocity(2200 * kLeftCoefficient);
			
			break;
		case Auto_5F1_RightLead:
			rightMasterMotor.changeControlMode(CANTalon.TalonControlMode.MotionMagic);

			configRightFeedback();

			configRightPID(0, kFGain_R, kPGain_R, kIGain_R, kDGain_R);
			
			rightMasterMotor.setMotionMagicAcceleration(2200);
			rightMasterMotor.setMotionMagicCruiseVelocity(2200);
			
			break;
		case Auto_2F1x2:
			leftMasterMotor.changeControlMode(CANTalon.TalonControlMode.MotionMagic);
			rightMasterMotor.changeControlMode(CANTalon.TalonControlMode.MotionMagic);

			configRightFeedback();
			configLeftFeedback();
			
			configRightPID(0, kFGain_R, kPGain_R, kIGain_R, kDGain_R);
			configLeftPID(0, kFGain_L, kPGain_L, kIGain_L, kDGain_L);
			
			leftMasterMotor.setMotionMagicAcceleration(2200);
			leftMasterMotor.setMotionMagicCruiseVelocity(2200 * kLeftCoefficient);

			rightMasterMotor.setMotionMagicAcceleration(2200);
			rightMasterMotor.setMotionMagicCruiseVelocity(2200);
			
			break;
		default:
			// for safety don't do what you don't know.
			break;
		}
	}

	// this method can be called by a command in its 'INIT' phase,
	// and it will run the delivered rotations. The finish() method
	// can have a boolean statement checking whether this has
	// actually reached the set rotations.
	public boolean setStraightMotionMagic(double distance, DriveTrainConfigurations config) {
		configMotionMagic(config);
		
		distance /= kRobot_EncoderToOutputProportion;
		
		//TODO: add a cruise velocity setting that utilizes the kLeftCoefficient?
		switch (config) {
		case Auto_5F1_LeftLead:
			leftMasterMotor.changeControlMode(TalonControlMode.MotionMagic);
			leftMasterMotor.set(distance);
			break;
		case Auto_5F1_RightLead:
			rightMasterMotor.changeControlMode(TalonControlMode.MotionMagic);
			rightMasterMotor.set(distance);
			break;
		case Auto_2F1x2:
			leftMasterMotor.changeControlMode(TalonControlMode.MotionMagic);
			leftMasterMotor.set(distance);
			rightMasterMotor.changeControlMode(TalonControlMode.MotionMagic);
			rightMasterMotor.set(distance);
			break;
		default:
			return false;
		}
		return true;
	}

	public boolean goStraightMotionMagic(double distance, DriveTrainConfigurations config, int precision) {
		precision =(int)(Math.abs(precision) / kRobot_EncoderToOutputProportion);
		distance /= kRobot_EncoderToOutputProportion;
		
		switch (config) {
		case Auto_5F1_LeftLead:
			leftMasterMotor.changeControlMode(TalonControlMode.MotionMagic);
			leftMasterMotor.set(distance);
			// *in whiny voice* are we there yet? love this comment :)
			boolean leftFinished = ((leftMasterMotor.getPosition() <= precision + leftArcLength) && (leftMasterMotor.getPosition() >= leftArcLength - precision));
			
			SmartDashboard.putNumber("Left str pos", leftMasterMotor.getPosition());
			
			return leftFinished;
		case Auto_5F1_RightLead:
			rightMasterMotor.changeControlMode(TalonControlMode.MotionMagic);
			rightMasterMotor.set(distance);

			// *in whiny voice* are we there yet?
			boolean rightFinished = ((rightMasterMotor.getPosition() <= precision + rightArcLength) && (rightMasterMotor.getPosition() >= rightArcLength - precision));
			
			SmartDashboard.putNumber("Right str pos", rightMasterMotor.getPosition());
			
			return rightFinished;
		case Auto_2F1x2:
			leftMasterMotor.set(distance);
			rightMasterMotor.set(distance);

			// *in whiny voice* are we there yet?
			leftFinished = ((leftMasterMotor.getPosition() <= precision + distance) && (leftMasterMotor.getPosition() >= distance - precision));
			rightFinished = ((rightMasterMotor.getPosition() <= precision + distance) && (rightMasterMotor.getPosition() >= distance - precision));
			
			SmartDashboard.putNumber("Left str pos", leftMasterMotor.getPosition());
			SmartDashboard.putNumber("Right str pos", rightMasterMotor.getPosition());
			SmartDashboard.putBoolean("Str Finished", leftFinished && rightFinished);
			
			return leftFinished && rightFinished;
		default:
			// when this method is 'finished' running return true.
			// So, if the config is bad, get out of here.
			return true;
		}
		// you obviously ain't there yet.
	}
	
	/* Quick(ish) info about turningMotionMagic:
	 * This method acts as a setter for right and left turning arc lengths.
	 * 
	 * When a robot turns, it can either turn around its center or around a side, or somewhere in between.
	 * This method simplifies that to only center or side.
	 * 
	 * If a robot were to turn around its center, the radius of the circle its wheels trace in a 360 degree
	 * revolution is the 1/2 the width of the robot, left wheel to right wheel, with the center of the robot
	 * acting as the center of the circle.
	 * 
	 * If that same robot were to turn around its side, the radius of the circle its wheels trace in a 360
	 * degree revolution is the entire width of the robot, left wheel to right wheel, with either the left 
	 * or the right wheel acting as the center of the circle.
	 * 
	 * Therefore, first determine LEFT or RIGHT turn from Direction dir, then aroundCenter or not, then do the turn.
	 * 
	 * Quick note about turning: to turn a bot to the right (or left), reverse the right (or left) side of the drivetrain or power the left (or right)
	 * side. This will assume that, for a Right turn NOT aroundCenter, the Right side of the drivetrain will stay static.
	 * 
	 * THIS METHOD SHOULD BE CALLED ONE TIME ONLY, IN THE INITIALIZE METHOD OF A COMMAND.
	 */
	public void setTurningMotionMagic(Direction dir, double degrees, boolean aroundCenter){
		configMotionMagic(DriveTrainConfigurations.Auto_2F1x2);	//because you're going to need to turn both sides independently either way.
		
		double proportion = degrees / 360;
		double tmp_leftArcLength = proportion * kRobot_CenterTurningCircumference;
		double tmp_rightArcLength = proportion * kRobot_CenterTurningCircumference;
		
		switch(dir){
		case Left:
			if(aroundCenter){
				tmp_leftArcLength = -tmp_leftArcLength;
			}else{
				tmp_leftArcLength = 0;
				tmp_rightArcLength *= 2;
			}
			break;
		case Right:
			if(aroundCenter){
				tmp_rightArcLength = -tmp_rightArcLength;
			}else{
				tmp_rightArcLength = 0;
				tmp_leftArcLength *= 2;
			}
			break;
		default:	//if you aren't turning, you're using this method wrong. So leave. Now.
			tmp_leftArcLength = 0;
			tmp_rightArcLength = 0;
			break;
		}
		
		leftArcLength = tmp_leftArcLength / kRobot_EncoderToOutputProportion;
		rightArcLength = tmp_rightArcLength / kRobot_EncoderToOutputProportion;
		
		leftMasterMotor.changeControlMode(TalonControlMode.MotionMagic);
		leftMasterMotor.set(leftArcLength);
		
		rightMasterMotor.changeControlMode(TalonControlMode.MotionMagic);
		rightMasterMotor.set(rightArcLength);
		
		SmartDashboard.putNumber("left arc: ", leftArcLength);
		SmartDashboard.putNumber("right arc: ", rightArcLength);
	}
	/*
	 * This method should be called during execute and isFinished of a command
	 * that initialized with the above method.
	 */
	public boolean goTurningMotionMagic(int precision){
		precision = (int)(Math.abs(precision) / kRobot_EncoderToOutputProportion);
		
		leftMasterMotor.changeControlMode(TalonControlMode.MotionMagic);
		leftMasterMotor.set(leftArcLength);
		
		rightMasterMotor.changeControlMode(TalonControlMode.MotionMagic);
		rightMasterMotor.setPosition(rightArcLength);
		
		boolean leftFinished = ((leftMasterMotor.getPosition() <= precision + leftArcLength) && (leftMasterMotor.getPosition() >= leftArcLength - precision));
		boolean rightFinished = ((rightMasterMotor.getPosition() <= precision + rightArcLength) && (rightMasterMotor.getPosition() >= rightArcLength - precision));
		
		SmartDashboard.putNumber("Left pos", leftMasterMotor.getPosition());
		SmartDashboard.putNumber("Right pos", rightMasterMotor.getPosition());
		
		return leftFinished && rightFinished;
	}

	private void configLeftPID(int profileNumber, double f, double p, double i, double d) {
		/* set closed loop gains in profile 0 or profile 1 only */
		/*
		 * from example at
		 * https://github.com/CrossTheRoadElec/FRC-Examples/blob/master/
		 * JAVA_VelocityClosedLoop/src/org/usfirst/frc/team469/robot/Robot.java
		 */
		leftMasterMotor.setProfile(profileNumber);
		leftMasterMotor.setF(f);
		leftMasterMotor.setP(p);
		leftMasterMotor.setI(i);
		leftMasterMotor.setD(d);
	}

	private void configRightPID(int profileNumber, double f, double p, double i, double d) {
		/* set closed loop gains in profile 0 or profile 1 only */
		/*
		 * from example at
		 * https://github.com/CrossTheRoadElec/FRC-Examples/blob/master/
		 * JAVA_VelocityClosedLoop/src/org/usfirst/frc/team469/robot/Robot.java
		 */
		rightMasterMotor.setProfile(profileNumber);
		rightMasterMotor.setF(f);
		rightMasterMotor.setP(p);
		rightMasterMotor.setI(i);
		rightMasterMotor.setD(d);
	}

	public void set(double left, double right) {

		// accommodate for the trippy drive train.
		leftMasterMotor.set(-left);

		// none needed here.
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
		leftSlave1Motor.enableBrakeMode(enable);
		leftSlave2Motor.enableBrakeMode(enable);
		rightSlave1Motor.enableBrakeMode(enable);
		rightSlave2Motor.enableBrakeMode(enable);
	}

	private void configVoltages(float nominal, double peak) {
		leftMasterMotor.changeControlMode(TalonControlMode.PercentVbus);
		leftMasterMotor.configNominalOutputVoltage(nominal, -nominal);
		leftMasterMotor.configPeakOutputVoltage(peak, -peak);

		rightMasterMotor.changeControlMode(TalonControlMode.PercentVbus);
		rightMasterMotor.configNominalOutputVoltage(nominal, -nominal);
		rightMasterMotor.configPeakOutputVoltage(peak, -peak);

		leftSlave1Motor.changeControlMode(TalonControlMode.PercentVbus);
		leftSlave1Motor.configNominalOutputVoltage(nominal, -nominal);
		leftSlave1Motor.configPeakOutputVoltage(peak, -peak);

		leftSlave2Motor.changeControlMode(TalonControlMode.PercentVbus);
		leftSlave2Motor.configNominalOutputVoltage(nominal, -nominal);
		leftSlave2Motor.configPeakOutputVoltage(peak, -peak);

		rightSlave1Motor.changeControlMode(TalonControlMode.PercentVbus);
		rightSlave1Motor.configNominalOutputVoltage(nominal, -nominal);
		rightSlave1Motor.configPeakOutputVoltage(peak, -peak);

		rightSlave2Motor.changeControlMode(TalonControlMode.PercentVbus);
		rightSlave2Motor.configNominalOutputVoltage(nominal, -nominal);
		rightSlave2Motor.configPeakOutputVoltage(peak, -peak);
	}

	private void configReversed(boolean leftInvert, boolean rightInvert) {
		leftMasterMotor.setInverted(leftInvert);
		rightMasterMotor.setInverted(rightInvert);
	}

	private void configFollower(DriveTrainConfigurations configType) {
		// TODO create a switch for the enum
		switch (configType) {
		case Teleop_2F1x2:
			leftSlave1Motor.changeControlMode(TalonControlMode.Follower);
			leftSlave1Motor.set(leftMasterMotor.getDeviceID());
			leftSlave2Motor.changeControlMode(TalonControlMode.Follower);
			leftSlave2Motor.set(leftMasterMotor.getDeviceID());
			rightSlave1Motor.changeControlMode(TalonControlMode.Follower);
			rightSlave1Motor.set(rightMasterMotor.getDeviceID());
			rightSlave2Motor.changeControlMode(TalonControlMode.Follower);
			rightSlave2Motor.set(rightMasterMotor.getDeviceID());

			SmartDashboard.putString("Drive Train Config Type: ", "Teleop_2F1x2");
			break;
		case Auto_5F1_LeftLead:
			// TODO: Does the right side need to be inverted?
			leftSlave1Motor.changeControlMode(TalonControlMode.Follower);
			leftSlave1Motor.set(leftMasterMotor.getDeviceID());
			leftSlave2Motor.changeControlMode(TalonControlMode.Follower);
			leftSlave2Motor.set(leftMasterMotor.getDeviceID());
			rightSlave1Motor.changeControlMode(TalonControlMode.Follower);
			rightSlave1Motor.set(leftMasterMotor.getDeviceID());
			rightSlave2Motor.changeControlMode(TalonControlMode.Follower);
			rightSlave2Motor.set(leftMasterMotor.getDeviceID());

			rightMasterMotor.changeControlMode(TalonControlMode.Follower);
			rightMasterMotor.set(leftMasterMotor.getDeviceID());

			SmartDashboard.putString("Drive Train Config Type: ", "Auto_5F1_LeftLead");
			break;
		case Auto_5F1_RightLead:
			// TODO: Does the left side need to be inverted?
			leftSlave1Motor.changeControlMode(TalonControlMode.Follower);
			leftSlave1Motor.set(rightMasterMotor.getDeviceID());
			leftSlave2Motor.changeControlMode(TalonControlMode.Follower);
			leftSlave2Motor.set(rightMasterMotor.getDeviceID());
			rightSlave1Motor.changeControlMode(TalonControlMode.Follower);
			rightSlave1Motor.set(rightMasterMotor.getDeviceID());
			rightSlave2Motor.changeControlMode(TalonControlMode.Follower);
			rightSlave2Motor.set(rightMasterMotor.getDeviceID());

			leftMasterMotor.changeControlMode(TalonControlMode.Follower);
			leftMasterMotor.set(rightMasterMotor.getDeviceID());

			SmartDashboard.putString("Drive Train Config Type: ", "Auto_5F1_RightLead");
			break;
		case Auto_2F1x2:
			// same as teleop2f1x2 for now
			leftSlave1Motor.changeControlMode(TalonControlMode.Follower);
			leftSlave1Motor.set(leftMasterMotor.getDeviceID());
			leftSlave2Motor.changeControlMode(TalonControlMode.Follower);
			leftSlave2Motor.set(leftMasterMotor.getDeviceID());
			rightSlave1Motor.changeControlMode(TalonControlMode.Follower);
			rightSlave1Motor.set(rightMasterMotor.getDeviceID());
			rightSlave2Motor.changeControlMode(TalonControlMode.Follower);
			rightSlave2Motor.set(rightMasterMotor.getDeviceID());

			SmartDashboard.putString("Drive Train Config Type: ", "Teleop_2F1x2");
			break;
		default:
			// default to teleop2f1x2
			leftSlave1Motor.changeControlMode(TalonControlMode.Follower);
			leftSlave1Motor.set(leftMasterMotor.getDeviceID());
			leftSlave2Motor.changeControlMode(TalonControlMode.Follower);
			leftSlave2Motor.set(leftMasterMotor.getDeviceID());
			rightSlave1Motor.changeControlMode(TalonControlMode.Follower);
			rightSlave1Motor.set(rightMasterMotor.getDeviceID());
			rightSlave2Motor.changeControlMode(TalonControlMode.Follower);
			rightSlave2Motor.set(rightMasterMotor.getDeviceID());

			SmartDashboard.putString("Drive Train Config Type: ", "Teleop_2F1x2 -- defaulted");
			break;
		}
	}

	// GEARING BASED METHODS BELOW HERE
	private void adjustGearing() {
		// low goes to high and high goes to low Automagically.
		// WE CANNOT SHIFT! THIS COMMAND IS (sorta) DEPRECATAED
		// TODO: Remove later
		if (inLowGear()) {
			leftShifter.set(kLowGearMin); // this works without feedback
			rightShifter.set(kLowGearMin);
		} else {
			leftShifter.set(kHighGearMax);
			rightShifter.set(kHighGearMax);
		}
	}

	public boolean inLowGear() {
		return in_low_gear_;
	}

	public void shift() {
		// WE CANNOT SHIFT, NEVER CALL THIS
		in_low_gear_ = !in_low_gear_;
		SmartDashboard.putBoolean("In Low Gear: ", inLowGear());
	}

	// SHUDDER METHODS BELOW HERE
	public void shudder_left() {
		set(-shudderMagnitude, shudderMagnitude);
	}

	public void shudder_right() {
		set(shudderMagnitude, -shudderMagnitude);
	}

	public void incrementShudder() {
		// slight logic change, first change it,
		// then test how large/small it is.
		// Should be foolproof now.
		shudderMagnitude += kVBusShudderIncrement;

		if (shudderMagnitude > kMaximumMagnitudePercentVBusShudder) {
			shudderMagnitude = kMaximumMagnitudePercentVBusShudder;
		}
		SmartDashboard.putNumber("Shudder Magnitude:", shudderMagnitude);
	}

	public void decrementShudder() {
		shudderMagnitude -= kVBusShudderIncrement;

		if (shudderMagnitude < kMinimumMagnitudePercentVBusShudder) {
			shudderMagnitude = kMinimumMagnitudePercentVBusShudder;
		}
		SmartDashboard.putNumber("Shudder Magnitude:", shudderMagnitude);
	}

	// THE ROBOT, TELEOP, and AUTONOMOUS generated methods go here. So they're
	// easy to find.
	public void robotInit() {
		configVoltages(kNominalVoltage, kPeakVoltage);
		configReversed(leftSideInverted, rightSideInverted);
		configFollower(DriveTrainConfigurations.Teleop_2F1x2);
	}

	public void teleopInit() {
		SmartDashboard.putNumber("Shudder Magnitude:", shudderMagnitude);
		configFollower(DriveTrainConfigurations.Teleop_2F1x2);
		setVBusMode();
		enableBrakeMode(false);
		SmartDashboard.putBoolean("In Low Gear: ", inLowGear());
	}

	public void autonomousInit() {
		enableBrakeMode(false);
		configFollower(DriveTrainConfigurations.Auto_2F1x2);
	}

	public void teleopPeriodic() {
		adjustGearing();
	}

	public void autonomousPeriodic() {
		adjustGearing();
	}

	public double getLeftCoefficient() {
		// TODO Auto-generated method stub
		return kLeftCoefficient;
	}
}
