import java.awt.*;

/**
 * Created by Galatex on 25.5.2016.
 */
public class MoveValue
{
    public Point move_start;
    public Point move_end;
    public int value;

    MoveValue(Point[] _move, int _value)
    {
        move_start = _move[0];
        move_end = _move[1];
        value = _value;
    }



}
