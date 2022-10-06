package figures;

import main.Board;

public class Bishop implements IFigure{

    private boolean isWhite;
    private char name = 'B';

    public Bishop(boolean isWhite) {
        this.isWhite = isWhite;
    }

    @Override
    public boolean move(int currentNumber, int currentLetter,int newNumber, int newLetter, boolean checkTake) {

        Board b = Board.GetBoard();
        Object[][] board = b.getCurrentBoard();
        boolean correctMove = true;

        if (currentNumber == newNumber && currentLetter == newLetter) {
            System.err.println("The Bishop can't be moved to the same place");
            return false;
        }

        if ((currentNumber + 1) - (currentLetter + 1) == (newNumber + 1) - (newLetter + 1) || currentNumber + currentLetter == newNumber + newLetter){
            int i = currentNumber;
            int j = currentLetter;

            if (checkTake) {
                if ((currentNumber + 1 == newNumber && currentLetter + 1 == newLetter) ||
                        (currentNumber - 1 == newNumber && currentLetter + 1 == newLetter) ||
                        (currentNumber + 1 == newNumber && currentLetter - 1 == newLetter) ||
                        (currentNumber - 1 == newNumber && currentLetter - 1 == newLetter)) {
                    return true;
                }
            }

            while(i != newNumber && j != newLetter) {
                if (Math.min(currentNumber, newNumber) == currentNumber && Math.min(currentLetter, newLetter) == currentLetter) {
                    i++;
                    j++;
                    if (checkTake) {
                        checkTake = !checkTake;
                        newNumber--;
                        newLetter--;
                    }
                }else {

                    if (Math.min(currentNumber, newNumber) == newNumber && Math.min(currentLetter, newLetter) == currentLetter) {
                        i--;
                        j++;
                        if (checkTake) {
                            checkTake = !checkTake;
                            newNumber++;
                            newLetter--;
                        }
                    }else {

                        if (Math.min(currentNumber, newNumber) == currentNumber && Math.min(currentLetter, newLetter) == newLetter) {
                            i++;
                            j--;
                            if (checkTake) {
                                checkTake = !checkTake;
                                newNumber--;
                                newLetter++;
                            }
                        }else {

                            if (Math.min(currentNumber, newNumber) == newNumber && Math.min(currentLetter, newLetter) == newLetter) {
                                i--;
                                j--;
                                if (checkTake) {
                                    checkTake = !checkTake;
                                    newNumber++;
                                    newLetter++;
                                }
                            }
                        }
                    }
                }
                Object[] place = (Object[]) board[i][j];
                if (place[1] != null) {
                    System.err.println("You are trying to jump another figure. The queen can't do that!");
                    correctMove = false;
                    break;
                }
            }
        }else {
            correctMove = false;
        }
        return correctMove;
    }

    @Override
    public boolean take(int currentNumber, int currentLetter, int newNumber, int newLetter) {
        if (move(currentNumber, currentLetter, newNumber, newLetter, true)) {
            Object[][] ob = Board.GetBoard().getCurrentBoard();
            Object[] o =(Object[]) ob[newNumber][newLetter];
            IFigure f = (IFigure) o[1];
            if (f.getColor().equals(this.getColor())) {
                System.err.println("You can't take your own figures!");
                return false;
            }else {
                return true;
            }
        }else {
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
