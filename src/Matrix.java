/*
    Matrix to keep data of the Perceptron
    Input->Hidden->Output
 */



import java.util.ArrayList;
import java.util.Random;

public class Matrix
{
    ArrayList<ArrayList<Double>> data;
    int rows;
    int columns;
    Matrix()
    {
        data = new ArrayList<ArrayList<Double>>();
    }
    public void setMatrix(int row, int column)
    {
        ArrayList<Double> temp = new ArrayList<>();
        rows = row;
        columns = column;
        for (int r = 0; r < rows; r++)
        {
            data.add(new ArrayList<Double>());
            for (int c = 0; c < columns; c++)
            {
                data.get(r).add(c, 0.0);
            }
        }

    }
    public void setAllZero()
    {
        for (int r = 0; r < rows; r++)
        {
            for (int c = 0; c < columns; c++)
            {
                setValueAt(0.0, r, c);
            }
        }
    }
    void setValueAt(double value, int r, int c)
    {
        data.get(r).set(c, value);
    }
    public void initializeMatrix(ArrayList<ArrayList<Double>> c)
    {
        data = c;
        rows = data.size();
        columns = data.get(0).size();
    }
    public Matrix multi(Matrix b)
    {
        ArrayList<ArrayList<Double>> storedData = new ArrayList<ArrayList<Double>>();
        Matrix returned = new Matrix();
        for (int r = 0; r < getRow(); r++)
        {
            storedData.add(new ArrayList<Double>());
            for (int c = 0; c < b.getColumn(); c++)
            {

                storedData.get(r).add(c, dotProduct(b, r, c));
            }
        }
        returned.initializeMatrix(storedData);
        return returned;
    }
    public double dotProduct(Matrix b, int r, int c)
    {
        Double sum = 0.0;
        for (int k = 0; k < getRow(); k++)
        {
            sum += getDataAt(r, k) * b.getDataAt(k, c);
        }
    return sum;
    }
    public int getRow()
    {
        return rows;
    }
    public int getColumn()
    {
        return columns;
    }
    public void randomize()
    {
        Random rand = new Random();
        double value;
        boolean isNegative;
        for (int r = 0; r < getRow(); r++)
        {
            for (int c = 0; c < getColumn(); c++)
            {
                value = rand.nextGaussian() + rand.nextInt(1);
                data.get(r).set(c, value);
            }
        }
    }
    public Double getDataAt(int r, int c)
    {
        return data.get(r).get(c);
    }
    public void displayMatrix()
    {
        for (int r = 0; r < getRow(); r++)
        {
            for (int c = 0; c < getColumn(); c++)
            {
                System.out.print(data.get(r).get(c) + " ");
            }
            System.out.println();
        }
    }
}