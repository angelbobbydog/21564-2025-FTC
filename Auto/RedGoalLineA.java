package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

/*
 * This OpMode illustrates the concept of driving a path based on time.
 * The code is structured as a LinearOpMode
 *
 * The code assumes that you do NOT have encoders on the wheels,
 *   otherwise you would use: RobotAutoDriveByEncoder;
 *
 *   The desired path in this example is:
 *   - Drive forward for 3 seconds
 *   - Spin right for 1.3 seconds
 *   - Drive Backward for 1 Second
 *
 *  The code is written in a simple form with no optimizations.
 *  However, there are several ways that this type of sequence could be streamlined,
 *
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this OpMode to the Driver Station OpMode list
 */


@Autonomous(name="Red Goal Line")

public class RedGoalLineA extends LinearOpMode {

    /* Declare OpMode members. */
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor leftFrontDrive = null;
    private DcMotor leftBackDrive = null;
    private DcMotor rightFrontDrive = null;
    private DcMotor rightBackDrive = null;
    private DcMotor leftShoot = null;
    private DcMotor rightShoot = null;
    private DcMotor popper = null;
    private CRServo frontleftintake = null;
    private CRServo rearleftintake = null;
    private CRServo frontrightintake = null;
    private CRServo rearrightintake = null;
    private CRServo angleServo = null;

    @Override
    public void runOpMode() {

        // Initialize the drive system variables.
        leftFrontDrive  = hardwareMap.get(DcMotor.class, "lf_drive");
        leftBackDrive  = hardwareMap.get(DcMotor.class, "lb_drive");
        rightFrontDrive = hardwareMap.get(DcMotor.class, "rf_drive");
        rightBackDrive = hardwareMap.get(DcMotor.class, "rb_drive");
        leftShoot = hardwareMap.get(DcMotor.class, "lshoot");
        rightShoot = hardwareMap.get(DcMotor.class, "rshoot");
        popper = hardwareMap.get(DcMotor.class, "popper");
        frontleftintake = hardwareMap.get(CRServo.class, "flintake");
        frontrightintake = hardwareMap.get(CRServo.class, "frintake");
        rearleftintake = hardwareMap.get(CRServo.class, "rlintake");
        rearrightintake = hardwareMap.get(CRServo.class, "rrintake");
        angleServo = hardwareMap.get(CRServo.class, "angle_servo");
        
        // To drive forward, most robots need the motor on one side to be reversed, because the axles point in opposite directions.
        // When run, this OpMode should start both motors driving forward. So adjust these two lines based on your first test drive.
        // Note: The settings here assume direct drive on left and right wheels.  Gear Reduction or 90 Deg drives may require direction flips
        leftFrontDrive.setDirection(DcMotor.Direction.REVERSE);
        leftBackDrive.setDirection(DcMotor.Direction.REVERSE);
        rightFrontDrive.setDirection(DcMotor.Direction.FORWARD);
        rightBackDrive.setDirection(DcMotor.Direction.FORWARD);
        leftShoot.setDirection(DcMotor.Direction.FORWARD);
        rightShoot.setDirection(DcMotor.Direction.REVERSE);
        popper.setDirection(DcMotor.Direction.FORWARD);
        
        popper.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        popper.setTargetPosition(0);
        popper.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        int popperSetPosition = 0;


        // Send telemetry message to signify robot waiting;
        telemetry.addData("Status", "Ready to run");    //
        telemetry.update();

        // Wait for the game to start (driver presses START)
        waitForStart();

        // Step through each leg of the path, ensuring that the OpMode has not been stopped along the way.

        // Step 1:  Drive forward for 3 seconds
        leftFrontDrive.setPower(-0.5);
        leftBackDrive.setPower(-0.5);
        rightFrontDrive.setPower(-0.5);
        rightBackDrive.setPower(-0.5);
        runtime.reset();
        while (opModeIsActive() && (runtime.seconds() < 1.5)) {
            telemetry.addData("Path", "Leg 1: %4.1f S Elapsed", runtime.seconds());
            telemetry.update();
        }
        
        
        leftFrontDrive.setPower(0);
        leftBackDrive.setPower(0);
        rightFrontDrive.setPower(0);
        rightBackDrive.setPower(0);
        leftShoot.setPower(0.67);
        rightShoot.setPower(0.67);
        runtime.reset();
        while (opModeIsActive() && (runtime.seconds() < 2.0)) {
            telemetry.addData("Path", "Leg 1: %4.1f S Elapsed", runtime.seconds());
            telemetry.update();
        }
        popperSetPosition = 68;
        popper.setTargetPosition(popperSetPosition);
        popper.setPower(5.0);
        leftShoot.setPower(0.67);
        rightShoot.setPower(0.67);
        runtime.reset();
        while (opModeIsActive() && (runtime.seconds() < 0.5)) {
            telemetry.addData("Path", "Leg 1: %4.1f S Elapsed", runtime.seconds());
            telemetry.update();
        }
        
        popperSetPosition = 0;
        popper.setTargetPosition(popperSetPosition);
        popper.setPower(5.0);
        frontrightintake.setPower(-0.5);
        frontleftintake.setPower(0.5);
        rearleftintake.setPower(0.67);
        rearrightintake.setPower(-0.67);
        leftShoot.setPower(0.67);
        rightShoot.setPower(0.67);
        runtime.reset();
        while (opModeIsActive() && (runtime.seconds() < 1.0)) {
            telemetry.addData("Path", "Leg 1: %4.1f S Elapsed", runtime.seconds());
            telemetry.update();
        }
        
        popperSetPosition = 68;
        popper.setTargetPosition(popperSetPosition);
        popper.setPower(5.0);
        leftShoot.setPower(0.67);
        rightShoot.setPower(0.67);
        frontrightintake.setPower(0);
        frontleftintake.setPower(0);
        rearleftintake.setPower(0);
        rearrightintake.setPower(0);
        runtime.reset();
        while (opModeIsActive() && (runtime.seconds() < 0.5)) {
            telemetry.addData("Path", "Leg 1: %4.1f S Elapsed", runtime.seconds());
            telemetry.update();
        }
        
        popperSetPosition = 0;
        popper.setTargetPosition(popperSetPosition);
        popper.setPower(5.0);
        frontrightintake.setPower(-0.5);
        frontleftintake.setPower(0.5);
        rearleftintake.setPower(0.67);
        rearrightintake.setPower(-0.67);
        leftShoot.setPower(0.67);
        rightShoot.setPower(0.67);
        runtime.reset();
        while (opModeIsActive() && (runtime.seconds() < 1.0)) {
            telemetry.addData("Path", "Leg 1: %4.1f S Elapsed", runtime.seconds());
            telemetry.update();
        }


        popperSetPosition = 68;
        popper.setTargetPosition(popperSetPosition);
        popper.setPower(5.0);
        leftShoot.setPower(0.67);
        rightShoot.setPower(0.67);
        frontrightintake.setPower(0);
        frontleftintake.setPower(0);
        rearleftintake.setPower(0);
        rearrightintake.setPower(0);
        runtime.reset();
        while (opModeIsActive() && (runtime.seconds() < 0.5)) {
            telemetry.addData("Path", "Leg 1: %4.1f S Elapsed", runtime.seconds());
            telemetry.update();
        }
        
        leftFrontDrive.setPower(0.5);
        rightFrontDrive.setPower(-0.5);
        leftBackDrive.setPower(-0.5);
        rightBackDrive.setPower(0.5);
        runtime.reset();
        while (opModeIsActive() && (runtime.seconds() < 1.0)) {
            telemetry.addData("Path", "Leg 1: %4.1f S Elapsed", runtime.seconds());
            telemetry.update();
        }
        
        
        // Step 4:  Stop
        leftFrontDrive.setPower(0);
        rightFrontDrive.setPower(0);
        leftBackDrive.setPower(0);
        rightBackDrive.setPower(0);
        popperSetPosition = 0;
        popper.setTargetPosition(popperSetPosition);
        popper.setPower(5.0);
        telemetry.addData("Path", "Complete");
        telemetry.update();
        sleep(1000);
    }
}
