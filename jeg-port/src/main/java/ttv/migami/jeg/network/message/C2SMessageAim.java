package ttv.migami.jeg.network.message;

import com.mrcrayfish.framework.api.network.MessageContext;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import ttv.migami.jeg.init.ModSyncedDataKeys;

public class C2SMessageAim
{

    public static final StreamCodec<RegistryFriendlyByteBuf, C2SMessageAim> STREAM_CODEC = StreamCodec.of((buf, msg) -> encode(msg, buf), C2SMessageAim::decode);
	private boolean aiming;

	public C2SMessageAim() {}

	public C2SMessageAim(boolean aiming)
	{
		this.aiming = aiming;
	}

	public static void encode(C2SMessageAim message, RegistryFriendlyByteBuf buffer)
	{
		buffer.writeBoolean(message.aiming);
	}

	public static C2SMessageAim decode(RegistryFriendlyByteBuf buffer)
	{
		return new C2SMessageAim(buffer.readBoolean());
	}

	public static void handle(C2SMessageAim message, MessageContext context)
	{
		context.execute(() ->
		{
			ServerPlayer player = (ServerPlayer) context.getPlayer().orElse(null);
			if(player != null && !player.isSpectator())
			{
				ModSyncedDataKeys.AIMING.setValue(player, message.aiming);
			}
		});
		context.setHandled(true);
	}
}
