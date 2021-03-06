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

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.vuforia.Vuforia;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.teamcode.commands.DriveDistanceCommand;
import org.firstinspires.ftc.teamcode.subsystems.DriveSubsystem;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

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

@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name="DriveDistance", group="Autos")  // @Autonomous(...) is the other common choice
//@Disabled
public class Autonomous extends LinearOpMode {

    private ElapsedTime runtime = new ElapsedTime();
    private CommandRunner driveFiftyInches;
    DriveSubsystem driveController;


    @Override
    public void runOpMode() {

        FtcDashboard dashboard = FtcDashboard.getInstance();
        dashboard.updateConfig();

        telemetry = dashboard.getTelemetry();
        String V_LICENSE = "AVXpNNv/////AAABmepa4M4Lb0Pji1YbsYaCl+8r5OjeMfRao7RA3siS0+MS1SCSgar/W48rh74JxmrcUczMHiI+i8exMdTIZBPGeBGIMgzq3zGckJ9v/5ry7uBCy4Db99U5jbTy4i+5VqGPzEYWhQgtD7t+3VoWWENGQiuawU33tAp4dHWPpe4gCbjF+MgTSp/SRrVZKPw4soYuoCr8JHhUbTiAFWMS4n3+P0Cxr+lxoYu9leKvaf1fiG9nEnb1uGf88N5UnaH5oA3uJ6KGS1ATgzRQIEkrTJElphxQb4zjOK8FyA+ERiVjJ6m7vwvmyokN6Dicm3xwS1cRPy3EBPrXTgM0TdhUWFVx30TqWrBz4KvEhNEnwzoFwHLM";

        driveController = new DriveSubsystem(hardwareMap, gamepad1, telemetry);
        double[] PID = {.006, 0, 0.0001};
        driveFiftyInches = new CommandRunner(this, new DriveDistanceCommand(driveController, Parameters.targetDistance, Parameters.targetVelocity, 100), telemetry);

        VuforiaLocalizer.Parameters vuforiaParams = new VuforiaLocalizer.Parameters(R.id.cameraMonitorViewId);
        vuforiaParams.vuforiaLicenseKey = V_LICENSE;
        vuforiaParams.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        VuforiaLocalizer vuforia = ClassFactory.getInstance().createVuforia(vuforiaParams);
        dashboard.startCameraStream(vuforia, 0);

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        driveFiftyInches.runCommand();
        runtime.reset();
        while(opModeIsActive()) {
        }
        dashboard.stopCameraStream();

    }


}
