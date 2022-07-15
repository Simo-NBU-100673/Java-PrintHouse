package Invoice;

import PrintHouse.PrintHouse;
import Publication.PrintHousePublication;

import java.math.BigDecimal;

public class Invoice {
    static int current_number = 1;
    private final int id;
    private final PrintHousePublication publication;
    private InvoiceState state;
    private final String printHouseName;

    public Invoice(PrintHousePublication publication, String printHouseName) {
        this.id = Invoice.current_number;
        this.publication = publication;
        this.state = InvoiceState.NOT_PAID;
        this.printHouseName = printHouseName;
        Invoice.current_number++;
    }

    public BigDecimal getPriceToPay(){
        return publication.getPriceForAllUnitsOfPublication();
    }

    public int getId() {
        return id;
    }

    public PrintHousePublication getPublication() {
        return publication;
    }

    public InvoiceState getState() {
        return state;
    }

    public String getPrintHouseName() {
        return printHouseName;
    }

    public void setState(InvoiceState state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "\n\nInvoice{" +
                ", id=" + "["+id + "]"+
                ", state=" + state +
                ", printHouseName='" + printHouseName + '\'' +
                "\npublication=" + publication +
                "}";
    }
}
