package org.usfirst.frc.team1294.robot.subsystems;

import org.usfirst.frc.team1294.robot.RobotMap;
import org.usfirst.frc.team1294.robot.commands.TankDriveWithJoystick;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.FeedbackDevice;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.interfaces.Gyro;

/**
 * An example subsystem.
 */
public class DriveBase extends Subsystem {
	public CANTalon leftFrontTalon;
	public CANTalon rightFrontTalon;
	public CANTalon leftBackTalon;
	public CANTalon rightBackTalon;
	public RobotDrive drive;
	private Gyro gyro;
	
	
	
    public DriveBase() {
      leftFrontTalon = new CANTalon(RobotMap.leftFrontTalon);
      leftBackTalon = new CANTalon(RobotMap.leftBackTalon);
      rightFrontTalon = new CANTalon(RobotMap.rightFrontTalon);
      rightBackTalon = new CANTalon(RobotMap.rightBackTalon);

      setDirection(Direction.FORWARDS);

      leftBackTalon.changeControlMode(CANTalon.TalonControlMode.Follower);
      leftBackTalon.set(leftFrontTalon.getDeviceID());

      rightBackTalon.changeControlMode(CANTalon.TalonControlMode.Follower);
      rightBackTalon.set(rightFrontTalon.getDeviceID());

      rightFrontTalon.reverseSensor(true);

      drive = new RobotDrive(leftFrontTalon, rightFrontTalon);

      gyro = new ADXRS450_Gyro();

      /*
      LiveWindow.addSensor(this.getName(), "AnalogGyro", (AnalogGyro) gyro);
    	LiveWindow.addActuator("Drive", "Left Front", leftFrontTalon);
    	LiveWindow.addActuator("Drive", "Left Rear", leftBackTalon);
    	LiveWindow.addActuator("Drive", "Right Front", rightFrontTalon);
    	LiveWindow.addActuator("Drive", "Right Rear", rightBackTalon);
      */
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
      rightFrontTalon.setFeedbackDevice(FeedbackDevice.QuadEncoder);
      leftFrontTalon.setFeedbackDevice(FeedbackDevice.QuadEncoder);
      rightFrontTalon.changeControlMode(CANTalon.TalonControlMode.Speed);
      leftFrontTalon.changeControlMode(CANTalon.TalonControlMode.Speed);
      rightFrontTalon.set(0);
      leftFrontTalon.set(0);
    	drive.setMaxOutput(2048); // TODO: validate this is indeed the max output we want to send to the talons
    }
    
    public void setTalonsToOpenLoop() {
      rightFrontTalon.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
      leftFrontTalon.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
      rightFrontTalon.set(0);
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
    return rightFrontTalon.getEncPosition() / RobotMap.distanceScaler;
  }

  /**
   * Inverts the motors depending on the direction specified.
   *
   * @param direction The direction for the motors to move.
   */
  public void setDirection(final Direction direction) {
    boolean leftFront;
    boolean leftBack;
    boolean rightFront;
    boolean rightBack;

    if (direction == Direction.FORWARDS) {
      leftFront = true;
      leftBack = false;
      rightFront = true;
      rightBack = false;
    } else {
      leftFront = false;
      leftBack = true;
      rightFront = false;
      rightBack = true;
    }

    leftFrontTalon.setInverted(leftFront);
    leftBackTalon.setInverted(leftBack);
    rightFrontTalon.setInverted(rightFront);
    rightBackTalon.setInverted(rightBack);

    leftFrontTalon.reverseOutput(leftFront);
    leftBackTalon.reverseOutput(leftBack);
    rightFrontTalon.reverseOutput(rightFront);
    rightBackTalon.reverseOutput(rightBack);
  }

  /**
   * A direction in which the robot can move.
   */
  public enum Direction {
    FORWARDS, BACKWARDS
  }
}
