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

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.interfaces.Accelerometer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc5933.Caroline.Robot;
import org.usfirst.frc5933.Caroline.RobotMap;
import org.usfirst.frc5933.Caroline.SocketVision;

/**
 *
 */
public class DriveToPeg extends Command {
	private static boolean finished = false;

	// a good distance for the robot to stop under, so the peg will always be
	// grabbed.
	// EXPERIMENTALLY DETERMINED
	private static final double kAcceptableDist = 0;

	// used in some math to make sure the change of speed doesn't exceed 0.2
	private static final double kDegrees_x_divisor = 960;
	private static final double kDefaultDriveSpeed = 0.20;
	private static final double kMaxImpact = 2.5;
	private static Accelerometer accel = RobotMap.autoShutOffSensor;

	// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_DECLARATIONS

	// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_DECLARATIONS

	// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTOR
	public DriveToPeg() {

		// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTOR
		// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_SETTING

		// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_SETTING
		// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES
        requires(Robot.driveTrainSystem);

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		SmartDashboard.putString("driveToPeg interupted.", "pickles");
		SmartDashboard.putBoolean("Peg vision good? ", Robot.peg_vision_working());
		finished = false;
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		double dist = Robot.get_peg_distance_height();

		if ((dist <= kAcceptableDist && (!(dist <= -0.99) && !(dist >= -1.01))) || accelerometerOver(kMaxImpact)) {
			// the gear is delivered
			finished = true;
		}

		if (!finished) {
			double x_dist = Robot.get_peg_degrees_x();

			// This is so maximum error will only change the speed by .25
			double driveSpeedModifier = Math.abs(x_dist / kDegrees_x_divisor);

			if (driveSpeedModifier > 0.2)
				driveSpeedModifier = 0.2; // safety catch for above reason

			// cause modifier is always positive
			String dir = Robot.get_peg_direction();

			// TODO: remove later
			SmartDashboard.putNumber("Peg degrees_x ", x_dist);
			SmartDashboard.putNumber("Speed modifier: ", driveSpeedModifier);
			SmartDashboard.putString("Direction ", dir);
			SmartDashboard.putNumber("Distance: ", dist);

			switch (dir) {
			case SocketVision.NADA:
				// 0.5 may be too fast, change var in top.
				Robot.driveTrainSystem.set(kDefaultDriveSpeed, kDefaultDriveSpeed);
				break;
			case SocketVision.LEFT:
				// always slow down the side you want to turn to.
				// Kinda like a boat's rudder.
				Robot.driveTrainSystem.set(kDefaultDriveSpeed - driveSpeedModifier, kDefaultDriveSpeed);
				break;
			case SocketVision.RIGHT:
				Robot.driveTrainSystem.set(kDefaultDriveSpeed, kDefaultDriveSpeed - driveSpeedModifier);
				break;
			default:
				// go slower if something isn't working
				Robot.driveTrainSystem.set(kDefaultDriveSpeed / 2, kDefaultDriveSpeed / 2);

				if (Robot.show_debug_vision) {
					System.err.println("Socket failed");
				}
				break;
			}
		}
	}

	/*
	 * this method combines the three vectors to get the absolute magnitude of
	 * the accelerometer's output, then compares it to a threshold to return
	 * either true if over threshold, or false if not over.
	 */
	private boolean accelerometerOver(double threshold) {

		// this is horizontal, maybe accommodate for noise?
		double x = Math.abs(accel.getX());

		// this is horizontal, maybe accommodate for noise?
		double y = Math.abs(accel.getY());

		// this is down, so accommodate for 1 g of force (remember, RIO is
		// upside down)
		double z = Math.abs(accel.getZ()) - 1;

		// combine the three into one vector.
		// If you EVER get over 'threshold', cut the motors
		double totalMagnitude = Math.sqrt(x * x + y * y + z * z);
		SmartDashboard.putNumber("Accelerometer Magnitude ", totalMagnitude);
		if (totalMagnitude > threshold)
			return true;
		return false;
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		return finished;
	}

	// Called once after isFinished returns true
	protected void end() {
		Robot.driveTrainSystem.stop();
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
		SmartDashboard.putString("driveToPeg interupted.", "fudge");
		end();
	}
}
