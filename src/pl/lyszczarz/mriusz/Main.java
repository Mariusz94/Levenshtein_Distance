package pl.lyszczarz.mriusz;

/**
 * Program calculate Levenshtein distance
 *
 * @author Mariusz Lyszczarz
 */
public class Main {
    public static void main(String[] args) {
        Main main = new Main();
        System.out.println("kitten" + " " + "sitting");
        System.out.println(main.calculateLevenshteinDistance("kitten".toCharArray(), "sitting".toCharArray()));

    }

    /**
     * Method calculate Levenshtein distance
     *
     * @param oneWord    First word in char array
     * @param secondWord Second word in char array
     * @return Return int value of Levenshtein distance
     */
    int calculateLevenshteinDistance(char[] oneWord, char[] secondWord) {
        int[][] d = new int[oneWord.length + 1][secondWord.length + 1];

        for (int i = 0; i < d.length; i++)
            d[i][0] = i;

        for (int i = 0; i < d[1].length; i++)
            d[0][i] = i;

        for (int i = 1; i < d.length; i++) {
            for (int j = 1; j < d[1].length; j++) {
                if (oneWord[i - 1] == secondWord[j - 1])
                    d[i][j] = d[i - 1][j - 1];
                else
                    d[i][j] = getMinimum(d[i - 1][j], d[i][j - 1], d[i - 1][j - 1]) + 1;
            }
        }

        //printArray(d,oneWord,secondWord);

        return d[d.length - 1][d[1].length - 1];
    }

    /**
     * Method return minimum number
     *
     * @param left   Type left ceil of main ceil
     * @param upper  Type upper ceil of main ceil
     * @param corner Type diagonal ceil of main ceil
     * @return Return int value minimum number
     */
    int getMinimum(int left, int upper, int corner) {
        int min = left;
        if (upper < min) min = upper;
        if (corner < min) min = corner;
        return min;
    }

    void printArray(int array[][], char mainWord[], char toCompareWord[]) {
        System.out.print("      ");
        for (char c : toCompareWord) {
            System.out.print(c + "  ");
        }
        System.out.println();
        System.out.print("   ");
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[1].length; j++) {
                System.out.print(array[i][j] + "  ");
            }
            System.out.println();
            if (i == mainWord.length) continue;
            System.out.print(mainWord[i] + "  ");
        }
    }
}
