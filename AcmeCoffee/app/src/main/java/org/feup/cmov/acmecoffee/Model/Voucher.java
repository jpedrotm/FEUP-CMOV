package org.feup.cmov.acmecoffee.Model;

public class Voucher {
    private String description;
    private voucherType type;
    private String image; // mesma situação que no Item, uma imagem para cada tipo de Voucher

    public Voucher(voucherType type) {
        this.type = type;
        this.description = getDescriptionFromType(type);
    }

    public String getDescription() {
        return description;
    }

    public voucherType getType() {
        return type;
    }

    private String getDescriptionFromType(voucherType type) {
        switch(type) {
            case FREE_COFFEE:
                return "Free Coffee in a new order.";
            case FIVE_PERCENT_DISCOUNT:
                return "Five percent discount in a new order.";
            default:
                return "Not a valid voucher.";
        }
    }

    public enum voucherType {
        FREE_COFFEE, FIVE_PERCENT_DISCOUNT
    }
}
