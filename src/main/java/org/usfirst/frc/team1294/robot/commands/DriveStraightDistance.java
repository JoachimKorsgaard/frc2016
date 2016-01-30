package org.usfirst.frc.team1294.robot.commands;

import org.usfirst.frc.team1294.robot.Robot;
import org.usfirst.frc.team1294.robot.RobotMap;

import edu.wpi.first.wpilibj.command.Command;

@Deprecated
public class DriveStraightDistance extends Command{
	private static final double K_P = 0.05;
	private double startDistance;
	private double desiredDistance;
	private double desiredSpeed;
	private double startAngle;
	public DriveStraightDistance(double distanceMeters, double speed) {
		requires (Robot.driveBase);
		this.desiredDistance = distanceMeters;
		this.desiredSpeed = speed;
	}

	protected void initialize() {
		Robot.driveBase.setTalonsToClosedLoopSpeed();
		startDistance = Robot.driveBase.leftFrontTalon.getPosition();
		// Robot.driveTrain.getGyro().reset();
		startAngle = Robot.driveBase.getRawAngle();
	}

	// Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	// double angle = Robot.driveTrain.getGyro().getAngle();
    	// Robot.driveTrain.arcadeDrive(-this.desiredSpeed, -angle*0.03);
    	Robot.driveBase.arcadeDrive(-this.desiredSpeed, correctAngle());
    }
    
    private double correctAngle() {
    	double angle = Robot.driveBase.getRawAngle() - startAngle;
    	System.out.println("current -> " + Robot.driveBase.getRawAngle());
    	System.out.println("start -> " + startAngle);
    	while (angle < -180)
    		angle += 360;
    	System.out.println("diff -> " + angle);
    	System.out.println("===============================");
    	return angle * -K_P;
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	return (Robot.driveBase.leftFrontTalon.getPosition() - startDistance) / RobotMap.distanceScaler >= desiredDistance;
    }

    // Called once after isFinished returns true
    protected void end() {
    	
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        Robot.driveBase.stop();
    }

}
