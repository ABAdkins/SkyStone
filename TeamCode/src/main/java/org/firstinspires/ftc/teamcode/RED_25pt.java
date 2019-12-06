/*
Copyright (c) 2016 Robert Atkinson

All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of Robert Atkinson nor the names of his contributors may be used to
endorse or promote products derived from this software without specific prior
written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESSFOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.commands.MecanumDriveCommand;
import org.firstinspires.ftc.teamcode.commands.MecanumDriveCommandSlow;
import org.firstinspires.ftc.teamcode.commands.TrackSkyStoneCommand;
import org.firstinspires.ftc.teamcode.commands.TurnGyroCommand;
import org.firstinspires.ftc.teamcode.libs.SuperGamepad;
import org.firstinspires.ftc.teamcode.subsystems.DriveSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.SkystoneArm;
import org.firstinspires.ftc.teamcode.subsystems.VuSubsystem;
//import org.firstinspires.ftc.teamcode.subsystems.SkystoneArm;

/**
 * This file contains an minimal example of a Linear "OpMode". An OpMode is a 'program' that runs in either
 * the autonomous or the teleop period of an FTC match. The names of OpModes appear on the menu
 * of the FTC Driver Station. When an selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 *
 * This particular OpMode just executes a basic Tank Drive Teleop for a PushBot
 * It includes all the skeletal structure that all linear OpModes contain.
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */
@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name="RED_25pt", group="25pt Autos")  // @Autonomous(...) is the other common choice
public class RED_25pt extends LinearOpMode {
    private ElapsedTime runtime = new ElapsedTime();
    private SuperGamepad driverGamepad;
    private CommandRunner scootIntoZone;
    private CommandRunner goToFoundation;
    private CommandRunner pullFoundation;
    private CommandRunner turnToZone;
    private CommandRunner strafeToZone;
    private CommandRunner makeWayForPartner;
    private CommandRunner backOutOfZone;
    private CommandRunner turnTowardBridge;
    private CommandRunner goToBlocks;
    private CommandRunner goToFirstSkyStone;
    private CommandRunner align;
    private CommandRunner strafeToSkystone1;
    private CommandRunner strafeAwaySkystone1;
    private CommandRunner driveBackToBase;
    private CommandRunner park;
    private CommandRunner scootOver;
    VuSubsystem vu;

    DriveSubsystem driveController;
    SkystoneArm arms;
    Servo leftFoundationServo, rightFoundationServo, leftClaw, rightClaw, capstoneServo;

    @Override
    public void runOpMode() {
        vu = new VuSubsystem(hardwareMap, telemetry, true);
        leftClaw = hardwareMap.servo.get("leftIntake");
        rightClaw = hardwareMap.servo.get("rightIntake");

        vu.init();

        leftFoundationServo = hardwareMap.servo.get("leftFServo");
        rightFoundationServo = hardwareMap.servo.get("rightFServo");
        capstoneServo = hardwareMap.servo.get("capstoneServo");


        driverGamepad = new SuperGamepad(gamepad1);
        driveController = new DriveSubsystem(hardwareMap, driverGamepad, telemetry);
        arms = new SkystoneArm(hardwareMap);
        driveController.reset();

        stowClaws();

        scootIntoZone = new CommandRunner(this, new MecanumDriveCommandSlow(driveController, 9, 180, 2, telemetry), telemetry);
        goToFoundation = new CommandRunner(this, new MecanumDriveCommandSlow(driveController, -31, 90, 5, telemetry), telemetry);
        pullFoundation = new CommandRunner(this, new MecanumDriveCommand(driveController, 10, 90, 10, telemetry), telemetry);
        turnToZone = new CommandRunner(this, new TurnGyroCommand(driveController, 110, 0.2, 5), telemetry);
        strafeToZone = new CommandRunner(this, new MecanumDriveCommand(driveController, -30, 0, 10, telemetry), telemetry);
        makeWayForPartner = new CommandRunner(this, new MecanumDriveCommand(driveController, 22, 0, 10, telemetry), telemetry);
        backOutOfZone = new CommandRunner(this, new MecanumDriveCommand(driveController, 35, 90, 10, telemetry), telemetry);
        turnTowardBridge = new CommandRunner(this, new TurnGyroCommand(driveController, 95, 2), telemetry);

        goToBlocks = new CommandRunner(this, new MecanumDriveCommand(driveController, 12, 90, 10, telemetry), telemetry);
        goToFirstSkyStone = new CommandRunner(this, new TrackSkyStoneCommand(driveController, vu, 20, telemetry), telemetry);
        strafeAwaySkystone1 = new CommandRunner(this, new MecanumDriveCommand(driveController, -18, 0, 10, telemetry), telemetry);
        scootOver = new CommandRunner(this, new MecanumDriveCommand(driveController, 6, 0, 3, telemetry), telemetry);


        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        capstoneServo.setPosition(0.7);
        scootOver.runCommand();
        arms.moveLeftArmUp();
        arms.moveRightArmUp();
        goToFoundation.runCommand();
        leftFoundationServo.setPosition(0.8);
        rightFoundationServo.setPosition(0.1);
        sleep(1500);
        pullFoundation.runCommand();
        turnToZone.runCommand();
        strafeToZone.runCommand();

        leftFoundationServo.setPosition(0.1);
        rightFoundationServo.setPosition(0.8);
        makeWayForPartner.runCommand();
        turnTowardBridge.runCommand();

        sleep(250);
        backOutOfZone.runCommand();

        goToBlocks.runCommand();
        driveController.robotDrive.resetEncoders();
        goToFirstSkyStone.runCommand();
        double firstSkystoneDistance = Math.abs(driveController.robotDrive.getLeftDistance()) + vu.horizontal_distance;
        align = new CommandRunner(this, new MecanumDriveCommand(driveController, vu.horizontal_distance + 2, 90, 0.2, 5,telemetry), telemetry);
        align.runCommand();
        strafeToSkystone1 = new CommandRunner(this, new MecanumDriveCommand(driveController, -(vu.distance - 1), 0, 10, telemetry), telemetry);

        strafeToSkystone1.runCommand();
        arms.moveRightArmDown();
        sleep(500);
        strafeAwaySkystone1.runCommand();
        driveBackToBase = new CommandRunner(this, new MecanumDriveCommand(driveController, -(firstSkystoneDistance + 18 + 12), 90, 5, telemetry), telemetry);
        driveBackToBase.runCommand();
        arms.moveRightArmUp();
        sleep(500);
        park = new CommandRunner(this, new MecanumDriveCommand(driveController, 15, 90, 10, telemetry), telemetry);

        park.runCommand();
        scootOver.runCommand();
    }

    public void stowClaws() {
        leftClaw.setPosition(1);
        rightClaw.setPosition(.18);
    }
}