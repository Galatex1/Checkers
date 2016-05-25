/**
 * Created by Galatex on 16.5.2016.
 */
enum PlayerSelect {

    P1(false),
    P2(true);

    private final boolean which;

    PlayerSelect(boolean which) {
        this.which = which;
    }

    public boolean getBool()
    {
        return this.which;
    }

    public int getInt()
    {
        return (!this.which ? 0 : 1);
    }
}

