package cn.blockforge.generated.semensyringe;

import com.mojang.serialization.MapCodec;
import java.util.List;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public final class FillSyringeRecipe implements CraftingRecipe {
    private static final MapCodec<FillSyringeRecipe> CODEC = CraftingBookCategory.CODEC.fieldOf("category")
            .xmap(FillSyringeRecipe::new, FillSyringeRecipe::category);
    private static final StreamCodec<RegistryFriendlyByteBuf, CraftingBookCategory> CATEGORY_STREAM_CODEC =
            ByteBufCodecs.fromCodecWithRegistries(CraftingBookCategory.CODEC);
    private static final StreamCodec<RegistryFriendlyByteBuf, FillSyringeRecipe> STREAM_CODEC = CATEGORY_STREAM_CODEC
            .map(FillSyringeRecipe::new, FillSyringeRecipe::category);
    public static final RecipeSerializer<FillSyringeRecipe> SERIALIZER = new RecipeSerializer<>(CODEC, STREAM_CODEC);

    private final CraftingBookCategory category;

    public FillSyringeRecipe(CraftingBookCategory category) {
        this.category = category;
    }

    @Override
    public boolean matches(CraftingInput input, Level level) {
        return find(input) != null;
    }

    @Override
    public ItemStack assemble(CraftingInput input) {
        Match match = find(input);
        return match == null ? ItemStack.EMPTY : SyringeLogic.makeFilled(match.syringe(), match.bottle());
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingInput input) {
        NonNullList<ItemStack> remainders = NonNullList.withSize(input.size(), ItemStack.EMPTY);
        Match match = find(input);
        if (match != null) {
            remainders.set(match.bottleSlot(), new ItemStack(Items.GLASS_BOTTLE));
        }
        return remainders;
    }

    @Override
    public CraftingBookCategory category() {
        return this.category;
    }

    @Override
    public String group() {
        return "";
    }

    @Override
    public PlacementInfo placementInfo() {
        return PlacementInfo.create(List.of(Ingredient.of(SemenSyringeMod.SYRINGE), Ingredient.of(Items.POTION)));
    }

    @Override
    public RecipeSerializer<FillSyringeRecipe> getSerializer() {
        return SERIALIZER;
    }

    private static Match find(CraftingInput input) {
        ItemStack syringe = ItemStack.EMPTY;
        ItemStack bottle = ItemStack.EMPTY;
        int bottleSlot = -1;
        for (int slot = 0; slot < input.size(); slot++) {
            ItemStack stack = input.getItem(slot);
            if (stack.isEmpty()) {
                continue;
            }
            if (syringe.isEmpty() && stack.is(SemenSyringeMod.SYRINGE)) {
                syringe = stack;
            } else if (bottle.isEmpty() && NonNatureBridge.isLiquidBottle(stack)) {
                bottle = stack;
                bottleSlot = slot;
            } else {
                return null;
            }
        }
        if (syringe.isEmpty() || bottle.isEmpty()) {
            return null;
        }
        SyringeContents contents = syringe.get(SyringeDataComponents.CONTENTS);
        if (contents != null && contents.milliliters() >= SyringeLogic.SYRINGE_CAPACITY) {
            return null;
        }
        return new Match(syringe, bottle, bottleSlot);
    }

    private record Match(ItemStack syringe, ItemStack bottle, int bottleSlot) {
    }
}
