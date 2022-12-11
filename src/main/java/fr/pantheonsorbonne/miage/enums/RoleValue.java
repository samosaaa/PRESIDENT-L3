package fr.pantheonsorbonne.miage.enums;

public enum RoleValue {
    PRESIDENT("1",1), 
    VICE_PRESIDENT("2",2),
    NEUTRE("3",3),
    VICE_TROU("4",4),
    TROU("5",5);

    final private String stringRepresentation;
    final private int value;
    int role;

    RoleValue(String stringRepresentation, int value) {
        this.stringRepresentation = stringRepresentation;
        this.value = value;
    }


    public int getRoleValue() {
        return value;
    }

}
