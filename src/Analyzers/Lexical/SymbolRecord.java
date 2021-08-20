package Analyzers.Lexical;

import Analyzers.Lexical.Symbol;
import javafx.beans.property.SimpleStringProperty;

public class SymbolRecord {
    SimpleStringProperty id;
    SimpleStringProperty category;
    SimpleStringProperty type;
    SimpleStringProperty memoryStructure;
    SimpleStringProperty value;
    SimpleStringProperty numberOfParameters;
    SimpleStringProperty parameterSequence;
    SimpleStringProperty referenceType;
    SimpleStringProperty reference;
    SimpleStringProperty level;
    SimpleStringProperty dimension;
    
    private final Symbol symbol;
    
    public SymbolRecord(Symbol symbol) {
        this.symbol = symbol;
        initialize();
    }
    
    private void initialize() {
        this.id = new SimpleStringProperty(symbol.id);
        this.category = new SimpleStringProperty(symbol.category);
        this.type = new SimpleStringProperty(symbol.type);
        this.memoryStructure = new SimpleStringProperty(symbol.memoryStructure);
        this.value = new SimpleStringProperty(symbol.value);
        this.numberOfParameters = new SimpleStringProperty(symbol.numberOfParameters);
        this.parameterSequence = new SimpleStringProperty(symbol.parameterSequence);
        this.referenceType = new SimpleStringProperty(symbol.referenceType);
        this.reference = new SimpleStringProperty(symbol.reference);
        this.level = new SimpleStringProperty(symbol.level);
        this.dimension = new SimpleStringProperty(symbol.dimension);
    }

    public String getId() {
        return id.get();
    }

    public SimpleStringProperty idProperty() {
        return id;
    }

    public String getCategory() {
        return category.get();
    }

    public SimpleStringProperty categoryProperty() {
        return category;
    }

    public String getType() {
        return type.get();
    }

    public SimpleStringProperty typeProperty() {
        return type;
    }

    public String getMemoryStructure() {
        return memoryStructure.get();
    }

    public SimpleStringProperty memoryStructureProperty() {
        return memoryStructure;
    }

    public String getValue() {
        return value.get();
    }

    public SimpleStringProperty valueProperty() {
        return value;
    }

    public String getNumberOfParameters() {
        return numberOfParameters.get();
    }

    public SimpleStringProperty numberOfParametersProperty() {
        return numberOfParameters;
    }

    public String getParameterSequence() {
        return parameterSequence.get();
    }

    public SimpleStringProperty parameterSequenceProperty() {
        return parameterSequence;
    }

    public String getReferenceType() {
        return referenceType.get();
    }

    public SimpleStringProperty referenceTypeProperty() {
        return referenceType;
    }

    public String getReference() {
        return reference.get();
    }

    public SimpleStringProperty referenceProperty() {
        return reference;
    }

    public String getLevel() {
        return level.get();
    }

    public SimpleStringProperty levelProperty() {
        return level;
    }

    public String getDimension() {
        return dimension.get();
    }

    public SimpleStringProperty dimensionProperty() {
        return dimension;
    }
}
