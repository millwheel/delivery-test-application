package msa.customer.exception;

public class FoodKindNullException extends NullPointerException{

    private static final String WRONG_FOODKIND_MESSAGE = "Wrong food Kind. %s is not in the list.";

    public FoodKindNullException(String foodKind) {
        super(String.format(WRONG_FOODKIND_MESSAGE, foodKind));
    }
}
