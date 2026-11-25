package ttv.migami.jeg.network.message;

import com.mrcrayfish.framework.api.network.MessageContext;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;

public class C2SMessageChargeSync {

    public static final StreamCodec<RegistryFriendlyByteBuf, C2SMessageChargeSync> STREAM_CODEC = StreamCodec.of((buf, msg) -> encode(msg, buf), C2SMessageChargeSync::decode);
    private float chargeProgress;

    public C2SMessageChargeSync() {}

    public C2SMessageChargeSync(float chargeProgress) {
        this.chargeProgress = chargeProgress;
    }

    public static void encode(C2SMessageChargeSync message, RegistryFriendlyByteBuf buffer) {
        buffer.writeFloat(message.chargeProgress);
    }

    public static C2SMessageChargeSync decode(RegistryFriendlyByteBuf buffer) {
        return new C2SMessageChargeSync(buffer.readFloat());
    }

    public static void handle(C2SMessageChargeSync message, MessageContext context) {
        context.execute(() -> {
            ServerPlayer player = (ServerPlayer) context.getPlayer().orElse(null);
            if(player != null) {
                ttv.migami.jeg.common.ChargeTracker.setServerChargeProgress(player.getUUID(), message.chargeProgress);
            }
        });
        context.setHandled(true);
    }

    public float getChargeProgress() {
        return this.chargeProgress;
    }
}