package fr.pantheonsorbonne.miage.enums;

public enum RoleValue {
    PRESIDENT("1",1), 
    VICE_PRESIDENT("2",2),
    VICE_TROU("3",3),
    TROU("4",4);

    final private String stringRepresentation;
    final private int value;

    RoleValue(String stringRepresentation, int value) {
        this.stringRepresentation = stringRepresentation;
        this.value = value;
    }


    public static RoleValue valueOfStr(String str) {
        for (RoleValue value : RoleValue.values()) {
            if (str.equals(value.getStringRepresentation())) {
                return value;
            }
        }

        throw new RuntimeException("failed to parse value");

    }

    public String getStringRepresentation() {
        return stringRepresentation;
    }

    public int getRoleValue() {
        return value;
    }
}
