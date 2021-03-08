public class Perceptron
{
    private int inputNodes, hiddenNodes, outputNodes, bias_h, bias_o;
    private Matrix input, hidden, output;
    private DNA nGenomes;
    public Perceptron(int iNodes, int hNodes, int oNodes)
    {
        inputNodes = iNodes;
        hiddenNodes = hNodes;
        outputNodes = oNodes;
        bias_h = bias_o = 1;
        nGenomes = new DNA();
        nGenomes.setLayers(inputNodes, hiddenNodes, outputNodes);
    }
    public double feedForward(Matrix input)
    {
        hidden = setFeedForward(nGenomes.getwHiddenInput(), input, bias_h);
        output = setFeedForward(nGenomes.getwOutputHidden(), hidden, bias_o);
        return output.getDataAt(0, 0);
    }

    public DNA getNGenomes() {
        return nGenomes;
    }
    public void setNGenomes(DNA dna)
    {
        nGenomes = dna;
    }


    public Matrix setFeedForward(Matrix curLayer, Matrix prevLayer, int bias)
    {
        Matrix layer = curLayer.multi(prevLayer);
        double value = 0;
        for (int r = 0; r < layer.getRow(); r++)
        {
            for (int c = 0; c < layer.getColumn(); c++)
            {
                value = Math.tanh(layer.getDataAt(r, c) + bias);
                layer.setValueAt(value, r, c);
            }
        }
        return layer;
    }
    public void randomize()
    {
        nGenomes.randomize();
    }
    public Perceptron crossOver(Perceptron bBrain)
    {
        Perceptron childBrain = new Perceptron(inputNodes, hiddenNodes, outputNodes);
        DNA childGenome;
        childGenome = nGenomes.crossOver(bBrain.getNGenomes());
        childBrain.setNGenomes(childGenome);
        return childBrain;
    }
    public void mutate()
    {
       Matrix hidInput = nGenomes.mutate(nGenomes.getwHiddenInput());
       Matrix outHid = nGenomes.mutate(nGenomes.getwOutputHidden());
       nGenomes.setNewLayers(hidInput, outHid);
    }
    public void display()
    {
        nGenomes.display();
    }
    public void setLayer(Matrix inputLayers, Matrix HiddenLayer)
    {
        nGenomes.setNewLayers(inputLayers, HiddenLayer);
    }
    public Matrix getInputLayer()
    {
       return nGenomes.getInputLayer();
    }
    public Matrix getHiddenLayer()
    {
        return nGenomes.getHiddenLayer();
    }
}