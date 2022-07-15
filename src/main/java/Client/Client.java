package Client;

import DataSaver.DataSaver;
import Invoice.Invoice;
import Paper.*;
import PrintHouse.PrintHouse;
import ProjectExceptions.ArgumentAlreadyExzistedException;
import ProjectExceptions.IllegalNegativeArgumentException;
import ProjectExceptions.InsufficientMoneyException;
import Publication.ClientDemand;
import Publication.PrintHousePublication;

import java.math.BigDecimal;
import java.util.ArrayList;

public class Client implements DataSaver {
    private String name;
    private BigDecimal portfolio;
    private ArrayList<ClientDemand> demandsList;
    private ArrayList<Invoice> invoices;

    public Client(String name) {
        this.name = name;
        this.setPortfolio(BigDecimal.valueOf(0));
        demandsList = new ArrayList<>();
        invoices = new ArrayList<>();
    }

    public Client(String name, BigDecimal portfolio) {
        this.name = name;
        this.setPortfolio(portfolio);
        demandsList = new ArrayList<>();
        invoices = new ArrayList<>();
    }

    public void setPortfolio(BigDecimal portfolio) {
        if(portfolio.compareTo(BigDecimal.valueOf(0))>-1){
            this.portfolio = portfolio;
        }else {
            try {
                throw new IllegalNegativeArgumentException("The portfolio of Client can not be a negative value!");
            } catch (IllegalNegativeArgumentException e) {
                if(this.portfolio == null){
                    this.portfolio= BigDecimal.valueOf(0);
                }
                e.printStackTrace();
            }
        }
    }

    public BigDecimal getPortfolio() {
        return portfolio;
    }

    public String getName() {
        return name;
    }

    public ArrayList<ClientDemand> getDemandsList() {
        return demandsList;
    }

    //Creating a demand and passing it to function called addDemandToDemandsList() with parameter the newly created ClientDemand
    public void createDemand(String titleOfPublication, int numOfUnits, SizeOfPaper sizeOfPaper, TypeOfPaper typeOfPaper){
        ClientDemand current = new ClientDemand(this.getName(),titleOfPublication,numOfUnits, new Paper(sizeOfPaper, typeOfPaper));
        try {
            this.addDemandToDemandsList(current);
        } catch (ArgumentAlreadyExzistedException e) {
            e.printStackTrace();
        }
    }

    //Adding the demand in DemandsList of the current Client
    private void addDemandToDemandsList(ClientDemand current) throws ArgumentAlreadyExzistedException {
        if(!checkIfInside(current, demandsList)){
            demandsList.add(current);
        }else {
            throw new ArgumentAlreadyExzistedException("Demands with the same name can't be added at the same time to the DemandsList of the same Client!");
        }
    }

    //Checks if the name of the Demand is in a Demand in the List of Demands
    private boolean checkIfInside(ClientDemand searched, ArrayList<ClientDemand> demands){
        for(ClientDemand i: demands){
            if (i.getTitleOfPublication().equals(searched.getTitleOfPublication())) {
                return true;
            }
        }
        return false;
    }

    //OverLoaded function that checks if the name of the Demand is in a Demand in the List of Demands
    private boolean checkIfInside(String searched, ArrayList<ClientDemand> demands){
        for(ClientDemand i: demands){
            if (i.getTitleOfPublication().equals(searched)) {
                return true;
            }
        }
        return false;
    }

    //Sends a Demand from Client to the PrintHouse
    public void sendDemand(PrintHouse printhouse, String titleOfDemand){
        if(checkIfInside(titleOfDemand, demandsList)){
            for(ClientDemand i: demandsList){
                if (i.getTitleOfPublication().equals(titleOfDemand)) {
                    printhouse.receiveDemandsFromClient(i);
                }
            }
        }else {
            try{
                throw new IllegalArgumentException("This Demand is not created from this Client so it can not be send from him!");
            }catch(IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
    }

    //Gets the invoices send by the PrintHouse and if the Invoice is new we add it to the List with Invoices
    public void receiveInvoice(Invoice i){
        if(!invoices.contains(i)){
            invoices.add(i);
        }else {
            try{
                throw new IllegalArgumentException("This invoice is already received by "+this.getName());
            }catch (IllegalArgumentException e){
                e.printStackTrace();
            }
        }
    }

    //Prints all invoices that the client has
    public void printInvoices(){
        int count = 1;
        System.out.println("\nInvoices of Client("+this.getName()+"): ");
        for(Invoice i:invoices){
            System.out.println("["+count+"]"+" {");
            System.out.println(i);
            System.out.println("}");
            count++;
        }
    }

    public void payInvoice(String titleOfPublicationInInvoice, PrintHouse printHouse){
        boolean hasInvoice = hasInvoice(titleOfPublicationInInvoice, this.invoices);
        boolean hasEnoughMoney = hasEnoughMoneyToPayThisInvoice(titleOfPublicationInInvoice, this.invoices, this.getPortfolio());
        boolean hasThisDemand = hasThisDemand(titleOfPublicationInInvoice, this.invoices, this.demandsList);

        try{
            if(!hasInvoice){
                throw new IllegalArgumentException("This Invoice is not in possesion of this Client!");
            }else if(!hasEnoughMoney){
                throw new InsufficientMoneyException("This Client does not have the needed money to pay this Invoice!");
            }else if(!hasThisDemand){
                throw new IllegalArgumentException("This Client doesn't have a demand that coresponds to this Invoice!");
            }else {
                for(Invoice invoice:invoices){
                    if(invoice.getPublication().getTitleOfPublication().equals(titleOfPublicationInInvoice)){
                        this.setPortfolio(this.getPortfolio().subtract(invoice.getPriceToPay()));
                        for (ClientDemand cd:demandsList){
                            if(cd.getTitleOfPublication().equals(invoice.getPublication().getTitleOfPublication())){
                                this.demandsList.remove(cd);
                                printHouse.getPaid(invoice);
                                break;
                            }
                        }
                    }
                }
            }
        }catch (IllegalArgumentException | InsufficientMoneyException e){
            e.printStackTrace();
        }
    }

    private boolean hasInvoice(String titleOfPublicationInInvoice, ArrayList<Invoice> invList){
        for(Invoice i:invList){
            if(i.getPublication().getTitleOfPublication().equals(titleOfPublicationInInvoice)){
                return true;
            }
        }
        return false;
    }

    private boolean hasEnoughMoneyToPayThisInvoice(String titleOfPublicationInInvoice, ArrayList<Invoice> invList, BigDecimal money){
        for(Invoice i:invList){
            if(i.getPublication().getTitleOfPublication().equals(titleOfPublicationInInvoice)){
                if(money.compareTo(i.getPriceToPay()) >= 0){
                    return true;
                }
            }
        }
        return false;
    }

    private boolean hasThisDemand(String titleOfPublicationInInvoice, ArrayList<Invoice> invList, ArrayList<ClientDemand> listOfDemands){
        for(Invoice i:invList){
            if(i.getPublication().getTitleOfPublication().equals(titleOfPublicationInInvoice)){
                for(ClientDemand cd: listOfDemands){
                    if(i.getPublication().getTitleOfPublication().equals(cd.getTitleOfPublication())){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "Client{" +
                "name='" + name + '\'' +
                ", portfolio=" + portfolio +
                "\n\tdemandsList=" + demandsList +
                ",\n\tinvoices=" + invoices +
                '}';
    }

    @Override
    public String getImportantInfo() {
        return "\nClient{" +
                "name='" + name + '\'' +
                ", portfolio=" + portfolio +
                "\ndemandsList=" + demandsList +
                ",\ninvoices=" + invoices +
                '}';
    }
}
