public class Movement
{

    int xMovement;

    int rotation;
    public Movement()
    {
        xMovement = rotation = 0;
    }
    public int getxMovement()
    {
        return xMovement;
    }
    public int getRotation()
    {
        return rotation;
    }
    public void setMovements(int deltaX, int newRotation)
    {
        xMovement = deltaX;
        rotation = newRotation;

    }
}