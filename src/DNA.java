/*
used for mutating and holding data
 */

import java.util.Random;

public class DNA
{
   private Matrix wHiddenInput, wOutputHidden;
   double mutateRate;
   boolean inheritFromParentA[];

   DNA()
   {
       mutateRate = 0.15;
       wHiddenInput = new Matrix();
       wOutputHidden = new Matrix();
       inheritFromParentA = new boolean[7];
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
        double valueWeight, mutation, value;
        Random rand = new Random();
        for (int r = 0; r < layer.getRow(); r++) {
            value = rand.nextGaussian() + rand.nextInt(1);
            mutation = rand.nextInt(100) / 100.0;
            for (int c = 0; c < layer.getColumn(); c++)
            {
                valueWeight = (mutation <= mutateRate ? value : layer.getDataAt(r, c));
                layer.setValueAt(valueWeight, r, c);
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
        Random rand = new Random();
        for (int r = 0; r < layer.getRow(); r++)
        {
            for (int c = 0; c < layer.getColumn(); c++)
            {
                value = (inheritFromParentA[r] ? layer.getDataAt(r, c) : bLayer.getDataAt(r, c));
                childLayer.setValueAt(value, r, c);

            }

        }
        return childLayer;

    }
    public DNA crossOver(DNA b)
    {
        DNA child = new DNA();
        decideInheritance();
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
    private void decideInheritance()
    {
        Random rand = new Random();
        for (int i = 0; i < inheritFromParentA.length; i++)
        { inheritFromParentA[i] = (rand.nextInt(100) < 90 ? true : false);
        }
    }
    public Matrix getInputLayer()
    {
        return wHiddenInput;
    }
    public Matrix getHiddenLayer()
    {
        return wOutputHidden;
    }

}