import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Galatex on 25.5.2016.
 */
public class Path
{
    Path(List<Point[]> _list){

        for(Point[] p : _list)
        {
            MoveValue m = Manager.ref.getMoveValue(p);
            path.add(m);
            path_value += m.value;
        }
    };

    Path(){};

    void add(Point[] p){
        MoveValue m = Manager.ref.getMoveValue(p);
        path.add(m);
        path_value += m.value;
    }

    void clear(){path.clear();path_value = 0;}

    List<MoveValue> path = new ArrayList<>();
    int path_value = 0;
}
