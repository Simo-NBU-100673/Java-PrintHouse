package Workers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WorkerTest {
    private Worker manager;
    private Worker operator;

    @BeforeEach
    void init(){
        manager = new Worker("Simeon",PositionType.MANAGER);
        operator = new Worker("John",PositionType.OPERATOR);
    }


    @Test
    @DisplayName("Creating a worker of type Manager and getting its position")
    void getPositionManager() {
        assertEquals(PositionType.MANAGER,manager.getPosition());
    }

    @Test
    @DisplayName("Creating a worker of type Operator and getting its position")
    void getPositionOperator() {
        assertEquals(PositionType.OPERATOR,operator.getPosition());
    }
}