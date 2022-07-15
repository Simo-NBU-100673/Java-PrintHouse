package PrintHouse;

import Client.Client;
import DataSaver.DataSaver;
import Invoice.*;
import Paper.*;
//import Publication.Publication; // HERE IS PUBLICATION <------------------------------------------
import Printer.Printer;
import ProjectExceptions.ArgumentAlreadyExzistedException;
import ProjectExceptions.IllegalNegativeArgumentException;
import ProjectExceptions.NotHiredPromotionException;
import ProjectExceptions.PrinterIsRunningException;
import Publication.ClientDemand;
import Publication.PrintHousePublication;
import Publication.PublicationState;
import Workers.PositionType;
import Workers.Worker;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;

public class PrintHouse implements DataSaver {
    private String name;
    private ArrayList<Worker> hiredWorkersList;
    private ArrayList<Paper> paperInStock;
    private ArrayList<PrintHousePublication> queue;
    private ArrayList<Printer> listOfPrinters;
    private ArrayList<Invoice> invoices;
    private BigDecimal baseSalary;
    private BigDecimal bonusPercent;
    private BigDecimal minGrossIncomeForBonus;
    private BigDecimal grossIncome;//At the beggining we start with zero
    private BigDecimal grossExpenses;//At the beggining we start with zero
    private BigDecimal total;//At the beggining we start with zero
    private BigDecimal workersExpenses = BigDecimal.valueOf(0);
    private static boolean isBonusAchiaved;

    public PrintHouse(String name, BigDecimal baseSalary,BigDecimal minGrossIncomeForBonus) {
        this.name = name;
        this.hiredWorkersList = new ArrayList<>();
        this.paperInStock = new ArrayList<>();
        this.queue = new ArrayList<>();
        this.invoices = new ArrayList<>();
        this.listOfPrinters = new ArrayList<>();
        this.baseSalary = baseSalary;
        this.bonusPercent = BigDecimal.valueOf(5);
        this.minGrossIncomeForBonus = minGrossIncomeForBonus;
        this.grossIncome = BigDecimal.valueOf(0);
        this.grossExpenses = BigDecimal.valueOf(0);
        this.total=BigDecimal.valueOf(0);
    }

    //hires a worker if he is not hired already and gives him a salary coresponding to his position
    public void hireWorker(Worker current) {
        if(!hiredWorkersList.contains(current)){
            hiredWorkersList.add(current);
            if(current.getPosition().equals(PositionType.MANAGER) && this.bonusAchieved()){
                grossExpenses=grossExpenses.add((baseSalary.multiply(bonusPercent)).divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP));
            }
            grossExpenses=grossExpenses.add(baseSalary);
        }else {
            try {
                throw new ArgumentAlreadyExzistedException("This worker is alread hired at the current PrintHouse!");
            } catch (ArgumentAlreadyExzistedException e) {
                e.printStackTrace();
            }
        }
    }

    //fires a worker
    public void fireWorker(Worker current) throws IllegalArgumentException{
        if(hiredWorkersList.contains(current)){
            if(current.getPosition().equals(PositionType.MANAGER) && this.bonusAchieved()){
                grossExpenses=grossExpenses.subtract((baseSalary.multiply(bonusPercent)).divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP));
            }
            grossExpenses=grossExpenses.subtract(baseSalary);
            hiredWorkersList.remove(current);
        }else {
            try{
                throw new IllegalArgumentException("Worker is not found to be working in the PrintHouse!");
            }catch(IllegalArgumentException e){
                e.printStackTrace();
            }
        }
    }

    //promotes a worker from Operator to Manager and sets the new salary
    public void promote(Worker current) {
        try {
            if(hiredWorkersList.contains(current)){
                if(current.getPosition().equals(PositionType.OPERATOR)){
                    current.setPosition(PositionType.MANAGER);
                    if(this.bonusAchieved()){
                        grossExpenses=grossExpenses.add((baseSalary.multiply(bonusPercent)).divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP));
                    }
                }else {
                    throw new IllegalArgumentException(current+" is already the highest position in the PrintHouse!");
                }
            }else {
                throw new NotHiredPromotionException(current+" can not be promoted because this worker is not hired!");
            }
        }catch (IllegalArgumentException | NotHiredPromotionException e){
            e.printStackTrace();
        }
    }

    //calculates the sum of all expenses and updates the salaries of the workers
    public BigDecimal workersSalaryExpenses(){
        BigDecimal sumOfAllSalaries = BigDecimal.valueOf(0);
        for(Worker current:hiredWorkersList){
            if(bonusAchieved() && current.getPosition().equals(PositionType.MANAGER)){
                sumOfAllSalaries = sumOfAllSalaries.add((baseSalary.multiply(bonusPercent)).divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP));
            }
            sumOfAllSalaries = sumOfAllSalaries.add(baseSalary);
        }
        return sumOfAllSalaries;
    }

    //adds income to the balance
    public void addIncome(BigDecimal income){
        try{
            if(income.compareTo(BigDecimal.ZERO)<0){
                throw new IllegalArgumentException("The added income must have a positive value!");
            }else {
                this.grossIncome = grossIncome.add(income);
                for (Worker wk: hiredWorkersList){
                    if(wk.getPosition().equals(PositionType.MANAGER) && bonusAchieved() && !isBonusAchiaved){
                        grossExpenses=grossExpenses.add((baseSalary.multiply(bonusPercent)).divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP));
                        isBonusAchiaved=true;
                    }
                }
            }
        }catch (IllegalArgumentException e){
            e.printStackTrace();
        }
    }

    //set new percent for the bonus
    public void setBonusPercent(BigDecimal bonus){
        if(bonus.compareTo(BigDecimal.valueOf(0))>-1){
            this.bonusPercent = bonus;
        }else {
            try {
                throw new IllegalNegativeArgumentException("The bonus can not be a negative value!");
            } catch (IllegalNegativeArgumentException e) {
                e.printStackTrace();
            }
        }
    }

    //buys paper and stores it inside paperInStock and increases the gross expenses
    public void buyPaper(Paper paper,int number){
        if(number>=0){
            for(int i=0;i<number;i++){
                paperInStock.add(paper);
            }
            this.grossExpenses=this.grossExpenses.add(paper.getPricePerPiece().multiply(BigDecimal.valueOf(number)));
        }else {
            try {
                throw new IllegalNegativeArgumentException("You can not buy a negative number of Paper!");
            } catch (IllegalNegativeArgumentException e) {
                e.printStackTrace();
            }
        }
    }

    //checks whether targeted grossIncome is achieved
    public boolean bonusAchieved(){
        return minGrossIncomeForBonus.compareTo(grossIncome)<0;
    }

    //this method will return all expenses including the automatic expenses when you buy paper
    public BigDecimal getAllExpenses(){
        //grossExpenses=grossExpenses.add(workersSalaryExpenses());
        return grossExpenses.setScale(2,RoundingMode.HALF_UP);
    }

    //This method receives the demands from Client
    public void receiveDemandsFromClient(ClientDemand current){
        PrintHousePublication tmp = new PrintHousePublication(current);
        //Checks if the same demand was sent from the same Client with the same name and if this is the second time
        //the same client send the same demand
        boolean isInside = false;
        for(PrintHousePublication php: queue) {
            if (php.getClientName().equals(current.getClientName()) && php.getTitleOfPublication().equals(current.getTitleOfPublication())) {
                try{
                    throw new IllegalArgumentException("This demand from the client is already added to the queue!");
                }catch (IllegalArgumentException e){
                    e.printStackTrace();
                    isInside = true;
                }
            }
        }
        //This statement will be executed only if the demand is created for the first time
        if(!isInside){
            queue.add(tmp);
        }
    }

    //If these criteria are true we will set the markup for the publication based on which later the invoice will be generated
    public void setMarkupOfPublication(String nameOfClient,String titleOfDemand, BigDecimal markup){
        boolean isInside =false;
        for(PrintHousePublication php: queue){
            if(php.getClientName().equals(nameOfClient)&&php.getTitleOfPublication().equals(titleOfDemand)){
                isInside = true;
                php.setMarkup(markup);
            }
        }
        if (!isInside){
            try{
                throw new IllegalArgumentException("This markup can not be assigned to a Publication that does not exist!");
            }catch (IllegalArgumentException e){
                e.printStackTrace();
            }
        }
    }

    //This will change the State of the Publication and will create an Invoice
    public void acceptDemandsFromClient(String clientName, String demandName){
        boolean isInside = false;
        for (PrintHousePublication i: queue){
            //If this is a correct name of client and demand title this will create invoice else it throws exception
            if(i.getClientName().equals(clientName) && i.getTitleOfPublication().equals(demandName)){
                i.setState(PublicationState.ACCEPTED);
                isInside = true;
                if(invoices.isEmpty()){
                    createInvoice(i);
                }else {
                    //Finds if this Invoice is being created for a second time
                    boolean invoiceCreatedAlready=false;
                    for(Invoice invoice: invoices){
                        if (invoice.getPublication().getTitleOfPublication().equals(demandName) && invoice.getPublication().getClientName().equals(clientName)) {
                            invoiceCreatedAlready = true;
                            break;
                        }
                    }
                    //If this invoice was not created from before here we pass it to the function that creates them and stores it in list of invoices of PrintHouse
                    if(!invoiceCreatedAlready){
                        createInvoice(i);
                    }else {
                        try {
                            throw new IllegalArgumentException("Another demand with the same name and client was accepted already!");
                        }catch (IllegalArgumentException e){
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        if(!isInside){
            try {
                throw new IllegalArgumentException("Client name or Demand title does not exist in this queue!");
            }catch (IllegalArgumentException e){
                e.printStackTrace();
            }
        }
    }

    //Creates Invoices
    private void createInvoice(PrintHousePublication current){
        Invoice inv1 = new Invoice(current,this.getName());
        if(!invoices.contains(inv1)){
            invoices.add(inv1);
        }
    }

    //Sends all invoices for this Client
    public void sendInvoiceToClient(Client client, String titleOfPublicationInInvoice){
        boolean isInside = false;
        for(Invoice i:invoices){
            if(i.getPublication().getClientName().equals(client.getName())&&i.getPublication().getTitleOfPublication().equals(titleOfPublicationInInvoice)){
                isInside = true;
                System.out.println("I was invoked "+titleOfPublicationInInvoice);
                client.receiveInvoice(i);
                break;
            }
        }
        if(!isInside){
            try{
                throw new IllegalArgumentException("This invoice does not exist! Please try different nameOfClient or titleOfPublication!");
            }catch (IllegalArgumentException e){
                e.printStackTrace();
            }
        }
    }

    //GETTERS:
    //Getter for hired workers list(In this list are stored the workers that are currently working in the PrintHouse)
    public ArrayList<Worker> getHiredWorkersList() {
        return hiredWorkersList;
    }

    //Get the base salary that was set when we initialize the PrintHouse
    public BigDecimal getBaseSalary() {
        return baseSalary;
    }

    //Gets the gross Income
    public BigDecimal getGrossIncome() {
        return grossIncome;
    }

    //Return the name of the PrintHouse
    public String getName() {
        return name;
    }

    //Returns the gross Expenses
    public BigDecimal getGrossExpenses() {
        return grossExpenses;
    }

    public BigDecimal getTotal() {
        return this.getGrossIncome().subtract(this.getGrossExpenses());
    }

    //Prints the Demands from Clients that are in Queue
    public void printQueue(){
        System.out.println("\nQueue of PrintHouse: \n");
        int count = 1;
        for(PrintHousePublication i: queue){
            System.out.println("["+count+"]"+" {");
            System.out.println(i);
            System.out.println("}");
            count++;
        }
    }

    //Prints the Invoices that are created by this PrintHouse
    public void printInvoices() {
        System.out.println("\nInvoices of PrintHouse: ");
        for (Invoice i : invoices) {
            System.out.println(i);
        }
    }

    //This will transfer the money from Client to PrintHouse
    public void getPaid(Invoice invoice){
        if(invoices.contains(invoice)){
            for(Invoice i:invoices){
                if(i.equals(invoice)){
                    //Adds money to the PrintHouse
                    this.addIncome(invoice.getPriceToPay());
                    //Sets the state of the Invoice to PAID
                    i.setState(InvoiceState.PAID);
                    //Delete the publication from queue
                    this.deletePublicationFromQueue(i.getPublication());
                }
            }
        }
    }

    //This method will change the state of the publication from whom was created the coresponding invoice which invoked this method
    private void deletePublicationFromQueue(PrintHousePublication publication) {
        queue.removeIf(php -> php.equals(publication));
    }

    //We can add multiple duplicates of one printer
    public void addPrinter(int maxPaper, int speedOfPrinting){
        if(maxPaper>0 && speedOfPrinting>0){
            listOfPrinters.add(new Printer(maxPaper,speedOfPrinting));
        }else {
            try {
                throw new IllegalNegativeArgumentException("MaxPaper and speedOfPrinting both must be n>0 values!");
            } catch (IllegalNegativeArgumentException e) {
                e.printStackTrace();
            }
        }
    }

    //We remove a Printer if it is inside listOfPrinters by its ID which is integer
    public void removePrinter(int idOfPrinter){
        boolean isInside=false;
        for(int i=0;i<listOfPrinters.size();i++){
            if(listOfPrinters.get(i).getCurrent_id()==idOfPrinter){
                listOfPrinters.remove(listOfPrinters.get(i));
                isInside = true;
                Printer.setId(Printer.getId()-1);
            }
        }
        if(!isInside){
            try{
                throw new IllegalArgumentException("A printer with id: "+idOfPrinter+" does not exist in this PrintHouse!");
            }catch (IllegalArgumentException e){
                e.printStackTrace();
            }
        }
    }

    //This loader
    public void loadPaperInPrinter(int id, Paper paper,int numberOfPapers){
        boolean hasThisPrinter = false;
        boolean hasThisPaper = false;
        boolean hasThisNumberOfPapers = false;
        int count=0;
        for (Printer pr:listOfPrinters){
            //Checks if a printer with this id is owned by this Printhouse
            if(pr.getCurrent_id()==id){
                hasThisPrinter = true;

                //Checks if a paper of this kind is even in Stock
                if(this.paperInStock.contains(paper)){
                    hasThisPaper = true;

                    //Check if the needed quantity of Paper is in stock
                    for(Paper pp: paperInStock){
                        if(pp.equals(paper)){
                            count++;
                            //System.out.println(count);
                        }
                    }
                    if(count>=numberOfPapers){
                        hasThisNumberOfPapers = true;

                        //Checks if the printer can be filled with that much paper
                        if(pr.getFreeSlotsForPaper()>=numberOfPapers){
                            pr.loadPaper_Printer(paper, numberOfPapers);
                        }else {
                            try{
                                throw new IllegalArgumentException("This amount of paper can not be loaded inside this printer!\n" +
                                        "This printer can be filled with: "+pr.getFreeSlotsForPaper()+" papers not: "+numberOfPapers);
                            }catch (IllegalArgumentException e){
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }

        if(!hasThisPrinter || !hasThisPaper || !hasThisNumberOfPapers){
            try {
                //System.out.println(hasThisPrinter);
                //System.out.println(hasThisPaper);
                //System.out.println(hasThisNumberOfPapers);
                throw new IllegalArgumentException("The printHouse may not have enough of this paper type or this printer ID!");
            }catch (IllegalArgumentException e){
                e.printStackTrace();
            }
        }
    }

    public void sendDemandToPrinter(String nameOfPublicationInInvoice,int idOfPrinter, boolean colored){
        boolean isInsidePrinter = false;
        boolean isInsideInvoice = false;
        boolean isPaid = false;
        boolean isRunning = true;
        //check is this id is a valid id of printer if it is not found this method throws exception
        for(Printer pr: listOfPrinters){
            if(pr.getCurrent_id()==idOfPrinter){
                isInsidePrinter= true;
                //check is this nameOfPublicationInInvoice is a valid name and a coresponding invoice is found else throws exception
                for(Invoice inv: invoices){
                    if(inv.getPublication().getTitleOfPublication().equals(nameOfPublicationInInvoice)){
                        isInsideInvoice = true;
                        if(inv.getState().equals(InvoiceState.PAID)){
                            isPaid = true;
                            if(!pr.isRunning()){
                                isRunning = false;
                                pr.receiveDemand(inv,colored);
                            }
                        }
                    }
                }
            }
        }

        try{
            if(!isInsidePrinter){
                throw new IllegalArgumentException("This id of printer is not found in this Printhouse!");
            }
            if(!isInsideInvoice){
                throw new IllegalArgumentException("This name of publication in invoice is not found in this Printhouse!");
            }
            if(!isPaid){
                throw new IllegalArgumentException("This invoice is not paid!");
            }
            if(isRunning){
                throw new PrinterIsRunningException("We can not make a new task while the privious is not finished!");
            }
        }catch (IllegalArgumentException | PrinterIsRunningException e){
            e.printStackTrace();
        }
    }

    public void startPrinter(int id){
        boolean isInside = false;
        try{
            for(Printer pr: listOfPrinters){
                if (pr.getCurrent_id() == id) {
                    isInside = true;
                    if(!pr.isRunning()){
                        pr.startPrinting();
                    }else {
                        throw new PrinterIsRunningException("This printer is currently running so it can not start again before ending its proccess!");
                    }
                    break;
                }
            }
            if(!isInside){
                throw new IllegalArgumentException("This printer id: " + id + " is not a valid one!");
            }
        }catch (PrinterIsRunningException | IllegalArgumentException e){
            e.printStackTrace();
        }
    }


    public ArrayList<Printer> getListOfPrinters() {
        return listOfPrinters;
    }

    public void getPrintedDemandFromPrinter(int id){
        for(Printer pr: listOfPrinters){
            if(pr.getCurrent_id()==id){
                if(pr.getPrintedPublication()!=null){
                    this.getPrintedInvoice(pr.getPrintedPublication());
                    //Clear the invoice from the printer
                    pr.clearPrinterInvoice();
                }
            }
        }
    }

    private void getPrintedInvoice(Invoice inv){
        for(Invoice invoice: invoices){
            if(invoice.getPublication().getTitleOfPublication().equals(inv.getPublication().getTitleOfPublication())){
                if(invoice.getPrintHouseName().equals(inv.getPrintHouseName())){
                    if(invoice.getPublication().getClientName().equals(inv.getPublication().getClientName())){
                        //Changes the state of the Publication inside the invoice
                        invoice.getPublication().setState(PublicationState.PRINTED);

                        //Removes the publication whom the invoice was created
                        queue.removeIf(php -> php.getClientName().equals(inv.getPublication().getClientName()) && php.getTitleOfPublication().equals(inv.getPublication().getTitleOfPublication()));
                    }
                }
            }
        }
    }

    public void printAvailablePaperInPrinter(int id){
        if(id>0){
            for(Printer pr: listOfPrinters){
                if(pr.getCurrent_id()==id){
                    System.out.println(pr.getListOfAvailablePaper().size());
                }
            }
        }else {
            try {
                throw new IllegalNegativeArgumentException("Id of printer is always a possitive value!");
            } catch (IllegalNegativeArgumentException e) {
                e.printStackTrace();
            }
        }
    }

    public ArrayList<PrintHousePublication> getQueue() {
        return queue;
    }

    @Override
    public String toString() {
        return "PrintHouse{" +
                "name='" + name + '\'' +
                ", hiredWorkersList=" + hiredWorkersList +
                ", paperInStock=" + paperInStock.size() +
                ", baseSalary=" + baseSalary +
                ", bonusPercent=" + bonusPercent +
                ", minGrossIncomeForBonus=" + minGrossIncomeForBonus +
                ", grossIncome=" + grossIncome +
                ", grossExpenses=" + grossExpenses +
                ", total=" + total +
                "\ninvoices: "+ invoices+
                "\nqueue: "+queue+
                '}';
    }

    @Override
    public String getImportantInfo() {
        return "\nPrintHouse{" +
                " Name='" + name+
                ", BaseSalary=" + baseSalary +
                ", paperInStock=" + paperInStock.size() +
                ", baseSalary=" + baseSalary +
                ", bonusPercent=" + bonusPercent +
                ", minGrossIncomeForBonus=" + minGrossIncomeForBonus +
                ", grossIncome=" + grossIncome +
                ", grossExpenses=" + grossExpenses +
                "\nHired workers: "+ this.getHiredWorkersList()+
                "\nPrinters :\n"+ this.getListOfPrinters()+
                "\nQueue:\n"+ this.getQueue()+
                "\nInvoices:\n"+this.invoices+
                "}";
    }
}
