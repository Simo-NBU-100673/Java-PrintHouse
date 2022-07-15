package PrintHouse;

import Paper.*;
import ProjectExceptions.ArgumentAlreadyExzistedException;
import Workers.PositionType;
import Workers.Worker;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class PrintHouseTest {
    private Worker manager = new Worker("Simeon", PositionType.MANAGER);
    private Worker operator = new Worker("John",PositionType.OPERATOR);
    private PrintHouse testPrintHouse = new PrintHouse("NY-Times", BigDecimal.valueOf(1500),BigDecimal.valueOf(1600));

    @Nested
    class HireWorkerTests{
        @Test
        void hireWorker(){
           testPrintHouse.hireWorker(manager);
            ArrayList<Worker> workersList = new ArrayList<>(testPrintHouse.getHiredWorkersList());
            assertTrue(workersList.contains(manager));
        }
        @Test
        void hireWorkerThatHasBeenHired(){
            int actual = 0;
            testPrintHouse.hireWorker(manager);
            ArrayList<Worker> workersList = new ArrayList<>(testPrintHouse.getHiredWorkersList());
            for(Worker current: workersList){
                if(current.equals(manager)){
                    actual++;
                }
            }
            assertEquals(1,actual);
        }
    }

    @Nested
    class FireWorkerTests{
        @Test
        void fireWorker(){
            testPrintHouse.hireWorker(manager);
            ArrayList<Worker> workersList = new ArrayList<>(testPrintHouse.getHiredWorkersList());
            testPrintHouse.fireWorker(manager);
            assertTrue(workersList.contains(manager));
        }

        @Test
        void fireWorkerThatIsNotHired(){
            ArrayList<Worker> workersListBefore = new ArrayList<>(testPrintHouse.getHiredWorkersList());
            testPrintHouse.fireWorker(manager);
            ArrayList<Worker> workersListAfter = new ArrayList<>(testPrintHouse.getHiredWorkersList());
            assertEquals(workersListBefore, workersListAfter);
        }
    }

    @Nested
    class PromoteTests {
        @Test
        void promoteWorkerThatIsHired(){
            testPrintHouse.hireWorker(operator);
            testPrintHouse.promote(operator);
            ArrayList<Worker> workersListBefore = new ArrayList<>(testPrintHouse.getHiredWorkersList());
            int counterOfManagers = 0;
            for(Worker current : workersListBefore){
                if(current.getPosition().equals(PositionType.MANAGER)){
                    counterOfManagers++;
                }
            }
            assertEquals(1,counterOfManagers);
        }
        @Test
        void promoteWorkerThatIsNotHired(){
            //At the start of this test there is a Worker with Name:operator and Position:MANAGER
            //Here we remove him from the list
            testPrintHouse.fireWorker(operator);


            testPrintHouse.promote(manager);
            ArrayList<Worker> workersListBefore = new ArrayList<>(testPrintHouse.getHiredWorkersList());
            assertEquals(0,workersListBefore.size());
        }
        @Test
        void promoteWorkerThatIsManager(){
            testPrintHouse.hireWorker(manager);
            testPrintHouse.promote(manager);
            ArrayList<Worker> workersListAfter = new ArrayList<>(testPrintHouse.getHiredWorkersList());
            int countOfManagers = 0;
            for (Worker current: workersListAfter){
                if(current.getPosition().equals(PositionType.MANAGER)){
                    countOfManagers++;
                }
            }
            assertEquals(1,countOfManagers);
        }
    }

    @Test
    void addIncomePositiveValue() {
        testPrintHouse.addIncome(BigDecimal.valueOf(150));
        assertEquals(BigDecimal.valueOf(150),testPrintHouse.getGrossIncome());
    }

    @Test
    void addIncomeNegativeValue() {
        testPrintHouse.addIncome(BigDecimal.valueOf(69));
        testPrintHouse.addIncome(BigDecimal.valueOf(-30));
        assertEquals(BigDecimal.valueOf(69),testPrintHouse.getGrossIncome());
    }

    @Nested
    class WorkersSalaryExpensesTests {
        @Test
        @DisplayName("Workers salary expenses without workers")
        void workersSalaryExpensesNone(){
            assertEquals(BigDecimal.valueOf(0),testPrintHouse.workersSalaryExpenses());
        }

        @Test
        @DisplayName("Workers salary expenses when we have one Manager")
        void workersSalaryExpensesManager(){
            testPrintHouse.hireWorker(manager);
            assertEquals(BigDecimal.valueOf(1500),testPrintHouse.workersSalaryExpenses());
        }
        @Test
        @DisplayName("Workers salary expenses when we have one Operator and one Manager")
        void workersSalaryExpensesMultiple(){
            testPrintHouse.hireWorker(operator);
            testPrintHouse.hireWorker(manager);
            //System.out.println(testPrintHouse.getHiredWorkersList());
            assertEquals(BigDecimal.valueOf(3000),testPrintHouse.workersSalaryExpenses());
        }
        @Test
        @DisplayName("Workers salary expenses when we have one Operator and one Manager and the manager gets a bonus")
        void workersSalaryExpensesMultipleBonus(){
            testPrintHouse.hireWorker(manager);
            testPrintHouse.hireWorker(operator);
            testPrintHouse.addIncome(BigDecimal.valueOf(1601));
            //System.out.println(testPrintHouse.getHiredWorkersList());

            //By default, the manager's bonus is set to be 5% over his income
            assertEquals(BigDecimal.valueOf(3075),testPrintHouse.workersSalaryExpenses());
        }
    }

    @Test
    void setBonusPercentPositive() {
        testPrintHouse.setBonusPercent(BigDecimal.valueOf(10));
        testPrintHouse.addIncome(BigDecimal.valueOf(1601));
        testPrintHouse.hireWorker(manager);
        assertEquals(BigDecimal.valueOf(1650),testPrintHouse.workersSalaryExpenses());
    }

    @Test
    void setBonusPercentNegative() {
        testPrintHouse.setBonusPercent(BigDecimal.valueOf(-10));
        testPrintHouse.addIncome(BigDecimal.valueOf(1601));
        testPrintHouse.hireWorker(manager);
        assertEquals(BigDecimal.valueOf(1575),testPrintHouse.workersSalaryExpenses());
    }

    @Test
    void buyPaperPositiveNumberOfPapers() {
        Paper test = new Paper(SizeOfPaper.A4, TypeOfPaper.NORMAL);
        testPrintHouse.buyPaper(test,1560);
        assertEquals(BigDecimal.valueOf(468).setScale(2, RoundingMode.HALF_UP),testPrintHouse.getAllExpenses());
    }

    @Test
    void buyPaperNegativeNumberOfPapers() {
        Paper test = new Paper(SizeOfPaper.A4, TypeOfPaper.NORMAL);
        testPrintHouse.buyPaper(test,-1560);
        assertEquals(BigDecimal.valueOf(0).setScale(2, RoundingMode.HALF_UP),testPrintHouse.getAllExpenses());
    }

    @Test
    void bonusAchieved() {
        testPrintHouse.addIncome(BigDecimal.valueOf(1601));
        assertTrue(testPrintHouse.bonusAchieved());
    }

    @Test
    void getAllExpenses() {
        assertEquals(BigDecimal.valueOf(0).setScale(2,RoundingMode.HALF_UP),testPrintHouse.getAllExpenses());
    }

    @Test
    @DisplayName("getAllExpenses() for Workers and Paper")
    void getAllExpensesWithWorkersAndBoughtPaper() {
        testPrintHouse.hireWorker(manager);
        testPrintHouse.hireWorker(operator);
        testPrintHouse.addIncome(BigDecimal.valueOf(1601));

        Paper test = new Paper(SizeOfPaper.A4, TypeOfPaper.NORMAL);
        testPrintHouse.buyPaper(test,1000);

        testPrintHouse.setBonusPercent(BigDecimal.valueOf(10));

        assertEquals(BigDecimal.valueOf(3450).setScale(2,RoundingMode.HALF_UP),testPrintHouse.getAllExpenses());
    }
}