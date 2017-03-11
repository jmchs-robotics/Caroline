package org.usfirst.frc5933.Caroline.commands;

import edu.wpi.first.wpilibj.command.*;

public class DelayCommand extends Command{
	private static double timeout_ = 0;
	
	public DelayCommand(){
		
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
	
	public static void setTheTimeout(double timeout){
		timeout_ = timeout;
		
	}
}
