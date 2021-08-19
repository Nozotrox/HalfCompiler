package Analyzers.Lexical;

public enum TokenType {
    DO("palavra reservada"), //:: do
    WHILE("palavra reservada"), //:: while
    OPEN_BRACKET("simbolo especial delimitador de parametro"), //:: (
    CLOSE_BRACKET("simbolo especial delimitador de parametro"), //:: )
    COMMA("simbolo especial"),
    OPEN_CURLY_BRACKET("simbolo especial delimitador de bloco de codigo"),//:: {
    CLOSE_CURLY_BRACKET("simbolo especial delimitador de bloco de codigo"),//:: }
    PRIMITIVE_TYPE("tipo primitivo"), //:: boolean ou char | char* abc = 'a'; | refere-se a char e boolean
    CHARACTER_CONSTANT("constante do tipo caractere"), //:: boolean ou char char abc = 'a'* | referese-se ao 'a', 'b', 'c', etc
    STRING_CONSTANT("constante do tipo string"),//:: boolean ou char
    BOOLEAN_CONSTANT("contante booleana"), //:: true e false
    SEMI_COLON("simbolo especial de fim de instrução"),
    SINGLE_QUOTE("simbolo especial delimitador de caractere"), //:: '
    DOUBLE_QUOTE("simbolo especial delimitador de strings"), //:: "
    EQUAL("simbolo especial de igualidade"), //:: =
    IDENTIFIER("identificador");


    private final String description;
    TokenType(String description) {
        this.description = description;
    }

    public String getName() {
        return this.toString().toLowerCase();
    }
    public String getDescription() { return this.description;}
}
