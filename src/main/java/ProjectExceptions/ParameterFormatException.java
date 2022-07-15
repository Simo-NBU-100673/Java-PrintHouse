package ProjectExceptions;

public class ParameterFormatException extends Exception {
    private final String message;
    private final String constMessage;
    public ParameterFormatException(String message) {
        this.message = message;
        this.constMessage = "\nThe format of the parameter \"markup\" must be in format [integer].[integer]% or [integer]% but got: "+this.message+
                "\nThe markup is set to \"0\" if you want to change the markup use \"Publication.getSimpleName()\".setMarkup()";
    }

    @Override
    public String getMessage() {
        return constMessage+"\n"+super.getMessage();
    }

    @Override
    public void printStackTrace() {
        System.out.println(constMessage);
        super.printStackTrace();
    }
}
