/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.java.com.mygdx.game.Extensions.modules;

import com.mygdx.game.Extensions.modules.Easy;
import com.mygdx.game.Sprites.MarioClone;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Владислав
 */
public class EasyTest {
    
    public EasyTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of run method, of class Easy.
     */
    @Test
    public void testMoveRight() {
        System.out.println("Move Right");
        Easy instance = new Easy();
        boolean expResult = true;
        boolean result = false;
        if(instance.player.b2body.getLinearVelocity().x > 1){
          result = true;
        }
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
    @Test
    public void testJump() {
        System.out.println("Jump");
        Easy instance = new Easy();
        boolean expResult = true;
        boolean result = false;
        if(instance.player.b2body.getLinearVelocity().y > 1){
          result = true;
        }
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    
}
