/*
used for mutating and holding data
 */

import java.util.Random;

public class DNA
{
   private Matrix wHiddenInput, wOutputHidden;
   double mutateRate;
   DNA()
   {
       mutateRate = 0.2;
       wHiddenInput = new Matrix();
       wOutputHidden = new Matrix();
   }
   public void setLayers(int inputNodes, int hiddenNodes, int outputNodes)
   {
       wHiddenInput.setMatrix(hiddenNodes, inputNodes);
       wOutputHidden.setMatrix(outputNodes, hiddenNodes);
   }
    public void randomize()
    {
        wHiddenInput.randomize();
        wOutputHidden.randomize();
    }
    public Matrix mutate(Matrix layer)
    {
        double value;
        Random rand = new Random();
        for (int r = 0; r < layer.getRow(); r++) {
            for (int c = 0; c < layer.getColumn(); c++) {
                value = (rand.nextDouble() <= mutateRate ? rand.nextDouble() : layer.getDataAt(r, c));
                layer.setValueAt(value, r, c);
            }

        }
        return layer;
    }
    public void setNewLayers(Matrix w_Hidden_Input, Matrix w_Output_Hidden)
    {
        wHiddenInput = w_Hidden_Input;
        wOutputHidden = w_Output_Hidden;
    }

    public Matrix getwHiddenInput()
    {
        return wHiddenInput;
    }

    public Matrix getwOutputHidden()
    {
        return wOutputHidden;
    }
    public Matrix setChildLayer(Matrix layer, Matrix bLayer)
    {
        Matrix childLayer = new Matrix();
        childLayer.setMatrix(layer.getRow(), layer.getColumn());
        childLayer.randomize();
        double value;
        for (int r = 0; r < layer.getRow(); r++)
        {
            for (int c = 0; c < layer.getColumn(); c++)
            {
                value = ((c + 1) % 2 == 0 ? layer.getDataAt(r, c) : bLayer.getDataAt(r, c));
                childLayer.setValueAt(value, r, c);

            }

        }
        return childLayer;

    }
    public DNA crossOver(DNA b)
    {
        DNA child = new DNA();
        Matrix wHidIn = setChildLayer(getwHiddenInput(), b.getwHiddenInput());
        Matrix wOutHid = setChildLayer(getwOutputHidden(), b.getwOutputHidden());
        child.setNewLayers(wHidIn, wOutHid);
        return child;

    }
    public void display()
    {
        System.out.println("Hidden");
        wHiddenInput.displayMatrix();
        System.out.println("OutPutLayer");
        wOutputHidden.displayMatrix();
    }
}