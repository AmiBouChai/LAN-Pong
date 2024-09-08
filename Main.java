// By Tyseer Ammar Shahriar
// Start Date: Around May 2022
// Last edited June 12, 2022

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.Graphics;
import java.awt.Dimension;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.concurrent.TimeUnit;
import java.awt.Graphics;
import java.util.Scanner;
import java.net.*;
import java.io.*;

class Main {
    public static void main(String args[]) {
        // Declare variables
        PrintWriter out = null;
        BufferedReader in = null;
        final int PORT = 57777;
        String host;
        JFrame window = new JFrame("PONG");
        int menuOption = 0;
        Picture titleScreen = new Picture("TitlePage.png");
        // I wasted too much time so I ain't doing GUI user input (Ugh)
        Scanner sc = new Scanner(System.in);
        Socket clientSocket = null;
        boolean playerIsOnRight = true;
        int ballTrajectoryX = -1;
        int ballTrajectoryY = -1;
        int w;
        int h;
        int rightScore = 0;
        int leftScore = 0;
        JLabel rightScoreFigure = new JLabel();
        JLabel leftScoreFigure = new JLabel();
        boolean isHost = false;
 
        // Setup window
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(730, 489);
        window.add(titleScreen);
        window.setVisible(true);   
        
        // Loop till input is valid
        while (menuOption != 1 && menuOption != 2) {
            // Print user menu
            System.out.println("Welcome to PONG");
            System.out.println("1. Challenge Another Player");
            System.out.println("2. Wait for Challenger");
            try {
                // Get user input
                menuOption = Integer.parseInt(sc.nextLine());

                if (menuOption == 1) {
                    // Paddle position is left
                    playerIsOnRight = false;
                    // Get IP address of the one to be challenged
                    System.out.println("Enter the local IP address of who you want to challenge:");
                    host = sc.nextLine();

                    // Act as client and connect to the computer of the challenged
                    // Create network socket connected to the challenged computer
                    clientSocket = new Socket(host, PORT);
                    
                    // Create writer and reader to read/write from socket input/output streams
                    out = new PrintWriter(clientSocket.getOutputStream(), true);
                    in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                    // Send message to show connection
                    out.println("Connected");
                    
                    // Print connection message recieved from server (the challenged computer)
                    System.out.println(in.readLine());

                } else if (menuOption == 2) {
                    // This player acts as server
                    isHost = true;
                    // Paddle position is right
                    playerIsOnRight = true;

                    // Act as server and accept connection from challenger
                    System.out.println("Waiting for a challenger...");
                    
                    // Create server socket to accept connections
                    ServerSocket serverSocket = new ServerSocket(PORT);
                    // Create socket connected to client (challenger) from accepted server socket connection
                    clientSocket = serverSocket.accept();

                    // Create writer and reader to read/write from socket input/output streams
                    out = new PrintWriter(clientSocket.getOutputStream(), true);
                    in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    
                    // Send message to show connection
                    out.println("Connected");

                    // Print connection message recieved from client (the challenger)
                    System.out.println(in.readLine());
                } else {
                    // In case user enters invalid input
                    System.out.println("Invalid input");
                }
            // Handle exceptions cuz compiler forced me to
            } catch (Exception e) {
                System.out.println("Whoops, looks like something went wrong. Try again I guess.");
                continue;
            }
        }
        
        // Remove title screen and add game panel
        window.getContentPane().removeAll();
        GamePanel gP = new GamePanel(playerIsOnRight);
        
        rightScoreFigure.setText(Integer.toString(rightScore));
        leftScoreFigure.setText(Integer.toString(leftScore));

        gP.setBackground(Color.BLACK);
        gP.add(leftScoreFigure);
        gP.add(rightScoreFigure);
        window.add(gP);
        window.revalidate();
        // Direct input to game panel
        gP.requestFocusInWindow();
        // Start ball at appropriate position
        gP.ball.setPosition(window.getWidth() / 2, window.getHeight() / 2);

        // GAME LOOP
        while (true) {
            try {
                // Get window width and height
                w = window.getWidth();
                h = window.getHeight();
                // Send window dimensions to opponent
                out.println(w);
                out.println(h);
                // Set window dimensions to opponent's window dimensions
                window.setSize(Integer.parseInt(in.readLine()), Integer.parseInt(in.readLine()));

                // Send y position of player's paddle to opponent
                out.println(gP.getPaddleY());
                // Recieve and set opponent's paddle's y position
                gP.setOpponentPaddleY(Integer.parseInt(in.readLine()));

                // Calculate ball size
                if (gP.getBallDimension() == 0) {
                    gP.setBallDimension(3);
                } else {
                    gP.setBallDimension(h / 50);
                }
                
                // Host calculates and sends its ball coordinates to opponent client so things don't become out of sync
                if (isHost) {
                    // Calculate ball trajectory
                    // If ball hits floor or ceiling bounce
                    if (gP.ball.shape.y <= 0 || gP.ball.shape.y >= h) {
                        ballTrajectoryY *= -1;
                    }

                    // If ball hits paddles bounce
                    if ((gP.ball.shape.x == ((w / 15) + (w / 60)) && gP.ball.shape.y >= gP.leftPaddle.shape.y && gP.ball.shape.y <= (gP.leftPaddle.shape.y + gP.leftPaddle.shape.height)) || (gP.ball.shape.x == (w - ((w / 15) + (w / 60))) && gP.ball.shape.y >= gP.rightPaddle.shape.y && gP.ball.shape.y <= (gP.rightPaddle.shape.y + gP.rightPaddle.shape.height))) {
                        ballTrajectoryX *= -1;
                    }

                    // Set ball position
                    gP.ball.shape.x += ballTrajectoryX;
                    gP.ball.shape.y += ballTrajectoryY;

                    out.println(gP.ball.shape.x);
                    out.println(gP.ball.shape.y);
                } else {
                    // If not host recieve ball coords
                    gP.ball.shape.x = Integer.parseInt(in.readLine());
                    gP.ball.shape.y = Integer.parseInt(in.readLine());
                }

                // Keep track of score
                if (gP.ball.shape.x >= w) {
                    leftScore += 1;
                    gP.remove(leftScoreFigure);
                    leftScoreFigure.setText(Integer.toString(leftScore));
                    gP.add(leftScoreFigure);
                    
                    // Reset ball
                    gP.ball.setPosition(w / 2, h / 2);
                    ballTrajectoryX = 1;
                    ballTrajectoryY = 1;
                }

                if (gP.ball.shape.x <= 0) {
                    rightScore += 1;
                    gP.remove(rightScoreFigure);
                    rightScoreFigure.setText(Integer.toString(rightScore));
                    gP.add(rightScoreFigure);

                    // Reset ball
                    gP.ball.setPosition(w / 2, h / 2);
                    ballTrajectoryX = 1;
                    ballTrajectoryY = 1;
                }

                // Repaint to render changes
                gP.repaint();

                // Pause for a bit so balll doesn't go crazy  fast (IMPORTANT NOTE: Game goes very slowly when testing from different computers)
                TimeUnit.MILLISECONDS.sleep((4));
            // Dear compiler, why are you so forceful?
            } catch (Exception e) {
                // Opponent may have quit so close connection
                try {
                    clientSocket.close();
                    System.out.println("Connection Closed");
                    break;
                } catch (Exception x) {
                    System.out.println("Whoops, looks like something went wrong. Close the program and try again I guess.");
                    break;
                }
            }
            continue;
        }
    }
}
