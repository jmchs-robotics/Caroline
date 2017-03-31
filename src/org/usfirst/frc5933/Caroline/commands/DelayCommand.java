package org.usfirst.frc5933.Caroline.commands;

import edu.wpi.first.wpilibj.command.*;

public class DelayCommand extends Command{
	private double timeout_ = 0;
	
	public DelayCommand(double timeout){
		timeout_ = timeout;
	}
	
	public DelayCommand(){
		timeout_ = 5;
	}
	
	protected void initialize(){
		setTimeout(timeout_);
	}
	
	protected void execute(){
		
	}
	
	protected boolean isFinished(){
		return isTimedOut();
	}
	
	protected void end(){
		
	}
	
	protected void interrupted(){
		end();
	}
}
