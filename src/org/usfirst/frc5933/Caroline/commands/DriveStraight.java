// RobotBuilder Version: 2.0
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.

package org.usfirst.frc5933.Caroline.commands;

import org.usfirst.frc5933.Caroline.Robot;
import org.usfirst.frc5933.Caroline.subsystems.DriveTrainSystem.DriveTrainConfigurations;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class DriveStraight extends Command {

	private static boolean usePresets_ = false;
	private static double feetPreset_ = 0;

	private static DriveTrainConfigurations config_ = null;

	// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_DECLARATIONS

	// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_DECLARATIONS

	// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTOR
	public DriveStraight() {

		// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTOR
		// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_SETTING

		// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_SETTING
		// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES
		requires(Robot.driveTrainSystem);

		// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		if (usePresets_) {

			if (!Robot.driveTrainSystem.setStraightMotionMagic(feetPreset_, config_)) {
				// if you aren't using a valid config, get out of here!
				end();
			}

		} else {
			setTimeout(10);
		}
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		if (usePresets_) {

			Robot.driveTrainSystem.goStraightMotionMagic(feetPreset_, config_);

		} else {
			Robot.driveTrainSystem.tankDrive(0.5, 0.5);
		}
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		if (usePresets_) {
			return Robot.driveTrainSystem.goStraightMotionMagic(feetPreset_, config_);

		} else {
			return isTimedOut();
		}
	}

	// Called once after isFinished returns true
	protected void end() {
		Robot.driveTrainSystem.stop();
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
		end();
	}

	public static void usePresets(boolean useThem) {
		usePresets_ = useThem;
	}

	public static void setFeet(double f) {
		feetPreset_ = f;
	}

	public static void setDriveTrainConfig(DriveTrainConfigurations configuration) {
		config_ = configuration;
	}
}
