package Workers;

import java.math.BigDecimal;

public class Worker {
    private String name;
    private PositionType position;

    public Worker(String name, PositionType position) {
        this.name = name;
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PositionType getPosition() {
        return position;
    }

    public void setPosition(PositionType position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "Worker{" +
                "name='" + name + '\'' +
                ", position=" + position +
                '}';
    }
}
