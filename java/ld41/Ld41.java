/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ld41;

import java.util.Scanner;

/**
 *
 * @author aaron
 */
public class Ld41 
{
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
		int width = 10;
		int height = 10;

		if (args.length >= 2)
		{
			width = Integer.parseInt(args[0]);
		}
		else if (args.length >= 3)
		{
			height = Integer.parseInt(args[1]);
		}

		Minesweeper game = new Minesweeper(width, height);

		Scanner sc = new Scanner(System.in);
		String s, command, values;
		s = sc.nextLine();
		while (!s.equals("exit") && !s.equals("quit")) // Just for testing purposes
			{
				String[] params = s.split(" ");
				command = params[0];
				values = params[1];
				System.out.println(game.processCommand(command, values));
				s = sc.nextLine();
			}
		}
}
