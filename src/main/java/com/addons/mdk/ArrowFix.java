package com.addons.mdk;

import com.addons.mdk.commands.ToggleCommand;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

import java.util.HashSet;
import java.util.Set;

@Mod(modid = "arrowfix", useMetadata=true)
public class ArrowFix {
    public static ArrowFix INSTANCE;
    public static Minecraft mc = Minecraft.getMinecraft();
    public static boolean isEnabled = true;
    private static final Set<String> bowCache = new HashSet<>();

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        INSTANCE = this;
        ClientCommandHandler.instance.registerCommand(new ToggleCommand());
    }

    public static void addChatMessage(String msg) {
        try {
            if (mc.thePlayer != null && mc.theWorld != null) {
                mc.addScheduledTask(() -> mc.thePlayer.addChatMessage(new ChatComponentText("§f[§6ArrowFix§f]§r" + " " + msg)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getSkyBlockID(ItemStack item) {
        if(item != null) {
            NBTTagCompound extraAttributes = item.getSubCompound("ExtraAttributes", false);
            if(extraAttributes != null && extraAttributes.hasKey("id")) {
                return extraAttributes.getString("id");
            }
        }
        return null;
    }

    public static boolean isShortbow(ItemStack item) {
        if (item == null || !item.hasTagCompound()) return false;
        if (item.getItem() != Items.BOW) return false;
        String skyblockID = getSkyBlockID(item);
        if (skyblockID == null) return false;
        if (bowCache.contains(skyblockID)) return true;

        NBTTagCompound display = item.getTagCompound().getCompoundTag("display");
        if (!display.hasKey("Lore", Constants.NBT.TAG_LIST)) return false;

        NBTTagList loreNBT = display.getTagList("Lore", Constants.NBT.TAG_STRING);

        for (int i = 0; i < loreNBT.tagCount(); i++) {
            String line = loreNBT.getStringTagAt(i);
            if (line.contains("Shortbow: Instantly shoots!")) {
                bowCache.add(skyblockID);
                return true;
            }
        }

        return false;
    }
}
