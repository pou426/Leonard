package strategy.actions;

import com.sun.tools.internal.jxc.ap.Const;
import strategy.Strategy;
import strategy.actions.offense.GoalKick;
import strategy.actions.other.DefendGoal;
import strategy.actions.other.GoToSafeLocation;
import strategy.actions.offense.ShuntKick;
import strategy.actions.other.GotoBall;
import strategy.points.basicPoints.BallPoint;
import strategy.robots.Fred;
import strategy.robots.RobotBase;
import vision.Ball;
import vision.Robot;
import vision.RobotType;
import vision.constants.Constants;
import vision.tools.VectorGeometry;

/**
 * Created by Simon Rovder
 */
enum BehaviourEnum {
    DEFEND, SHUNT, GOAL, SAFE, EMPTY
}

/**
 * The main Action class. It basically plays the game.
 */
public class Behave extends StatefulActionBase<BehaviourEnum> {


    public static boolean RESET = true;


    public Behave(RobotBase robot) {
        super(robot, null);
    }

    @Override
    public void enterState(int newState) {
        if (newState == 0) {
            this.robot.setControllersActive(true);
        }
        this.state = newState;
    }


    @Override
    public void tok() throws ActionException {

        this.robot.MOTION_CONTROLLER.clearObstacles();
        if (this.robot instanceof Fred) ((Fred) this.robot).GRABBER_CONTROLLER.setActive(false);
        this.lastState = this.nextState;
        switch (this.nextState) {
            case DEFEND:
                this.enterAction(new DefendGoal(this.robot), 0, 0);
                break;
            case GOAL:
                this.enterAction(new GotoBall(this.robot, new BallPoint()), 0, 0);
                break;
            case SHUNT:
                this.enterAction(new ShuntKick(this.robot), 0, 0);
                break;
            case SAFE:
                this.enterAction(new GoToSafeLocation(this.robot), 0, 0);
                break;
        }
    }

    /** returns true if foe (possibly) has the ball and is on our side of the pitch */
    public boolean foeATTACK(Robot foe, VectorGeometry ball, VectorGeometry ourGoal) {
        if (foe.location.distance(ball) <= 10 && foe.location.distance(ourGoal) <= Constants.PITCH_WIDTH/2) {
            return true;
        }
        return false;
    }

    @Override
    protected BehaviourEnum getState() {
        Ball ball = Strategy.world.getBall();
        if (ball == null) {
            this.nextState = BehaviourEnum.DEFEND;
        } else {
            Robot us = Strategy.world.getRobot(this.robot.robotType);
            if (us == null) {
                // TODO: Angry yelling
                // tell friend that we are lost
                return;
            }
            VectorGeometry ballVG = new VectorGeometry(ball.location.x, ball.location.y);
            Robot foe1 = Strategy.world.getRobot(RobotType.FOE_1);
            Robot foe2 = Strategy.world.getRobot(RobotType.FOE_2);

            VectorGeometry ourGoal = new VectorGeometry(-Constants.PITCH_WIDTH / 2, 0);

            if (foeATTACK(foe1, ballVG, ourGoal) || foeATTACK(foe2, ballVG, ourGoal)) { // if enemy is close to ball, assuming they have the ball
                // TODO: with a friend - decide who to DEFEND or ATTACK and send packets
                System.out.println("Enemy has the ball : calling SAFE");
                this.nextState = BehaviourEnum.SAFE;
                return this.nextState;
            }

            if ()


                // us.location.distance(ourGoal) )
//            Robot us = Strategy.world.getRobot(this.robot.robotType);
//            if (us == null) {
//                // TODO: Angry yelling
//            } else {
//                VectorGeometry ourGoal = new VectorGeometry(-Constants.PITCH_WIDTH / 2, 0);
//                if (us.location.distance(ourGoal) > ball.location.distance(ourGoal)) {
//                    this.nextState = BehaviourEnum.SAFE;
//                } else {
////                    if(Math.abs(ball.location.x) > Constants.PITCH_WIDTH/2 - 20 && Math.abs(ball.location.y) > Constants.PITCH_HEIGHT/2 - 20){
////                        this.nextState = BehaviourEnum.SHUNT;
////                    } else {
//                    boolean canKick = true;
//                    boolean closer = true;
//                    for (Robot r : Strategy.world.getRobots()) {
//                        if (r != null && r.type != RobotType.FRIEND_2 && r.velocity.length() < 1)
//                            canKick = canKick && r.location.distance(ball.location) > 5;
//                            closer = closer && us.location.distance(ball.location)< r.location.distance(ball.location);
//                    }
//                    if (canKick && (this.lastState != BehaviourEnum.DEFEND ||
//                            VectorGeometry.angle(ball.velocity, VectorGeometry.fromTo(ball.location, ourGoal)) > 2)
//                            || closer) {
//                        this.nextState = BehaviourEnum.GOAL;
//                    } else {
//                        this.nextState = BehaviourEnum.DEFEND;
//                    }
////                    }
//                }
//            }
        }
        return this.nextState;
    }
}
