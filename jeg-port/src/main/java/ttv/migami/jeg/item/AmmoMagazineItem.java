package ttv.migami.jeg.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import ttv.migami.jeg.Config;
import ttv.migami.jeg.Reference;
import ttv.migami.jeg.util.MagazineAmmo;
import ttv.migami.jeg.util.StackData;

import java.util.List;
import java.util.Set;

/**
 * JEG: Modern - a Magazine stores loose bullets of one kind. While magazine feed is enabled, guns reload from the
 * magazines in the player's inventory instead of from loose ammo (see {@link MagazineAmmo}).
 * <p>
 * Hold right-click to load rounds from your offhand (or inventory); sneak + right-click unloads it again.
 */
public class AmmoMagazineItem extends Item
{
    public static final String AMMO_KEY = "MagAmmoItem";
    public static final String COUNT_KEY = "MagAmmoCount";

    /** Ticks between each round being loaded while the use key is held. */
    private static final int LOAD_INTERVAL = 3;

    public enum Family
    {
        PISTOL("pistol_ammo"),
        RIFLE("rifle_ammo", "blaze_round", "spectre_round"),
        MACHINE_GUN("rifle_ammo", "blaze_round", "spectre_round"),
        MINIGUN("pistol_ammo");

        private final Set<ResourceLocation> ammo;

        Family(String... ammoIds)
        {
            this.ammo = Set.of(java.util.Arrays.stream(ammoIds).map(Reference::id).toArray(ResourceLocation[]::new));
        }

        public boolean supports(ResourceLocation id)
        {
            return this.ammo.contains(id);
        }
    }

    /** Capacity, plus how much slower the magazine is to swap in and how much worse it makes recoil (educated guesses). */
    public enum Type
    {
        PISTOL(Family.PISTOL, 16, 1.0F, 1.0F),
        PISTOL_EXTENDED(Family.PISTOL, 30, 1.2F, 1.1F),
        PISTOL_DRUM(Family.PISTOL, 60, 1.5F, 1.25F),
        RIFLE(Family.RIFLE, 30, 1.0F, 1.0F),
        RIFLE_EXTENDED(Family.RIFLE, 60, 1.25F, 1.1F),
        RIFLE_DRUM(Family.RIFLE, 100, 1.6F, 1.3F),
        MACHINE_GUN(Family.MACHINE_GUN, 100, 1.0F, 1.0F),
        MINIGUN(Family.MINIGUN, 200, 1.0F, 1.0F);

        private final Family family;
        private final int capacity;
        private final float reloadMultiplier;
        private final float recoilMultiplier;

        Type(Family family, int capacity, float reloadMultiplier, float recoilMultiplier)
        {
            this.family = family;
            this.capacity = capacity;
            this.reloadMultiplier = reloadMultiplier;
            this.recoilMultiplier = recoilMultiplier;
        }

        public int capacity()
        {
            return this.capacity;
        }

        public Family family()
        {
            return this.family;
        }

        public float reloadMultiplier()
        {
            return this.reloadMultiplier;
        }

        public float recoilMultiplier()
        {
            return this.recoilMultiplier;
        }

        public boolean supports(ResourceLocation id)
        {
            return this.family.supports(id);
        }
    }

    private final Type type;

    public AmmoMagazineItem(Properties properties, Type type)
    {
        super(properties);
        this.type = type;
    }

    public Type getType()
    {
        return this.type;
    }

    public int getCapacity()
    {
        return this.type.capacity();
    }

    public int getAmmoCount(ItemStack stack)
    {
        return Mth.clamp(StackData.getInt(stack, COUNT_KEY), 0, this.type.capacity());
    }

    /** The ammo item stored in this magazine, or null when empty. */
    public ResourceLocation getAmmoId(ItemStack stack)
    {
        if(getAmmoCount(stack) <= 0)
        {
            return null;
        }
        String id = StackData.getString(stack, AMMO_KEY);
        return id.isEmpty() ? null : ResourceLocation.tryParse(id);
    }

    /** Adds one round of the given ammo. Returns false if it doesn't fit or doesn't match. */
    public boolean addRound(ItemStack magazine, ResourceLocation ammoId)
    {
        if(!this.type.supports(ammoId))
        {
            return false;
        }
        int count = getAmmoCount(magazine);
        if(count >= this.type.capacity())
        {
            return false;
        }
        ResourceLocation stored = getAmmoId(magazine);
        if(stored != null && !stored.equals(ammoId))
        {
            return false;
        }
        StackData.putString(magazine, AMMO_KEY, ammoId.toString());
        StackData.putInt(magazine, COUNT_KEY, count + 1);
        return true;
    }

    /** Removes up to {@code amount} rounds and returns how many were removed. */
    public int removeRounds(ItemStack magazine, int amount)
    {
        int count = getAmmoCount(magazine);
        int removed = Math.min(count, amount);
        if(removed <= 0)
        {
            return 0;
        }
        int left = count - removed;
        StackData.putInt(magazine, COUNT_KEY, left);
        if(left <= 0)
        {
            StackData.removeKey(magazine, AMMO_KEY);
            StackData.removeKey(magazine, COUNT_KEY);
        }
        return removed;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand)
    {
        ItemStack stack = player.getItemInHand(hand);

        if(player.isShiftKeyDown())
        {
            ResourceLocation ammoId = getAmmoId(stack);
            if(ammoId == null)
            {
                return InteractionResultHolder.pass(stack);
            }
            if(!level.isClientSide)
            {
                Item ammoItem = BuiltInRegistries.ITEM.get(ammoId);
                int rounds = removeRounds(stack, Integer.MAX_VALUE);
                while(rounds > 0)
                {
                    int give = Math.min(rounds, ammoItem.getDefaultMaxStackSize());
                    ItemStack ammoStack = new ItemStack(ammoItem, give);
                    if(!player.getInventory().add(ammoStack))
                    {
                        player.drop(ammoStack, false);
                    }
                    rounds -= give;
                }
                level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.6F, 0.8F);
            }
            return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
        }

        if(getAmmoCount(stack) >= this.type.capacity())
        {
            return InteractionResultHolder.pass(stack);
        }

        player.startUsingItem(hand);
        return InteractionResultHolder.consume(stack);
    }

    @Override
    public void onUseTick(Level level, LivingEntity entity, ItemStack stack, int remainingUseDuration)
    {
        if(level.isClientSide || !(entity instanceof Player player))
        {
            return;
        }
        int used = getUseDuration(stack, entity) - remainingUseDuration;
        if(used % LOAD_INTERVAL != 0)
        {
            return;
        }

        if(getAmmoCount(stack) >= this.type.capacity())
        {
            player.stopUsingItem();
            return;
        }

        ItemStack source = findAmmoSource(player, stack);
        if(source.isEmpty())
        {
            player.stopUsingItem();
            return;
        }

        ResourceLocation ammoId = BuiltInRegistries.ITEM.getKey(source.getItem());
        if(addRound(stack, ammoId))
        {
            if(!player.getAbilities().instabuild)
            {
                source.shrink(1);
            }
            level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.CROSSBOW_LOADING_MIDDLE.value(), SoundSource.PLAYERS, 0.5F, 1.4F + level.random.nextFloat() * 0.2F);
        }
        else
        {
            player.stopUsingItem();
        }
    }

    /** Offhand first, then the rest of the inventory; only ammo this magazine accepts. */
    private ItemStack findAmmoSource(Player player, ItemStack magazine)
    {
        ItemStack offhand = player.getOffhandItem();
        if(accepts(magazine, offhand))
        {
            return offhand;
        }
        for(int i = 0; i < player.getInventory().items.size(); i++)
        {
            ItemStack candidate = player.getInventory().items.get(i);
            if(accepts(magazine, candidate))
            {
                return candidate;
            }
        }
        return ItemStack.EMPTY;
    }

    private boolean accepts(ItemStack magazine, ItemStack ammo)
    {
        if(ammo.isEmpty())
        {
            return false;
        }
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(ammo.getItem());
        if(!this.type.supports(id))
        {
            return false;
        }
        ResourceLocation stored = getAmmoId(magazine);
        return stored == null || stored.equals(id);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack)
    {
        return UseAnim.NONE;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity)
    {
        return 72000;
    }

    @Override
    public boolean isBarVisible(ItemStack stack)
    {
        return getAmmoCount(stack) > 0;
    }

    @Override
    public int getBarWidth(ItemStack stack)
    {
        return Math.round(13.0F * getAmmoCount(stack) / this.type.capacity());
    }

    @Override
    public int getBarColor(ItemStack stack)
    {
        return 0xC8A84B;
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag)
    {
        tooltip.add(Component.translatable("info.jeg.magazine.rounds", getAmmoCount(stack), this.type.capacity()).withStyle(ChatFormatting.GRAY));
        ResourceLocation id = getAmmoId(stack);
        Component ammoName = id == null
                ? Component.translatable("info.jeg.magazine.empty").withStyle(ChatFormatting.DARK_GRAY)
                : new ItemStack(BuiltInRegistries.ITEM.get(id)).getHoverName();
        tooltip.add(Component.translatable("info.jeg.magazine.loaded_with", ammoName).withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("info.jeg.magazine.usage").withStyle(ChatFormatting.DARK_GRAY));
        if(!Config.magazineFeedEnabled())
        {
            tooltip.add(Component.translatable("info.jeg.magazine.disabled").withStyle(ChatFormatting.RED));
        }
    }
}
