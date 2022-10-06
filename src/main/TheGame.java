package main;

import java.util.Scanner;

public class TheGame {

    static boolean won = false;
    static boolean whitesWin = false;
    static boolean stalmate = false;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Board b = Board.GetBoard();
        boolean whitesTurn = true;
        b.printBoard();
        do {

            if (whitesTurn) {
                System.out.println("Whites wanted action(move/take):");
            }else {
                System.out.println("Blacks wanted action(move/take):");
            }

            String action = sc.nextLine();
            action = action.toLowerCase();
            action = action.trim();

            if (!action.equals("move") ^ action.equals("take")) {
                System.err.println("Wrong action!");
                continue;
            }

            System.out.println("Place of figure(use a letter and a number):");
            String place = sc.nextLine();

            if (place.length() != 2) {
                System.err.println("Wrong place!");
                continue;
            }

            System.out.println("New place of figure(use a letter and a number):");
            String newPlace = sc.nextLine();

            if (newPlace.length() != 2) {
                System.err.println("Wrong place!");
                continue;
            }

            place = place.trim();
            place = place.toUpperCase();
            newPlace = newPlace.trim();
            newPlace = newPlace.toUpperCase();
            if (action.equals("move")) {
                char currentLetter = place.charAt(0);
                int currentNumberOfFigure = (int)place.charAt(1) - 49;
                char newLetterOfFigure = newPlace.charAt(0);
                int newNumberOfFigure = (int)newPlace.charAt(1) - 49;
                int currentLetterOfFigureDecimal = (int)currentLetter;
                currentLetterOfFigureDecimal -= 65;
                int newLetterOfFigureDecimal = (int)newLetterOfFigure;
                newLetterOfFigureDecimal -= 65;
                boolean moveMade = b.makeMove(currentNumberOfFigure, currentLetterOfFigureDecimal, newNumberOfFigure, newLetterOfFigureDecimal, whitesTurn);
                if (!moveMade) {
                    System.err.println("Wrong move. Please try a valid one!");
                    continue;
                }
            }else {
                char currentLetter = place.charAt(0);
                int currentNumberOfFigure = (int)place.charAt(1) - 49;
                char newLetterOfFigure = newPlace.charAt(0);
                int newNumberOfFigure = (int)newPlace.charAt(1) - 49;
                int currentLetterOfFigureDecimal = (int)currentLetter;
                currentLetterOfFigureDecimal -= 65;
                int newLetterOfFigureDecimal = (int)newLetterOfFigure;
                newLetterOfFigureDecimal -= 65;
                boolean moveMade = b.takeFigure(currentNumberOfFigure, currentLetterOfFigureDecimal, newNumberOfFigure, newLetterOfFigureDecimal, whitesTurn);
                if (!moveMade) {
                    System.err.println("Wrong move. Pleasy try a valid one!");
                    continue;
                }
            }
            b.printBoard();

            if (whitesTurn) {
                System.out.println("Blacks turn");
                whitesTurn = false;
            }else {
                System.out.println("Whites turn");
                whitesTurn = true;
            }
        } while (!won);
        sc.close();
        if (stalmate) {
            System.out.println("The game has ended with stalmate!");
        }else {
            if (whitesWin) {
                System.out.println("Whites won congratulations!");
            }else {
                System.out.println("Blacks won congratulations!");
            }
        }
    }

}
