package processor;

import java.util.ArrayList;

public class OtsuBinaryConverter extends BinaryConverter{

    public static int countThreshold(int size, int[] histogram) {
        int threshold = 0;
        float sum = 0;
        for (int t=0 ; t<histogram.length ; t++) sum += t * histogram[t];

        float sumB = 0;
        int wB = 0;
        int wF = 0;

        float varMax = 0;

        for (int t=0 ; t<histogram.length; t++) {
            wB += histogram[t];               // Weight Background
            if (wB == 0) continue;

            wF = size - wB;                 // Weight Foreground
            if (wF == 0) break;

            sumB += (float) (t * histogram[t]);

            float mB = sumB / wB;            // Mean Background
            float mF = (sum - sumB) / wF;    // Mean Foreground

            // Calculate Between Class Variance
            float varBetween = (float)wB * (float)wF * (mB - mF) * (mB - mF);

            // Check if new maximum found
            if (varBetween > varMax) {
                varMax = varBetween;
                threshold = t;
            }
        }
        return threshold;
    }
}
