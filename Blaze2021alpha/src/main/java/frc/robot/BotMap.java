package frc.robot;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating aroun
 **/

public class BotMap {

    // 0 = comp bot
    // 1 = practice bot

    public static int botVal= 0; 

    public static class CAN {
         //changes motor ports into integers
        public final static int FRONT_LEFT = 8;
        public final static int FRONT_RIGHT = 3;
        public final static int BACK_LEFT = 4;
        public final static int BACK_RIGHT = 2;
    }

    public static class Controller {
        public final static int RUNNER = 1; 
        public final static int GUNNER = 2;

    }

    public static class Encoders { 

    }

    public static class DigitalInputs {
        public final static int GATE_LIMIT_TOP = 1;
        public final static int GATE_LIMIT_BOTTOM = 2;
    }
    
}
