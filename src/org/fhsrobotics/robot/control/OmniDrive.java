/*----------------------------------------------------------------------------*/
/* Copyright (c) FHS 2010. All Rights Reserved.                               */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.fhsrobotics.robot.control;

import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Joystick;

/**
 * @author Davis
 * 
 * This class moves the robot in an omni way. It moves as if it's a tank drive,
 * but moving joysticks sideways makes the robot move sideways the average of
 * the two joysticks' x movement in magnitude.
 */
public class OmniDrive
{
	//Left and right joysticks
	Joystick leftJoy, rightJoy;

	//Jaguar controls.
	//Front right, front left, back right, back left.
	Jaguar frJag, flJag, brJag, blJag;

	/**
	 * Creates a new OmniDrive system with the specified settings.
	 * @param settings
	 */
	public OmniDrive()
	{
		//Initialise the joysticks!
		leftJoy = new Joystick(0);
		rightJoy = new Joystick(1);

		//Initialise the jaguars!
		frJag = new Jaguar(1);
		flJag = new Jaguar(2);
		brJag = new Jaguar(3);
		blJag = new Jaguar(4);
	}

	/**
	 * Creates an omni drive with the specified joysticks and jaguars.
	 * @param rJoystick
	 * @param lJoystick
	 * @param frJaguar Front right jaguar.
	 * @param flJaguar Front left jaguar.
	 * @param brJaguar Back right jaguar.
	 * @param blJaguar Back left jaguar.
	 */
	public OmniDrive(Joystick rJoystick, Joystick lJoystick,
			Jaguar frJaguar, Jaguar flJaguar,
			Jaguar brJaguar, Jaguar blJaguar)
	{
		//Set the joysticks.
		leftJoy = lJoystick;
		rightJoy = rJoystick;

		//Set the jaguars.
		frJag = frJaguar;
		flJag = flJaguar;
		brJag = brJaguar;
		blJag = blJaguar;
	}

	/**
	 * Call this method whenever you want the motors to update with new data
	 * from the joysticks.
	 */
	public void updateTank()
	{
		//Get the average value on the X axis of both joysticks.
		double averageJoyX = (rightJoy.getX()+leftJoy.getX())/2.0;

		//The whole max/min business is to limit the result from -1 to 1.

		//First, set the front jaguars. They both will go forward if the
		//joysticks are pushed forwards, the right will go backwards if the
		//joysticks are pushed to the right, and the left will go forwards if
		//the joysticks are pushed to the right.
		frJag.set(Math.max(Math.min(rightJoy.getY() - averageJoyX, 1),-1));
		flJag.set(Math.max(Math.min(leftJoy.getY() + averageJoyX, 1),-1));

		//Then set the rear jaguars. They both will go forward if the
		//joysticks are pushed forwards, the right will go forwards if the
		//joysticks are pushed to the right, and the left will go backwards if
		//the joysticks are pushed to the right.
		brJag.set(Math.max(Math.min(rightJoy.getY() + averageJoyX, 1),-1));
		blJag.set(Math.max(Math.min(leftJoy.getY() - averageJoyX, 1),-1));
	}

	public void updateIndependant()
	{
		//This drives the robot, using the L joy for rot, r joy for trans.
		//lJoy+, robot cw
		/*frJag.set(Math.max(Math.min(rightJoy.getY() + rightJoy.getX() + leftJoy.getX() + rightJoy.getZ(), 1),-1));
		flJag.set(Math.max(Math.min(rightJoy.getY() - rightJoy.getX() - leftJoy.getX() - rightJoy.getZ(), 1),-1));
		brJag.set(Math.max(Math.min(rightJoy.getY() - rightJoy.getX() + leftJoy.getX() + rightJoy.getZ(), 1),-1));
		blJag.set(Math.max(Math.min(rightJoy.getY() + rightJoy.getX() - leftJoy.getX() - rightJoy.getZ(), 1),-1));*/

		frJag.set(Math.max(Math.min(rightJoy.getY() - rightJoy.getX() - leftJoy.getX(), 1),-1));
		flJag.set(Math.max(Math.min(rightJoy.getY() + rightJoy.getX() + leftJoy.getX(), 1),-1));
		brJag.set(Math.max(Math.min(rightJoy.getY() + rightJoy.getX() - leftJoy.getX(), 1),-1));
		blJag.set(Math.max(Math.min(rightJoy.getY() - rightJoy.getX() + leftJoy.getX(), 1),-1));

		//flJag.set(1);
		//frJag.set(1);
	}
}
