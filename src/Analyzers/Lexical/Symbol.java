package Analyzers.Lexical;

public class Symbol {
    String id;
    String category;
    String type;
    String memoryStructure;
    String value;
    String numberOfParameters;
    String parameterSequence;
    String referenceType;
    String reference;
    String level;
    String dimension;

    public Symbol(String name) {
        this.id = name;
        this.value = "--";
        this.numberOfParameters = "--";
        this.parameterSequence = "--";
        this.referenceType = "--";
        this.reference = "--";
        this.dimension = "--";
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public void printSymbol() {
        System.out.printf("%-20s %-20s %-20s %-20s %-20s %-20s %-20s %-20s %-20s %-20s %-20s\n",
                id,
                category,
                type,
                memoryStructure,
                value,
                numberOfParameters,
                parameterSequence,
                referenceType,
                reference,
                level,
                dimension);
    }
}
