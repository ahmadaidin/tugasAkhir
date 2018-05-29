package processor;

import java.util.ArrayList;

/**
 * Created by Aidin - 2 on 26/09/2016.
 */

public class MatrixOperator {

    public static double[][] rotateLeft(double[][] mtx) {
        int size = mtx.length;
        double[][] result =  new double[size][size];
        // Consider all squares one by one
        for (int i = 0; i < size / 2; i++)
        {
            // Consider elements in group of 4 in
            // current square
            for (int j = i; j < size-i-1; j++)
            {
                // store current cell in temp variable
                double temp = mtx[i][j];
                // move values from right to top
                result[i][j]=mtx[j][size-1-i];
                // move values from bottom to right
                result[j][size-1-i] = mtx[size-1-i][size-1-j];
                // move values from left to bottom
                result[size-1-i][size-1-j]=mtx[size-1-j][i];
                // assign temp to left
                result[size-1-j][i] = temp;
            }
        }
        return result;
    }


    public static double[][] rotateHalfRight(double[][] matrix) {
        int m = matrix.length;
        int n = m;

        double[][] result = new double[m][n];

        int row = 0, col = 0;
        double prev, curr;

    /*
       row - Staring row index
       m - ending row index
       col - starting column index
       n - ending column index
       i - iterator
    */
        while (row < m && col < n)
        {

            if (row + 1 == m || col + 1 == n)
                break;

            // Store the first element of next row, this
            // element will replace first element of current
            // row
            prev = matrix[row+1][col];

         /* Move elements of first row from the remaining rows */
            for (int i = col; i < n; i++)
            {
                curr = matrix[row][i];
                result[row][i]= prev;
                prev = curr;
            }
            row++;

        /* Move elements of last column from the remaining columns */
            for (int i = row; i < m; i++)
            {
                curr = matrix[i][n-1];
                result[i][n-1] = prev;
                prev = curr;
            }
            n--;

         /* Move elements of last row from the remaining rows */
            if (row < m)
            {
                for (int i = n-1; i >= col; i--)
                {
                    curr = matrix[m-1][i];
                    result[m-1][i]= prev;
                    prev = curr;
                }
            }
            m--;

        /* Move elements of first column from the remaining rows */
            if (col < n)
            {
                for (int i = m-1; i >= row; i--)
                {
                    curr = matrix[i][col];
                    result[i][col] = prev;
                    prev = curr;
                }
            }
            col++;
        }
        return result;
    }

    public static double[][] rotateRight(double[][] matrix) {
        int n = matrix.length;

        double[][] ret = new double[n][n];
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                ret[i][j] = matrix[n - j - 1][i];
            }
        }

        return ret;
    }

}
