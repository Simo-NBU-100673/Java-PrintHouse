package Publication;

import Paper.Paper;
import ProjectExceptions.IllegalNegativeArgumentException;

public class ClientDemand{
    private final String clientName;
    private String titleOfPublication;
    private int numberOfUnits;
    private Paper paper;

    public ClientDemand(String clientName, String titleOfPublication, int numberOfUnits, Paper paper) {
        this.clientName = clientName;
        this.titleOfPublication = titleOfPublication;
        this.numberOfUnits = Math.max(numberOfUnits, 0);
        this.paper = paper;
    }

    public void setTitleOfPublication(String titleOfPublication) {
        this.titleOfPublication = titleOfPublication;
    }

    public void setNumberOfUnits(int numberOfUnits) {
        if (numberOfUnits>=0){
            this.numberOfUnits = numberOfUnits;
        }else {
            try {
                throw new IllegalNegativeArgumentException("The numbers of units can not be a negative value!");
            } catch (IllegalNegativeArgumentException e) {
                e.printStackTrace();
            }
        }
    }

    public void setPaper(Paper paper) {
        this.paper = paper;
    }

    public String getClientName() {
        return clientName;
    }

    public String getTitleOfPublication() {
        return titleOfPublication;
    }

    public int getNumberOfUnits() {
        return numberOfUnits;
    }

    public Paper getPaper() {
        return paper;
    }

    @Override
    public String toString() {
        return "ClientDemand{" +
                "clientName = '" + clientName + '\'' +
                ", titleOfPublication = '" + titleOfPublication + '\'' +
                ", numberOfUnits = " + numberOfUnits +
                ", paper = " + paper +
                "}";
    }
}
