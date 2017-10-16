package io.julian.imitate.jikelike.util;

import android.content.res.Resources;

/**
 * @author Zhu Liang
 */

public class DimenUtils {

    public static int dpToPx(int dpi) {
        return (int) (Resources.getSystem().getDisplayMetrics().density * dpi);
    }
}
