package Paper;

import java.math.BigDecimal;

public class Paper {
    private SizeOfPaper sizeOfPaper;
    private TypeOfPaper typeOfPaper;

    public Paper(SizeOfPaper sizeOfPaper, TypeOfPaper typeOfPaper) {
        this.sizeOfPaper = sizeOfPaper;
        this.typeOfPaper = typeOfPaper;
    }

    public SizeOfPaper getSizeOfPaper() {
        return sizeOfPaper;
    }

    public TypeOfPaper getTypeOfPaper() {
        return typeOfPaper;
    }

    public BigDecimal getPricePerPiece() {
        return sizeOfPaper.getPrice().add(typeOfPaper.getPrice());
    }

    public void setSizeOfPaper(SizeOfPaper sizeOfPaper) {
        this.sizeOfPaper = sizeOfPaper;
    }

    public void setTypeOfPaper(TypeOfPaper typeOfPaper) {
        this.typeOfPaper = typeOfPaper;
    }

    @Override
    public String toString() {
        return "Paper{" +
                "sizeOfPaper=" + sizeOfPaper +
                ", typeOfPaper=" + typeOfPaper +
                '}';
    }
}
