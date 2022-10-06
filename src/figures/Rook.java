package figures;

import main.Board;

public class Rook implements IFigure{

    private boolean isWhite;
    private char name = 'R';
    private boolean hasMoved = false;

    public Rook(boolean isWhite) {
        this.isWhite = isWhite;
    }

    public boolean getHasMoved() {
        return hasMoved;
    }

    @Override
    public boolean move(int currentNumber, int currentLetter,int newNumber, int newLetter, boolean checkTake) {
        Board b = Board.GetBoard();
        Object[][] board = b.getCurrentBoard();
        int beginOfIterationNum;
        int beginOfIterationLetter;
        int endOfIterationNum;
        int endOfIterationLetter;

        if (Math.min(currentNumber, newNumber) == currentNumber) {
            beginOfIterationNum = currentNumber + 1;
            if (checkTake) {
                endOfIterationNum = newNumber - 1;
            }else {
                endOfIterationNum = newNumber;
            }
        }else {
            if (checkTake) {
                beginOfIterationNum = newNumber + 1;
            }else {
                beginOfIterationNum = newNumber;
            }
            endOfIterationNum = currentNumber -1;
        }

        if (Math.min(currentLetter, newLetter) == currentLetter) {
            beginOfIterationLetter = currentLetter + 1;
            if (checkTake) {
                endOfIterationLetter = newLetter - 1;
            }else {
                endOfIterationLetter = newLetter;
            }
        }else {
            if(checkTake) {
                beginOfIterationLetter = newLetter + 1;
            }else {
                beginOfIterationLetter = newLetter;
            }
            endOfIterationLetter = currentLetter - 1;
        }

        if (currentNumber == newNumber || currentLetter == newLetter) {

            if (currentNumber == newNumber) {
                for (int i = beginOfIterationLetter; i <= endOfIterationLetter; i++) {
                    Object[] place = (Object[]) board[currentNumber][i];
                    if (place[1] != null) {
                        System.err.println("You are trying to jump another figure. The Rook can't do that!");
                        return false;
                    }
                }
            }else {
                if (currentLetter == newLetter) {

                    if (currentNumber == newNumber && currentLetter == newLetter) {
                        System.err.println("The Rook can't be moved to the same place");
                        return false;
                    }

                    for (int i = beginOfIterationNum; i <= endOfIterationNum; i++) {
                        Object[] place = (Object[]) board[i][currentLetter];
                        if (place[1] != null) {
                            System.err.println("You are trying to jump another figure. The Rook can't do that!");
                            return false;
                        }
                    }

                }else {
                    return false;
                }
            }
            hasMoved = true;
            return true;
        }else {
            return false;
        }
    }

    @Override
    public boolean take(int currentNumber, int currentLetter, int newNumber, int newLetter) {
        if (move(currentNumber, currentLetter, newNumber, newLetter, true)) {
            Object[][] ob = Board.GetBoard().getCurrentBoard();
            Object[] o =(Object[]) ob[newNumber][newLetter];
            IFigure f = (IFigure) o[1];
            if (f.getColor().equals(this.getColor())) {
                System.out.println("You can't take your own figures!");
                return false;
            }else {
                hasMoved = true;
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
