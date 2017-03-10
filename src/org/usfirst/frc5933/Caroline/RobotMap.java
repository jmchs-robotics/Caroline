// RobotBuilder Version: 2.0
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.

package org.usfirst.frc5933.Caroline;

// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=IMPORTS
import com.ctre.CANTalon;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Victor;

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=IMPORTS
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.livewindow.LiveWindowSendable;
import edu.wpi.first.wpilibj.interfaces.Accelerometer;
import edu.wpi.first.wpilibj.BuiltInAccelerometer;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
	// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    public static SpeedController gearCollectionSystemFrontFlapperMotor;
    public static SpeedController gearCollectionSystemBackFlapperMotor;
    public static CANTalon climbingSystemClimbingSpindleMotor;
    public static SpeedController climbingSystemCLimbingClawMotor;
    public static CANTalon flyWheelSystemFlyWheelMotor;
    public static SpeedController hopperSystemAgitiatorMotor;
    public static CANTalon driveTrainSystemLeftMasterMotor;
    public static CANTalon driveTrainSystemRightMasterMotor;
    public static RobotDrive driveTrainSystemRobotDrive;
    public static Servo driveTrainSystemLeftShifter;
    public static Servo driveTrainSystemRightShifter;
    public static PowerDistributionPanel powerSystemPowerDistributionPanel;
    public static DigitalInput roboRioSystemDIP1;
    public static DigitalInput roboRioSystemDIP2;
    public static DigitalInput roboRioSystemDIP3;
    public static DigitalInput roboRioSystemDIP4;
    public static DigitalInput roboRioSystemDIP5;
    public static DigitalInput roboRioSystemDIP6;
    public static DigitalInput roboRioSystemDIP7;

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
	public static CANTalon driveTrainSystemLeftSlaveMotor1;
	public static CANTalon driveTrainSystemLeftSlaveMotor2;
	public static CANTalon driveTrainSystemRightSlaveMotor1;
	public static CANTalon driveTrainSystemRightSlaveMotor2;

	public static Accelerometer autoShutOffSensor;

	public static void init() {
		// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
        gearCollectionSystemFrontFlapperMotor = new Spark(2);
        LiveWindow.addActuator("GearCollectionSystem", "FrontFlapperMotor", (Spark) gearCollectionSystemFrontFlapperMotor);
        
        gearCollectionSystemBackFlapperMotor = new Spark(1);
        LiveWindow.addActuator("GearCollectionSystem", "BackFlapperMotor", (Spark) gearCollectionSystemBackFlapperMotor);
        
        climbingSystemClimbingSpindleMotor = new CANTalon(19);
        LiveWindow.addActuator("ClimbingSystem", "ClimbingSpindleMotor", climbingSystemClimbingSpindleMotor);
        
        climbingSystemCLimbingClawMotor = new Spark(5);
        LiveWindow.addActuator("ClimbingSystem", "CLimbingClawMotor", (Spark) climbingSystemCLimbingClawMotor);
        
        flyWheelSystemFlyWheelMotor = new CANTalon(11);
        LiveWindow.addActuator("FlyWheelSystem", "FlyWheelMotor", flyWheelSystemFlyWheelMotor);
        
        hopperSystemAgitiatorMotor = new Victor(0);
        LiveWindow.addActuator("HopperSystem", "AgitiatorMotor", (Victor) hopperSystemAgitiatorMotor);
        
        driveTrainSystemLeftMasterMotor = new CANTalon(6);
        LiveWindow.addActuator("DriveTrainSystem", "LeftMasterMotor", driveTrainSystemLeftMasterMotor);
        
        driveTrainSystemRightMasterMotor = new CANTalon(5);
        LiveWindow.addActuator("DriveTrainSystem", "RightMasterMotor", driveTrainSystemRightMasterMotor);
        
        driveTrainSystemRobotDrive = new RobotDrive(driveTrainSystemLeftMasterMotor, driveTrainSystemRightMasterMotor);
        
        driveTrainSystemRobotDrive.setSafetyEnabled(true);
        driveTrainSystemRobotDrive.setExpiration(0.1);
        driveTrainSystemRobotDrive.setSensitivity(0.5);
        driveTrainSystemRobotDrive.setMaxOutput(1.0);

        driveTrainSystemLeftShifter = new Servo(3);
        LiveWindow.addActuator("DriveTrainSystem", "LeftShifter", driveTrainSystemLeftShifter);
        
        driveTrainSystemRightShifter = new Servo(4);
        LiveWindow.addActuator("DriveTrainSystem", "RightShifter", driveTrainSystemRightShifter);
        
        powerSystemPowerDistributionPanel = new PowerDistributionPanel(3);
        LiveWindow.addSensor("PowerSystem", "PowerDistributionPanel", powerSystemPowerDistributionPanel);
        
        roboRioSystemDIP1 = new DigitalInput(0);
        LiveWindow.addSensor("RoboRioSystem", "DIP1", roboRioSystemDIP1);
        
        roboRioSystemDIP2 = new DigitalInput(1);
        LiveWindow.addSensor("RoboRioSystem", "DIP2", roboRioSystemDIP2);
        
        roboRioSystemDIP3 = new DigitalInput(2);
        LiveWindow.addSensor("RoboRioSystem", "DIP3", roboRioSystemDIP3);
        
        roboRioSystemDIP4 = new DigitalInput(3);
        LiveWindow.addSensor("RoboRioSystem", "DIP4", roboRioSystemDIP4);
        
        roboRioSystemDIP5 = new DigitalInput(4);
        LiveWindow.addSensor("RoboRioSystem", "DIP5", roboRioSystemDIP5);
        
        roboRioSystemDIP6 = new DigitalInput(5);
        LiveWindow.addSensor("RoboRioSystem", "DIP6", roboRioSystemDIP6);
        
        roboRioSystemDIP7 = new DigitalInput(6);
        LiveWindow.addSensor("RoboRioSystem", "DIP7", roboRioSystemDIP7);
        

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS

		// Declare the slave motors
		driveTrainSystemLeftSlaveMotor1 = new CANTalon(8);
		LiveWindow.addActuator("DriveTrainSystem", "LeftSlaveMotorl", driveTrainSystemLeftSlaveMotor1);

		driveTrainSystemLeftSlaveMotor2 = new CANTalon(10);
		LiveWindow.addActuator("DriveTrainSystem", "LeftSlaveMotor2", driveTrainSystemLeftSlaveMotor2);

		driveTrainSystemRightSlaveMotor1 = new CANTalon(7);
		LiveWindow.addActuator("DriveTrainSystem", "RightSlaveMotorl", driveTrainSystemRightSlaveMotor1);

		driveTrainSystemRightSlaveMotor2 = new CANTalon(9);
		LiveWindow.addActuator("DriveTrainSystem", "RightSlaveMotor2", driveTrainSystemRightSlaveMotor2);

		// configure the accelerometer to 4g sensing range
		autoShutOffSensor = new BuiltInAccelerometer(Accelerometer.Range.k4G);
	}
}
