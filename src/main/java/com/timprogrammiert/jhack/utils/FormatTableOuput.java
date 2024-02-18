package com.timprogrammiert.jhack.utils;

/**
 * Author: Tim
 * Date: 18.02.2024
 * Version: 1.0
 */
public class FormatTableOuput {
    /**
     * Parses the input string into a 2D array representing the table data.
     *
     * @param input The input string containing table data.
     * @return The 2D array representing the table data.
     */
    private static String[][] getDataArray(String input) {
        String[] lines = input.split("\n");
        int numberOfColumns = lines[0].split(" ").length;
        String[][] dataArray = new String[lines.length][numberOfColumns];

        // Populate the 2D array with parsed data
        for (int i = 0; i < lines.length; i++) {
            dataArray[i] = lines[i].split(" ");
        }

        return dataArray;
    }

    /**
     * Computes the maximum character lengths for each column in the data array.
     *
     * @param dataArray The 2D array representing the table data.
     * @return An array containing the maximum character lengths for each column.
     */
    private static int[] getMaxCharAmount(String[][] dataArray) {
        int[] columnMaxWidth = new int[dataArray[0].length];

        // Iterate through the data array to find maximum character lengths for each column
        for (String[] strings : dataArray) {
            for (int j = 0; j < strings.length; j++) {
                columnMaxWidth[j] = Math.max(columnMaxWidth[j], strings[j].length());
            }
        }

        return columnMaxWidth;
    }

    /**
     * Generates formatted table output with padded columns.
     *
     * @return The formatted table output string.
     */
    public static String getTableOutput(String input) {
        String[][] dataArray = getDataArray(input);
        int[] maxCharAmountInLine = getMaxCharAmount(dataArray);

        StringBuilder output = new StringBuilder();

        // Iterate through the data array to generate formatted output
        for (int i = 0; i < dataArray.length; i++) {
            for (int j = 0; j < dataArray[i].length; j++) {
                int charAmount = dataArray[i][j].length();

                // Pad the string if needed
                if (charAmount < maxCharAmountInLine[j]) {
                    dataArray[i][j] = dataArray[i][j] + " ".repeat(maxCharAmountInLine[j] - charAmount);
                }

                output.append(dataArray[i][j]).append(" ");
            }

            output.append("\n");
        }

        return output.toString();
    }
}
