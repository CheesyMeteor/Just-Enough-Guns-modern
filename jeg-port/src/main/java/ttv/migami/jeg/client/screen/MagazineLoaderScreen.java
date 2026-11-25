package ttv.migami.jeg.client.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import ttv.migami.jeg.common.container.MagazineLoaderMenu;

/** Magazine Loader screen: ammo in the top slot, magazine below it, loaded magazine out the right. */
public class MagazineLoaderScreen extends AbstractContainerScreen<MagazineLoaderMenu>
{
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(ttv.migami.jeg.Reference.MOD_ID, "textures/gui/container/magazine_loader.png");

    public MagazineLoaderScreen(MagazineLoaderMenu menu, Inventory inventory, Component title)
    {
        super(menu, inventory, title);
    }

    @Override
    public void init()
    {
        super.init();
        this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick)
    {
        this.renderBackground(graphics, mouseX, mouseY, partialTick);
        super.render(graphics, mouseX, mouseY, partialTick);
        this.renderTooltip(graphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY)
    {
        graphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        int width = this.menu.getProgressWidth();
        if(width > 0)
        {
            graphics.blit(TEXTURE, this.leftPos + 78, this.topPos + 35, 176, 0, width, 16);
        }
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY)
    {
        graphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 0xFFD8D2C0, false);
        graphics.drawString(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, 0xFFB8B2A4, false);
    }
}
