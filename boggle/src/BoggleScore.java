
public class BoggleScore {
    public int getScore(String s, int boardSize){
        switch (s.length()) {
            case 0:
            case 1:
            case 2:
                return 0;
            case 3:
                return boardSize <= 4 ? 1 : 0;
            case 4:
                return 1;
            case 5:
                return 2;
            case 6:
                return 3;
            case 7:
                return 5;
            default:
                return 11;
        }
    }
}
