package gestisimal.exceptions;

public class WarehouseArticleNotExistsException extends Exception {

  private static final long serialVersionUID = 1L;

  public WarehouseArticleNotExistsException(String msg) {
    super(msg);
    System.err.println(msg);
  }
}
