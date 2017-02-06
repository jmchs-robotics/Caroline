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
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.SpeedController;

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=IMPORTS
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

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
    public static SpeedController climbingSystemClimbingClawMotor;
    public static CANTalon flyWheelSystemFlyWheelMotor;
    public static CANTalon ballCollectionSystemElevatorMotor;
    public static CANTalon ballCollectionSystemVacuumMotor;
    public static CANTalon hopperSystemAgitiatorMotor;
    public static CANTalon driveTrainSystemLeftMasterMotor;
    public static CANTalon driveTrainSystemRightMasterMotor;
    public static RobotDrive driveTrainSystemRobotDrive;
    public static PowerDistributionPanel powerSystemPowerDistributionPanel;

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS

    public static void init() {
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
        gearCollectionSystemFrontFlapperMotor = new Spark(2);
        LiveWindow.addActuator("GearCollectionSystem", "FrontFlapperMotor", (Spark) gearCollectionSystemFrontFlapperMotor);
        
        gearCollectionSystemBackFlapperMotor = new Spark(1);
        LiveWindow.addActuator("GearCollectionSystem", "BackFlapperMotor", (Spark) gearCollectionSystemBackFlapperMotor);
        
        climbingSystemClimbingSpindleMotor = new CANTalon(6);
        LiveWindow.addActuator("ClimbingSystem", "ClimbingSpindleMotor", climbingSystemClimbingSpindleMotor);
        
        climbingSystemClimbingClawMotor = new Spark(0);
        LiveWindow.addActuator("ClimbingSystem", "ClimbingClawMotor", (Spark) climbingSystemClimbingClawMotor);
        
        flyWheelSystemFlyWheelMotor = new CANTalon(20);
        LiveWindow.addActuator("FlyWheelSystem", "FlyWheelMotor", flyWheelSystemFlyWheelMotor);
        
        ballCollectionSystemElevatorMotor = new CANTalon(8);
        LiveWindow.addActuator("BallCollectionSystem", "ElevatorMotor", ballCollectionSystemElevatorMotor);
        
        ballCollectionSystemVacuumMotor = new CANTalon(9);
        LiveWindow.addActuator("BallCollectionSystem", "VacuumMotor", ballCollectionSystemVacuumMotor);
        
        hopperSystemAgitiatorMotor = new CANTalon(10);
        LiveWindow.addActuator("HopperSystem", "AgitiatorMotor", hopperSystemAgitiatorMotor);
        
        driveTrainSystemLeftMasterMotor = new CANTalon(11);
        LiveWindow.addActuator("DriveTrainSystem", "LeftMasterMotor", driveTrainSystemLeftMasterMotor);
        
        driveTrainSystemRightMasterMotor = new CANTalon(12);
        LiveWindow.addActuator("DriveTrainSystem", "RightMasterMotor", driveTrainSystemRightMasterMotor);
        
        driveTrainSystemRobotDrive = new RobotDrive(driveTrainSystemLeftMasterMotor, driveTrainSystemRightMasterMotor);
        
        driveTrainSystemRobotDrive.setSafetyEnabled(true);
        driveTrainSystemRobotDrive.setExpiration(0.1);
        driveTrainSystemRobotDrive.setSensitivity(0.5);
        driveTrainSystemRobotDrive.setMaxOutput(1.0);

        powerSystemPowerDistributionPanel = new PowerDistributionPanel(0);
        LiveWindow.addSensor("PowerSystem", "PowerDistributionPanel", powerSystemPowerDistributionPanel);
        

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
    }
}
