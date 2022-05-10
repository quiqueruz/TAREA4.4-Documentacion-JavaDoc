package gestisimal.tests;

import java.util.Scanner;
import gestisimal.classes.Warehouse;
import gestisimal.exceptions.ArticleIllegalArgumentException;
import gestisimal.exceptions.ArticleStockException;
import gestisimal.exceptions.WarehouseArticleNotExistsException;
import gestisimal.exceptions.WarehouseArticleRepeatedException;
import gestisimal.util.Menu;
import gestisimal.classes.Article;

/**
 * Programa para probar la clase Warehouse.
 * 
 * @author Rafael del Castillo Gomariz
 *
 */

public class TestWarehouse {
  static Scanner sc = new Scanner(System.in);
  private static Warehouse warehouse = new Warehouse();

  public static void main(String[] args) {
    Menu menu = createMenu();    
    fillWarehouse();  // valores de prueba

    int option;
    do {
      option = menu.choose();
      switch (option) {
        case 1 -> showWarehouse();
        case 2 -> addArticle();
        case 3 -> removeArticle();
        case 4 -> modifyArticle();
        case 5 -> increaseStock();
        case 6 -> decreaseStock();
      }
    } while (option != menu.lastOption());

    System.out.println("¡Hasta la próxima! ;-)");
  }
  
  private static Menu createMenu() {
    return new Menu("\nMenú de opciones",
        "Listado", "Alta de artículo", "Baja de artículo", "Modificación de artículo",
        "Entrada de mercancía", "Salida de mercancía", "Terminar");
  }

  private static void fillWarehouse() {
    for (int i = 1; i <= 5; i++) {
      try {
        warehouse.addArticle("Artículo" + i, "Marca" + i, randomInt(1, 100), 
            randomInt(10, 100), randomInt(50, 500));
      } 
      catch (WarehouseArticleRepeatedException e) {
        e.printStackTrace();
      }
    }
  }



  private static void showWarehouse() {
    System.out.println(warehouse);
  }

  private static void addArticle() {
    try {
      warehouse.addArticle(readStr("Nombre de artículo a dar de alta"), readStr("Marca"), 
         readDouble("Precio compra"), readDouble("Precio venta"), readInt("Unidades"),
          readInt("Stock de seguridad"), readInt("Stock máximo"));
    } 
    catch (WarehouseArticleRepeatedException e) {
      System.err.println("ERROR: El artículo que has intentado añadir ya existe en el almacén.");
    } 
    catch (ArticleIllegalArgumentException e) {
      System.err.println("ERROR: " + e.getMessage());
    }
  }





  private static void removeArticle() {
    try {
      warehouse.deleteArticle(readInt("Código de artículo a dar de baja"));
    } 
    catch (WarehouseArticleNotExistsException e) { 
      printCodeError();
    } 
  }

  private static void modifyArticle() {
    try {
Article article = warehouse.returnArticle(readInt("Código de artículo a modificar"));
      System.out.println(article);
      warehouse.modifyArticle(article.getCode(), readStr("Nombre"), readStr("Marca"),
          readDouble("Precio compra"), readDouble("Precio venta"),readInt("Unidades"), 
          readInt("Stock de seguridad"), readInt("Stock máximo")); 
    } 
    catch (WarehouseArticleNotExistsException e) {
      printCodeError();
    } 
    catch (ArticleIllegalArgumentException e) {
      System.err.println("ERROR: " + e.getMessage());
    }
  }

  private static void increaseStock() {
    try {
      warehouse.incrementUnitsOfArticle(readInt("Código del artículo a incrementar stock"), 
          readInt("Unidades"));
    } 
    catch (WarehouseArticleNotExistsException e) {
      printCodeError();
    }
    catch (ArticleIllegalArgumentException e) {
      System.err.println("ERROR: " + e.getMessage());
    }
  }

  private static void decreaseStock() {
    try {
      warehouse.decreaseUnitsOfArticle(readInt("Código de artículo de decrementar stock"), 
          readInt("Unidades"));
    } 
    catch (WarehouseArticleNotExistsException e) {
      printCodeError();
    } catch (ArticleStockException e) {
      System.err.println("ERROR: No hay suficientes unidades en el almacén.");
    }
    catch (ArticleIllegalArgumentException e) {
      System.err.println("ERROR: " + e.getMessage());
    }
  }
  
  private static void printCodeError() {
    System.err.println("ERROR: Ese código no corresponde a ningún artículo.");
  }
  
  private static int readInt(String msg) {
    System.out.println(msg);
    int num = sc.nextInt();
    sc.nextLine();
    return num;
  }
  private static int randomInt(int max, int min) {
    return (int) ((Math.random() * (max - min)) + min);
  }
  
  private static String readStr(String msg) {
    System.out.println(msg);
    String msg2 = sc.nextLine();
    return msg2;
  }
  private static double readDouble(String msg) {
    System.out.println(msg);
    double msg2= sc.nextDouble();
    sc.nextLine();
    return msg2;
  }
}
