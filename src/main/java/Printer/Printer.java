package Printer;

import Invoice.Invoice;
import Paper.*;
import ProjectExceptions.InsufficientPaperException;
import Publication.PrintHousePublication;
import Publication.PublicationState;

import javax.naming.InsufficientResourcesException;
import java.math.BigDecimal;
import java.util.ArrayList;

public class Printer implements Runnable{
    private int maxPaper;
    private int speedOfPrinting;

    //This is default so that we can not make the printer to print something else before it's done printing
    private boolean isRunning = false;
    private boolean  isColored = false;
    private ArrayList<Paper> listOfAvailablePaper;
    private Invoice invoiceToBePrinted= null;
    private int current_id;
    static int id=0;

    public Printer(int maxPaper, int speedOfPrinting) {
        this.maxPaper = maxPaper;
        this.setSpeedOfPrinting(speedOfPrinting);
        listOfAvailablePaper = new ArrayList<>();
        current_id = ++id;
    }

    //This function is invoked by a function in the PrintHouse to fill the printer with paper
    public void loadPaper_Printer(Paper paper, int numberOfPapers){
        //no need to check for duplicates or compatibility because this was done in PrintHouse
        //Before invoking this function
        for(int i=0;i<numberOfPapers;i++){
            Paper current = new Paper(paper.getSizeOfPaper(),paper.getTypeOfPaper());
            listOfAvailablePaper.add(current);
        }
    }

    public void receiveDemand(Invoice invoice, boolean color){
        if(invoiceToBePrinted == null){
            invoiceToBePrinted = invoice;
            this.isColored = color;
        }
    }

    @Override
    public void run() {
        int counter = 1;
        //This will prevent the printer to be loaded with new Invoice before this is printed completely
        isRunning = true;
        int unitsToPrint = invoiceToBePrinted.getPublication().getNumberOfUnits();
        TypeOfPaper typeOfPaper  = invoiceToBePrinted.getPublication().getPaper().getTypeOfPaper();
        SizeOfPaper sizeOfPaper = invoiceToBePrinted.getPublication().getPaper().getSizeOfPaper();

        //TODO THIS WORKS (MUST HANDLE MORE PRINTED)
        while (0<unitsToPrint){
            boolean havePaper = false;
            for(int j=0;j<speedOfPrinting;j++){
                //There is paper of this type!
                for(int i =0;i<listOfAvailablePaper.size();i++){
                    //remove one of it
                    if(listOfAvailablePaper.get(i).getSizeOfPaper().equals(sizeOfPaper)&&listOfAvailablePaper.get(i).getTypeOfPaper().equals(typeOfPaper)){
                        havePaper = true;
                        //This removes paper from list with paper in stock
                        listOfAvailablePaper.remove(listOfAvailablePaper.get(i));
                        break;
                    }
                }
            }

            unitsToPrint-=speedOfPrinting;

            System.out.println("\n\n["+Thread.currentThread().getName()+"] Printed :" +
                    "\nName: "+ invoiceToBePrinted.getPublication().getTitleOfPublication()+
                    "\nPaper: "+invoiceToBePrinted.getPublication().getPaper()+
                    "\nUnits to be printed: "+unitsToPrint);
            counter++;

            //If there is no more paper but the printer did not printed all of the units for this demand
            if (!havePaper && unitsToPrint>0){
                try {
                    throw new InsufficientPaperException("You need to fill this printer with more paper!");
                } catch (InsufficientPaperException e) {
                    e.printStackTrace();
                    System.err.println("Printer ["+this.getCurrent_id()+"] does not have enough paper please load:\n" +
                            "Type of paper= "+invoiceToBePrinted.getPublication().getPaper()+
                            "\nNumber of units= "+unitsToPrint);
                    break;
                }
            }
        }

        if(0==unitsToPrint){
            invoiceToBePrinted.getPublication().setState(PublicationState.PRINTED);
            isRunning = false;
        }else if(0>unitsToPrint){
            invoiceToBePrinted.getPublication().setState(PublicationState.PRINTED);
            isRunning = false;
            System.out.println("The printer printed more publication than needed!");
            System.out.println("PrintHouse must return: "+invoiceToBePrinted.getPublication().getPricePerUnit().multiply(BigDecimal.valueOf(Math.abs(unitsToPrint))));
        }
    }

    public void startPrinting(){
        Thread tread = new Thread(this,"Printer "+this.getCurrent_id());
        tread.start();
    }

    public int getCurrent_id() {
        return current_id;
    }

    public static void setId(int id) {
        Printer.id = id;
    }

    public static int getId() {
        return id;
    }

    public int getMaxPaper() {
        return maxPaper;
    }

    public ArrayList<Paper> getListOfAvailablePaper() {
        return listOfAvailablePaper;
    }

    public int getFreeSlotsForPaper(){
        return this.maxPaper-this.listOfAvailablePaper.size();
    }

    public boolean loadedInvoices(){
        return invoiceToBePrinted != null;
    }

    public void setSpeedOfPrinting(int speedOfPrinting) {
        if(speedOfPrinting>0){
            this.speedOfPrinting = speedOfPrinting;
        }else {
            try{
                throw new IllegalArgumentException("The printing speed can not be 0!\nCurrently the speed is not changed!");
            }catch (IllegalArgumentException e){
                e.printStackTrace();
            }
        }
    }

    public boolean isRunning() {
        return isRunning;
    }

    public Invoice getPrintedPublication(){
        if(invoiceToBePrinted.getPublication().getState().equals(PublicationState.PRINTED)){
            return invoiceToBePrinted;
        }else {
            return null;
        }
    }
    public void clearPrinterInvoice(){
        if(invoiceToBePrinted!=null){
            invoiceToBePrinted=null;
        }
    }

    @Override
    public String toString() {
        return "Printer{" +
                "maxPaper=" + maxPaper +
                ", speedOfPrinting=" + speedOfPrinting +
                ", current_id=" + current_id +
                ", paperInPrinter: "+listOfAvailablePaper.size()+
                ", loadedInvoices: "+this.loadedInvoices()+
                '}';
    }
}
