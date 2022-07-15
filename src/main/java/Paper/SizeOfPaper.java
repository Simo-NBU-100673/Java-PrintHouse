package Paper;

import ProjectExceptions.IllegalNegativeArgumentException;

import java.math.BigDecimal;

public enum SizeOfPaper {
    A1(BigDecimal.valueOf(0.05)), A2(BigDecimal.valueOf(0.1)), A3(BigDecimal.valueOf(0.15)), A4(BigDecimal.valueOf(0.2)), A5(BigDecimal.valueOf(0.25));

    private BigDecimal price;

    SizeOfPaper(BigDecimal price) {
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
                throw new IllegalNegativeArgumentException("The paper property SIZE can not hava a negative value for price!");
            } catch (IllegalNegativeArgumentException e) {
                e.printStackTrace();
            }
        }
    }
}
