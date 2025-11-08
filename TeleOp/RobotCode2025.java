package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraDirection;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.List;
import android.util.Size;



@TeleOp(name="Basic: Omni Linear OpMode", group="Linear OpMode")

public class RobotCode2025 extends LinearOpMode {
    
    private static final boolean USE_WEBCAM = true;
    
    private AprilTagProcessor aprilTag;
    
    private VisionPortal visionPortal;
    List<AprilTagDetection> currentDetections;
        
        

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
    
    private boolean shooterHighSpeed = false;

    @Override
    public void runOpMode() {

        // Initialize the hardware variables. Note that the strings used here must correspond
        // to the names assigned during the robot configuration step on the DS or RC devices.
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
        

        // ########################################################################################
        // !!!            IMPORTANT Drive Information. Test your motor directions.            !!!!!
        // ########################################################################################
        // Most robots need the motors on one side to be reversed to drive forward.
        // The motor reversals shown here are for a "direct drive" robot (the wheels turn the same direction as the motor shaft)
        // If your robot has additional gear reductions or uses a right-angled drive, it's important to ensure
        // that your motors are turning in the correct direction.  So, start out with the reversals here, BUT
        // when you first test your robot, push the left joystick forward and observe the direction the wheels turn.
        // Reverse the direction (flip FORWARD <-> REVERSE ) of any wheel that runs backward
        // Keep testing until ALL the wheels move the robot forward when you push the left joystick forward.
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
        
        
        initAprilTag();
        
        // Wait for the game to start (driver presses START)
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            
            if(gamepad2.right_bumper){
                leftShoot.setPower(0.9);
                rightShoot.setPower(0.9);
                
            }
            else{
                leftShoot.setPower(0);
                rightShoot.setPower(0);
            }
            if(gamepad2.a){
                popperSetPosition = 68;
            }else{
                popperSetPosition = 0;
            }
            popper.setTargetPosition(popperSetPosition);
            popper.setPower(1.0);
            
            if(gamepad2.left_bumper){
                frontleftintake.setPower(1);
                frontrightintake.setPower(-1);
                rearleftintake.setPower(1);
                rearrightintake.setPower(-1);
            }
            else{
                frontleftintake.setPower(0.0);
                frontrightintake.setPower(0.0);
                rearleftintake.setPower(0.0);
                rearrightintake.setPower(0.0);
            }
            
            if(gamepad2.b){
                rearleftintake.setPower(-1);
                rearrightintake.setPower(1);
            }
            
         /*   if(gamepad1.x){
                angleServo.setPower(167.0);
            }else if(gamepad1.b){
                angleServo.setPower(-0.5);
                runtime.reset();
        while (opModeIsActive() && (runtime.seconds() < 0.5)) {
        }
            }
           */ 
            
            
            double max;

            // POV Mode uses left joystick to go forward & strafe, and right joystick to rotate.
            double axial   = -gamepad1.left_stick_y;  // Note: pushing stick forward gives negative value
            double lateral =  gamepad1.left_stick_x;
            double yaw     =  gamepad1.right_stick_x;
            
            currentDetections = aprilTag.getDetections();
            for(AprilTagDetection detection: aprilTag.getDetections()){
                if(detection.id == 20 && (gamepad2.right_trigger > 0.25 || gamepad1.right_trigger  > 0.25)){
                    if(detection.center.x > 950){
                        yaw = 0.5;
                    }
                    else if(detection.center.x > 750){
                        yaw = 0.15;
                    }
                    else if(detection.center.x < 450){
                        yaw = -0.5;
                    }
                    else if(detection.center.x < 650){
                        yaw = -0.15;
                    }
    
                }
                if(detection.id == 24 && gamepad2.right_trigger > 0.25){
                    if(detection.center.x > 950){
                        yaw = 0.5;
                    }
                    else if(detection.center.x > 750){
                        yaw = 0.15;
                    }
                    else if(detection.center.x < 450){
                        yaw = -0.5;
                    }
                    else if(detection.center.x < 650){
                        yaw = -0.15;
                    }
    
                }
                if(detection.id == 21){
                    //pattern = "PGP";
                }
                else if(detection.id == 22){
                    //pattern = "GPP";
                }
                else if(detection.id == 23){
                    //pattern = "PPG";
                }
                if(detection.id == 20 || detection.id == 24){
                    if(detection.center.y > 200){
                        shooterHighSpeed = true;
                    }
                    else{
                        shooterHighSpeed = false;
                    }
                }
            }

            // Combine the joystick requests for each axis-motion to determine each wheel's power.
            // Set up a variable for each drive wheel to save the power level for telemetry.
            double leftFrontPower  = axial + lateral + yaw;
            double rightFrontPower = axial - lateral - yaw;
            double leftBackPower   = axial - lateral + yaw;
            double rightBackPower  = axial + lateral - yaw;

            // Normalize the values so no wheel power exceeds 100%
            // This ensures that the robot maintains the desired motion.
            max = Math.max(Math.abs(leftFrontPower), Math.abs(rightFrontPower));
            max = Math.max(max, Math.abs(leftBackPower));
            max = Math.max(max, Math.abs(rightBackPower));

            if (max > 1.0) {
                leftFrontPower  /= max;
                rightFrontPower /= max;
                leftBackPower   /= max;
                rightBackPower  /= max;
            }

            // This is test code:
            //
            // Uncomment the following code to test your motor directions.
            // Each button should make the corresponding motor run FORWARD.
            //   1) First get all the motors to take to correct positions on the robot
            //      by adjusting your Robot Configuration if necessary.
            //   2) Then make sure they run in the correct direction by modifying the
            //      the setDirection() calls above.
            // Once the correct motors move in the correct direction re-comment this code.

            /*
            leftFrontPower  = gamepad1.x ? 1.0 : 0.0;  // X gamepad
            leftBackPower   = gamepad1.a ? 1.0 : 0.0;  // A gamepad
            rightFrontPower = gamepad1.y ? 1.0 : 0.0;  // Y gamepad
            rightBackPower  = gamepad1.b ? 1.0 : 0.0;  // B gamepad
            */

            // Send calculated power to wheels
            leftFrontDrive.setPower(leftFrontPower);
            rightFrontDrive.setPower(rightFrontPower);
            leftBackDrive.setPower(leftBackPower);
            rightBackDrive.setPower(rightBackPower);

            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Front left/Right", "%4.2f, %4.2f", leftFrontPower, rightFrontPower);
            telemetry.addData("Back  left/Right", "%4.2f, %4.2f", leftBackPower, rightBackPower);
            telemetry.addData("popper position", popperSetPosition);
            telemetry.update();
        }
        visionPortal.close();
    }
    
    private void initAprilTag() {

        // Create the AprilTag processor.
        aprilTag = new AprilTagProcessor.Builder()

            // The following default settings are available to un-comment and edit as needed.
            //.setDrawAxes(false)
            //.setDrawCubeProjection(false)
            //.setDrawTagOutline(true)
            .setTagFamily(AprilTagProcessor.TagFamily.TAG_36h11)
            //.setTagLibrary(AprilTagGameDatabase.getCenterStageTagLibrary())
            //.setOutputUnits(DistanceUnit.INCH, AngleUnit.DEGREES)

            // == CAMERA CALIBRATION ==
            // If you do not manually specify calibration parameters, the SDK will attempt
            // to load a predefined calibration for your camera.
            //.setLensIntrinsics(578.272, 578.272, 402.145, 221.506)
            // ... these parameters are fx, fy, cx, cy.

            .build();

        // Adjust Image Decimation to trade-off detection-range for detection-rate.
        // eg: Some typical detection data using a Logitech C920 WebCam
        // Decimation = 1 ..  Detect 2" Tag from 10 feet away at 10 Frames per second
        // Decimation = 2 ..  Detect 2" Tag from 6  feet away at 22 Frames per second
        // Decimation = 3 ..  Detect 2" Tag from 4  feet away at 30 Frames Per Second (default)
        // Decimation = 3 ..  Detect 5" Tag from 10 feet away at 30 Frames Per Second (default)
        // Note: Decimation can be changed on-the-fly to adapt during a match.
        //aprilTag.setDecimation(3);

        // Create the vision portal by using a builder.
        VisionPortal.Builder builder = new VisionPortal.Builder();

        // Set the camera (webcam vs. built-in RC phone camera).
        if (USE_WEBCAM) {
            builder.setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"));
        } else {
            builder.setCamera(BuiltinCameraDirection.BACK);
        }
        
        // builder.setTilt;

        // Choose a camera resolution. Not all cameras support all resolutions.
         builder.setCameraResolution(new Size(1280, 800));
        

        // Enable the RC preview (LiveView).  Set "false" to omit camera monitoring.
        //builder.enableLiveView(true);

        // Set the stream format; MJPEG uses less bandwidth than default YUY2.
        builder.setStreamFormat(VisionPortal.StreamFormat.MJPEG);

        // Choose whether or not LiveView stops if no processors are enabled.
        // If set "true", monitor shows solid orange screen if no processors enabled.
        // If set "false", monitor shows camera view without annotations.
        //builder.setAutoStopLiveView(false);

        // Set and enable the processor.
        builder.addProcessor(aprilTag);

        // Build the Vision Portal, using the above settings.
        visionPortal = builder.build();

        // Disable or re-enable the aprilTag processor at any time.
        //visionPortal.setProcessorEnabled(aprilTag, true);

    }   // end method initAprilTag()
    
}
