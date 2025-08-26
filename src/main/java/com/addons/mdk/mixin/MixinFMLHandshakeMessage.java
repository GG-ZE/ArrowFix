package com.addons.mdk.mixin;

import net.minecraftforge.fml.common.network.handshake.FMLHandshakeMessage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

import static com.addons.mdk.ArrowFix.mc;

@SuppressWarnings("rawtypes")
@Mixin(value = FMLHandshakeMessage.ModList.class, remap = false)
public class MixinFMLHandshakeMessage {
    @Shadow
    private Map<String, String> modTags;

    @Inject(method = "<init>(Ljava/util/List;)V", at = @At(value = "RETURN"))
    public void hideModIDs(java.util.List modList, CallbackInfo ci) {
        if (mc.isSingleplayer()) {
            return;
        }
        this.modTags.remove("arrowfix")
    }

}
