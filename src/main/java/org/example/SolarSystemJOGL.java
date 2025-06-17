package org.example;

import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.newt.event.WindowAdapter;
import com.jogamp.newt.event.WindowEvent;

import com.jogamp.opengl.*;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;
import com.jogamp.opengl.util.FPSAnimator;

public class SolarSystemJOGL implements GLEventListener {

    private float earthAngle = 0.0f;
    private float moonAngle = 0.0f;

    public static void main(String[] args) {

        System.setProperty("sun.java2d.opengl", "false");

        // use Gl profile
        GLProfile profile = GLProfile.get(GLProfile.GL2);
        GLCapabilities capabilities = new GLCapabilities(profile);

        // Setting bits for design
        capabilities.setRedBits(10);
        capabilities.setGreenBits(10);
        capabilities.setBlueBits(10);
        capabilities.setAlphaBits(2);
        capabilities.setDepthBits(24);
        capabilities.setStencilBits(8);
        capabilities.setDoubleBuffered(true);
        capabilities.setHardwareAccelerated(true);

        // Create GLWindow
        GLWindow window = GLWindow.create(capabilities);
        // for displaying the window title gl window
        window.setTitle("3D Solar System - JOGL (GLWindow)");
        window.setSize(800, 600);
        // setting window visible true
        window.setVisible(true);
        // this setting is used to resize the window size
        window.setResizable(true);

        // Attach this class as the GLEventListener
        window.addGLEventListener(new SolarSystemJOGL());

        //  shutdown the window
        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowDestroyNotify(WindowEvent e) {
                System.exit(0);
            }
        });

        // Start the rendering loop
        FPSAnimator animator = new FPSAnimator(window, 60, true);
        animator.start();
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glEnable(GL2.GL_DEPTH_TEST);
        gl.glEnable(GL2.GL_LIGHTING);
        gl.glEnable(GL2.GL_LIGHT0);
        gl.glEnable(GL2.GL_COLOR_MATERIAL);
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {

    }

    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();

        // Camera positioning
        gl.glTranslatef(0f, 0f, -20.0f);

        // Sun
        gl.glColor3f(1f, 1f, 0f); // sun colour ( yellow)
        drawSphere(gl, 2.0, 30, 30); // To get the sphere shape

        // Earth
        gl.glPushMatrix();
        gl.glRotatef(earthAngle, 0f, 1f, 0f);
        gl.glTranslatef(7f, 0f, 0f);
        gl.glColor3f(0f, 0f, 1f); // earth colour (Blue)
        drawSphere(gl, 1.0, 20, 20); // To get the sphere shape

        // Moon
        gl.glPushMatrix();
        gl.glRotatef(moonAngle, 0f, 1f, 0f);
        gl.glTranslatef(2f, 0f, 0f);
        gl.glColor3f(0.5f, 0.5f, 0.5f); // Moon colour (gray)
        drawSphere(gl, 0.3, 15, 15); // To get the sphere shape
        gl.glPopMatrix();

        gl.glPopMatrix();

        // Increment angles
        earthAngle += 0.5f;
        moonAngle += 2.0f;
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL2 gl = drawable.getGL().getGL2();
        float aspect = (float) width / height;
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        new GLU().gluPerspective(45.0, aspect, 1.0, 100.0);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
    }

    private void drawSphere(GL2 gl, double radius, int slices, int stacks) {
        GLU glu = new GLU();
        GLUquadric quad = glu.gluNewQuadric();
        glu.gluSphere(quad, radius, slices, stacks);
        glu.gluDeleteQuadric(quad);
    }
}