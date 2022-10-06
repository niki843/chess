package main;

import java.util.Scanner;

import figures.Bishop;
import figures.IFigure;
import figures.King;
import figures.Knight;
import figures.Pawn;
import figures.Queen;
import figures.Rook;

public class Board {

    private Object[][] board;
    private Object[][] whitePieces;
    private Object[][] blackPieces;
    private static Board b = null;
    private int move = 0;
    private King whiteKing;
    private King blackKing;


    private Board() {
        board = new Object[8][8];
        whitePieces = new Object[2][8];
        blackPieces = new Object[2][8];
        addFiguresToBoard();
    }

    private void addFiguresToBoard() {
        for (int i = (board.length -  1); i >= 0; i--) {
            int number = 65;
            for (int j = 0; j < board[i].length; j++) {
                Object[] ob = new Object[2];
                char letter = (char)number;
                String place = letter + "" + (i + 1);

                if (i == (board.length -  1) && (j == 0 || j == board[i].length - 1)) {
                    Rook r = new Rook(false);
                    ob[0] = place;
                    ob[1] = r;
                    blackPieces[0][j] = r;
                }

                if(i == (board.length -  1) && (j == 1 || j == board[i].length - 2)) {
                    Knight k = new Knight(false);
                    ob[0] = place;
                    ob[1] = k;
                    blackPieces[0][j] = k;
                }

                if(i == (board.length -  1) && (j == 2 || j == board[i].length - 3)) {
                    Bishop b = new Bishop(false);
                    ob[0] = place;
                    ob[1] = b;
                    blackPieces[0][j] = b;
                }

                if(i == (board.length -  1) && j == 3) {
                    Queen q = new Queen(false);
                    ob[0] = place;
                    ob[1] = q;
                    blackPieces[0][j] = q;
                }

                if (i == (board.length -  1) && j == 4) {
                    King k = new King(false);
                    ob[0] = place;
                    ob[1] = k;
                    blackKing = k;
                }

                if(i == (board.length -  2)) {
                    Pawn p = new Pawn(false, i, j);
                    ob[0] = place;
                    ob[1] = p;
                    blackPieces[1][j] = p;
                }


                if (i == 0 && (j == 0 || j == board[i].length - 1)) {
                    Rook r = new Rook(true);
                    ob[0] = place;
                    ob[1] = r;
                    whitePieces[0][j] = r;
                }

                if(i == 0 && (j == 1 || j == board[i].length - 2)) {
                    Knight k = new Knight(true);
                    ob[0] = place;
                    ob[1] = k;
                    whitePieces[0][j] = k;
                }

                if(i == 0 && (j == 2 || j == board[i].length - 3)) {
                    Bishop b = new Bishop(true);
                    ob[0] = place;
                    ob[1] = b;
                    whitePieces[0][j] = b;
                }

                if(i == 0 && j == 3) {
                    Queen q = new Queen(true);
                    ob[0] = place;
                    ob[1] = q;
                    whitePieces[0][j] = q;
                }

                if (i == 0 && j == 4) {
                    King k = new King(true);
                    ob[0] = place;
                    ob[1] = k;
                    whiteKing = k;
                }

                if(i == 1) {
                    Pawn p = new Pawn(true, i, j);
                    ob[0] = place;
                    ob[1] = p;
                    whitePieces[1][j] = p;
                }

                if(i > 1 && i < 6) {
                    ob[0] = place;
                }

                board[i][j] = ob;
                number++;
            }
        }
    }

    public boolean makeMove(int number, int letter, int newNumber, int newLetter, boolean whitesTurn) {
        if(number < 0 || number > 7 || letter < 0 || letter > 7 || newNumber < 0 || newNumber > 7 || newLetter < 0 || newLetter > 7){
            System.err.println("Wrong coordinates. Please try again!");
            return false;
        }
        Object[] o = (Object[]) board[number][letter];
        IFigure f = (IFigure) o[1];
        Object[] ob = (Object[]) board[newNumber][newLetter];
        boolean whiteKingIsInCheck = whiteKing.getIsInCheck();
        boolean blackKingIsInCheck = blackKing.getIsInCheck();

        if (ob[1] != null) {
            System.err.println("There is a figure there already.Try a different move or use take!");
            return false;
        }

        if (f == null) {
            System.err.println("There is no figure there. Please try again!");
            return false;
        }

        String color = f.getColor().toLowerCase();

        if (whitesTurn) {
            if (!color.equals("white")) {
                System.err.println("That is a black piece!");
                return false;
            }
        }else {
            if (!color.equals("black")) {
                System.err.println("That is a white piece");
                return false;
            }
        }

        boolean isMovePossible = false;
        if (f instanceof King) {
            King k = (King) f;
            isMovePossible = k.move(number, letter, newNumber, newLetter, false);

        }
        if (f instanceof Queen) {
            Queen q = (Queen) f;
            isMovePossible = q.move(number, letter, newNumber, newLetter, false);
        }
        if (f instanceof Pawn) {
            Pawn p = (Pawn) f;
            isMovePossible = p.move(number, letter, newNumber, newLetter, false);
        }
        if (f instanceof Knight) {
            if (ob[1] != null) {
                System.out.println("There is a figure there. You could use the command take.");
                return false;
            }
            Knight k = (Knight) f;
            isMovePossible = k.move(number, letter, newNumber, newLetter, false);
        }
        if (f instanceof Rook) {
            Rook r = (Rook) f;
            isMovePossible = r.move(number, letter, newNumber, newLetter, false);
        }
        if (f instanceof Bishop) {
            Bishop b = (Bishop) f;
            isMovePossible = b.move(number, letter, newNumber, newLetter, false);
        }

        f =(IFigure) o[1];

        if(isMovePossible) {
            move++;
            o[1] = null;
            ob[1] = f;

            if (whiteKing.checkIfInCheck()) {
                if (whitesTurn) {
                    if (whitesTurn && !whiteKingIsInCheck && !(f instanceof King)) {
                        System.err.println("You can't do that you are opening check!");
                        ob[1] = null;
                        o[1] = f;
                        move --;
                        if (f instanceof Pawn) {
                            Pawn p =(Pawn) f;
                            p.setPlaceNumber(number);
                        }
                        return false;
                    }
                    if (f instanceof King && !whiteKingIsInCheck && whitesTurn) {
                        System.err.println("You are check there please chose another move!");
                        King k =(King) f;
                        ob[1] = null;
                        o[1] = k;
                        move --;
                        k.setPlaceNumber(number);
                        k.setPlaceLetter(letter);
                        return false;
                    }
                    if (whiteKingIsInCheck && whitesTurn) {
                        System.err.println("White King is in check!Please move it.");
                        ob[1] = null;
                        o[1] = f;
                        move--;
                        if (f instanceof King) {
                            King k =(King) f;
                            k.setPlaceNumber(number);
                            k.setPlaceLetter(letter);
                        }
                        if (f instanceof Pawn) {
                            Pawn p =(Pawn) f;
                            p.setPlaceNumber(number);
                        }
                        return false;
                    }
                }else {
                    System.err.println("White King is in ckeck!");
                    whiteKing.setCheck(true);
                    boolean kingCantMove = checkPlacesNearKing(true);
                    if (kingCantMove) {
                        checkMate(ob, newNumber, newLetter, true);
                    }
                }
            }else {
                whiteKing.setCheck(false);
                checkStalemate(true);
            }

            if (blackKing.checkIfInCheck()) {
                if (!whitesTurn) {
                    if (!whitesTurn && !blackKingIsInCheck && !(f instanceof King)) {
                        System.err.println("You can't do that you are opening check!");
                        ob[1] = null;
                        o[1] = f;
                        move--;
                        if (f instanceof Pawn) {
                            Pawn p =(Pawn) f;
                            p.setPlaceNumber(number);
                        }
                        return false;
                    }
                    if (f instanceof King && !blackKingIsInCheck && !whitesTurn) {
                        System.err.println("You are check there please chose another move!");
                        King k =(King) f;
                        ob[1] = null;
                        o[1] = f;
                        move--;
                        k.setPlaceNumber(number);
                        k.setPlaceLetter(letter);
                        return false;
                    }
                    if (blackKingIsInCheck && !whitesTurn) {
                        System.err.println("Black King is in check!Please move it.");
                        ob[1] = null;
                        o[1] = f;
                        move--;
                        if (f instanceof King) {
                            King k =(King) f;
                            k.setPlaceNumber(number);
                            k.setPlaceLetter(letter);
                        }
                        if (f instanceof Pawn) {
                            Pawn p =(Pawn) f;
                            p.setPlaceNumber(number);
                        }
                        return false;
                    }
                }else {
                    System.err.println("Black King is in check!");
                    blackKing.setCheck(true);
                    boolean kingCantMove = checkPlacesNearKing(false);

                    if (kingCantMove) {
                        checkMate(ob, newNumber, newLetter, false);
                    }
                }
            }else {
                blackKing.setCheck(false);
                checkStalemate(false);
            }
            return true;
        }else {
            return false;
        }
    }

    public boolean takeFigure(int number, int letter, int newNumber, int newLetter, boolean whitesTurn) {
        if(number < 0 || number > 7 || letter < 0 || letter > 7 || newNumber < 0 || newNumber > 7 || newLetter < 0 || newLetter > 7){
            System.err.println("Wrong coordinates. Please try again!");
            return false;
        }
        Object[] o = (Object[]) board[number][letter];
        IFigure f = (IFigure) o[1];
        Object[] ob = (Object[]) board[newNumber][newLetter];
        boolean whiteKingIsInCheck = whiteKing.getIsInCheck();
        boolean blackKingIsInCheck = blackKing.getIsInCheck();

        if (f == null) {
            System.err.println("There is no figure there. Please try again!");
            return false;
        }

        if (ob[1] == null && !(f instanceof Pawn)) {
            System.out.println("There is no figure to take. Plesa try again!");
            return false;
        }

        String color = f.getColor().toLowerCase();

        if (whitesTurn) {
            if (!color.equals("white")) {
                System.err.println("That is a black piece!");
                return false;
            }
        }else {
            if (!color.equals("black")) {
                System.err.println("That is a white piece");
                return false;
            }
        }

        boolean isMovePossible = false;

        if (f instanceof King) {
            King k = (King) f;
            isMovePossible = k.take(number, letter, newNumber, newLetter);

        }
        if (f instanceof Queen) {
            Queen q = (Queen) f;
            isMovePossible = q.take(number, letter, newNumber, newLetter);
        }
        if (f instanceof Pawn) {
            Pawn p = (Pawn) f;
            isMovePossible = p.take(number, letter, newNumber, newLetter);
        }
        if (f instanceof Knight) {
            Knight k = (Knight) f;
            isMovePossible = k.take(number, letter, newNumber, newLetter);
        }
        if (f instanceof Rook) {
            Rook r = (Rook) f;
            isMovePossible = r.take(number, letter, newNumber, newLetter);
        }
        if (f instanceof Bishop) {
            Bishop b = (Bishop) f;
            isMovePossible = b.take(number, letter, newNumber, newLetter);
        }

        f =(IFigure) o[1];

        if(isMovePossible) {
            IFigure takenFigure =(IFigure) ob[1];
            move++;
            o[1] = null;
            ob[1] = f;

            if (whiteKing.checkIfInCheck()) {
                if (whitesTurn) {
                    if (!whiteKingIsInCheck && !(f instanceof King)) {
                        System.err.println("You can't do that you are opening check!");
                        ob[1] = takenFigure;
                        o[1] = f;
                        move--;
                        if (f instanceof Pawn) {
                            Pawn p =(Pawn) f;
                            p.setPlaceNumber(number);
                        }
                        return false;
                    }
                    if (f instanceof King && !whiteKingIsInCheck) {
                        System.err.println("You are check there please chose another move!");
                        King k =(King) f;
                        ob[1] = takenFigure;
                        o[1] = f;
                        move--;
                        k.setPlaceNumber(number);
                        k.setPlaceLetter(letter);
                        return false;
                    }
                    if (whiteKingIsInCheck) {
                        System.err.println("White King is in check!Please move it.");
                        ob[1] = takenFigure;
                        o[1] = f;
                        move--;
                        if (f instanceof King) {
                            King k =(King) f;
                            k.setPlaceNumber(number);
                            k.setPlaceLetter(letter);
                        }
                        if (f instanceof Pawn) {
                            Pawn p =(Pawn) f;
                            p.setPlaceNumber(number);
                        }
                        return false;
                    }
                }else {
                    System.err.println("White King is in ckeck!");
                    whiteKing.setCheck(true);
                    boolean kingCantMove = checkPlacesNearKing(true);

                    if (kingCantMove) {
                        checkMate(ob, newNumber, newLetter, true);
                    }
                }
            }else {
                whiteKing.setCheck(false);
                checkStalemate(true);
            }

            if (blackKing.checkIfInCheck()) {
                if(!whitesTurn) {
                    if (!blackKingIsInCheck && !(f instanceof King)) {
                        System.err.println("You can't do that you are opening check!");
                        ob[1] = takenFigure;
                        o[1] = f;
                        move--;
                        if (f instanceof Pawn) {
                            Pawn p =(Pawn) f;
                            p.setPlaceNumber(number);
                        }
                        return false;
                    }
                    if (f instanceof King && !blackKingIsInCheck) {
                        System.err.println("You are check there please chose another move!");
                        King k =(King) f;
                        ob[1] = takenFigure;
                        o[1] = f;
                        move--;
                        k.setPlaceNumber(number);
                        k.setPlaceLetter(letter);
                        return false;
                    }
                    if (blackKingIsInCheck) {
                        System.err.println("Black King is in check!Please move it.");
                        ob[1] = takenFigure;
                        o[1] = f;
                        move--;
                        if (f instanceof King) {
                            King k =(King) f;
                            k.setPlaceNumber(number);
                            k.setPlaceLetter(letter);
                        }
                        if (f instanceof Pawn) {
                            Pawn p =(Pawn) f;
                            p.setPlaceNumber(number);
                        }
                        return false;
                    }
                }else {
                    System.err.println("Black King is in check!");
                    blackKing.setCheck(true);
                    boolean kingCantMove = checkPlacesNearKing(false);

                    if (kingCantMove) {
                        checkMate(ob, newNumber, newLetter, false);
                    }
                }
            }else {
                blackKing.setCheck(false);
                checkStalemate(false);
            }
            if (takenFigure.getColor().equals("white")) {
                for (int i = 0; i < whitePieces.length; i++) {
                    for (int j = 0; j < whitePieces[i].length; j++) {
                        if (whitePieces[i][j] == takenFigure) {
                            whitePieces[i][j] = null;
                        }
                    }
                }
            }else {
                for (int i = 0; i < blackPieces.length; i++) {
                    for (int j = 0; j < blackPieces[i].length; j++) {
                        if (blackPieces[i][j] == takenFigure) {
                            blackPieces[i][j] = null;
                        }
                    }
                }
            }
            return true;
        }else {
            return false;
        }

    }

    public void changePawn(int number, int letter, boolean isWhite) {
        Scanner sc = new Scanner(System.in);
        boolean newFigureNotCorrect = true;
        Object[] ob =(Object[]) board[number][letter];
        do {
            System.out.println("You can chose another figure now(Q,N,R,B).");
            String newFigure = sc.next();
            newFigure = newFigure.toUpperCase();
            if (newFigure.equals("Q")) {
                ob[1] = new Queen(isWhite);
                newFigureNotCorrect = false;
            }else {
                if (newFigure.equals("N")) {
                    ob[1] = new Knight(isWhite);
                    newFigureNotCorrect = false;
                }else {
                    if (newFigure.equals("R")) {
                        ob[1] = new Rook(isWhite);
                        newFigureNotCorrect = false;
                    }else {
                        if (newFigure.equals("B")) {
                            ob[1] = new Bishop(isWhite);
                            newFigureNotCorrect = false;
                        }else {
                            System.out.println("You did not enter a valid symbol!");
                        }
                    }
                }
            }
        } while (newFigureNotCorrect);
        sc.close();
    }

    public boolean checkSquareIsHit(int number,int letter, boolean forWhites, boolean checkForKing) {

        if (this.checkForKnight(number, letter, 2, 1, forWhites)) {
            return true;
        }
        if (this.checkForKnight(number, letter, 2, -1, forWhites)) {
            return true;
        }
        if (this.checkForKnight(number, letter, -2, 1, forWhites)) {
            return true;
        }
        if (this.checkForKnight(number, letter, -2, -1, forWhites)) {
            return true;
        }
        if (this.checkForKnight(number, letter, -1, 2, forWhites)) {
            return true;
        }
        if (this.checkForKnight(number, letter, -1, -2, forWhites)) {
            return true;
        }
        if (this.checkForKnight(number, letter, 1, 2, forWhites)) {
            return true;
        }
        if (this.checkForKnight(number, letter, 1, -2, forWhites)) {
            return true;
        }

        if (checkHorizontalVertical(number, letter, forWhites, checkForKing)) {
            return true;
        }

        if (checkDiagonals(number, letter, forWhites, checkForKing)) {
            return true;
        }

        return false;
    }

    private boolean checkForKnight(int currentNumber, int currentLetter, int number, int letter, boolean forWhites) {
        Object[] knightPlace;
        try {
            knightPlace =(Object[]) board[currentNumber + number][currentLetter + letter];
            if (knightPlace[1] != null) {
                if (knightPlace[1] instanceof Knight ) {
                    Knight k =(Knight) knightPlace[1];
                    if (forWhites) {
                        if (k.getColor().equals("black")) {
                            return true;
                        }
                    }else {
                        if (k.getColor().equals("white")) {
                            return true;
                        }
                    }
                }
            }
        }catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }
        return false;
    }

    private boolean checkHorizontalVertical(int currentNumber,int currentLetter, boolean forWhites, boolean checkForKing) {
        Object[] horizontalUpperSquare = null;
        Object[] horizontalLowerSquare = null;
        Object[] verticalUpperSquare = null;
        Object[] verticalLowerSquare = null;
        boolean stillChecking = true;
        boolean foundHorizontalUpper = false;
        boolean foundHorizontalLower = false;
        boolean foundVerticalUpper = false;
        boolean foundVerticalLower = false;
        int i = currentNumber;
        int j = currentNumber;
        int k = currentLetter;
        int l = currentLetter;
        String color;

        if (forWhites) {
            color = "white";
        }else {
            color = "black";
        }

        while(stillChecking) {

            if ((i >= 7 && j <= 0 && k >= 7 && l <= 0) || (foundHorizontalLower && foundHorizontalUpper && foundVerticalLower && foundVerticalUpper)) {
                stillChecking = false;
                continue;
            }else {
                if (i < 7) {
                    i++;
                }
                if (j > 0) {
                    j--;
                }
                if (k < 7) {
                    k++;
                }
                if (l > 0) {
                    l--;
                }
            }

            if (!foundHorizontalUpper) {
                horizontalUpperSquare =(Object[]) board[currentNumber][k];

                if ( k == currentNumber + 1 && horizontalUpperSquare[1] != null) {
                    if (horizontalUpperSquare[1] instanceof King && !checkForKing) {
                        return true;
                    }
                }

                if (horizontalUpperSquare[1] != null) {
                    IFigure first =(IFigure) horizontalUpperSquare[1];
                    switch (first.getName()) {
                        case ('Q'):
                            if (!first.getColor().equals(color)) {
                                return true;
                            }
                            foundHorizontalUpper = true;
                            break;
                        case ('R'):
                            if (!first.getColor().equals(color)) {
                                return true;
                            }
                            foundHorizontalUpper = true;
                            break;
                        default:
                            foundHorizontalUpper = true;
                            break;
                    }
                }
            }

            if (!foundHorizontalLower) {
                horizontalLowerSquare =(Object[]) board[currentNumber][l];

                if (l == currentNumber - 1 && horizontalLowerSquare[1] != null) {
                    if (horizontalLowerSquare[1] instanceof King && !checkForKing) {
                        return true;
                    }
                }

                if (horizontalLowerSquare[1] != null) {
                    IFigure first =(IFigure) horizontalLowerSquare[1];
                    switch (first.getName()) {
                        case ('Q'):
                            if (!first.getColor().equals(color)) {
                                return true;
                            }
                            foundHorizontalLower = true;
                            break;
                        case ('R'):
                            if (!first.getColor().equals(color)) {
                                return true;
                            }
                            foundHorizontalLower = true;
                            break;
                        default:
                            foundHorizontalLower = true;
                            break;
                    }
                }
            }

            if (!foundVerticalUpper) {
                verticalUpperSquare =(Object[]) board[i][currentLetter];

                if (i == currentLetter + 1 && verticalUpperSquare[1] != null) {
                    if (verticalUpperSquare[1] instanceof King && !checkForKing) {
                        return true;
                    }
                }

                if (verticalUpperSquare[1] != null) {
                    IFigure first =(IFigure) verticalUpperSquare[1];
                    switch (first.getName()) {
                        case ('Q'):
                            if (!first.getColor().equals(color)) {
                                return true;
                            }
                            foundVerticalUpper = true;
                            break;
                        case ('R'):
                            if (!first.getColor().equals(color)) {
                                return true;
                            }
                            foundVerticalUpper = true;
                            break;
                        default:
                            foundVerticalUpper = true;
                            break;
                    }
                }
            }

            if (!foundVerticalLower) {
                verticalLowerSquare =(Object[]) board[j][currentLetter];

                if (j == currentLetter - 1 && verticalLowerSquare[1] != null) {
                    if (verticalLowerSquare[1] instanceof King && !checkForKing) {
                        return true;
                    }
                }

                if (verticalLowerSquare[1] != null) {
                    IFigure first =(IFigure) verticalLowerSquare[1];
                    switch (first.getName()) {
                        case ('Q'):
                            if (!first.getColor().equals(color)) {
                                return true;
                            }
                            foundVerticalLower = true;
                            break;
                        case ('R'):
                            if (!first.getColor().equals(color)) {
                                return true;
                            }
                            foundVerticalLower = true;
                            break;
                        default:
                            foundVerticalLower = true;
                            break;
                    }
                }
            }
        }
        return false;
    }

    private boolean checkDiagonals(int currentNumber,int currentLetter, boolean forWhites, boolean checkForKing) {
        Object[] upperRightDiagonalSquare = null;
        Object[] lowerLeftDiagonalSquare = null;
        Object[] upperLeftDiagonalSquare = null;
        Object[] lowerRightDiagonalSquare = null;
        boolean stillChecking = true;
        boolean foundUpperRight = false;
        boolean foundUpperLeft = false;
        boolean foundLowerRight = false;
        boolean foundLowerLeft = false;
        int i = currentNumber;
        int j = currentLetter;
        int k = currentNumber;
        int l = currentLetter;
        String color;

        if (forWhites) {
            color = "white";
        }else {
            color = "black";
        }

        while(stillChecking) {

            if ((i >= 7 && j <= 0 && k >= 7 && l <= 0) || (foundLowerLeft && foundLowerRight && foundUpperLeft && foundUpperRight)) {
                stillChecking = false;
                continue;
            }else {
                if (i < 7) {
                    i++;
                }
                if (j < 7) {
                    j++;
                }
                if (k > 0) {
                    k--;
                }
                if (l > 0) {
                    l--;
                }
            }

            if (i <= 7) {

                if (i == 7 && j == 7) {
                    foundUpperRight = true;
                }

                if (j <= 7 && !foundUpperRight) {
                    upperRightDiagonalSquare = (Object[]) board[i][j];

                    if (j == currentLetter + 1 && i == currentNumber + 1 && upperRightDiagonalSquare[1] != null) {
                        if (upperRightDiagonalSquare[1] instanceof King && !checkForKing) {
                            return true;
                        }
                    }

                    if (upperRightDiagonalSquare[1] != null ) {
                        IFigure first =(IFigure) upperRightDiagonalSquare[1];
                        if (forWhites && i == currentNumber + 1 && j == currentLetter + 1 && first instanceof Pawn) {
                            Pawn p =(Pawn) upperRightDiagonalSquare[1];
                            if (p.getColor().equals("black")) {
                                return true;
                            }else {
                                foundUpperRight= true;
                            }
                        }

                        switch (first.getName()) {
                            case ('Q'):
                                if (!first.getColor().equals(color)) {
                                    return true;
                                }
                                foundUpperRight = true;
                                break;
                            case ('B'):
                                if (!first.getColor().equals(color)) {
                                    return true;
                                }
                                foundUpperRight = true;
                                break;
                            default:
                                foundUpperRight = true;
                                break;
                        }
                    }
                }

                if (i == 7 && l == 0) {
                    foundUpperLeft = true;
                }

                if (l >= 0 && !foundUpperLeft) {
                    upperLeftDiagonalSquare = (Object[]) board[i][l];

                    if (l == currentLetter - 1 && i == currentNumber + 1 && upperLeftDiagonalSquare[1] != null) {
                        if (upperLeftDiagonalSquare[1] instanceof King && !checkForKing) {
                            return true;
                        }
                    }

                    if (upperLeftDiagonalSquare[1] != null) {
                        IFigure first =(IFigure) upperLeftDiagonalSquare[1];

                        if (forWhites && i == currentNumber + 1 && l == currentLetter - 1 && upperLeftDiagonalSquare[1] instanceof Pawn) {
                            Pawn p =(Pawn) upperLeftDiagonalSquare[1];
                            if (p.getColor().equals("black")) {
                                return true;
                            }else {
                                foundUpperLeft = true;
                            }
                        }

                        switch (first.getName()) {
                            case ('Q'):
                                if (!first.getColor().equals(color)) {
                                    return true;
                                }
                                foundUpperLeft = true;
                                break;
                            case ('B'):
                                if (!first.getColor().equals(color)) {
                                    return true;
                                }
                                foundUpperLeft = true;
                                break;
                            default:
                                foundUpperLeft = true;
                                break;
                        }
                    }
                }
            }else {
                foundUpperLeft = true;
                foundUpperRight = true;
            }

            if (k >= 0) {
                if (k == 0 && j == 7) {
                    foundLowerRight = true;
                }
                if (j <= 7 && !foundLowerRight) {
                    lowerRightDiagonalSquare = (Object[]) board[k][j];

                    if (j == currentLetter + 1 && k == currentNumber - 1 && lowerRightDiagonalSquare[1] != null) {
                        if (lowerRightDiagonalSquare[1] instanceof King && !checkForKing) {
                            return true;
                        }
                    }

                    if (lowerRightDiagonalSquare[1] != null) {
                        IFigure first =(IFigure) lowerRightDiagonalSquare[1];

                        if (!forWhites && j == currentNumber - 1 && j == currentLetter + 1 && lowerRightDiagonalSquare[1] instanceof Pawn) {
                            Pawn p =(Pawn) lowerRightDiagonalSquare[1];
                            if (p.getColor().equals("white")) {
                                return true;
                            }else {
                                foundLowerRight = true;
                            }
                        }

                        switch (first.getName()) {
                            case ('Q'):
                                if (!first.getColor().equals(color)) {
                                    return true;
                                }
                                foundLowerRight = true;
                                break;
                            case ('B'):
                                if (!first.getColor().equals(color)) {
                                    return true;
                                }
                                foundLowerRight = true;
                                break;
                            default:
                                foundLowerRight = true;
                                break;
                        }
                    }
                }

                if (k == 0 && l == 0) {
                    foundLowerLeft = true;
                }
                if (l >= 0 && !foundLowerLeft) {
                    lowerLeftDiagonalSquare = (Object[]) board[k][l];

                    if (k == currentNumber - 1 && l == currentLetter - 1 && lowerLeftDiagonalSquare[1] != null) {
                        if (lowerLeftDiagonalSquare[1] instanceof King && !checkForKing) {
                            return true;
                        }
                    }

                    if (lowerLeftDiagonalSquare[1] != null) {
                        IFigure first =(IFigure) lowerLeftDiagonalSquare[1];

                        if (!forWhites && j == currentNumber - 1 && l == currentLetter - 1 && lowerLeftDiagonalSquare[1] instanceof Pawn) {
                            Pawn p =(Pawn) lowerLeftDiagonalSquare[1];
                            if (p.getColor().equals("white")) {
                                return true;
                            }else {
                                foundLowerLeft = true;
                            }
                        }

                        switch (first.getName()) {
                            case ('Q'):
                                if (!first.getColor().equals(color)) {
                                    return true;
                                }
                                foundLowerLeft = true;
                                break;
                            case ('B'):
                                if (!first.getColor().equals(color)) {
                                    return true;
                                }
                                foundLowerLeft = true;
                                break;
                            default:
                                foundLowerLeft = true;
                                break;
                        }
                    }
                }
            }else {
                foundLowerLeft = true;
                foundLowerRight = true;
            }
        }
        return false;
    }

    private boolean checkPlacesNearKing(boolean isKingWhite) {
        int KingNumber;
        int KingLetter;
        String color;
        if (isKingWhite) {
            KingNumber = whiteKing.getNumber();
            KingLetter = whiteKing.getLetter();
            color = "white";
        }else {
            KingNumber = blackKing.getNumber();
            KingLetter = blackKing.getLetter();
            color = "black";
        }
        boolean upperSquareHit = false;
        boolean lowerSquareHit = false;
        boolean leftSquareHit = false;
        boolean rightSquareHit = false;
        boolean upperRightSquareHit = false;
        boolean lowerLeftSquareHit = false;
        boolean upperLeftSquareHit = false;
        boolean lowerRightSquareHit = false;
        Object[]  object;

        if (KingNumber < 7) {
            object =(Object[]) board[KingNumber + 1][KingLetter];
            if (object[1] != null) {
                IFigure figure =(IFigure) object[1];
                if (figure.getColor().equals(color)) {
                    upperSquareHit = true;
                }else {
                    upperSquareHit = checkSquareIsHit(KingNumber + 1, KingLetter, isKingWhite, false);
                }
            }else {
                upperSquareHit = checkSquareIsHit(KingNumber + 1, KingLetter, isKingWhite, false);
            }
        }else {
            upperSquareHit = true;
        }

        if (KingNumber > 0) {
            object =(Object[]) board[KingNumber - 1][KingLetter];
            if (object[1] != null) {
                IFigure figure =(IFigure) object[1];
                if (figure.getColor().equals(color)) {
                    lowerSquareHit =true;
                }else {
                    lowerSquareHit = checkSquareIsHit(KingNumber - 1, KingLetter, isKingWhite, false);
                }
            }else {
                lowerSquareHit = checkSquareIsHit(KingNumber - 1, KingLetter, isKingWhite, false);
            }
        }else {
            lowerSquareHit =true;
        }

        if (KingLetter < 7) {
            object =(Object[]) board[KingNumber][KingLetter + 1];
            if (object[1] != null) {
                IFigure figure =(IFigure) object[1];
                if (figure.getColor().equals(color)) {
                    rightSquareHit = true;
                }else {
                    rightSquareHit = checkSquareIsHit(KingNumber, KingLetter + 1, isKingWhite, false);
                }
            }else {
                rightSquareHit = checkSquareIsHit(KingNumber, KingLetter + 1, isKingWhite, false);
            }
        }else {
            rightSquareHit = true;
        }

        if (KingLetter > 0) {
            object =(Object[]) board[KingNumber][KingLetter - 1];
            if (object[1] != null) {
                IFigure figure =(IFigure) object[1];
                if (figure.getColor().equals(color)) {
                    leftSquareHit = true;
                }else {
                    leftSquareHit = checkSquareIsHit(KingNumber, KingLetter - 1, isKingWhite, false);
                }
            }else {
                leftSquareHit = checkSquareIsHit(KingNumber, KingLetter - 1, isKingWhite, false);
            }
        }else {
            leftSquareHit = true;
        }

        if (KingNumber < 7 && KingLetter < 7) {
            object =(Object[]) board[KingNumber + 1][KingLetter + 1];
            if (object[1] != null) {
                IFigure figure =(IFigure) object[1];
                if (figure.getColor().equals(color)) {
                    upperRightSquareHit = true;
                }else {
                    upperRightSquareHit = checkSquareIsHit(KingNumber + 1, KingLetter + 1, isKingWhite, false);
                }
            }else {
                upperRightSquareHit = checkSquareIsHit(KingNumber + 1, KingLetter + 1, isKingWhite, false);
            }
        }else {
            upperRightSquareHit = true;
        }

        if (KingNumber > 0 && KingLetter > 0) {
            object =(Object[]) board[KingNumber - 1][KingLetter - 1];
            if (object[1] != null) {
                IFigure figure =(IFigure) object[1];
                if (figure.getColor().equals(color)) {
                    lowerLeftSquareHit = true;
                }else {
                    lowerLeftSquareHit = checkSquareIsHit(KingNumber - 1, KingLetter - 1, isKingWhite, false);
                }
            }else {
                lowerLeftSquareHit = checkSquareIsHit(KingNumber - 1, KingLetter - 1, isKingWhite, false);
            }
        }else {
            lowerLeftSquareHit = true;
        }

        if (KingNumber > 0 && KingLetter < 7) {
            object =(Object[]) board[KingNumber - 1][KingLetter + 1];
            if (object[1] != null) {
                IFigure figure =(IFigure) object[1];
                if (figure.getColor().equals(color)) {
                    lowerRightSquareHit = true;
                }else {
                    lowerRightSquareHit = checkSquareIsHit(KingNumber - 1, KingLetter + 1, isKingWhite, false);
                }
            }else {
                lowerRightSquareHit = checkSquareIsHit(KingNumber - 1, KingLetter + 1, isKingWhite, false);
            }
        }else {
            lowerRightSquareHit = true;
        }

        if (KingNumber < 7 && KingLetter > 0) {
            object =(Object[]) board[KingNumber + 1][KingLetter - 1];
            if (object[1] != null) {
                IFigure figure =(IFigure) object[1];
                if (figure.getColor().equals(color)) {
                    upperLeftSquareHit = true;
                }else {
                    upperLeftSquareHit = checkSquareIsHit(KingNumber + 1, KingLetter - 1, isKingWhite, false);
                }
            }else {
                upperLeftSquareHit = checkSquareIsHit(KingNumber + 1, KingLetter - 1, isKingWhite, false);
            }
        }else {
            upperLeftSquareHit = true;
        }
        if (upperSquareHit && lowerSquareHit && rightSquareHit && leftSquareHit && upperRightSquareHit && lowerLeftSquareHit && upperLeftSquareHit && lowerRightSquareHit) {
            return true;
        }else {
            return false;
        }
    }

    private void checkMate(Object[] ob, int newNumber, int newLetter, boolean isKingWhite) {
        int kingNumber;
        int kingLetter;

        if (isKingWhite) {
            kingNumber = whiteKing.getNumber();
            kingLetter = whiteKing.getLetter();
        }else {
            kingNumber = blackKing.getNumber();
            kingLetter = blackKing.getLetter();
        }

        if (ob[1] instanceof Knight) {
            if (!checkSquareIsHit(newNumber, newLetter, false, true)) {
                TheGame.won = true;
                if (isKingWhite) {
                    TheGame.whitesWin = false;
                }else {
                    TheGame.whitesWin = true;
                }
            }
        }else {
            if (kingNumber == newNumber) {
                if (kingLetter > newLetter) {
                    if (!checkSquareIsHit(kingNumber, kingLetter - 1, !isKingWhite, true) && checkSquareIsHit(newNumber, newLetter, !isKingWhite, true)) {
                        TheGame.won = true;
                        if (isKingWhite) {
                            TheGame.whitesWin = false;
                        }else {
                            TheGame.whitesWin = true;
                        }
                    }
                }
                if (kingLetter < newLetter) {
                    if (!checkSquareIsHit(kingNumber, kingLetter + 1, !isKingWhite, true) && !checkSquareIsHit(newNumber, newLetter, !isKingWhite, true)) {
                        TheGame.won = true;
                        if (isKingWhite) {
                            TheGame.whitesWin = false;
                        }else {
                            TheGame.whitesWin = true;
                        }
                    }
                }
            }
            if (kingLetter == newLetter) {
                if (kingNumber > newNumber) {
                    if (!checkSquareIsHit(kingNumber - 1, kingLetter, !isKingWhite, true) && !checkSquareIsHit(newNumber, newLetter, !isKingWhite, true)) {
                        TheGame.won = true;
                        if (isKingWhite) {
                            TheGame.whitesWin = false;
                        }else {
                            TheGame.whitesWin = true;
                        }
                    }
                }
                if (kingNumber < newNumber) {
                    if (!checkSquareIsHit(kingNumber + 1, kingLetter, !isKingWhite, true) && !checkSquareIsHit(newNumber, newLetter, !isKingWhite, true)) {
                        TheGame.won = true;
                        if (isKingWhite) {
                            TheGame.whitesWin = false;
                        }else {
                            TheGame.whitesWin = true;
                        }
                    }
                }
            }
            if ((kingNumber + 1) - (kingLetter + 1) == (newNumber + 1) - (newLetter + 1)) {
                if (kingNumber < newNumber && kingLetter < newLetter) {
                    if (!checkSquareIsHit(kingNumber + 1, kingLetter + 1, !isKingWhite, true) && !checkSquareIsHit(newNumber, newLetter, !isKingWhite, true)) {
                        TheGame.won = true;
                        if (isKingWhite) {
                            TheGame.whitesWin = false;
                        }else {
                            TheGame.whitesWin = true;
                        }
                    }
                }
                if (kingNumber > newNumber && kingLetter > newLetter) {
                    if (!checkSquareIsHit(kingNumber - 1, kingLetter - 1, !isKingWhite, true) && !checkSquareIsHit(newNumber, newLetter, !isKingWhite, true)) {
                        TheGame.won = true;
                        if (isKingWhite) {
                            TheGame.whitesWin = false;
                        }else {
                            TheGame.whitesWin = true;
                        }
                    }
                }
            }
            if ((kingNumber + 1) + (kingLetter + 1) == (newNumber + 1) + (newLetter + 1)) {
                if (kingNumber > newNumber && kingLetter < newLetter) {
                    if (!checkSquareIsHit(kingNumber - 1, kingLetter + 1, !isKingWhite, true) && !checkSquareIsHit(newNumber, newLetter, !isKingWhite, true)) {
                        TheGame.won = true;
                        if (isKingWhite) {
                            TheGame.whitesWin = false;
                        }else {
                            TheGame.whitesWin = true;
                        }
                    }
                }
                if (kingNumber < newNumber && kingLetter > newLetter) {
                    if (!checkSquareIsHit(kingNumber + 1, kingLetter - 1, !isKingWhite, true) && !checkSquareIsHit(newNumber, newLetter, !isKingWhite, true)) {
                        TheGame.won = true;
                        if (isKingWhite) {
                            TheGame.whitesWin = false;
                        }else {
                            TheGame.whitesWin = true;
                        }
                    }
                }
            }
        }
    }

    public void checkStalemate(boolean isKingWhite) {
        boolean stalmate = true;
        boolean noFiguresLeft = true;
        if (isKingWhite) {
            for (int i = 0; i < whitePieces.length; i++) {
                for (int j = 0; j < whitePieces[i].length; j++) {
                    if (whitePieces[i][j] != null) {
                        noFiguresLeft = false;
                    }
                    if (i == 1) {
                        if (whitePieces[i][j] != null) {
                            Pawn p =(Pawn) whitePieces[i][j];
                            if (p.move(p.getPlaceNumber(), p.getPlaceLetter(), p.getPlaceNumber() + 1, p.getPlaceLetter(), true)) {
                                stalmate = false;
                            }
                        }
                    }else {
                        if (whitePieces[i][j] != null) {
                            stalmate = false;
                        }
                    }
                }
            }

            if (stalmate && checkPlacesNearKing(true)) {
                TheGame.won = true;
                TheGame.stalmate = true;
            }
        }else {
            for (int i = 0; i < blackPieces.length; i++) {
                for (int j = 0; j < blackPieces[i].length; j++) {
                    if (blackPieces[i][j] != null) {
                        noFiguresLeft = false;
                    }
                    if (i == 1) {
                        if (blackPieces[i][j] != null) {
                            Pawn p =(Pawn) blackPieces[i][j];
                            if (p.move(p.getPlaceNumber(), p.getPlaceLetter(), p.getPlaceNumber() + 1, p.getPlaceLetter(), true)) {
                                stalmate = false;
                            }
                        }
                    }else {
                        if (blackPieces[i][j] != null) {
                            stalmate = false;
                        }
                    }
                }
            }
            if (noFiguresLeft) {
                System.out.println("There are no figures left!");
                TheGame.won = true;
                TheGame.stalmate = true;
            }
            if (stalmate && checkPlacesNearKing(true)) {
                TheGame.won = true;
                TheGame.stalmate = true;
            }
            if (stalmate) {
                TheGame.won = true;
                TheGame.stalmate = true;
            }
        }
    }

    public void printBoard() {
        for(int i = board.length - 1; i >= 0; i--) {
            for(int j = 0; j < board[i].length; j++) {
                Object[] o = (Object[]) board[i][j];
                IFigure f = (IFigure) o[1];
                if (f != null) {
                    System.out.print("{" + o[0] + "," + f.getNameAndColor() + "};");
                }else {
                    System.out.print("{" + o[0] + ",  NaN  };");
                }
            }
            System.out.println();
        }
    }

    public int getCurrecntMove() {
        return move;
    }

    public static Board GetBoard() {
        if(b == null) {
            b = new Board();
        }
        return b;
    }

    public Object[][] getCurrentBoard(){
        Object[][] ob = board.clone();
        return ob;
    }

}
