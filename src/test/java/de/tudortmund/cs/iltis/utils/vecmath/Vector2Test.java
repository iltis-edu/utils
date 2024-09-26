package de.tudortmund.cs.iltis.utils.vecmath;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class Vector2Test {

    private static final double PREC = 1E-5;

    private Vector2F unitRight, unitUp, unitLeft, unitDown;

    @Before
    public void prepare() {
        unitRight = new Vector2F(1, 0);
        unitUp = new Vector2F(0, 1);
        unitLeft = new Vector2F(-1, 0);
        unitDown = new Vector2F(0, -1);
    }

    @Test
    public void simpleAngle() {
        double angle = unitUp.angle(unitRight);

        assertEquals(90, angle, PREC);
    }

    @Test
    public void angle180() {
        double angle = unitUp.angle(unitDown);
        assertEquals(180, angle, PREC);
    }

    @Test
    public void angleOver180() {
        double angle = unitDown.angle(unitRight);

        assertEquals(270, angle, PREC);
    }

    @Test
    public void angleNot360() {
        double angle = unitLeft.angle(unitLeft);

        assertEquals(0, angle, PREC);
    }

    @Test
    public void simpleLenghts() {
        assertEquals(1, unitRight.len(), PREC);
        assertEquals(1, unitUp.len(), PREC);
        assertEquals(1, unitLeft.len(), PREC);
        assertEquals(1, unitDown.len(), PREC);
    }

    @Test
    public void simpleRotation() {
        Vector2F unitRightRot = unitRight.rot(90);
        AssertionUtilities.assertAlmostEquals("", unitUp, unitRightRot);
    }

    @Test
    public void rotation45() {
        Vector2F v45 = unitRight.add(unitUp);
        Vector2F v45rot = v45.rot(90);
        Vector2F v135 = unitUp.add(unitLeft);
        AssertionUtilities.assertAlmostEquals("", v135, v45rot);
    }
}
