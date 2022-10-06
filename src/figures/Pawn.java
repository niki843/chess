package figures;

import main.Board;

public class Pawn implements IFigure{

    private boolean isWhite;
    private char name = 'P';
    private int madeTwoMovesAt = 0;
    private int placeNumber;
    private int placeLetter;

    public Pawn(boolean isWhite, int placeNumber, int placeLetter) {
        this.isWhite = isWhite;
        this.placeNumber = placeNumber;
        this.placeLetter = placeLetter;
    }

    public int getMadeTwoMovesAt() {
        return madeTwoMovesAt;
    }

    public void setPlaceNumber(int placeNumber) {
        this.placeNumber = placeNumber;
    }

    public int getPlaceNumber() {
        return this.placeNumber;
    }

    public int getPlaceLetter() {
        return this.placeLetter;
    }

    @Override
    public boolean move(int currentNumber, int currentLetter,int newNumber, int newLetter, boolean checkTake) {
        int oneMove;
        int twoMoves;
        int lastSquareNum;
        if (isWhite) {
            lastSquareNum = 7;
            oneMove = 1;
            twoMoves = 2;
        }else {
            lastSquareNum = 0;
            oneMove = -1;
            twoMoves = -2;
        }

        if ((newNumber == currentNumber + oneMove || newNumber == currentNumber + twoMoves) && newLetter == currentLetter) {
            if (newNumber == currentNumber + twoMoves) {
                if (isWhite) {
                    if (currentNumber == 1) {
                        Object[] ob = (Object[])Board.GetBoard().getCurrentBoard()[currentNumber+1][currentLetter];
                        if (ob[1] != null) {
                            System.out.println("You are trying to jump another figure. The pawn can't do that!");
                            return false;
                        }
                        madeTwoMovesAt = Board.GetBoard().getCurrecntMove();
                        return true;
                    }else {
                        System.err.println("The Pawn can't move two places after you have already moved it!");
                        return false;
                    }
                }else {
                    if (currentNumber == 6) {
                        Object[] ob = (Object[])Board.GetBoard().getCurrentBoard()[currentNumber-1][currentLetter];
                        if (ob[1] != null) {
                            System.out.println("You are trying to jump another figure. The pawn can't do that!");
                            return false;
                        }
                        madeTwoMovesAt = Board.GetBoard().getCurrecntMove();
                        placeNumber += 2;
                        return true;
                    }else {
                        System.err.println("The Pawn can't move two places after you have already moved it!");
                        return false;
                    }
                }
            }else {
                if (newNumber == lastSquareNum) {
                    if (!checkTake) {
                        Board.GetBoard().changePawn(currentNumber, currentLetter, isWhite);
                        return true;
                    }else {
                        return true;
                    }
                }else {
                    if (!checkTake) {
                        placeNumber += 1;
                    }
                    return true;
                }
            }
        }else {
            return false;
        }
    }

    @Override
    public boolean take(int currentNumber, int currentLetter, int newNumber, int newLetter) {

        int oneMove;
        int lastSquareNum;
        if (isWhite) {
            lastSquareNum = 7;
            oneMove = 1;
        }else {
            lastSquareNum = 0;
            oneMove = -1;
        }

        Object[][] ob = Board.GetBoard().getCurrentBoard();
        Object[] firstPlace =(Object[]) ob[newNumber][newLetter];
        Object[] secondPlace =(Object[]) ob[newNumber - oneMove][newLetter];
        IFigure firstFigure = (IFigure) firstPlace[1];
        IFigure secondFigure = (IFigure) secondPlace[1];
        if (newNumber == currentNumber + oneMove && (newLetter == currentLetter + 1 || newLetter == currentLetter - 1)) {

            if (firstFigure != null) {
                if (firstFigure.getColor().equals(this.getColor())) {
                    System.out.println("You can't take your own figures!");
                    return false;
                }else {
                    if (newNumber == lastSquareNum) {
                        Board.GetBoard().changePawn(currentNumber, currentLetter, isWhite);
                        return true;
                    }else {
                        return true;
                    }
                }
            }else {
                if (secondFigure != null) {
                    if (!(secondFigure instanceof Pawn)) {
                        System.err.println("You can take only a pawn that way!");
                        return false;
                    }else {
                        Pawn p = (Pawn) secondFigure;
                        if (p.getMadeTwoMovesAt() != 0) {
                            if (p.getMadeTwoMovesAt() == Board.GetBoard().getCurrecntMove() - 1) {
                                secondPlace[1] = null;
                                return true;
                            }else {
                                System.err.println("You can't take that way!The pawn has been moved two places before one move!");
                                return false;
                            }
                        }else {
                            System.err.println("You can't take that way!The pawn was not moved two places before!");
                            return false;
                        }
                    }
                }else {
                    System.err.println("There is no figure there!");
                    return false;
                }
            }
        }else {
            System.err.println("You can't take that way with the Pawn!");
            return false;
        }
    }

    @Override
    public String getColor() {
        if(isWhite) {
            return "white";
        }else {
            return "black";
        }
    }

    @Override
    public String getNameAndColor() {
        if (isWhite) {
            return name + "-White";
        }else {
            return name + "-Black";
        }
    }

    @Override
    public char getName() {
        return name;
    }

}
