package gestisimal.classes;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import gestisimal.exceptions.WarehouseArticleRepeatedException;
import gestisimal.exceptions.ArticleStockException;
import gestisimal.exceptions.WarehouseArticleNotExistsException;


/**
 * Clase Almacén que realice el alta, baja, modificación, entrada de mercancía (incrementa unidades), salida de mercancía (decrementa unidades).
 * El estado será un ArrayList de artículos (pero la clase no será un ArrayList)
 * Su comportamiento será: añadir artículos (no puede haber dos artículos con el mismo nombre y marca), eliminar artículos, 
 * incrementar las existencias de un artículo (se delega en la clase Artículo),  decrementar las existencias de un artículo 
 * (nunca por debajo de cero, se delega en la clase Artículo), devolver un artículo (para mostrarlo). Para  listar el almacén 
 * podría devolverse una cadena con todos los artículos del almacén (toString)
 * 
 * @author Quique Ruz Del Río
 */
public class Warehouse {
  /**
   * Lista donde se almacenan los articulos a añadir.
   */
  private List<Article> articles;
  /**
   * Crea un nuevo almacen vacio.
   */
  public Warehouse() {
    articles = new ArrayList<Article>();
  }
  
  /**
   * Crea un nuevo almacen que se extrae de un fichero xml.
   * @param route   Ruta del fichero xml donde se guarda el almacen.
   */
  
  public Warehouse(String route) {
    try {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = factory.newDocumentBuilder();
    Document document = builder.parse(new File(route));
    
    document.getDocumentElement().normalize();
    
    NodeList nodes = document.getElementsByTagName("Article");
    
    for(int i = 0; i < nodes.getLength(); i++) {
      // Nodo Articulo
      Node node = nodes.item(i);
      Element article = (Element) node;
      
      // Nodos de cada articulo pasados a variables
      // int code = Integer.parseInt(article.getAttribute("Code"));
      String name = article.getElementsByTagName("Name").item(0).getTextContent();
      String brand = article.getElementsByTagName("Brand").item(0).getTextContent();
      double buyingPrice = Double.parseDouble(article.getElementsByTagName("BuyingPrice").item(0).getTextContent());
      double sellingPrice = Double.parseDouble(article.getElementsByTagName("SellingPrice").item(0).getTextContent());
      int units = Integer.parseInt(article.getElementsByTagName("Units").item(0).getTextContent());
      int securityStock = Integer.parseInt(article.getElementsByTagName("SecurityStock").item(0).getTextContent());
      int maxStock = Integer.parseInt(article.getElementsByTagName("MaxStock").item(0).getTextContent());
      
      articles.add(new Article(name, brand, buyingPrice, sellingPrice, units, securityStock, maxStock));
    }
    
    }catch (ParserConfigurationException e) {
      e.printStackTrace();
    } catch (SAXException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  /**
   * Crea un nuevo almacen a partir de un ArrayList de articulos.
   * @param articles    Array List con los articulos del almacen.
   */
  Warehouse(ArrayList<Article> articles) {
    this.articles = new ArrayList<Article>(articles);
    
  }
  /**
   * Añade un articulo al almacen, el articulo se crea internamente.
   * @param name            Nombre del Articulo a añadir.
   * @param brand           Marca del Articulo a añadir.
   * @param buyingPrice     Precio de compra del Articulo a añadir.
   * @param sellingPrice    Precio de venta del Articulo a añadir.
   * @param units           Unidades del Articulo a añadir.
   * @param securityStock   Stock de seguridad del Articulo a añadir.
   * @param maxStock        Stock Maximo del Articulo a añadir.
   * @throws WarehouseArticleRepeatedException      Si ya existe el articulo
   */
  public void addArticle(String name, String brand, double buyingPrice, double sellingPrice, 
      int units, int securityStock, int maxStock) throws WarehouseArticleRepeatedException{
    Article aux = createArticle(name, brand, buyingPrice, sellingPrice, units, securityStock, maxStock);
    throwExceptionIfArticleToAddExits(aux.getCode());
    articles.add(aux);
  }
  /**
   * Añade un articulo al almacen, el articulo se crea internamente.
   * @param name            Nombre del Articulo a añadir.
   * @param brand           Marca del Articulo a añadir.
   * @param buyingPrice     Precio de compra del Articulo a añadir.
   * @param sellingPrice    Precio de venta del Articulo a añadir.
   * @param units           Unidades del Articulo a añadir.
   * @throws WarehouseArticleRepeatedException      Si ya existe el articulo.
   */
  public void addArticle(String name, String brand, double buyingPrice, double sellingPrice, int units) throws WarehouseArticleRepeatedException{
    addArticle(name, brand, buyingPrice, sellingPrice, units, 0, 0);
  }
  /**
   * Elimina un articulo del almacen.
   * @param code    Codigo del articulo a eliminar.
   * @throws WarehouseArticleNotExistsException     Si no existe el articulo.
   */
  public void deleteArticle(int code) throws WarehouseArticleNotExistsException {
    throwExceptionIfArticleDoesNotExist(code);
    articles.removeIf(art -> art.getCode()==code);
  }
  /**
   * Incrementa las unidades de un articulo 
   * @param code    Codigo del articulo.
   * @param units   Unidades a incrementar.
   * @throws WarehouseArticleNotExistsException     Si no existe el articulo.
   */
  public void incrementUnitsOfArticle(int code, int units) throws WarehouseArticleNotExistsException {
    throwExceptionIfArticleDoesNotExist(code);
    articles.get(code).increaseUnits(units);
  }
  /**
   * Decrementa unidades de un articulo.
   * @param code    Codigo del articulo.
   * @param units   Unidades a decrementar
   * @throws WarehouseArticleNotExistsException     Si no existe el articulo.
   * @throws ArticleStockException                  Si el stock da error.
   */
  public void decreaseUnitsOfArticle(int code, int units) throws WarehouseArticleNotExistsException, ArticleStockException{
    throwExceptionIfArticleDoesNotExist(code);
    throwExceptionIfArticleStockAreLessThanZero(code,units);
    articles.get(code).decreaseUnits(units);
  }
  /**
   * Devuelve el articulo en cuestion.
   * @param code    Codigo del articulo en cuestion.
   * @return        Articulo que tiene el codigo en cuestion.
   * @throws WarehouseArticleNotExistsException     Si no existe el articulo.
   */
  public Article returnArticle(int code) throws WarehouseArticleNotExistsException {
    throwExceptionIfArticleDoesNotExist(code);
    for (Article i : articles) {
      if (i.getCode()==code) {
        return articles.get(code);
      }
    }
    return null;
  }
  
  @Override
  public String toString() {
    return "Warehouse [articles=" + articles + "]";
  }
  /**
   * Comprueba si el articulo a añadir existe.
   * @param code    Codigo del articulo a añadir.
   * @throws WarehouseArticleRepeatedException      Si el articulo existe.
   */
  private void throwExceptionIfArticleToAddExits(int code) throws WarehouseArticleRepeatedException {
    for(Article i : articles) {
      if (i.getCode()==code) {
        throw new WarehouseArticleRepeatedException("El articulo ya existe en el almacen.");
      }
    }
  }
  /**
   * Crea un articulo provisional.
   * @param name            Nombre del Articulo a añadir.
   * @param brand           Marca del Articulo a añadir.
   * @param buyingPrice     Precio de compra del Articulo a añadir.
   * @param sellingPrice    Precio de venta del Articulo a añadir.
   * @param units           Unidades del Articulo a añadir.
   * @param securityStock   Stock de seguridad del Articulo a añadir.
   * @param maxStock        Stock Maximo del Articulo a añadir.
   * @return
   */
  private Article createArticle(String name, String brand, double buyingPrice, double sellingPrice, 
      int units, int securityStock, int maxStock) {
    Article aux = new Article(name, brand, buyingPrice, sellingPrice, units, securityStock, maxStock);
    return aux;
  }
  /**
   * Modifica el articulo deseado
   * @param code
   * @param newName
   * @param newBrand
   * @param newBuyingPrice
   * @param newSellingPrice
   * @param newUnits
   * @param newSecurityStock
   * @param newMaxStock
   * @throws WarehouseArticleNotExistsException
   */
  public void modifyArticle(int code, String newName, String newBrand, double newBuyingPrice, double newSellingPrice, 
      int newUnits, int newSecurityStock, int newMaxStock) throws WarehouseArticleNotExistsException {
    throwExceptionIfArticleDoesNotExist(code);
    Article aux = articles.get(code);
    aux.setName(newName);
    aux.setBrand(newBrand);
    aux.setBuyingPrice(newBuyingPrice);
    aux.setSellingPrice(newSellingPrice);
    aux.setUnits(newUnits);
    aux.setSecurityStock(newSecurityStock);
    aux.setMaxStock(newMaxStock);
  }
  
  /**
   * Lanza una excepción cuando el artículo no existe
   * 
   * @param code
   * @throws WarehouseArticleNotExistsException
   */
  
  private void throwExceptionIfArticleDoesNotExist(int code) throws WarehouseArticleNotExistsException {
    for (Article i: articles) {
      if(i.getCode()==code) {
        return;
      }
    }
    throw new WarehouseArticleNotExistsException("El articulo no existe");
  }
  
  
  /**
   * Lanza una excepción cuando el stock sea menor a 0
   * 
   * @param code
   * @param units
   * @throws ArticleStockException
   */
  private void throwExceptionIfArticleStockAreLessThanZero(int code, int units) throws ArticleStockException {
    if(articles.get(code).getUnits() - units < 0) {
      throw new ArticleStockException("El stock no puede ser inferior a 0");
    }
  }
  
  /**
   * Guarda el fichero en un XML
   * 
   * @param fileName    Nombre del archivo XML
   */
  
  public void save(String fileName) {
    try {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = factory.newDocumentBuilder();
    Document document = builder.newDocument();
    
    Element root = document.createElement("Warehouse");
    document.appendChild(root);
    
    for (Article art : articles) {
      // Nodo Articulo
      Element elementArt = document.createElement("Article");
      root.appendChild(elementArt);
      
      //Atributo Codigo
      Attr attrCode = document.createAttribute("Code");
      attrCode.setValue(Integer.toString(art.getCode()));
      elementArt.setAttributeNode(attrCode);
      
      // Nodo Nombre
      Element elementName = document.createElement("Name");
      elementName.appendChild(document.createTextNode(art.getName()));
      elementArt.appendChild(elementName);
      
      // Nodo Marca
      Element elementBrand = document.createElement("Brand");
      elementBrand.appendChild(document.createTextNode(art.getBrand()));
      elementArt.appendChild(elementBrand);
      
      // Nodo Precio de Compra
      Element elementBuyingPrice = document.createElement("BuyingPrice");
      elementBuyingPrice.appendChild(document.createTextNode(Double.toString(art.getBuyingPrice())));
      elementArt.appendChild(elementBuyingPrice);
      
      // Nodo Precio de Venta
      Element elementSellingPrice = document.createElement("SellingPrice");
      elementSellingPrice.appendChild(document.createTextNode(Double.toString(art.getSellingPrice())));
      elementArt.appendChild(elementSellingPrice);
      
      // Nodo Unidades
      Element elementUnits = document.createElement("Units");
      elementUnits.appendChild(document.createTextNode(Integer.toString(art.getUnits())));
      elementArt.appendChild(elementUnits);
      
      // Nodo Stock de seguridad
      Element elementSecurityStock = document.createElement("SecurityStock");
      elementSecurityStock.appendChild(document.createTextNode(Integer.toString(art.getSecurityStock())));
      elementArt.appendChild(elementSecurityStock);
      
      // Nodo Stock Máximo
      Element elementMaxStock = document.createElement("MaxStock");
      elementMaxStock.appendChild(document.createTextNode(Integer.toString(art.getMaxStock())));
      elementArt.appendChild(elementMaxStock);
    }
    
    TransformerFactory tf = TransformerFactory.newInstance();
    Transformer transformer = tf.newTransformer();
    DOMSource source = new DOMSource(document);
    StreamResult result = new StreamResult(new FileWriter("Warehouse.xml"));
    transformer.transform(source, result);
    } catch (DOMException e) {
    } catch (TransformerConfigurationException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (TransformerException e) {
      e.printStackTrace();
    } catch (ParserConfigurationException e) {
      e.printStackTrace();
    }
  }
}
