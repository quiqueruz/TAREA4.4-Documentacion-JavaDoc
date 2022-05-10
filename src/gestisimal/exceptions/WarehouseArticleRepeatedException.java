package gestisimal.exceptions;

public class WarehouseArticleRepeatedException extends Exception {

  private static final long serialVersionUID = 1L;

  public WarehouseArticleRepeatedException(String msg) {
    super(msg);
    System.err.println(msg);
  }
}
