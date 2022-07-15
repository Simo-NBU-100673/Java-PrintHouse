package Paper;


import ProjectExceptions.IllegalNegativeArgumentException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class PaperTest {

    private Paper paperTest;

    @BeforeEach
    void init(){
        paperTest = new Paper(SizeOfPaper.A1,TypeOfPaper.GLOSSY);
    }

    @Test
    @DisplayName("Testing getPricePerPiece with A1 and GLOSSY properties")
    void getPricePerPiece() {
        BigDecimal expected = BigDecimal.valueOf(0.05+0.20);
        BigDecimal actual = paperTest.getPricePerPiece();
        assertEquals(expected,actual);
    }

    @Test
    @DisplayName("Testing getSizeOfPaper with A1 and GLOSSY properties")
    void getSizeOfPaper() {
        assertEquals(SizeOfPaper.A1,paperTest.getSizeOfPaper());
    }

    @Nested
    class TypeOfPaperTest{
        @Test
        void setPriceForTypePositiveValue(){
            TypeOfPaper.GLOSSY.setPrice(BigDecimal.valueOf(15));
            assertEquals(BigDecimal.valueOf(15.05), paperTest.getPricePerPiece());
        }
        @Test
        void setPriceForTypeNegativeValue(){
            TypeOfPaper.GLOSSY.setPrice(BigDecimal.valueOf(-4));
//            System.out.println(paperTest);
            assertEquals(BigDecimal.valueOf(15.05), paperTest.getPricePerPiece());
        }
    }

    @Nested
    class SizeOfPaperTest{
        private final Paper sizeTest = new Paper(SizeOfPaper.A2,TypeOfPaper.GLOSSY);
        @Test
        void setPriceForSizePositiveValue(){
            SizeOfPaper.A2.setPrice(BigDecimal.valueOf(10));
            assertEquals(BigDecimal.valueOf(10.20), sizeTest.getPricePerPiece());
        }
        @Test
        void setPriceForSizeNegativeValue(){
            SizeOfPaper.A2.setPrice(BigDecimal.valueOf(-4));
            assertEquals(BigDecimal.valueOf(10.20), sizeTest.getPricePerPiece());
        }
    }
}