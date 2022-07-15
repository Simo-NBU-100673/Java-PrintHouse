package Paper;

import ProjectExceptions.IllegalNegativeArgumentException;

import java.math.BigDecimal;

public enum TypeOfPaper {
    NORMAL(BigDecimal.valueOf(0.10)), GLOSSY(BigDecimal.valueOf(0.20)), NEWSPAPER_PAPER(BigDecimal.valueOf(0.15));

    private BigDecimal price;

    TypeOfPaper(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        if (price.compareTo(BigDecimal.valueOf(0))>-1){
            this.price = price;
        }else {
            try {
                throw new IllegalNegativeArgumentException("The paper property TYPE OF PAPER can not hava a negative value for price!");
            } catch (IllegalNegativeArgumentException e) {
                e.printStackTrace();
            }
        }
    }
}
