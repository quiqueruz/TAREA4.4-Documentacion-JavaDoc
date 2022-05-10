package gestisimal.exceptions;

public class ArticleIllegalArgumentException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public ArticleIllegalArgumentException(String msg) {
    super(msg);
  }
}
