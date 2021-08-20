package Ui;

import Analyzers.Lexical.*;
import Analyzers.Semantics.SemanticsAnalyzer;
import Analyzers.Syntax.SyntaxAnalyzer;
import Exceptions.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.web.HTMLEditor;
import javafx.stage.FileChooser;


import javax.naming.OperationNotSupportedException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Scanner;

public class MainController implements Initializable {
    private class Line {
        final String lineString;
        final int lineNumber;
        Line(String lineString, int lineNumber) {
            this.lineString = lineString;
            this.lineNumber = lineNumber;
        }
    }
    @FXML
    private MenuItem fileChooserMenuOption;

    @FXML
    private ListView<Line> codeViewListView;

    @FXML
    private HTMLEditor codeEditorHTMLEditor;

    @FXML
    private Button analyzeButton;

    @FXML
    private Button loadFileButton;

    @FXML
    private HTMLEditor outputHtmlEditor;

    @FXML
    private TabPane tabPane;

    @FXML
    private TableColumn<TokenRecord, String> tokenString_col;

    @FXML
    private TableColumn<TokenRecord, String> tokenType_col;

    @FXML
    private TableColumn<TokenRecord, String> line_col;

    @FXML
    private Tab symbolTableTab;

    @FXML
    private TableView<TokenRecord> lexicalTableTableView;

    @FXML
    private TableView<SymbolRecord> symbolTableTableView;

    @FXML
    private TableColumn<SymbolRecord, String> id_col;

    @FXML
    private TableColumn<SymbolRecord, String> category_col;

    @FXML
    private TableColumn<SymbolRecord, String> type_col;

    @FXML
    private TableColumn<SymbolRecord, String> memoryStructure_col;

    @FXML
    private TableColumn<SymbolRecord, String> value_col;

    @FXML
    private TableColumn<SymbolRecord, String> numberOfParameters_col;

    @FXML
    private TableColumn<SymbolRecord, String> parameterSequence_col;

    @FXML
    private TableColumn<SymbolRecord, String> referenceType_col;

    @FXML
    private TableColumn<SymbolRecord, String> reference_col;

    @FXML
    private TableColumn<SymbolRecord, String> level_col;

    @FXML
    private TableColumn<SymbolRecord, String> dimension_col;

    private File file;

    private int lineWithError = -1;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        /*codeViewListView.setCellFactory(cell -> {
            ListCell<Line> cel = new ListCell<Line>() {
                @Override
                protected void updateItem(Line line, boolean b) {
                    super.updateItem(line, b);
                    setDisable(true);
                    setBackground(new Background(new BackgroundFill(Paint.valueOf("white"), null, null)));
                    if(line != null) {
                        setText(line.lineString);
                        if(line.lineNumber == 2)
                            this.setTextFill(Paint.valueOf("red"));
                    } else {
                        setText("");
                    }
                }
            };
            return cel;
        });
        codeViewListView.setItems(observableList);*/
        codeEditorHTMLEditor.setHtmlText("<body contentEditable=\"false\" style='background-color: #232425; color: white; font-family:Consolas; margin: 0; padding: 0;'>" +
                "<p style='color:#606060'><span style='color: #646668; display: inline-block; background-color: #323435; padding: 5px 10px; margin-right: 10px'>1</span>&emsp;//Nenhum Ficheiro Selecionado</p></body>");
        outputHtmlEditor.setHtmlText("<body contentEditable=\"false\" style='background-color: #232425; color: #FFEB09; font-family:Consolas; margin: 0; padding: 0;'>Carregue Um Ficheiro</body>");
        initializeTables();

        analyzeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    //:: Analise Lexica
                    LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer(file);
                    lexicalAnalyzer.analyze();

                    populateLexicalTable(lexicalAnalyzer.getParsedTokens());
                    populateSymbolTable(lexicalAnalyzer.getSymbolTable());

                    //:: Analise Sintatica
                    SyntaxAnalyzer syntaxAnalisis = new SyntaxAnalyzer(lexicalAnalyzer.getParsedTokens(), false);
                    syntaxAnalisis.analyze();

                    //:: Analise Semantica
                    SemanticsAnalyzer semanticsAnalyzer = new SemanticsAnalyzer(lexicalAnalyzer.getParsedTokens(), lexicalAnalyzer.getDeclaraionsMap(), lexicalAnalyzer.getSymbolTable());
                    semanticsAnalyzer.analyze();


                    writeToOutput("::: DECLARACAO DO WHILE VALIDA", true);

                } catch (FileNotFoundException e) {
                    writeToOutput(e.getMessage(), false);
                } catch (IOException e) {
                    writeToOutput(e.getMessage(), false);
                } catch (LexicalException e) {
                    handleError(e);
                } catch (SemanticsException e) {
                    handleError(e);
                } catch (SyntaxException e) {
                    handleError(e);
                } catch (OperationNotSupportedException e) {
                    writeToOutput(e.getMessage(), false);
                } catch (EmptyAutomataStackException e) {
                    writeToOutput(e.getMessage(), false);
                } catch (StateNotFoundException e) {
                    handleError(e);
                }
            }
        });


        loadFileButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                onLoadFile(actionEvent);
            }
        });
    }

    private void initializeTables() {
        //::>> Tabela de Simbolos
        this.id_col.setCellValueFactory(new PropertyValueFactory<SymbolRecord, String>("id"));
        this.category_col.setCellValueFactory(new PropertyValueFactory<SymbolRecord, String>("category"));
        this.type_col.setCellValueFactory(new PropertyValueFactory<SymbolRecord, String>("type"));
        this.memoryStructure_col.setCellValueFactory(new PropertyValueFactory<SymbolRecord, String>("memoryStructure"));
        this.value_col.setCellValueFactory(new PropertyValueFactory<SymbolRecord, String>("value"));
        this.numberOfParameters_col.setCellValueFactory(new PropertyValueFactory<SymbolRecord, String>("numberOfParameters"));
        this.parameterSequence_col.setCellValueFactory(new PropertyValueFactory<SymbolRecord, String>("parameterSequence"));
        this.referenceType_col.setCellValueFactory(new PropertyValueFactory<SymbolRecord, String>("referenceType"));
        this.reference_col.setCellValueFactory(new PropertyValueFactory<SymbolRecord, String>("reference"));
        this.level_col.setCellValueFactory(new PropertyValueFactory<SymbolRecord, String>("level"));
        this.dimension_col.setCellValueFactory(new PropertyValueFactory<SymbolRecord, String>("dimension"));
        //::>> Tabela de lexico
        this.tokenString_col.setCellValueFactory(new PropertyValueFactory<TokenRecord, String>("tokenString"));
        this.tokenType_col.setCellValueFactory(new PropertyValueFactory<TokenRecord, String>("tokenType"));
        this.line_col.setCellValueFactory(new PropertyValueFactory<TokenRecord, String>("line"));
    }

    private void populateLexicalTable(ArrayList<Token> tokens) {
        ObservableList<TokenRecord> tokensRecords = FXCollections.observableArrayList();
        for(Token token : tokens)
            tokensRecords.add(new TokenRecord(token));
        this.lexicalTableTableView.setItems(tokensRecords);
    }

    private void populateSymbolTable(ArrayList<Symbol> symbols) {
        ObservableList<SymbolRecord> symbolRecords = FXCollections.observableArrayList();
        for(Symbol symbol : symbols)
            symbolRecords.add(new SymbolRecord(symbol));
        symbolTableTableView.setItems(symbolRecords);
    }

    private void clearTables() {
        this.symbolTableTableView.getItems().clear();
        this.lexicalTableTableView.getItems().clear();
    }

    private void handleError(Exception e) {
        CustomException exception = (CustomException) e;
        this.lineWithError = exception.getLine();
        writeToOutput("\n" + exception.getErrorMessage(), false);
        loadContentToView();
    }

    private void writeToOutput(String content, boolean isSuccess) {
        String color = isSuccess? "#698857": "#FF6B68";
        String htmlContent = insertIntoColoredSpan(content, color);
        String body = insertIntoBodyTag(htmlContent);
        outputHtmlEditor.setHtmlText(body);
    }

    @FXML
    void onLoadFile(ActionEvent event) {
        clearTables();
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Files", "*.java", "*.txt"));
        this.file = fc.showOpenDialog(null);
        loadContentToView();

    }

    private void loadContentToView() {
        try {
            loadFileContentToHtmlEditor(this.file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void loadFileContentToHtmlEditor(File file) throws FileNotFoundException {
        try {
            Scanner scanner = new Scanner(file);
            String content = "", lineStr = "";
            int lineNmbr = 1;
            while (scanner.hasNextLine()) {
                lineStr = scanner.nextLine();
                lineStr = lineStr.replaceAll(" ", "&nbsp;");
                content += getNewParagraphWith(lineStr, lineNmbr, lineNmbr == this.lineWithError);
                lineNmbr++;
            }
            String body = insertIntoBodyTag(content);
            this.codeEditorHTMLEditor.setHtmlText(body);
            this.lineWithError = -1;
        } catch (NullPointerException e) {
            writeToOutput("Nenhum ficheiro selecionado.", false);
        }
    }

    private String getLineNumberElement(int lineNumber) {
        String lineNumberStyle = "style='color: #646668; display: inline-block; background-color: #323435; padding: 5px 10px; margin-right: 10px; width: 20px;'";
        String lineNumberTag = "<span " + lineNumberStyle + ">" + lineNumber + "</span>";
        return lineNumberTag;
    }

    private String getNewParagraphWith(String content, int lineNumber, boolean hasError) {
        String paragraphOpening = hasError? "<p style='margin: 0; padding: 0; background-color: #3A2323'>" : "<p style='margin: 0; padding: 0;'>";
        return  paragraphOpening + getLineNumberElement(lineNumber) + "<span>" + getFormattedElements(content) + "</span></p>";
    }

    private String getFormattedElements(String content) {
        String formatted = content.replaceAll("do", insertIntoColoredSpan("do", "#CE7827"));
        formatted = formatted.replaceAll("while", insertIntoColoredSpan("while", "#CE7827"));
        formatted = formatted.replaceAll("true", insertIntoColoredSpan("true", "#9876AA"));
        formatted = formatted.replaceAll("false", insertIntoColoredSpan("false", "#9876AA"));
        formatted = formatted.replaceAll("char", insertIntoColoredSpan("char", "#CE7827"));
        formatted = formatted.replaceAll("boolean", insertIntoColoredSpan("boolean", "#CE7827"));
        return formatted;
    }

    private String insertIntoColoredSpan(String content, String hexColor) {
        return "<span style='color: " + hexColor + ";'>" + content + "</span>";
    }

    private String insertIntoBodyTag(String content) {
        return "<body contentEditable=\"false\" style='background-color: #232425; color: white; font-family:Consolas; margin: 0; padding: 0;'>" + content + "</body>";
    }

}
