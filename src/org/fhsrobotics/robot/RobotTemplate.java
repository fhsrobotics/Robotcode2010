/*----------------------------------------------------------------------------*/
/* Copyright (c) FHS 2010. All Rights Reserved.                               */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.   October Pi                                                  */
/*----------------------------------------------------------------------------*/

package org.fhsrobotics.robot;


import edu.wpi.first.wpilibj.*;

import edu.wpi.first.wpilibj.camera.AxisCamera;
import org.fhsrobotics.robot.control.OmniDrive;


/*

		PWM
		1: Front right motor
		2: Front left motor
		3: Back right motor
		4: Back left motor
		5: Camera servo l/r (unused)
		6: Camera servo u/d (unused)

		Digital I/O
		1: Pressure switch

		Relay
		1: Compressor spike controller

		cRIO digital module
		1: Solenoid
		2: Solenoid
		Solenoid port numbers can be freely swapped.

*/

/**
 * The RobotMain is the iterative class that makes the robot operate!
 *
 * @author Davis
 *    @author Davis
 */
public class RobotTemplate extends IterativeRobot
{
	//How the robot moves around.
	OmniDrive drive;

	Compressor c;

	//Solenoids. Two, because this was meant for only one solenoid valve.
	Solenoid sol1, sol2;

	//Joystick references.
	Joystick lJoy, rJoy, tJoy;

	//The trigger that releases the kicker.
	Object trigger;

	//The limit switch that triggers when the pneumatics has reached the kicker.
	DigitalInput limitPneumatic;


	//Control vars for the shooter.
	boolean isCocked;
	int triggerTimeRemaining;
	boolean extending;
	boolean shotAuto;

	boolean inTankDrive;
	boolean lJoyTriggerLast;

	AxisCamera axis;

	Servo camx;
	Servo camy;

	double posx;
	double posy;

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit()
	{
		Watchdog.getInstance().setEnabled(false);

		Timer.delay(10);
		axis = AxisCamera.getInstance();
		axis.writeResolution(AxisCamera.ResolutionT.k640x480);
		//axis.writeRotation(AxisCamera.RotationT.k180);
		axis.writeCompression(0);


		//Initialize everything.
		c = new Compressor(1,1);
		c.start();

		sol1 = new Solenoid(7,1);
		sol2 = new Solenoid(7,2);

		lJoy = new Joystick(1);
		rJoy = new Joystick(2); // not the same controller!
//		tJoy = new Joystick(3);

		drive = new OmniDrive(rJoy, lJoy,
				new Jaguar(1), new Jaguar(2),
				new Jaguar(3), new Jaguar(4));

		//Initialize the new trigger.
		trigger = null;

		//Initialize the limit switch
		//limitPneumatic = new DigitalInput(1);

		isCocked = true;
		triggerTimeRemaining = 0;
		extending = false;
		shotAuto = false;

		inTankDrive = true;

		lJoyTriggerLast = false;

		//camx = new Servo(7);
		//camy = new Servo(8);

		posx = 0;
		posy = 0;

		count = 0;
    }

	int count;

	public void autonomousInit()
	{
		count = 0;
	}

    /**
     * This function is called periodically during autonomous.
     */
    public void autonomousPeriodic()
	{
		if(count < Integer.MAX_VALUE)
			++count;

		if(count < 80)
		{
			sol1.set(false);
			sol2.set(true);
			//Joystick values
			drive.updateIndependant(-0.5, 0, 0, 0);
		}
		else if(count < 88)
		{
			//Extend
			sol1.set(true);
			sol2.set(false);
		}
		else
		{
			sol1.set(false);
			sol2.set(true);
			drive.updateIndependant(0, 0, 0, 0);
		}

		//And charge the accumulators if need be.
		if(!c.enabled())
			c.start();
	}

    /**
     * This function is called periodically during operator control.
     */
    public void teleopPeriodic()
	{
//		System.out.print(tJoy.getRawAxis(1));
//		System.out.print(" ");
//		System.out.print(tJoy.getRawAxis(2));
//		System.out.print(" ");
//		System.out.print(tJoy.getRawAxis(3));
//		System.out.print(" ");
//		System.out.print(tJoy.getRawAxis(4));
//		System.out.print(" ");
//		System.out.print(tJoy.getRawAxis(5));
//		System.out.print(" ");
//		System.out.print(tJoy.getRawAxis(6));
//		System.out.println(" ");

		if(rJoy.getTrigger() && !lJoyTriggerLast)
			inTankDrive = !inTankDrive;
		lJoyTriggerLast = rJoy.getTrigger();


		//Update the drive code.
		//Changed to only have independant.
		//if(inTankDrive)
		//	drive.updateTank();
		//else
			drive.updateIndependant();

		//This might not be necessary, but I added it because I didn't want to
		//	build it and have such a little thing fail the robot.
        if(!c.enabled())
			c.start();

		//Make it extend with trigger pressed, retract with released.
		sol1.set(lJoy.getTrigger());
		sol2.set(!lJoy.getTrigger());

		//Begin kicker control code.
		//Removed in favor of more driver-dependant code.
		/*if(isCocked == true && rJoy.getTrigger())
		{
			//trigger.fire();
			isCocked = false;
			//Make the code wait 2 seconds before retracting the solenoids.
			triggerTimeRemaining = (int)(3000);
			extending = true;
			//Extend the solenoids.
			sol1.set(true);
			sol2.set(false);
		}
		if(extending == true)
		{
			if(triggerTimeRemaining > 0)
				triggerTimeRemaining--;
			else if(triggerTimeRemaining <= 0)
			{
				//Reverse the solenoids to normal, and set the timer for the next shot.
				sol1.set(false);
				sol2.set(true);
				extending = false;
				this.
				triggerTimeRemaining = (int)(3000);
			}
		}
		else
		{
			if(triggerTimeRemaining > 0)
				triggerTimeRemaining--;
			else if(triggerTimeRemaining <= 0)
			{

				//The kicker just finished cocking.
				sol1.set(false);
				sol2.set(true);
				extending = false;
				isCocked = true;
			}
		}*/

    }

}
