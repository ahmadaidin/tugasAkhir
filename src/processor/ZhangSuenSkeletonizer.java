package processor;


import java.awt.Point;

import java.util.ArrayList;
import java.util.List;

public class ZhangSuenSkeletonizer {

    final static int[][] nbrs = {{0, -1}, {1, -1}, {1, 0}, {1, 1}, {0, 1},
            {-1, 1}, {-1, 0}, {-1, -1}, {0, -1}};

    final static int[][][] nbrGroups = {{{0, 2, 4}, {2, 4, 6}}, {{0, 2, 6},
            {0, 4, 6}}};

    static List<Point> toBackground = new ArrayList<>();
    static int background = 0;
    static int foreground = 255;
    private static int[][] grayscale;

    public static MyImage skeletonize(MyImage binaryImage) {
        grayscale = ImgFilterer.getGrayscale(binaryImage);
        MyImage result = new MyImage(binaryImage.getHeight(), binaryImage.getWidth());
        for(int y=0; y<binaryImage.getHeight();y++){
            for(int x=0; x<binaryImage.getWidth();x++){
                result.setRGB(x,y,binaryImage.getRGB(x,y));
            }
        }
        boolean firstStep = false;
        boolean hasChanged;

        do {
            hasChanged = false;
            firstStep = !firstStep;

            for (int r = 1; r < binaryImage.getHeight() - 1; r++) {
                for (int c = 1; c < binaryImage.getWidth() - 1; c++) {

                    if (grayscale[r][c] != foreground)
                        continue;

                    int nn = numNeighbors(r, c);
                    if (nn < 2 || nn > 6)
                        continue;

                    if (numTransitions(r, c) != 1)
                        continue;

                    if (!atLeastOneIsWhite(r, c, firstStep ? 0 : 1))
                        continue;

                    toBackground.add(new Point(c, r));
                    hasChanged = true;
                }
            }

            for (Point p : toBackground) {
                grayscale[p.y][p.x] = background;
                int rgb = MyImage.makeRGB(background,background,background);
                result.setRGB(p.x,p.y,rgb);
            }
            toBackground.clear();
        } while (hasChanged);
        return result;
    }

    private static int numNeighbors(int r, int c) {
        int count = 0;
        for (int i = 0; i < nbrs.length - 1; i++)
            if (grayscale[r + nbrs[i][1]][c + nbrs[i][0]] == foreground)
                count++;
        return count;
    }

    private static int numTransitions(int r, int c) {
        int count = 0;
        for (int i = 0; i < nbrs.length - 1; i++)
            if (grayscale[r + nbrs[i][1]][c + nbrs[i][0]] == background) {
                if (grayscale[r + nbrs[i + 1][1]][c + nbrs[i + 1][0]] == foreground)
                    count++;
            }
        return count;
    }

    public static boolean atLeastOneIsWhite(int r, int c, int step) {
        int count = 0;
        int[][] group = nbrGroups[step];
        for (int i = 0; i < 2; i++)
            for (int j = 0; j < group[i].length; j++) {
                int[] nbr = nbrs[group[i][j]];
                if (grayscale[r + nbr[1]][c + nbr[0]] == background ) {
                    count++;
                    break;
                }
            }
        return count > 1;
    }

}
