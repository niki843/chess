package figures;

import main.Board;

public class Knight implements IFigure{

    private boolean isWhite;
    private char name = 'N';

    public Knight(boolean isWhite) {
        this.isWhite = isWhite;
    }


    @Override
    public boolean move(int currentNumber, int currentLetter,int newNumber, int newLetter, boolean checkTake) {

        if (currentNumber == newNumber && currentLetter == newLetter) {
            System.err.println("The Knight can't be moved to the same place");
            return false;
        }

        if ((newNumber == currentNumber + 2 && newLetter == currentLetter - 1) || (newNumber == currentNumber + 2 && newLetter == currentLetter +1)
                || (newNumber == currentNumber + 1 && newLetter == currentLetter - 2) || (newNumber == currentNumber + 1 && newLetter == currentLetter + 2)
                || (newNumber == currentNumber - 1 && newLetter == currentLetter - 2) || (newNumber == currentNumber - 1 && newLetter == currentLetter + 2)
                || (newNumber == currentNumber - 2 && newLetter == currentLetter - 1) || (newNumber == currentNumber - 2 && newLetter == currentLetter + 1)) {
            return true;
        }else {
            System.err.println("The Knight can't move that way!");
            return false;
        }
    }

    @Override
    public boolean take(int currentNumber, int currentLetter, int newNumber, int newLetter) {
        if (move(currentNumber, currentLetter, newNumber, newLetter, true)) {
            Object[][] ob = Board.GetBoard().getCurrentBoard();
            Object[] o =(Object[]) ob[newNumber][newLetter];
            IFigure f = (IFigure) o[1];
            if (f == null) {
                System.out.println("There is no figure there!");
                return false;
            }
            if (f.getColor().equals(this.getColor())) {
                System.out.println("You can't take your own figures!");
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
