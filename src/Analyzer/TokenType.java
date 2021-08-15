package Analyzer;

public enum TokenType {
    RESERVED_WORD("palavra reservada"),
    OPEN_BRACKET("simbolo especial delimitador de parametro"),
    CLOSE_BRACKET("simbolo especial delimitador de parametro"),
    OPEN_CURLY_BRACKET("simbolo especial delimitador de bloco de codigo"),
    CLOSE_CURLY_BRACKET("simbolo especial delimitador de bloco de codigo"),
    PRIMITIVE_TYPE("tipo primitivo"),
    CHARACTER_CONSTANT("constante do tipo caractere"),
    STRING_CONSTANT("constante do tipo string"),
    BOOLEAN_CONSTANT("contante booleana"),
    SEMI_COLON("simbolo especial de fim de instrução"),
    SINGLE_QUOTE("simbolo especial delimitador de caractere"),
    DOUBLE_QUOTE("simbolo especial delimitador de strings"),
    EQUAL("simbolo especial de igualidade"),
    IDENTIFIER("identificador");

    private final String description;
    TokenType(String description) {
        this.description = description;
    }
}
