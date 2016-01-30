package org.usfirst.frc.team1294.robot.subsystems;

import org.usfirst.frc.team1294.robot.RobotMap;
import org.usfirst.frc.team1294.robot.commands.TankDriveWithJoystick;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.FeedbackDevice;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * An example subsystem.
 */
public class DriveBase extends Subsystem {
	public CANTalon leftFrontTalon;
	public CANTalon rightFrontTalon;
	public CANTalon leftBackTalon;
	public CANTalon rightBackTalon;
	public RobotDrive drive;
	private ADXRS450_Gyro gyro;
	
	
	
    public DriveBase() {
    	//encoders are on talon one and four
        // Set left feedback device and talon numbers
    	leftFrontTalon = new CANTalon(RobotMap.leftFrontTalon);
    	//leftFrontTalon.setFeedbackDevice(FeedbackDevice.QuadEncoder);
    	leftBackTalon = new CANTalon(RobotMap.leftBackTalon);
    	// Set right feedback device and talon number
    	rightFrontTalon = new CANTalon(RobotMap.rightFrontTalon);
    	rightBackTalon = new CANTalon(RobotMap.rightBackTalon);
    	//rightBackTalon.setFeedbackDevice(FeedbackDevice.QuadEncoder);
    	leftFrontTalon.setInverted(true);
    	leftBackTalon.reverseOutput(true);
    	rightBackTalon.setInverted(true);
    	//set left back talon to slave mode
    	leftBackTalon.changeControlMode(CANTalon.TalonControlMode.Follower);
    	leftBackTalon.set(leftFrontTalon.getDeviceID());
    	//set right front talon to slave mode
    	rightFrontTalon.changeControlMode(CANTalon.TalonControlMode.Follower);
    	rightFrontTalon.set(rightBackTalon.getDeviceID());
    	//set control mode for encoder talons
    	
    	drive = new RobotDrive(leftFrontTalon, rightBackTalon);

			gyro = new ADXRS450_Gyro(); // calibrated automatically in the constructor - must ensure that robot is stationary

			// Automatically sent to LiveWindow in each object's constructor
			// LiveWindow.addSensor(this.getName(), "AnalogGyro", analogGyro);
//    	LiveWindow.addActuator("Drive", "Left Front", leftFrontTalon);
//    	LiveWindow.addActuator("Drive", "Left Rear", leftBackTalon);
//    	LiveWindow.addActuator("Drive", "Right Front", rightFrontTalon);
//    	LiveWindow.addActuator("Drive", "Right Rear", rightBackTalon);
    }

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new TankDriveWithJoystick());
    }
    
    public void tankDrive(Joystick left, Joystick right) {
		drive.tankDrive(left, right);
	}
    
    public void arcadeDrive(double speed, double rotation){
    	drive.arcadeDrive(speed, rotation);
    }
    
    public void setTalonsToClosedLoopSpeed(){
    	rightBackTalon.setFeedbackDevice(FeedbackDevice.QuadEncoder);
    	leftFrontTalon.setFeedbackDevice(FeedbackDevice.QuadEncoder);
    	rightBackTalon.changeControlMode(CANTalon.TalonControlMode.Speed);
    	leftFrontTalon.changeControlMode(CANTalon.TalonControlMode.Speed);
    	rightBackTalon.set(0);
    	leftFrontTalon.set(0);
    	drive.setMaxOutput(2048); // TODO: validate this is indeed the max output we want to send to the talons
    }
    
    public void setTalonsToOpenLoop() {
    	rightBackTalon.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
    	leftFrontTalon.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
    	rightBackTalon.set(0);
    	leftFrontTalon.set(0);
    }
	
	public void stop() {
		drive.drive(0,0);
	}
	

	public void tankDrive(double leftSpeed, double rightSpeed) {
		drive.tankDrive(leftSpeed, rightSpeed);
	}
	
	public double getRawAngle() {
		return gyro.getAngle();
	}
	
	public double getNormalizedAngle() {
		return gyro.getAngle() % 360;
	}
	
	public void resetGyro() {
		gyro.reset();
	}
	
	public double getLeftPosition() {
		return leftFrontTalon.getEncPosition() / RobotMap.distanceScaler;
	}
	
	public double getRightPosition() {
		return rightBackTalon.getEncPosition() / RobotMap.distanceScaler;
	}
}
