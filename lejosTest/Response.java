package lejosTest;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.utility.Delay;

/*This class produces the robot's movements*/
/**
 * Created by
 * Hadas Barel
 * Channa Goldberg
 * Meizy Gambash
 * on 10/09/2017.
 */

public class Response {
	private EV3LargeRegulatedMotor motorD;
	private EV3MediumRegulatedMotor motorB;
	private EV3MediumRegulatedMotor motorA;
	private EV3MediumRegulatedMotor motorC;
	public static Response responseInstance=null;
	
	public static Response getResponseInstance(){
		if(responseInstance==null){
			responseInstance = new Response();
		}
		return responseInstance;
	}
	private Response(){
		motorA = new EV3MediumRegulatedMotor(MotorPort.A);
		motorD = new EV3LargeRegulatedMotor(MotorPort.D);
		motorB = new EV3MediumRegulatedMotor(MotorPort.B);
		motorC = new EV3MediumRegulatedMotor(MotorPort.C);
	}
	
	public void hello(){
		int i=0;
		while (i<4){
    		motorB.setSpeed(800);
    		motorB.rotate(30);
	        motorB.resetTachoCount();
	        motorB.setSpeed(800);
	        motorB.rotate(-30);
	        motorB.resetTachoCount();
	        i++;
	        }
	    motorB.stop();
	  
    	}
	
	
	public void happy(){
		int i=0;
		motorD.setSpeed(400);
    	motorD.rotate(-10);
        motorD.resetTachoCount();
    	while (i<3){
    		motorD.setSpeed(400);
    		motorD.rotate(20);
	        motorD.resetTachoCount();
	        motorD.setSpeed(400);
	        motorD.rotate(-20);
	        motorD.resetTachoCount();
	        i++;
    	}
    	motorD.setSpeed(400);
    	motorD.rotate(10);
        motorD.resetTachoCount();
        motorD.stop();
	}
	
	public void wrong(){
		motorC.setSpeed(800);
        motorC.rotate(700);
        motorC.stop();
        Delay.msDelay(2000);
        motorC.setSpeed(800);
        motorC.rotate(-700);
        motorC.stop();
	
	}

	public void waiting(){
		motorD.setSpeed(200);
    	motorD.rotate(-40,true);
        motorD.resetTachoCount();
		motorA.setSpeed(300);
		motorB.setSpeed(300);
		motorA.rotate(-40,true);
		motorB.rotate(40);
		motorA.resetTachoCount();
        motorB.resetTachoCount();
        Delay.msDelay(300);
        motorD.setSpeed(200);
    	motorD.rotate(40,true);
        motorD.resetTachoCount();
        motorA.setSpeed(300);
		motorB.setSpeed(300);
		motorA.rotate(40,true);
		motorB.rotate(-40);
		motorA.resetTachoCount();
        motorB.resetTachoCount();
        motorA.stop();
	    motorB.stop();
        motorD.stop();
	}
	
	public void won(){
		int i=0;
		motorD.setSpeed(400);
    	motorD.rotate(-10);
        motorD.resetTachoCount();
		while (i<4){
			motorA.setSpeed(800);
    		motorB.setSpeed(800);
    		motorA.rotate(-30,true);
    		motorB.rotate(-30,true);
    		motorA.resetTachoCount();
	        motorB.resetTachoCount();
	        motorD.setSpeed(400);
    		motorD.rotate(20);
	        motorD.resetTachoCount();
	        motorA.setSpeed(800);
	        motorB.setSpeed(800);
	        motorA.rotate(30,true);
	        motorB.rotate(30,true);
	        motorA.resetTachoCount();
	        motorB.resetTachoCount();
	        motorD.setSpeed(400);
	        motorD.rotate(-20);
	        motorD.resetTachoCount();
	        i++;
	        }
		motorD.setSpeed(400);
    	motorD.rotate(10);
        motorD.resetTachoCount();
        motorA.stop();
	    motorB.stop();
        motorD.stop();
	
	}
	
	public void close(){
		motorA.close();
		motorB.close();
		motorC.close();
		motorD.close();
	}
}
