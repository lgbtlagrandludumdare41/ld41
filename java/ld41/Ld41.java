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
		int width = Minesweeper.DEFAULT_WIDTH_HEIGHT;
		int height = Minesweeper.DEFAULT_WIDTH_HEIGHT;

		if (args.length >= 1)
		{
			try
			{
				width = Integer.parseInt(args[0]);
			}
			catch (Exception e)
			{
				// Leave width at the default
			}
		}
		if (args.length >= 2)
		{
			try
			{
				height = Integer.parseInt(args[1]);
			}
			catch (Exception e)
			{
				// Leave height at the default
			}
		}

		Minesweeper game = new Minesweeper(width, height);

		Scanner sc = new Scanner(System.in);
		String s;
		s = sc.nextLine();
		while (!s.equals("exit") && !s.equals("quit")) // Just for testing purposes
		{
			try
			{
				String[] params = s.split(" ");
				System.out.println(game.processCommand(params[0], params[1]));
			}
			catch (Exception e)
			{
				System.out.println("Invalid");
			}
			s = sc.nextLine();
		}
	}
}
