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

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class FlyWheelSystem extends Subsystem {

	public final static float kNominalVoltage = 0;
	public final static float kPeakVoltage = 12;

	public final static double kVbusIncrement = 0.01;
	public final static double kVbusMin = 0;
	public final static double kVbusMax = 1;

	public final static double kSpeedIncrement = 5;
	public final static double kSpeedMin = 0;
	public final static double kSpeedMax = 500;

	private double vbus_ = 0;
	private double speed_ = 0;

	public boolean vbus_mode = false;
	private boolean running_ = false; 

	// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANTS

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANTS

	// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    private final CANTalon flyWheelMotor = RobotMap.flyWheelSystemFlyWheelMotor;

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS

	// Put methods for controlling this subsystem
	// here. Call these from Commands.

	public void initDefaultCommand() {
		// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND


    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND

		// Set the default command for a subsystem here.
		// setDefaultCommand(new MySpecialCommand());
	}

	public void robotInit() {
		configVoltages(kNominalVoltage, kPeakVoltage);
		configFeedback();
	}

	public void teleopInit() {
	}

	public void autonomousInit() {
	}

	private void configVoltages(float nominal, double peak) {
		flyWheelMotor.configNominalOutputVoltage(nominal, -nominal);
		flyWheelMotor.configPeakOutputVoltage(peak, -peak);
	}

	private void configFeedback() {
		flyWheelMotor.setFeedbackDevice(FeedbackDevice.QuadEncoder);
	}

	public void setVbusMode() {
		flyWheelMotor.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
		vbus_mode = true;
	}

	public void setVbus(double s) {
		setVbusMode();
		vbus_ = s;
		double vbus = getAdjustedVbus();
		flyWheelMotor.set(vbus);
		SmartDashboard.putNumber("Fly Wheel Vbus", vbus);
		if (Robot.show_debug_flywheel) {
			System.out.println("vbus: " + vbus);
		}
	}

	private void goVbus() {
		setVbusMode();					//a safety check
		double vbus = getAdjustedVbus();
		flyWheelMotor.set(vbus);
		if (Robot.show_debug_flywheel) {
			System.out.println("Adjusted vbus: " + vbus);
			System.out.println("Encoder Out: " + flyWheelMotor.getSpeed());
		}
	}

	public void stop() {
		flyWheelMotor.set(0);
		if (Robot.show_debug_flywheel) {
			System.out.println("FlyWheelMotor Off");
		}
	}

	public void setSpeedMode() {
		flyWheelMotor.changeControlMode(CANTalon.TalonControlMode.Speed);
		flyWheelMotor.configEncoderCodesPerRev(40); // 20 per channel, two
		// channels, 20*2 = 40
		// Pulses Per Revolution:
		// 20/channel
		flyWheelMotor.setInverted(true);
		vbus_mode = false;
	}

	public void setSpeed(double s) {
		setSpeedMode();
		flyWheelMotor.set(s);
		speed_ = s;
		SmartDashboard.putNumber("Fly Wheel Speed", speed_);
		if (Robot.show_debug_flywheel) {
			System.out.println("Speed: " + s);
		}
	}

	private void goSpeed() {
		setSpeedMode();				//safety check
		flyWheelMotor.set(speed_);
		if (Robot.show_debug_flywheel) {
			System.out.println("Given Speed: " + speed_);
			System.out.println("Encoder Out: " + flyWheelMotor.getSpeed());
		}
	}

	public void speedUp() {
		speed_ += kSpeedIncrement;
		if (speed_ > kSpeedMax)
			speed_ = kSpeedMax;

		if (Robot.show_debug_flywheel) {
			System.out.println("speed: " + speed_);
		}
	}

	public void speedDown() {
		speed_ -= kSpeedIncrement;
		if (speed_ < kSpeedMin)
			speed_ = kSpeedMin;

		if (Robot.show_debug_flywheel) {
			System.out.println("speed: " + speed_);
		}
	}

	public void vbusDown() {
		vbus_ -= kVbusIncrement;
		if (vbus_ < kVbusMin)
			vbus_ = kVbusMin;

		if (Robot.show_debug_flywheel) {
			System.out.println("vbus: " + vbus_);
		}
	}

	public void vbusUp() {
		vbus_ += kVbusIncrement;
		if (vbus_ > kVbusMax)
			vbus_ = kVbusMax;

		if (Robot.show_debug_flywheel) {
			System.out.println("vbus: " + vbus_);
		}
	}

	private double getAdjustedVbus() {
		return vbus_ * kPeakVoltage / RobotMap.powerSystemPowerDistributionPanel.getVoltage();
	}

	public void teleopPeriodic() {
		if(!running_)
			return;
		if (vbus_mode) {
			goVbus();
		} else {
			goSpeed();
		}
	}

	public void autonomousPeriodic() {
		if (vbus_mode) {
			goVbus();
		} else {
			goSpeed();
		}
	}

	public void toggle() {
		running_ = !running_;
	}

	public boolean isRunnning() {
		return running_;
	}

}

