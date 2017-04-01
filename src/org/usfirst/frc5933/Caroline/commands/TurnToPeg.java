package org.usfirst.frc5933.Caroline.commands;

import org.usfirst.frc5933.Caroline.Robot;
import org.usfirst.frc5933.Caroline.SocketVision;
import org.usfirst.frc5933.Caroline.subsystems.DriveTrainSystem.Direction;

import edu.wpi.first.wpilibj.command.Command;

public class TurnToPeg extends Command {

	private Direction direction_;
	private boolean finished;
	private static long startTime = 0;
	public TurnToPeg(){
		requires(Robot.driveTrainSystem);
		direction_ = null;
	}

	public TurnToPeg(Direction direction){
		requires(Robot.driveTrainSystem);
		direction_ = direction;
	}

	protected void initialize(){
		if(direction_ == null || !Robot.peg_vision_working()){
			finished = true;
		}
		setTimeout(5);
		startTime = System.currentTimeMillis();
	}

	protected void execute(){
		switch(direction_){
		case Left:			
			Robot.driveTrainSystem.tankDrive(0.4 * Robot.driveTrainSystem.getLeftCoefficient(), -0.4);
			break;
		case Right:
			Robot.driveTrainSystem.tankDrive(-0.4 * Robot.driveTrainSystem.getLeftCoefficient(), 0.4);
			break;
		default: 
			finished = true;
			break;
		}
	}

	protected boolean isFinished() {
		finished = (Robot.get_peg_direction() == SocketVision.NADA) && (Math.round(Robot.get_peg_degrees_x()) == -1) && (System.currentTimeMillis() - startTime > 800);
		return finished || isTimedOut();
	}

	protected void end(){
		Robot.driveTrainSystem.stop();
	}

	protected void interrupted(){
		end();
	}

}
