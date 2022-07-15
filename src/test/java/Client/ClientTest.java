package Client;

import Paper.*;
import Publication.ClientDemand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ClientTest {

    @Test
    void setPortfolioDefaultConstructor() {
        Client testClient = new Client("Simo");
        assertEquals(BigDecimal.valueOf(0),testClient.getPortfolio());
    }

    @Test
    void setPortfolioConstructorWithParameterNegativeValue() {
        Client testClient = new Client("Simo",BigDecimal.valueOf(-10));
        assertEquals(BigDecimal.valueOf(0),testClient.getPortfolio());
    }

    @Test
    void setPortfolioAfterConstructed() {
        Client testClient = new Client("Simo",BigDecimal.valueOf(22));
        testClient.setPortfolio(BigDecimal.valueOf(-15));
        assertEquals(BigDecimal.valueOf(22),testClient.getPortfolio());
    }

    @Test
    void createDemand(){
        Client testClient = new Client("Simo",BigDecimal.valueOf(3000));
        testClient.createDemand("24-Hours",20000, SizeOfPaper.A4, TypeOfPaper.NEWSPAPER_PAPER);
        boolean flag = false;
        for (ClientDemand current: testClient.getDemandsList()){
            if (current.getTitleOfPublication().equals("24-Hours")) {
                flag = true;
                break;
            }
        }
        assertTrue(flag);
    }

    @Test
    void createDemandNegativeNumberOfUnits(){
        Client testClient = new Client("Simo",BigDecimal.valueOf(3000));
        testClient.createDemand("24-Hours",-20000, SizeOfPaper.A4, TypeOfPaper.NEWSPAPER_PAPER);
        boolean flag = false;
        for (ClientDemand current: testClient.getDemandsList()){
            if (current.getTitleOfPublication().equals("24-Hours")) {
                flag = true;
                break;
            }
        }
        assertTrue(flag);
    }

    @Test
    void createDemandTwice(){
        Client testClient = new Client("Simo",BigDecimal.valueOf(3000));
        testClient.createDemand("24-Hours",10000, SizeOfPaper.A4, TypeOfPaper.NEWSPAPER_PAPER);
        testClient.createDemand("24-Hours",10000, SizeOfPaper.A4, TypeOfPaper.NEWSPAPER_PAPER);
        int flag = 0;
        for (ClientDemand current: testClient.getDemandsList()){
            if (current.getTitleOfPublication().equals("24-Hours")) {
                flag++;
            }
        }
        assertEquals(1,flag);
    }

    @Test
    @DisplayName("Test the case sensitivity when creating a new demand")
    void createDemandTwiceWithAlmostTheSameName(){
        Client testClient = new Client("Simo",BigDecimal.valueOf(3000));
        testClient.createDemand("24-Hours",10000, SizeOfPaper.A4, TypeOfPaper.NEWSPAPER_PAPER);
        testClient.createDemand("24-hours",10000, SizeOfPaper.A4, TypeOfPaper.NEWSPAPER_PAPER);
        assertEquals(2,testClient.getDemandsList().size());
    }
}