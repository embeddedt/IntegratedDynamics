package org.cyclops.integrateddynamics.core.helper;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import net.minecraft.ResourceLocationException;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.cyclops.cyclopscore.datastructure.DimPos;
import org.cyclops.cyclopscore.helper.BlockEntityHelpers;
import org.cyclops.cyclopscore.helper.FluidHelpers;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Helper methods.
 * @author rubensworks
 */
public final class Helpers {

    public static final Predicate<Entity> SELECTOR_IS_PLAYER = entity -> entity instanceof Player;

    /**
     * Get the fluidstack from the given itemstack.
     * @param itemStack The itemstack.
     * @return The fluidstack or null.
     */
    public static FluidStack getFluidStack(ItemStack itemStack) {
        FluidStack fluidStack = FluidUtil.getFluidContained(itemStack).orElse(FluidStack.EMPTY);
        if (fluidStack.isEmpty()
                && itemStack.getItem() instanceof BlockItem
                && ((BlockItem) itemStack.getItem()).getBlock() instanceof LiquidBlock) {
            fluidStack = new FluidStack(((LiquidBlock) ((BlockItem) itemStack.getItem()).getBlock()).fluid, FluidHelpers.BUCKET_VOLUME);
        }
        return fluidStack;
    }

    /**
     * Get the fluidstack capacity from the given itemstack.
     * @param itemStack The itemstack.
     * @return The capacity
     */
    public static int getFluidStackCapacity(ItemStack itemStack) {
        IFluidHandler fluidHandler = FluidUtil.getFluidHandler(itemStack).orElse(null);
        if (fluidHandler != null) {
            if (fluidHandler.getTanks() > 0) {
                return fluidHandler.getTankCapacity(0);
            }
        }
        return 0;
    }

    /**
     * Retrieves a Stream of items that are registered to this tag name.
     *
     * @param name The tag name, directly calls OreDictionary.getOres
     * @return A Stream containing ItemStacks registered for this ore
     */
    public static Stream<ItemStack> getTagValues(String name) throws ResourceLocationException {
        Optional<HolderSet.Named<Item>> tag = BuiltInRegistries.ITEM
                .getTag(TagKey.create(Registries.ITEM, ResourceLocation.parse(name)));
        return tag.stream().flatMap(named -> named.stream().map(ItemStack::new));
    }

    /**
     * Add the given element to a copy of the given list/
     * @param list The list.
     * @param newElement The element.
     * @param <T> The type.
     * @return The new joined list.
     */
    public static <T> List<T> joinList(List<T> list, T newElement) {
        ImmutableList.Builder<T> builder = ImmutableList.<T>builder().addAll(list);
        if(newElement != null) {
            builder.add(newElement);
        }
        return builder.build();
    }

    /**
     * Create a string of 'length' times '%s' seperated by ','.
     * @param length The length for the series of '%s'.
     * @return The string.
     */
    public static String createPatternOfLength(int length) {
        StringBuilder pattern = new StringBuilder();
        boolean first = true;
        for (int i = 0; i < length; i++) {
            if (first) {
                first = false;
            } else {
                pattern.append(",");
            }
            pattern.append("%s");
        }
        return pattern.toString();
    }

    private static final List<IInterfaceRetriever> INTERFACE_RETRIEVERS = Lists.newArrayList();
    static {
        addInterfaceRetriever(BlockEntityHelpers::get);
    }

    /**
     * Check for the given interface at the given position.
     * @param world The world.
     * @param pos The position.
     * @param clazz The class to find.
     * @param <C> The class type.
     * @return The optional instance.
     */
    private static <C> Optional<C> getInterface(BlockGetter world, BlockPos pos, Class<C> clazz) {
        for(IInterfaceRetriever interfaceRetriever : INTERFACE_RETRIEVERS) {
            Optional<C> optionalInstance = interfaceRetriever.getInterface(world, pos, clazz);
            if(optionalInstance.isPresent()) {
                return optionalInstance;
            }
        }
        return Optional.empty();
    }

    /**
     * Check for the given interface at the given position.
     * @param dimPos The dimensional position.
     * @param clazz The class to find.
     * @param forceLoad If the world should be loaded if it was not loaded yet.
     * @param <C> The class type.
     * @return The optional instance.
     */
    public static <C> Optional<C> getInterface(DimPos dimPos, Class<C> clazz, boolean forceLoad) {
        Level world = dimPos.getLevel(forceLoad);
        return world != null ? getInterface(world, dimPos.getBlockPos(), clazz) : Optional.empty();
    }

    /**
     * Get a localized string showing the ratio of stored energy vs the capacity.
     * @param stored The stored amount of energy.
     * @param capacity The capacity of the energy container.
     * @return The localized string.
     */
    public static Component getLocalizedEnergyLevel(int stored, int capacity) {
        return Component.literal(String.format(Locale.ROOT, "%,d", stored))
                .append(" / ")
                .append(String.format(Locale.ROOT, "%,d", capacity))
                .append(" ")
                .append(Component.translatable(L10NValues.GENERAL_ENERGY_UNIT));
    }

    // This is copied from Forge's TPSCommand
    public static double calculateTps(long[] times) {
        double worldTickTime = mean(times) * 1.0E-6D;
        double worldTPS = Math.min(1000.0 / worldTickTime, 20);
        return worldTPS;
    }

    public static long mean(long[] values) {
        long sum = 0L;
        for (long v : values)
            sum += v;
        return sum / values.length;
    }

    public static void addInterfaceRetriever(IInterfaceRetriever interfaceRetriever) {
        INTERFACE_RETRIEVERS.add(interfaceRetriever);
    }

    /**
     * Return a string with the first character capitalized.
     * @param value A string.
     * @return A capitalized string.
     */
    public static String capitalizeString(String value) {
        if (value == null) {
            return null;
        } else if (value.length() == 0) {
            return "";
        } else {
            return Character.toTitleCase(value.charAt(0)) + value.substring(1);
        }
    }

    public static interface IInterfaceRetriever {

        /**
         * Attempt to get a given interface instance.
         * @param world The world.
         * @param pos The position.
         * @param clazz The class to find.
         * @param <C> The class type.
         * @return The optional instance.
         */
        public <C> Optional<C> getInterface(BlockGetter world, BlockPos pos, Class<C> clazz);

    }

    @SuppressWarnings("unchecked")
    public static <T extends Exception, R> R sneakyThrow(Exception t) throws T {
        throw (T) t;
    }

}
