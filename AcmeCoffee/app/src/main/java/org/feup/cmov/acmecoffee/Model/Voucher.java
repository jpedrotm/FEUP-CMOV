package org.feup.cmov.acmecoffee.Model;

public class Voucher {
    private Long id;
    private String description;
    private VoucherType type;

    public Voucher(Long id, String type) {
        this.id = id;
        this.type = getTypeFromString(type);
        this.description = getDescriptionFromType(this.type);
    }

    private String getDescriptionFromType(VoucherType type) {
        switch(type) {
            case FREE_COFFEE:
                return "Free Coffee in a new order.";
            case FIVE_PERCENT_DISCOUNT:
                return "Five percent discount in a new order.";
            default:
                return "Not a valid voucher.";
        }
    }

    private VoucherType getTypeFromString(String t) {
        switch(t) {
            case "FREE_COFFEE":
                return VoucherType.FREE_COFFEE;
            case "FIVE_PERCENT_DISCOUNT":
                return VoucherType.FIVE_PERCENT_DISCOUNT;
            default:
                return null;
        }
    }

    public String getStringFromType(VoucherType type){
        switch(type) {
            case FREE_COFFEE:
                return "FREE_COFFEE";
            case FIVE_PERCENT_DISCOUNT:
                return "FIVE_PERCENT_DISCOUNT";
            default:
                return "Not a valid voucher type.";
        }
    }

    public String getDescription() {
        return description;
    }

    public VoucherType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Voucher{" +
                "description='" + description + '\'' +
                ", type=" + type +
                '}';
    }

    public enum VoucherType {
        FREE_COFFEE, FIVE_PERCENT_DISCOUNT
    }

    public Long getId() {
        return id;
    }
}
