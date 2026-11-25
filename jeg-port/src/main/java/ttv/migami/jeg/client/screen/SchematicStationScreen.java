package ttv.migami.jeg.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.*;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.minecraft.core.registries.BuiltInRegistries;
import org.lwjgl.opengl.GL11;
import ttv.migami.jeg.Reference;
import ttv.migami.jeg.util.StackData;
import ttv.migami.jeg.client.util.RenderUtil;
import ttv.migami.jeg.common.container.SchematicStationMenu;
import ttv.migami.jeg.item.AdvancedBlueprintItem;
import ttv.migami.jeg.item.BlueprintItem;
import ttv.migami.jeg.item.GunItem;

import java.util.Arrays;

public class SchematicStationScreen extends AbstractContainerScreen<SchematicStationMenu> {
   private static final ResourceLocation BG_LOCATION = ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "textures/gui/schematic_station.png");
   private ItemStack displayStack;

   public SchematicStationScreen(SchematicStationMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
      super(pMenu, pPlayerInventory, pTitle);
      this.titleLabelY -= 2;
   }

   @Override
   public void containerTick() {
      super.containerTick();
      this.updateItem();
   }

   private void updateItem() {
      if (this.menu.getSlot(0).hasItem() && StackData.has(this.menu.getSlot(0).getItem())) {
         if (this.menu.getSlot(0).getItem().getItem() instanceof BlueprintItem) {
            ItemStack blueprintStack = this.menu.getSlot(0).getItem();

            String namespace = StackData.get(blueprintStack).getString("Namespace");
            String path = StackData.get(blueprintStack).getString("Path");

            ResourceLocation itemLocation = ResourceLocation.fromNamespaceAndPath(namespace, path);
            Item item = BuiltInRegistries.ITEM.get(itemLocation);

            if (item != Items.AIR) {
               this.displayStack = item.getDefaultInstance();
            }
         }
         else if (this.menu.getSlot(0).getItem().getItem() instanceof GunItem) {
            this.displayStack = this.menu.getSlot(0).getItem().getItem().getDefaultInstance();
         }
      } else {
         this.displayStack = null;
      }
   }

   /**
    * Renders the graphical user interface (GUI) element.
    * @param pGuiGraphics the GuiGraphics object used for rendering.
    * @param pMouseX the x-coordinate of the mouse cursor.
    * @param pMouseY the y-coordinate of the mouse cursor.
    * @param pPartialTick the partial tick time.
    */
   public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
      this.renderBackground(pGuiGraphics, pMouseX, pMouseY, pPartialTick); // PORT: moved here from renderBg (1.21 4-arg form)
      super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
      this.renderTooltip(pGuiGraphics, pMouseX, pMouseY);

      int startX = (this.width - this.imageWidth) / 2;
      int startY = (this.height - this.imageHeight) / 2;

      if(RenderUtil.isMouseWithin(pMouseX, pMouseY, startX + 15, startY + 52, 16, 16))
      {
         if(this.menu.getSlot(0).getItem().isEmpty() && this.menu.getCarried().getItem() instanceof GunItem gunItem && !gunItem.getGun().getGeneral().canBeBlueprinted())
         {
            pGuiGraphics.renderComponentTooltip(this.font, Arrays.asList(Component.translatable("slot.jeg.blueprint.incompatible").withStyle(ChatFormatting.YELLOW)), pMouseX, pMouseY);
         }
         else if(this.menu.getSlot(0).getItem().isEmpty() && this.menu.getCarried().getItem() instanceof AdvancedBlueprintItem)
         {
            pGuiGraphics.renderComponentTooltip(this.font, Arrays.asList(Component.translatable("slot.jeg.blueprint.advanced").withStyle(ChatFormatting.YELLOW)), pMouseX, pMouseY);
         }
         else if(this.menu.getSlot(0).getItem().isEmpty())
         {
            pGuiGraphics.renderComponentTooltip(this.font, Arrays.asList(Component.translatable("slot.jeg.blueprint.blueprint_help").withStyle(ChatFormatting.WHITE)), pMouseX, pMouseY);
         }
      }
      if(RenderUtil.isMouseWithin(pMouseX, pMouseY, startX + 15, startY + 15, 16, 16))
      {
         if(this.menu.getSlot(1).getItem().isEmpty() && this.menu.getCarried().getItem() instanceof AdvancedBlueprintItem)
         {
            pGuiGraphics.renderComponentTooltip(this.font, Arrays.asList(Component.translatable("slot.jeg.blueprint.overwrite").withStyle(ChatFormatting.YELLOW)), pMouseX, pMouseY);
         }
         else if(this.menu.getSlot(1).getItem().isEmpty())
         {
            pGuiGraphics.renderComponentTooltip(this.font, Arrays.asList(Component.translatable("slot.jeg.blueprint.blank_help").withStyle(ChatFormatting.WHITE)), pMouseX, pMouseY);
         }
      }
   }

   protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
      int i = this.leftPos;
      int j = this.topPos;
      pGuiGraphics.blit(BG_LOCATION, i, j, 0, 0, this.imageWidth, this.imageHeight);
      ItemStack itemstack = this.menu.getSlot(1).getItem();
      boolean flag = itemstack.getItem() instanceof BlueprintItem;
      boolean flag1 = itemstack.getItem() instanceof BlueprintItem;
      boolean flag2 = itemstack.getItem() instanceof GunItem;
      ItemStack itemstack1 = this.menu.getSlot(0).getItem();
      MapItemSavedData mapitemsaveddata;
      if (itemstack1.is(Items.FILLED_MAP)) {
         mapitemsaveddata = MapItem.getSavedData(itemstack1, this.minecraft.level); // PORT-VERIFY: 1.21.1 MapItem.getSavedData(ItemStack, Level)
         if (mapitemsaveddata != null) {
            if (mapitemsaveddata.locked) {
               if (flag1 || flag2) {
                  pGuiGraphics.blit(BG_LOCATION, i + 35, j + 31, this.imageWidth + 50, 132, 28, 21);
               }
            }

            if (flag1 && mapitemsaveddata.scale >= 4) {
               pGuiGraphics.blit(BG_LOCATION, i + 35, j + 31, this.imageWidth + 50, 132, 28, 21);
            }
         }
      }

      int startX = this.leftPos;
      int startY = this.topPos;

      if (this.displayStack != null) {
         ItemStack currentItem = this.displayStack;
         GL11.glEnable(GL11.GL_SCISSOR_TEST);
         RenderUtil.scissor(startX + 63, startY + 15, 74, 57);

         PoseStack modelViewStack = pGuiGraphics.pose(); // PORT: 1.21 model-view stack is a JOML Matrix4fStack; use the GUI pose stack
         modelViewStack.pushPose();
         {
            modelViewStack.translate(startX + 100, startY + 40, 100);
            modelViewStack.scale(40F, -40F, 40F);
            modelViewStack.mulPose(Axis.XP.rotationDegrees(5F));
            modelViewStack.mulPose(Axis.YP.rotationDegrees(Minecraft.getInstance().player.tickCount + pPartialTick));
            MultiBufferSource.BufferSource buffer = this.minecraft.renderBuffers().bufferSource();
            Minecraft.getInstance().getItemRenderer().render(currentItem, ItemDisplayContext.FIXED, false, pGuiGraphics.pose(), buffer, 15728880, OverlayTexture.NO_OVERLAY, RenderUtil.getModel(currentItem));
            buffer.endBatch();
         }
         modelViewStack.popPose();

         GL11.glDisable(GL11.GL_SCISSOR_TEST);
      }
   }
}