package ttv.migami.jeg.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/** Replacement for ItemStack.ATTRIBUTE_MODIFIER_FORMAT, which no longer exists in 1.21. */
public final class NumberFormats {
    public static final DecimalFormat ATTRIBUTE_MODIFIER_FORMAT = new DecimalFormat("#.##", DecimalFormatSymbols.getInstance(Locale.ROOT));

    private NumberFormats() {}
}
