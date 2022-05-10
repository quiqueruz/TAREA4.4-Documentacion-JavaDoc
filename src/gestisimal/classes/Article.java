package gestisimal.classes;

import java.util.Objects;
import gestisimal.exceptions.ArticleIllegalArgumentException;

/**
 * Clase que se utiliza para generar artículos.
 * 
 * @author Quique Ruz Del Río
 *
 */

public class Article {
  private static int lastCode = 0;
  private int code;
  private String name;
  private String brand;
  private double buyingPrice;
  private double sellingPrice;
  private int units;
  private int securityStock;
  private int maxStock;
  
  /**
   * Crea un artículo con estos parámetros
   * 
   * @param name                Nombre
   * @param brand               Marca
   * @param buyingPrice         Precio Compra
   * @param sellingPrice        Precio Venta
   * @param units               Unidades a añadir
   * @param securityStock       Stocl de seguridad
   * @param maxStock            Stock máximo
   */
  
  
  Article(String name, String brand, double buyingPrice, double sellingPrice, int units, int securityStock, int maxStock) {
    this.code = Article.lastCode++;
    setName(name);
    setBrand(brand);
    setBuyingPrice(buyingPrice);
    setSellingPrice(sellingPrice);
    setUnits(units);
    setSecurityStock(securityStock);
    setMaxStock(maxStock);
  }
  
  /**
   * Crea un artículo con estos parámetros
   * 
   * @param name                Nombre
   * @param brand               Marca
   * @param buyingPrice         Precio Compra
   * @param sellingPrice        Precio Venta
   * @param units               Unidades a añadir
   */

  Article(String name, String brand, double buyingPrice, double sellingPrice, int units){
    this(name, brand, buyingPrice, sellingPrice, units, 0, 0);
  }


  
  @Override
  public String toString() {
    return "Article [code=" + code + ", name=" + name + ", brand=" + brand + ", buyingPrice="
        + buyingPrice + ", sellingPrice=" + sellingPrice + ", units=" + units + ", securityStock="
        + securityStock + ", maxStock=" + maxStock + "]";
  }

  /**
   * Devuelve el Código
   * 
   * @return
   */
  
  public int getCode() {
    return code;
  }
  
  /**
   * Cambia el Código
   * 
   * @param code
   */
  void setCode(int code) {
    this.code = code;
  }

  /**
   * Devuelve el nombre
   * 
   * @return
   */
  String getName() {
    return name;
  }

  /**
   * Cambia el nombre
   * 
   * @param name
   */
  
  void setName(String name) {
    throwExceptionIfStringIsNotValid(name);
    this.name = name;
  }

  /**
   * Devuelve la marca
   * 
   * @return
   */
  
  String getBrand() {
    return brand;
  }

  /**
   * Cambia la marca
   * 
   * @param brand
   */
  
  void setBrand(String brand) {
    throwExceptionIfStringIsNotValid(brand);
    this.brand = brand;
  }

  /**
   * Devuelve el precio de compra
   * 
   * @return
   */
  
  double getBuyingPrice() {
    return buyingPrice;
  }
  
  /**
   * Cambia el precio de compra
   * 
   * @param buyingPrice
   */

  void setBuyingPrice(double buyingPrice){
    throwExceptionIfNegativePrice(buyingPrice);
    this.buyingPrice = buyingPrice;
  }
  
  /**
   * Devuelve el precio de venta
   * 
   * @return
   */

  double getSellingPrice() {
    return sellingPrice;
  }

  /**
   * Cambia el precio de venta
   * 
   * @param sellingPrice
   */
  
  void setSellingPrice(double sellingPrice){
    throwExceptionIfNegativePrice(sellingPrice);
    this.sellingPrice = sellingPrice;
  }
  
  /**
   * Devuelve las unidades
   * 
   * @return
   */
   
  int getUnits() {
    return units;
  }

  /**
   * Cambia las unidades
   * 
   * @param units
   */
  
  void setUnits(int units) {
    throwsExceptionIfUnitsAreNegative(units);
    this.units = units;
  }
  
  /**
   * Aumenta las unidades en X números
   * 
   * @param units
   */
  
  void increaseUnits(int units) {
    throwsExceptionIfArticleUnitsAreNegative(units);
    this.units += units;
  }

  /**
   * Descienden las unidades X números
   * 
   * @param units
   */
  
  void decreaseUnits(int units){
    throwsExceptionIfArticleUnitsAreNegative(units);
    this.units -= units;
  }

  /**
   * Devuelve el stock de seguridad
   * 
   * @return
   */
  
  int getSecurityStock() {
    return securityStock;
  }
  
  /**
   * Cambia el stock de seguridad
   * 
   * @param securityStock
   */

  void setSecurityStock(int securityStock) {
    this.securityStock = securityStock;
  }
  
  /**
   * Devuelve el stock máximo
   * 
   * @return
   */

  int getMaxStock() {
    return maxStock;
  }

  /**
   * Cambia el stock máximo
   * 
   * @param maxStock
   */
  
  void setMaxStock(int maxStock) {
    this.maxStock = maxStock;
  }

  @Override
  public int hashCode() {
    return Objects.hash(brand, name);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Article other = (Article) obj;
    return Objects.equals(brand, other.brand) && Objects.equals(name, other.name);
  }
  
  /**
   * Excepción unidades negativas
   * 
   * @param units
   */
  
  private void throwsExceptionIfUnitsAreNegative(int units) {
    if(units < 0) {
      throw new ArticleIllegalArgumentException("Las unidades a modificar no pueden ser negativa");
    }
  }
  
  /**
   * Excepción Precio Compra Negativo
   * 
   * @param buyingPrice
   */
  
  void throwExceptionIfNegativePrice(double buyingPrice) {
    if (buyingPrice < 0) {
      throw new ArticleIllegalArgumentException("El precio no puede ser negativo");
    }
  }
  
  /**
   * Excepción Nombre no válido
   * 
   * @param name
   */

  private void throwExceptionIfStringIsNotValid(String name) {
    if (name == null || name == "") {
      throw new ArticleIllegalArgumentException("La cadena no puede ser null o estar vacia.");
    }
  }
  
  /**
   * Excepción unidades del artículo negativas
   * 
   * @param units
   */
  
  private void throwsExceptionIfArticleUnitsAreNegative(int units){
    if ((this.units-units) <= 0) {
      throw new ArticleIllegalArgumentException("Las unidades no pueden ser negativas.");
    }
  }
}
