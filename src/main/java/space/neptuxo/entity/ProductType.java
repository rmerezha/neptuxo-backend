package space.neptuxo.entity;

public enum ProductType {
    REAL_ESTATE("Real Estate"),
    TRANSPORT("Transport"),
    ELECTRONICS("Electronics"),
    CLOTHING_AND_FOOTWEAR("Clothing and Footwear"),
    HOME_AND_GARDEN("Home and Garden"),
    CHILDRENS_GOODS("Children's Goods"),
    SPORTS_AND_RECREATION("Sports and Recreation"),
    ANIMALS("Animals");

    private final String type;

    ProductType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}