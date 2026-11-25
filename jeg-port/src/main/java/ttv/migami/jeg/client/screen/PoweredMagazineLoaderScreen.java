package ttv.migami.jeg.client.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import ttv.migami.jeg.Reference;
import ttv.migami.jeg.common.container.PoweredMagazineLoaderMenu;

/** Powered Magazine Loader screen: fuel + energy bar on the left, ammo -> magazines -> output to the right. */
public class PoweredMagazineLoaderScreen extends AbstractContainerScreen<PoweredMagazineLoaderMenu>
{
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "textures/gui/container/powered_magazine_loader.png");

    private static final int BAR_X = 8;
    private static final int BAR_Y = 20;
    private static final int BAR_W = 12;
    private static final int BAR_H = 54;

    public PoweredMagazineLoaderScreen(PoweredMagazineLoaderMenu menu, Inventory inventory, Component title)
    {
        super(menu, inventory, title);
        this.imageWidth = 230;
        this.imageHeight = 190;
        this.inventoryLabelX = PoweredMagazineLoaderMenu.INV_X;
        this.inventoryLabelY = 96;
        this.titleLabelX = 8;
        this.titleLabelY = 6;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick)
    {
        this.renderBackground(graphics, mouseX, mouseY, partialTick);
        super.render(graphics, mouseX, mouseY, partialTick);
        this.renderTooltip(graphics, mouseX, mouseY);

        int x = mouseX - this.leftPos;
        int y = mouseY - this.topPos;
        if(x >= BAR_X && x < BAR_X + BAR_W && y >= BAR_Y && y < BAR_Y + BAR_H)
        {
            graphics.renderTooltip(this.font, Component.literal(this.menu.getEnergy() + " / " + this.menu.getCapacity() + " RF"), mouseX, mouseY);
        }
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY)
    {
        graphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        int filled = this.menu.getEnergy() * BAR_H / this.menu.getCapacity();
        if(this.menu.getEnergy() > 0)
        {
            filled = Math.max(1, filled);
        }
        if(filled > 0)
        {
            graphics.blit(TEXTURE, this.leftPos + BAR_X, this.topPos + BAR_Y + (BAR_H - filled), 0, 194 + (BAR_H - filled), BAR_W, filled);
        }

        int flame = this.menu.getFlameHeight();
        if(flame > 0)
        {
            graphics.blit(TEXTURE, this.leftPos + 27, this.topPos + 41 + (14 - flame), 14, 194 + (14 - flame), 14, flame);
        }
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY)
    {
        graphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 0xFFD8D2C0, false);
        graphics.drawString(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, 0xFFB8B2A4, false);
    }
}
