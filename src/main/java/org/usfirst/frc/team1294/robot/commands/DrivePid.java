package org.usfirst.frc.team1294.robot.commands;

import org.usfirst.frc.team1294.robot.Robot;
import org.usfirst.frc.team1294.robot.subsystems.DriveBase;

import edu.wpi.first.wpilibj.command.PIDCommand;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DrivePid extends PIDCommand {

	private static final double PID_TOLERANCE = 0.25;
	private static final double PID_P = 0.05;
	private static final double PID_I = 0.01;
	private static final double PID_D = 0.00;
	
	private DriveBase driveBase;
	private double heading;
	private double speed;
	private double distance;
	private double leftEncoderStart;
	private double rightEncoderStart;
		
	public DrivePid(double heading, double speed, double distance) {
		// TODO: tune this pid
		super(String.format("DrivePid(%f, %f, %f)", heading, speed, distance), PID_P, PID_I, PID_D);
		
		requires(Robot.driveBase);
		this.driveBase = Robot.driveBase;
		this.heading = heading;
		this.speed = speed;
		this.distance = distance;
		
		// since this is a closed loop command, and may be running without operator intervention, set a timeout
		this.setTimeout(15);
		
		
		
		LiveWindow.addActuator("DriveSystem", "DriveHeadingPid", this.getPIDController());
	}

	@Override
	protected void initialize() {
		// if heading was not provided, use current heading
		if (heading < 0) {
			this.setSetpoint(driveBase.getNormalizedAngle());
			System.out.println("setpoint to current heading " + driveBase.getNormalizedAngle());
		} else {
			this.setSetpoint(heading);
		}
		
		this.getPIDController().setInputRange(0, 360);
		this.getPIDController().setOutputRange(-1, 1);
		this.getPIDController().setContinuous();
		this.getPIDController().setPercentTolerance(PID_TOLERANCE);
		
		this.driveBase.setTalonsToClosedLoopSpeed();
		
		this.leftEncoderStart = this.driveBase.getLeftPosition();
		this.rightEncoderStart = this.driveBase.getRightPosition();
	
		SmartDashboard.putData(this);
	}
	
	@Override
	protected double returnPIDInput() {
		return this.driveBase.getNormalizedAngle();
	}

	@Override
	protected void usePIDOutput(double output) {
		System.out.println("output " + output);
		this.driveBase.arcadeDrive(speed, output);
	}

	@Override
	protected void execute() {
		
	}

	@Override
	protected boolean isFinished() {
		if (isTimedOut()) {
			return true;
		}
		
		// return false if robot is not pointing the correct direction
		if (Math.abs(driveBase.getNormalizedAngle() - this.heading) > PID_TOLERANCE) {
			return false;
		}
		
		// return false if robot has not traveled far enough
		double distanceDriven = (
				Math.abs(driveBase.getLeftPosition() - leftEncoderStart) + 
				Math.abs(driveBase.getRightPosition() - rightEncoderStart)) / 2;
		if (distanceDriven < distance) {
			return false;
		}
		
		return true;
	}

	@Override
	protected void end() {
		this.driveBase.setTalonsToOpenLoop();
		this.driveBase.arcadeDrive(0, 0);
	}

	@Override
	protected void interrupted() {
		end();
	}
}
