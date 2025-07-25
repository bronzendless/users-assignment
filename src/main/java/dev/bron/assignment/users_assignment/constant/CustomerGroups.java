package dev.bron.assignment.users_assignment.constant;

public enum CustomerGroups {
    CUSTOMER("customer"),
    PLATINUM("platinum"),
    GOLD("gold"),
    SILVER("silver");

    public final String label;

    CustomerGroups(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}
