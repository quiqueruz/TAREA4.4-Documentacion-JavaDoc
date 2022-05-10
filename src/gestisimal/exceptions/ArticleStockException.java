package gestisimal.exceptions;

public class ArticleStockException extends Exception {

  private static final long serialVersionUID = 1L;

  public ArticleStockException(String msg) {
    super(msg);
    System.err.println(msg);
  }
}
