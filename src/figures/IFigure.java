package figures;

public interface IFigure {

    public String getColor();
    public String getNameAndColor();
    public char getName();
    public boolean move(int currentNumber, int currentLetter,int newNumber, int newLetter, boolean checkTake);
    public boolean take(int currentNumber, int currentLetter,int newNumber, int newLetter);

}
