public enum Genero {
    ROCK("Rock"), 
    POP("POP"), 
    MPB("MPB"), 
    JAZZ("Jazz"), 
    ELETRONICA("Eletrônica"), 
    CLASSICA("Clássica"),;

    public final String variavelTemporaria;

    Genero(String variavelTemporaria) {
        this.variavelTemporaria = variavelTemporaria;
    }
    public String getVariavelTemporaria() {
        return variavelTemporaria;
    }
    public static Genero fromVariavelTemporaria(String nome) {
        for (Genero genero : Genero.values()) {
            if (genero.variavelTemporaria.equalsIgnoreCase(nome)) {
                return genero;
            }
        }
        return null; 
}
}