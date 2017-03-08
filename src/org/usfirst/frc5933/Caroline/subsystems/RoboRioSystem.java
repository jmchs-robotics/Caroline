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

import org.usfirst.frc5933.Caroline.RobotMap;
import edu.wpi.first.wpilibj.DigitalInput;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class RoboRioSystem extends Subsystem {

	// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANTS

	// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANTS

	// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
	private final DigitalInput dIP1 = RobotMap.roboRioSystemDIP1;
	private final DigitalInput dIP2 = RobotMap.roboRioSystemDIP2;
	private final DigitalInput dIP3 = RobotMap.roboRioSystemDIP3;
	private final DigitalInput dIP4 = RobotMap.roboRioSystemDIP4;

	// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS

	// just add more dIPs to this and the code will adapt.
	private final DigitalInput[] dip_array_ = { dIP1, dIP2, dIP3, dIP4 };

	// Put methods for controlling this subsystem
	// here. Call these from Commands.

	public void initDefaultCommand() {
		// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND

		// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND

		// Set the default command for a subsystem here.
		// setDefaultCommand(new MySpecialCommand());
	}

	public void robotInit() {

	}

	public void teleopInit() {

	}

	public void autonomousInit() {

	}

	public void teleopPeriodic() {

	}

	public void autonomousPeriodic() {

	}

	/*
	 * The getDIP() method reads the Digital Input pins on the roboRIO and
	 * returns an integer based on how many switches are flipped. For example,
	 * if the set of switches is 0011101 where 0 is closed and 1 is open, this
	 * strikingly binary system is transformed into 29 and returned.
	 */
	public int getDIP() {
		int sum = 0;
		int index = 0;
		// this code will take a dIP array of any length and turn it into an
		// integer that
		// represents the selection made on the dip switch
		for (DigitalInput dip : dip_array_) {
			// if switched on,
			if (dip.get()) {
				// increment the sum by 2^count
				sum += Math.pow(2, index);
			}
			++index;
		}
		return sum;
	}
}