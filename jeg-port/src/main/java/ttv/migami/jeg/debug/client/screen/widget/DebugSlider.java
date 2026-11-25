package ttv.migami.jeg.debug.client.screen.widget;

import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.client.gui.widget.ExtendedSlider;
import ttv.migami.jeg.debug.IDebugWidget;

import java.util.function.Consumer;

/**
 * Author: MrCrayfish
 */
// PORT: ForgeSlider -> NeoForge ExtendedSlider (PORT-VERIFY: class/package + same ctor signature)
public class DebugSlider extends ExtendedSlider implements IDebugWidget {
    private final Consumer<Double> callback;

    public DebugSlider(double minValue, double maxValue, double currentValue, double stepSize, int precision, Consumer<Double> callback) {
        super(0, 0, 0, 14, Component.empty(), Component.empty(), minValue, maxValue, currentValue, stepSize, precision, true);
        this.callback = callback;
    }

    @Override
    protected void applyValue() {
        this.callback.accept(this.getValue());
    }

    // PORT-DROPPED: custom handle-only renderWidget (used the removed WIDGETS_LOCATION texture); the default slider rendering is used.
}
