import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;

import java.io.FileReader;
import java.io.IOException;
import java.util.Set;

public class FormatConverter {
    public static void main(String... args){
        Table table = new Table();
        Matrix matrix = new Matrix();

        JSONObject jsonObject = getJsonObject();
        Set keySet = jsonObject.keySet();
        JSONArray valuesArray = getValuesArray(jsonObject, keySet);
        int cellWidth = table.getCellWidth(keySet);
        int rowsNumber = table.getRowsNumber(keySet, valuesArray);

        String[][] transparentMatrix = matrix.getTransparentMatrix(valuesArray, rowsNumber, cellWidth);
        table.showTableHeader(keySet.size(), cellWidth, keySet);
        table.showTableBody(transparentMatrix, rowsNumber, valuesArray, cellWidth);
    }

    //Getting values array from json object.
    private static JSONArray getValuesArray(JSONObject jsonObject, Set keySet){
        JSONArray valuesArray = new JSONArray();
        for(int i = 0; i < keySet.size(); i++){
            valuesArray.add(jsonObject.get(keySet.toArray()[i]));
        }
        return valuesArray;
    }

    //Getting JsonObject from the necessary json file.
    private static JSONObject getJsonObject(){
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject = (JSONObject) jsonParser.parse(new FileReader("file.json"));
        }catch (FileNotFoundException ex){
            System.out.println("File not found!");
            System.exit(0);
        } catch (ParseException | IOException e) {
            //e.printStackTrace();
            System.err.println(e.toString());
        }
        return jsonObject;
    }
}

class Matrix{
    //Getting matrix of values from values array.
    private String[][] getMatrix(int jColumns, JSONArray valuesArray){
        int iRows = valuesArray.size();
        String[][] matrix = new String[iRows][jColumns];
        for(int i = 0; i < iRows; i++){
            String[] intermediateArray = valuesArray.toArray()[i].toString().split(",");
            System.arraycopy(intermediateArray, 0, matrix[i], 0, intermediateArray.length);
        }
        return matrix;
    }

    //Getting transparent matrix of values from matrix for right showing in the table.
    public String[][] getTransparentMatrix(JSONArray valuesArray, int jColumns, int formattedLengthString){
        int iRows = valuesArray.size();
        String[][] matrix = getMatrix(jColumns, valuesArray);
        String[][] transparentMatrix = new String[jColumns][iRows];

        for(int i = 0; i < jColumns; i++){
            for(int j = 0; j < iRows; j++){
                transparentMatrix[i][j] = matrix[j][i];
                if(transparentMatrix[i][j] != null){
                    if(transparentMatrix[i][j].length() > formattedLengthString){
                        formattedLengthString = transparentMatrix[i][j].length();
                    }
                }
            }
        }
        return transparentMatrix;
    }
}

class Table{
    //Getting rows number according to the size of the longest value array.
    public int getRowsNumber(Set keySet, JSONArray valuesArray){
        int rows = 0;
        for(int i = 0; i < keySet.size(); i++){
            if(valuesArray.toArray()[i].toString().split(",").length > rows){
                rows = (valuesArray.toArray()[i].toString().split(",")).length;
            }
        }
        return rows;
    }

    //Getting cells width according to the longest value in the table.
    public int getCellWidth(Set keySet){
        int cellWidth = 0;
        for(int i = 0; i < keySet.size(); i++){
            if((keySet.toArray()[i].toString()).length() > cellWidth){
                cellWidth = (keySet.toArray()[i].toString()).length();
            }
        }
        return cellWidth;
    }

    //Show table header according to the json keys number and values.
    public void showTableHeader(int keySetSize, int maxLengthValue, Set keySet) {
        for (int row = 0; row < 3; row++){//3 is the number of two borders and the title header body
            if(row == 0 || row == 2){
                for(int i = 0; i < keySetSize; i++) {
                    System.out.print(" ");
                    for (int j = 0; j < maxLengthValue; j++) {
                        System.out.print("-");
                    }
                    if (i == keySetSize - 1) {
                        System.out.print("\n");
                    }
                }
            }else{
                for(int i = 0; i < keySetSize; i++) {
                    System.out.print("|");
                    String headerStr = keySet.toArray()[i].toString();
                    if(headerStr.length() < maxLengthValue) {
                        System.out.print(toSize(headerStr, maxLengthValue - headerStr.length()));
                    } else System.out.print(headerStr);
                    if(i == keySet.size() - 1) {
                        System.out.print("|\n");
                    }
                }
            }
        }
    }

    /*Show table body with cells according to the values from the json file.
    valuesArray is the values array according to the each key from the json file.
    Matrix is the transparent matrix from values array.
    Rows is the number calculated based on the maximum value of the length of the values array.
    Columns is the number calculated based on the valuesArray size or number of keys from json file. */
    public void showTableBody(String[][] matrix, int rows, JSONArray valuesArray, int formattedLengthString){
        int columns = valuesArray.size();
        for(int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                System.out.print("|");
                if (matrix[i][j] == null) {
                    System.out.print(toSize("", formattedLengthString));
                } else {
                    matrix[i][j] = matrix[i][j].replaceAll("\\[|\\]", "");
                    if (matrix[i][j].length() < formattedLengthString) {
                        System.out.print(toSize(matrix[i][j], formattedLengthString - (matrix[i][j].length())));
                    } else System.out.print(matrix[i][j]);
                }
                if (j == columns - 1) {
                    System.out.print("|");
                }
            }
            System.out.println();
        }
        showEndTable(columns, rows);
    }

    //Drawing the end table border
    private static void showEndTable(int columns, int rows){
        for(int i = 0; i < columns; i++) {
            System.out.print(" ");
            for (int j = 0; j < rows - 1; j++) {
                System.out.print("-");
            }
        }
    }

    //Formatting table cells to right cells size in width according the longest value in the table.
    private static String toSize(String str, int times){
        String newStr = "";
        int before = times/2; //For centering position
        if(str.length()%2 == 0){
            before += 1;
        }
        for (int i = 0; i < before; i++)
            newStr = newStr.concat(" ");

        newStr = newStr.concat(str);

        for (int i = 0; i < times/2; i++)
            newStr = newStr.concat(" ");

        return newStr;
    }
}