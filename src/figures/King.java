package figures;

import main.Board;

public class King implements IFigure{

    private boolean isWhite;
    private char name = 'K';
    private boolean hasMoved = false;
    private int placeNumber;
    private int placeLetter;
    private boolean isInCheck = false;

    public King(boolean isWhite) {
        this.isWhite = isWhite;
        placeLetter = 4;
        if (isWhite) {
            placeNumber = 0;
        }else {
            placeNumber = 7;
        }
    }

    public boolean getIsInCheck() {
        return this.isInCheck;
    }

    public void setCheck(boolean isInCheck) {
        this.isInCheck = isInCheck;
    }

    public void setPlaceNumber(int placeNumber) {
        this.placeNumber = placeNumber;
    }

    public void setPlaceLetter(int placeLetter) {
        this.placeLetter = placeLetter;
    }

    @Override
    public boolean move(int currentNumber, int currentLetter,int newNumber, int newLetter, boolean checkTake) {

        if (newNumber == currentNumber && (newLetter == currentLetter + 2 || newLetter == currentLetter - 2) && !hasMoved) {
            Object[][] ob = Board.GetBoard().getCurrentBoard();
            Object[] o;
            int beginOfIteration;
            int endOfIteration;
            int rookPalceLetter;
            int rookNewPlaceLetter;
            if (newLetter == currentLetter + 2) {
                rookPalceLetter = 7;
                o =(Object[]) ob[currentNumber][rookPalceLetter];
                beginOfIteration = currentLetter + 1;
                endOfIteration = newLetter;
                rookNewPlaceLetter = currentLetter + 1;
            }else {
                rookPalceLetter = 0;
                o =(Object[]) ob[currentNumber][rookPalceLetter];
                beginOfIteration = newLetter;
                endOfIteration = currentLetter - 1;
                rookNewPlaceLetter = currentLetter - 1;
            }
            IFigure f = (IFigure) o[1];
            if (f != null) {
                if (f instanceof Rook) {
                    Rook r =(Rook) f;
                    if (r.getHasMoved()) {
                        System.err.println("The Rook has already been moved!");
                        return false;
                    }else {
                        for (int i = beginOfIteration; i <= endOfIteration; i++) {
                            if (Board.GetBoard().checkSquareIsHit(currentNumber, i, isWhite, false)) {
                                System.err.println("One or more of the places are hit, please chose another move!");
                                return false;
                            }
                            if (i == endOfIteration) {
                                if (Board.GetBoard().checkSquareIsHit(currentNumber, i + 1, isWhite, false)) {
                                    System.err.println("One or more of the places are hit, please chose another move!");
                                    return false;
                                }
                            }
                            o =(Object[]) ob[currentNumber][i];
                            if (o[1] != null) {
                                System.err.println("There is something in the way.Plese try a different move.");
                                return false;
                            }
                        }
                        Board.GetBoard().makeMove(currentNumber, rookPalceLetter, currentNumber, rookNewPlaceLetter, isWhite);
                        placeLetter = newLetter;
                        placeNumber = newNumber;
                        hasMoved = true;
                        return true;
                    }
                }else {
                    return false;
                }
            }else {
                return false;
            }
        }

        if (newNumber == currentNumber + 1  || newNumber == currentNumber - 1 || newNumber == currentNumber) {
            if (newLetter == currentLetter + 1 || newLetter == currentLetter - 1 || newLetter == currentLetter) {
                if (newNumber == currentNumber && newLetter == currentLetter) {
                    System.err.println("The King can't be moved to the same place!");
                    return false;
                }else {
                    placeLetter = newLetter;
                    placeNumber = newNumber;
                    hasMoved = true;
                    return true;
                }
            }else {
                System.err.println("The King can't move that way!");
                return false;
            }
        }else {
            System.err.println("The King can't move that way!");
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
                placeLetter = newLetter;
                placeNumber = newNumber;
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

    public boolean checkIfStalemate() {
        return false;
    }

    public boolean checkIfInCheck() {
        Object[][] board = Board.GetBoard().getCurrentBoard();

        if (this.checkForKnight(board, 2, 1)) {
            return true;
        }
        if (this.checkForKnight(board, 2, -1)) {
            return true;
        }
        if (this.checkForKnight(board, -2, 1)) {
            return true;
        }
        if (this.checkForKnight(board, -2, -1)) {
            return true;
        }
        if (this.checkForKnight(board, -1, 2)) {
            return true;
        }
        if (this.checkForKnight(board, -1, -2)) {
            return true;
        }
        if (this.checkForKnight(board, 1, 2)) {
            return true;
        }
        if (this.checkForKnight(board, 1, -2)) {
            return true;
        }

        if (checkHorizontalVertical(board)) {
            return true;
        }

        if (checkDiagonals(board)) {
            return true;
        }

        return false;
    }

    private boolean checkForKnight(Object[][] board,int number,int letter){
        Object[] knightPlace;
        try {
            knightPlace =(Object[]) board[placeNumber + number][placeLetter + letter];
            if (knightPlace[1] != null) {
                if (knightPlace[1] instanceof Knight ) {
                    Knight k =(Knight) knightPlace[1];
                    if (!(k.getColor().equals(this.getColor()))) {
                        return true;
                    }
                }
            }
        }catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }
        return false;
    }

    private boolean checkHorizontalVertical(Object[][] board) {
        Object[] horizontalUpperSquare = null;
        Object[] horizontalLowerSquare = null;
        Object[] verticalUpperSquare = null;
        Object[] verticalLowerSquare = null;
        boolean stillChecking = true;
        boolean foundHorizontalUpper = false;
        boolean foundHorizontalLower = false;
        boolean foundVerticalUpper = false;
        boolean foundVerticalLower = false;
        int i = placeNumber;
        int j = placeNumber;
        int k = placeLetter;
        int l = placeLetter;

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
                horizontalUpperSquare =(Object[]) board[placeNumber][k];

                if ( k == placeLetter + 1 && horizontalUpperSquare[1] != null) {
                    if (horizontalUpperSquare[1] instanceof King) {
                        System.err.println("You are trying to move a king to another king!");
                        return true;
                    }
                }

                if (horizontalUpperSquare[1] != null) {
                    IFigure first =(IFigure) horizontalUpperSquare[1];
                    switch (first.getName()) {
                        case ('Q'):
                            if (!first.getColor().equals(this.getColor())) {
                                return true;
                            }
                            foundHorizontalUpper = true;
                            break;
                        case ('R'):
                            if (!first.getColor().equals(this.getColor())) {
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
                horizontalLowerSquare =(Object[]) board[placeNumber][l];

                if (l == placeLetter - 1 && horizontalLowerSquare[1] != null) {
                    if (horizontalLowerSquare[1] instanceof King) {
                        System.err.println("You are trying to move a king to another king!");
                        return true;
                    }
                }

                if (horizontalLowerSquare[1] != null) {
                    IFigure first =(IFigure) horizontalLowerSquare[1];
                    switch (first.getName()) {
                        case ('Q'):
                            if (!first.getColor().equals(this.getColor())) {
                                return true;
                            }
                            foundHorizontalLower = true;
                            break;
                        case ('R'):
                            if (!first.getColor().equals(this.getColor())) {
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
                verticalUpperSquare =(Object[]) board[i][placeLetter];

                if (i == placeNumber + 1 && verticalUpperSquare[1] != null) {
                    if (verticalUpperSquare[1] instanceof King) {
                        System.err.println("You are trying to move a king to another king!");
                        return true;
                    }
                }

                if (verticalUpperSquare[1] != null) {
                    IFigure first =(IFigure) verticalUpperSquare[1];
                    switch (first.getName()) {
                        case ('Q'):
                            if (!first.getColor().equals(this.getColor())) {
                                return true;
                            }
                            foundVerticalUpper = true;
                            break;
                        case ('R'):
                            if (!first.getColor().equals(this.getColor())) {
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
                verticalLowerSquare =(Object[]) board[j][placeLetter];

                if (j == placeNumber - 1 && verticalLowerSquare[1] != null) {
                    if (verticalLowerSquare[1] instanceof King) {
                        System.err.println("You are trying to move a king to another king!");
                        return true;
                    }
                }

                if (verticalLowerSquare[1] != null) {
                    IFigure first =(IFigure) verticalLowerSquare[1];
                    switch (first.getName()) {
                        case ('Q'):
                            if (!first.getColor().equals(this.getColor())) {
                                return true;
                            }
                            foundVerticalLower = true;
                            break;
                        case ('R'):
                            if (!first.getColor().equals(this.getColor())) {
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

    private boolean checkDiagonals(Object[][] board) {
        Object[] upperRightDiagonalSquare = null;
        Object[] lowerLeftDiagonalSquare = null;
        Object[] upperLeftDiagonalSquare = null;
        Object[] lowerRightDiagonalSquare = null;
        boolean stillChecking = true;
        boolean foundUpperRight = false;
        boolean foundUpperLeft = false;
        boolean foundLowerRight = false;
        boolean foundLowerLeft = false;
        int i = placeNumber;
        int j = placeLetter;
        int k = placeNumber;
        int l = placeLetter;

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

                if (j <= 7 && !foundUpperRight) {
                    upperRightDiagonalSquare = (Object[]) board[i][j];

                    if (j == placeLetter + 1 && i == placeNumber + 1 && upperRightDiagonalSquare[1] != null) {
                        if (upperRightDiagonalSquare[1] instanceof King) {
                            System.err.println("You are trying to move a king to another king!");
                            return true;
                        }
                    }

                    if (upperRightDiagonalSquare[1] != null ) {
                        IFigure first =(IFigure) upperRightDiagonalSquare[1];
                        if (isWhite && i == placeNumber + 1 && j == placeLetter + 1 && first instanceof Pawn) {
                            Pawn p =(Pawn) upperRightDiagonalSquare[1];
                            if (p.getColor().equals("black")) {
                                return true;
                            }else {
                                foundUpperRight= true;
                            }
                        }

                        switch (first.getName()) {
                            case ('Q'):
                                if (!first.getColor().equals(this.getColor())) {
                                    return true;
                                }
                                foundUpperRight = true;
                                break;
                            case ('B'):
                                if (!first.getColor().equals(this.getColor())) {
                                    return true;
                                }
                                foundUpperRight = true;
                                break;
                            default:
                                foundUpperRight = true;
                                break;
                        }
                    }
                    if (i == 7 || j == 7) {
                        foundUpperRight = true;
                    }
                }

                if (l >= 0 && !foundUpperLeft) {
                    upperLeftDiagonalSquare = (Object[]) board[i][l];

                    if (l == placeLetter - 1 && i == placeNumber + 1 && upperLeftDiagonalSquare[1] != null) {
                        if (upperLeftDiagonalSquare[1] instanceof King) {
                            System.err.println("You are trying to move a king to another king!");
                            return true;
                        }
                    }

                    if (upperLeftDiagonalSquare[1] != null) {
                        IFigure first =(IFigure) upperLeftDiagonalSquare[1];

                        if (isWhite && i == placeNumber + 1 && l == placeLetter - 1 && upperLeftDiagonalSquare[1] instanceof Pawn) {
                            Pawn p =(Pawn) upperLeftDiagonalSquare[1];
                            if (p.getColor().equals("black")) {
                                return true;
                            }else {
                                foundUpperLeft = true;
                            }
                        }

                        switch (first.getName()) {
                            case ('Q'):
                                if (!first.getColor().equals(this.getColor())) {
                                    return true;
                                }
                                foundUpperLeft = true;
                                break;
                            case ('B'):
                                if (!first.getColor().equals(this.getColor())) {
                                    return true;
                                }
                                foundUpperLeft = true;
                                break;
                            default:
                                foundUpperLeft = true;
                                break;
                        }
                    }
                    if (i == 7 || l == 0) {
                        foundUpperLeft = true;
                    }
                }
            }else {
                foundUpperLeft = true;
                foundUpperRight = true;
            }

            if (k >= 0) {

                if (j <= 7 && !foundLowerRight) {
                    lowerRightDiagonalSquare = (Object[]) board[k][j];

                    if (j == placeLetter + 1 && k == placeNumber - 1 && lowerRightDiagonalSquare[1] != null) {
                        if (lowerRightDiagonalSquare[1] instanceof King) {
                            System.err.println("You are trying to move a king to another king!");
                            return true;
                        }
                    }

                    if (lowerRightDiagonalSquare[1] != null) {
                        IFigure first =(IFigure) lowerRightDiagonalSquare[1];

                        if (!isWhite && k == placeNumber - 1 && j == placeLetter + 1 && lowerRightDiagonalSquare[1] instanceof Pawn) {
                            Pawn p =(Pawn) lowerRightDiagonalSquare[1];
                            if (p.getColor().equals("white")) {
                                return true;
                            }else {
                                foundLowerRight = true;
                            }
                        }

                        switch (first.getName()) {
                            case ('Q'):
                                if (!first.getColor().equals(this.getColor())) {
                                    return true;
                                }
                                foundLowerRight = true;
                                break;
                            case ('B'):
                                if (!first.getColor().equals(this.getColor())) {
                                    return true;
                                }
                                foundLowerRight = true;
                                break;
                            default:
                                foundLowerRight = true;
                                break;
                        }
                    }
                    if (k == 0 || j == 7) {
                        foundLowerRight = true;
                    }
                }

                if (l >= 0 && !foundLowerLeft) {
                    lowerLeftDiagonalSquare = (Object[]) board[k][l];

                    if (k == placeLetter - 1 && l == placeNumber - 1 && lowerLeftDiagonalSquare[1] != null) {
                        if (lowerLeftDiagonalSquare[1] instanceof King) {
                            System.err.println("You are trying to move a king to another king!");
                            return true;
                        }
                    }

                    if (lowerLeftDiagonalSquare[1] != null) {
                        IFigure first =(IFigure) lowerLeftDiagonalSquare[1];

                        if (!isWhite && k == placeNumber - 1 && l == placeLetter - 1 && lowerLeftDiagonalSquare[1] instanceof Pawn) {
                            Pawn p =(Pawn) lowerLeftDiagonalSquare[1];
                            if (p.getColor().equals("white")) {
                                return true;
                            }else {
                                foundLowerLeft = true;
                            }
                        }

                        switch (first.getName()) {
                            case ('Q'):
                                if (!first.getColor().equals(this.getColor())) {
                                    return true;
                                }
                                foundLowerLeft = true;
                                break;
                            case ('B'):
                                if (!first.getColor().equals(this.getColor())) {
                                    return true;
                                }
                                foundLowerLeft = true;
                                break;
                            default:
                                foundLowerLeft = true;
                                break;
                        }
                    }
                    if (k == 0 || l == 0) {
                        foundLowerLeft = true;
                    }
                }
            }else {
                foundLowerLeft = true;
                foundLowerRight = true;
            }
        }
        return false;
    }

    public int getLetter() {
        return this.placeLetter;
    }

    public int getNumber() {
        return this.placeNumber;
    }

}
