// Java Program of JavaBean class

package com.techbarn.webapp;

public class ItemBean implements java.io.Serializable {
    private int id;
    private String name;
    private Boolean inStock;
    private String brand;
    private String color;   
    private String condition;
    private String description;
    private String imagePath;
    private java.math.BigDecimal minPrice;
    private java.math.BigDecimal maxPrice;
    private Boolean hasActiveAuction;

    public ItemBean() {}
  
    public void setId(int id) { this.id = id; }
    public int getId() { return id; }
    
    public void setName(String name) { this.name = name; }
    public String getName() { return name; }

    public void setStock (Boolean stock){this.inStock = stock;};
    public Boolean getStock(){return inStock;};

    public void setBrand(String brand){this.brand = brand;};
    public String getBrand(){return brand;};

    public void setColor (String color){this.color = color;};
    public String getColor(){return color;};

    public void setCondition(String condition){this.condition = condition;};
    public String getCondition(){return condition;};

    public void setDescription(String descr){this.description = descr;};
    public String getDescription(){return description;};

    public void setImagePath(String imagePath){this.imagePath = imagePath;};
    public String getImagePath(){return imagePath;};

    public void setMinPrice(java.math.BigDecimal minPrice){this.minPrice = minPrice;};
    public java.math.BigDecimal getMinPrice(){return minPrice;};

    public void setMaxPrice(java.math.BigDecimal maxPrice){this.maxPrice = maxPrice;};
    public java.math.BigDecimal getMaxPrice(){return maxPrice;};

    public void setHasActiveAuction(Boolean hasActiveAuction){this.hasActiveAuction = hasActiveAuction;};
    public Boolean getHasActiveAuction(){return hasActiveAuction;};

}