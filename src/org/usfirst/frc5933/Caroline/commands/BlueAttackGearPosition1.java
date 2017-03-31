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

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.usfirst.frc5933.Caroline.subsystems.*;
import org.usfirst.frc5933.Caroline.subsystems.DriveTrainSystem.Direction;
import org.usfirst.frc5933.Caroline.subsystems.DriveTrainSystem.DriveTrainConfigurations;

/**
 *
 */
public class BlueAttackGearPosition1 extends CommandGroup {

	// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=PARAMETERS
	public BlueAttackGearPosition1() {

		// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=PARAMETERS
		// Add Commands here:
		// e.g. addSequential(new Command1());
		// addSequential(new Command2());
		// these will run in order.

		// To run multiple commands at the same time,
		// use addParallel()
		// e.g. addParallel(new Command1());
		// addSequential(new Command2());
		// Command1 and Command2 will run in parallel.

		// A command group will require all of the subsystems that each member
		// would require.
		// e.g. if Command1 requires chassis, and Command2 requires arm,
		// a CommandGroup containing them would require both the chassis and the
		// arm.
		// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=COMMAND_DECLARATIONS
		addSequential(new SimpleDriveStraight(2));
		addSequential(new TurnToPeg(Direction.Right));
		addSequential(new DriveToPeg());
		addSequential(new DefaultAutonomousCommand(15));

		// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=COMMAND_DECLARATIONS

		// FIXME: do these need to be before the addSequential is called?
		// so they don't accidentally run the basic 10 second tank drive?
		// or will that throw an error as DriveStraight doesn't technically
		// exist yet?

	}
}
