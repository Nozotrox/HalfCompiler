package Analyzers.Lexical;

import javafx.beans.property.SimpleStringProperty;

public class TokenRecord {
    SimpleStringProperty tokenString;
    SimpleStringProperty tokenType;
    SimpleStringProperty line;

    private final Token token;
    public TokenRecord(Token token) {
        this.token = token;
        this.initialize();
    }

    private void initialize() {
        this.tokenString = new SimpleStringProperty(token.tokenString);
        this.tokenType = new SimpleStringProperty(token.tokenType.getDescription());
        this.line = new SimpleStringProperty(String.valueOf(token.line));
    }

    public String getTokenString() {
        return tokenString.get();
    }

    public SimpleStringProperty tokenStringProperty() {
        return tokenString;
    }

    public String getTokenType() {
        return tokenType.get();
    }

    public SimpleStringProperty tokenTypeProperty() {
        return tokenType;
    }

    public String getLine() {
        return line.get();
    }

    public SimpleStringProperty lineProperty() {
        return line;
    }
}
