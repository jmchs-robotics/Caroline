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
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class ComplexDriveStraight extends Command {
	
	double feetPreset_ = 0;
	DriveTrainConfigurations config_ = null;
	int precision_;

	// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_DECLARATIONS

	// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_DECLARATIONS

	// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTOR
	public ComplexDriveStraight() {

		// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTOR
		// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_SETTING

		// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_SETTING
		// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES
        requires(Robot.driveTrainSystem);

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES
        feetPreset_ = 0;
		config_ = null;
		precision_ = 0;
	}
	
	public ComplexDriveStraight(double feetPreset, DriveTrainConfigurations config, int precision){
		feetPreset_ = feetPreset;
		config_ = config;
		precision_ = precision;
		
		requires(Robot.driveTrainSystem);

	}

	// Called just before this Command runs the first time
	protected void initialize() {
		if(!Robot.driveTrainSystem.setStraightMotionMagic(feetPreset_, config_))
			end();
		SmartDashboard.putString("Auto Stage: ", "Complex Drive Straight");
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		Robot.driveTrainSystem.goStraightMotionMagic(feetPreset_, config_, precision_);
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
			return Robot.driveTrainSystem.goStraightMotionMagic(feetPreset_, config_,precision_);
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
}
