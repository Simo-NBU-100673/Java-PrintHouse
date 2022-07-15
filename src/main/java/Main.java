import Client.Client;
import DataSaver.DataSaver;
import Paper.Paper;
import Paper.SizeOfPaper;
import Paper.TypeOfPaper;
import PrintHouse.PrintHouse;
import Printer.Printer;
import Publication.ClientDemand;
import Publication.PrintHousePublication;
import Workers.PositionType;
import Workers.Worker;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Main {

    //create a function that can get one parameter which is every object that implements interface Comparable
    public static <T extends DataSaver> void saveData(T obj){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        try(FileWriter fout = new FileWriter(new File("src/main/resources/Files/Data.txt"),true);){
            if(obj!=null){
                fout.append(obj.getImportantInfo()).append(System.lineSeparator());
                fout.append("<|===============================================Last modified:(").append(dtf.format(now)).append(")===============================================|> \n");
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        //test for Paper and its usages
        Paper type1 = new Paper(SizeOfPaper.A1,TypeOfPaper.GLOSSY);
        System.out.println("Price of Paper:\n"+ type1);
        System.out.println(type1.getPricePerPiece());

        //test for Enum usage and how Paper adapt
        TypeOfPaper.GLOSSY.setPrice(BigDecimal.valueOf(.50));
        System.out.println("Price of Paper after we change the default price of the Glossy property of paper to be priced 0.50:\n"+ type1);
        System.out.println(type1.getPricePerPiece());

        //Makes an instance for the two workers
        Worker pesho = new Worker("Pesho", PositionType.OPERATOR);
        Worker gosho = new Worker("Gosho",PositionType.MANAGER);

        //create a PrintHouse
        PrintHouse NY_times = new PrintHouse("NY_times",BigDecimal.valueOf(1200),BigDecimal.valueOf(5000));

        //hire workers in NY_times (here the salaries are assigned for the first time)
        NY_times.hireWorker(pesho);
        NY_times.hireWorker(gosho);


        //Before promotion
        System.out.println("Before promotion: \n"+NY_times);

        //Promoting
        NY_times.promote(pesho);

        //After promotion
        System.out.println("\nAfter promotion: \n"+NY_times);

        //The sum of all workers salaries
        System.out.println("\nSum of all expenses for workers: "+NY_times.workersSalaryExpenses());

        //Creating a new worker
        Worker gosho2 = new Worker("gosho",PositionType.MANAGER);

        //This will throw an exception because gosho2 is not hired so it can not be promoted
        //NY_times.promote(gosho2);

        //Fire a worker with instance gosho
        NY_times.fireWorker(gosho);
        System.out.println("\nAfter gosho was fired: \n"+NY_times);
        System.out.println("\nSum of all expenses for workers: "+NY_times.workersSalaryExpenses());
        System.out.println("\nWithout gosho: \n"+NY_times);

        //Fire a worker that is already fired
        //NY_times.fireWorker(gosho);


        NY_times.addIncome(BigDecimal.valueOf(5001));
        System.out.println("\nNow we added 5001 to our income which will result with the bonus to be given to the workers\n"+NY_times);
        System.out.println("\nSum of all expenses for workers: "+NY_times.workersSalaryExpenses());

        System.out.println("\nPrice per piece of the bought paper"+type1.getPricePerPiece());
        NY_times.buyPaper(type1,10502);
        System.out.println("\nNY_times after we bought 10502 papers: \n"+NY_times);

        NY_times.hireWorker(gosho2);
        System.out.println("\nSum of all expenses for workers after new worker is hired as manager: "+NY_times.workersSalaryExpenses());

        Worker misho = new Worker("misho",PositionType.OPERATOR);

        NY_times.hireWorker(misho);
        System.out.println("\nSum of all expenses for workers after new worker is hired as operator: "+NY_times.workersSalaryExpenses());

        //This will cause an exception because you can not hire a worker that is already working for the company
        //NY_times.hireWorker(misho);

        System.out.print("\nAll expenses are: "+NY_times.getAllExpenses());
        System.out.println("\n"+NY_times);

        //creating the paper for the newspaper
        Paper newspaperPaper = new Paper(SizeOfPaper.A4,TypeOfPaper.NEWSPAPER_PAPER);

        //Create a clientDemand
        ClientDemand test1 = new ClientDemand("Novedes","24 chasa",15,newspaperPaper);
        System.out.println("\n"+test1);

        //Create a Publication from Client demand
        PrintHousePublication test2 = new PrintHousePublication(test1);
        System.out.println(test2);

        //Create a Publication from Client demand with added markup
        PrintHousePublication test3 = new PrintHousePublication(test1, BigDecimal.valueOf(140.9));
        System.out.println(test3);

        //-=======================================================================================-
        //TESTS

//        //Creating a PrintHouse with name, baseSalary for workers and minGrossIncome for bonuses
//        PrintHouse sofarma = new PrintHouse("Sofarma",BigDecimal.valueOf(1200),BigDecimal.valueOf(1600));
//
//        //The client is different object that will make a correspondence with the PrintHouse
//        //We can create a client with just setting its name (Default portfolio is ZERO)
//        Client zeroClient = new Client("ZeroClient");
//        System.out.println(zeroClient);
//
//        //Clients must have a name and money in their portfolio
//        Client novedes = new Client("Novedes", BigDecimal.valueOf(150000));
//        Client bebelan = new Client("Bebelan", BigDecimal.valueOf(150000));
//        System.out.println(novedes);
//
//        //! (1) A demand is created by the current Client because the demand is theirs
//        //The demand is stored in list which is in possession of current Client
//        novedes.createDemand("Aspirin",150,SizeOfPaper.A4,TypeOfPaper.NORMAL);
//        novedes.createDemand("Analgin",150,SizeOfPaper.A4,TypeOfPaper.NORMAL);
//        bebelan.createDemand("Pampersi",670,SizeOfPaper.A5,TypeOfPaper.GLOSSY);
//        System.out.println(bebelan);
//        System.out.println(novedes);
//
//        //This will throw an exception because a ClientDemand with the same name can not be duplicated
//        //If the user want to make a change in this publication, he must change the existing publication or
//        //remove the current with that name and create a new one
//        //novedes.createDemand("Analgin",150,SizeOfPaper.A4,TypeOfPaper.NORMAL);
//
//        //This function adds the new info to Data.txt file(It is static and generic(ONLY CLASSES THAT IMPLEMENT DataSaver can be passed to this function!!!))
//        Main.saveData(novedes);
//
//        //! Here the correspondence begins!!!
//        //The demand is sent to a PrintHouse to be processed!
//        //CONSTRAINS: the name of the demand that is sent must be the same name that was used when creating the demand
//        //if we try to send a demand with name that is not a name of the current client demands this will cause and exception
//        //! (2) Sending the demand will invoke the receiveDemandsFromClient() method of PrintHouse which will transform the ClientDemand to PrintHousePublication
//        //and will set the markup value to ZERO.
//        System.out.println("\n"+sofarma);
//        novedes.sendDemand(sofarma, "Aspirin");
//
//        //This will cause an exception in PrintHouse because this demand is already gotten from the PrintHouse from this Client
//        //novedes.sendDemand(sofarma, "Aspirin");
//
//        //This will cause an exception in Client because a demand with this name from this client has not been created
//        //novedes.sendDemand(sofarma, "Aspirinan");
//        //System.out.println("\n"+sofarma);
//
//        //We added some demands from two different clients
//        novedes.sendDemand(sofarma, "Analgin");
//        bebelan.sendDemand(sofarma,"Pampersi");
//        System.out.println("\n"+sofarma);
//
//        //Because the receiveDemandsFromClient() method will create a Publication with ZERO markup
//        //we can assign it ourselves with setMarkupOfPublication() method that takes parameters:
//        //(1)-> Name of the Client from which is created the demand
//        //(2)-> Title of the Publication(Demand)
//        //(3)-> markupPercent
//        //CONSTRAINS:
//        //if there is not a Publication with this name of client and title of demand this will cause an exception
//        //sofarma.setMarkupOfPublication("Noved","Aspirin",BigDecimal.valueOf(20));
//        //System.out.println("\n"+sofarma);
//
//        //sofarma.setMarkupOfPublication("Novedes","Aspiin",BigDecimal.valueOf(15));
//        //System.out.println("\n"+sofarma);
//
//        //if the markup value is a negative value this will cause an exception
//        //sofarma.setMarkupOfPublication("Novedes","Aspirin",BigDecimal.valueOf(-1));
//        //System.out.println("\n"+sofarma);
//
//        //This is the correct way to change the markup of this demand!
//        sofarma.setMarkupOfPublication("Novedes","Aspirin",BigDecimal.valueOf(10000));
//        sofarma.setMarkupOfPublication("Bebelan","Pampersi",BigDecimal.valueOf(1500));
//        //System.out.println("\n"+sofarma);
//        sofarma.printQueue();
//
//        //! (3) The PrintHouse must accept the client demand to continue to the printing phase
//        // Method acceptDemandsFromClient() gets two parameters ClientName and demandName
//        // If acceptDemandsFromClient() methods accepts these parameters successfully it will create invoice and store it in list of invoices of the PrintHouse
//
//        //This will cause exception because there is no demand send from this client with this name and an invoice will not be created!
//        //sofarma.acceptDemandsFromClient("Novedes","Aspin");
//        //sofarma.acceptDemandsFromClient("Nove","Aspirin");
//        //System.out.println("\n"+sofarma);
//
//        //This invoke will create invoices for these demands!
//        //Every invoice has its own unique ID
//        sofarma.acceptDemandsFromClient("Novedes","Aspirin");
//
//        //This second attempt to accept an invoice that has been already accepted will throw and exception
//        //sofarma.acceptDemandsFromClient("Novedes","Aspirin");
//
//        sofarma.acceptDemandsFromClient("Novedes","Analgin");
//        sofarma.acceptDemandsFromClient("Bebelan","Pampersi");
//        //System.out.println("\n"+sofarma);
//        //Here is sofarma.printQueue() you can see that the demands that are accepted will change their state from default to accepted
//        sofarma.printQueue();
//        sofarma.printInvoices();
//
//        //! (4) Sending all invoices that are with recipient the current Client and title as the passed parameter
//        //This method invokes receiveInvoice() method of the client and adds Invoices that are uni`ue in list with invoices
//        sofarma.sendInvoiceToClient(novedes, "Aspirin");
//
//        //This will cause an exception because we can not send Invoice which is sent already
//        //sofarma.sendInvoiceToClient(novedes, "Aspirin");
//
//        //This will cause an exception because we can not send Invoice which is not created
//        //sofarma.sendInvoiceToClient(novedes, "Aspiri");
//
//        sofarma.sendInvoiceToClient(novedes, "Analgin");
//        sofarma.sendInvoiceToClient(bebelan,"Pampersi");
//
//        //This prints will show the invoices that are sent to the current client
//        novedes.printInvoices();
//        bebelan.printInvoices();
//
//        //! (5) Paying for invoices!
//        //This method transfers the money from client's portfolio to the client's grossIncome
//        //If the client has insufficient amount of money to pay the current invoice this method will throw exception
//        //Removes the demand from the list of demands in Client
//        //Removes the publication from queue of PrintHouse
//        System.out.println("\n\n\n===================================BEFORE==========================================");
//        System.out.println(sofarma);//To see the variable grossIncome before paying
//        System.out.println("\n"+novedes);//To see the portfolio of the client
//        novedes.payInvoice("Aspirin",sofarma);
//        novedes.payInvoice("Analgin",sofarma);
//        bebelan.payInvoice("Pampersi",sofarma);
//        System.out.println("\n\n\n===================================AFTER==========================================");
//        System.out.println(sofarma);//To see the variable grossIncome before paying
//        System.out.println("\n"+novedes);//To see the portfolio of the client
//
//        //This will throw an exception because after the invoice is paid the client will remove its demand
//        //novedes.payInvoice("Aspirin",sofarma);
//
//        //Shows the current expenses of PrintHouse
//        System.out.println("\nExpenses: "+ sofarma.getAllExpenses());
//
//        Worker manager = new Worker("Simo", PositionType.MANAGER);
//        sofarma.hireWorker(manager);
//
//        System.out.println("Expenses: "+ sofarma.getAllExpenses());
//        System.out.println(sofarma);
//
//        Paper test1 = new Paper(SizeOfPaper.A4,TypeOfPaper.NORMAL);
//        Paper test2 = new Paper(SizeOfPaper.A2,TypeOfPaper.GLOSSY);
//
//        //! (6) Buy paper for printers
//        //This function generates paper and stores it inside list of paper in stock
//        //when you buy paper the price is added to gross expenses of the PrintHouse
//        sofarma.buyPaper(test1,16000);
//
//        System.out.println("Expenses: "+ sofarma.getAllExpenses());
//        System.out.println(sofarma);
//
//        //(r) This function saves data in Data.txt
//        Main.saveData(novedes);
//        Main.saveData(bebelan);
//        Main.saveData(sofarma);
//
//        //! (7) Buying Printers
//        //This method gets two parameters: maxPaper that a printer can store and paper per equal amount of time (printing speed)
//        //This method will store the printers in list of printers in PrintHouse
//
//        //Both the speed and max paper must be n>0
//        //sofarma.addPrinter(1000,-1);
//        //sofarma.addPrinter(-15,1);
//
//        sofarma.addPrinter(1000,1);
//        sofarma.addPrinter(1000,2);
//        sofarma.addPrinter(1000,5);
//
//        //This will print the available printers
//        System.out.println("\n\n"+sofarma.getListOfPrinters());
//
//
//        //! (8) Load paper in Printer
//        //This function loads paper in printers
//        //If the amount of paper that we want to load exceeds the max paper that a printer can handle this will throw exception
//        //sofarma.loadPaperInPrinter(1,test1,2000);
//
//        //If the amount of paper that we want to load in the printer is greater than the paper in stock of that type this will throw an exception
//        //sofarma.loadPaperInPrinter(1,test1,16001);
//
//        //If the type of paper that we want is not bought this will throw an exception
//        //sofarma.loadPaperInPrinter(1,test2,100);
//
//        //If the ID of the printer is not a ID of a printer that this PrintHouse has this will throw exception
//        //sofarma.loadPaperInPrinter(5,test1,100);
//
//        //This will be OK
//        sofarma.loadPaperInPrinter(1,test1,200);
//        sofarma.loadPaperInPrinter(2,test1,400);
//        sofarma.loadPaperInPrinter(3,test1,500);
//
//        //Prints the list of printers
//        System.out.println(sofarma.getListOfPrinters());
//        sofarma.printAvailablePaperInPrinter(1);
//        sofarma.printAvailablePaperInPrinter(2);
//        sofarma.printAvailablePaperInPrinter(3);
//
//        //This will throw exception because the id is from 1 to ...
//        //sofarma.printAvailablePaperInPrinter(0);
//
//        //! (9) Send to printers what to print!
//        //The printer must know what to print so that is why we send the whole invoice and from it the printer gets the publication info
//        //This function will throw exception if:
//        //1.If id is a valid id of printer if it is not found this method throws exception
//        //sofarma.sendDemandToPrinter("Aspirin",5,true);
//
//        //2.If this nameOfPublicationInInvoice is a valid name and a corresponding invoice is found else throws exception
//        //sofarma.sendDemandToPrinter("Aspi",1,true);
//
//        //3.This invoice is not paid! // TO TEST THIS COMMENT line 251
//        //sofarma.sendDemandToPrinter("Pampersi",1,true);
//
//        //This will be OK
//        sofarma.sendDemandToPrinter("Aspirin",1,true);
//        sofarma.sendDemandToPrinter("Aspirin",2,true);
//        sofarma.sendDemandToPrinter("Aspirin",3,true);
//
//        //Now in field loaded will be true(THIS FIELD CORRESPONDS TO IS_INVOICE_LOADED)
//        System.out.println(sofarma.getListOfPrinters());
//
//        //! (10) Start printing!
//        //This method invokev the mothod for starting the multithreading and printing of the publications that are paid via invoices
//        //This method will create a thread for every printer with current ID == id that is passed to the method
//        //After the thread starts it check if the paper that the printer has is the one that this publication needs and if it does its prints it
//        //(removes it from the list of available papers!)
//
//        //This will throw an exception because the paper for the demand in the invoice in this printer is not loaded or when printer prints but the paper in the printer is over
//        //TO CHECK THIS CHANGE 343 line nameOfPublicationInInvoice: Pampersi//
//        //sofarma.startPrinter(3);
//
//        //The method of PrintHouse -> startPrinter() gets 1 parameter which is the id of the printer we want to start and if it is
//        //not in the presenrt ID's of printers in PrintHouse throws exceptiopn
//        //sofarma.startPrinter(0);
//
//        //To see the problem of not having enough paper to continue CHANGE LINE 315 numberOfPapers:140
//
//        //This will be OK
//        sofarma.startPrinter(1);
//        sofarma.startPrinter(2);
//        sofarma.startPrinter(3);
//
//
//        //! (11) Get the printed Publications
//        //Here we make the Main thread slower to wait the other threads to stop
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        //This function retrieves the publication that was printed from the printer
//        //Sets the state of the Invoice of the client to Printed
//        //Sets the state of the Invoice of the PrintHouse to Printed
//        //The slot for invoice to be printed is resetted to null
//        sofarma.getPrintedDemandFromPrinter(1);
//
//        System.out.println("\nPrinting the available paper in printer ID(1): ");
//        sofarma.printAvailablePaperInPrinter(1);
//        System.out.println(sofarma.getImportantInfo());
//        System.out.println(novedes.getImportantInfo());
//
//        //Saves data
//        Main.saveData(sofarma);
//        Main.saveData(novedes);
//
//        //This function returns the gross Income - gross Expenses
//        System.out.println(sofarma.getTotal());
    }
}
