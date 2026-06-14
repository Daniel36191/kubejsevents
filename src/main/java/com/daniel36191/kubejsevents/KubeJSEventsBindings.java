package com.daniel36191.kubejsevents;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import java.util.Map;

public class KubeJSEventsBindings {

    // ==================== POLLUTANT QUERIES (reflection-safe) ====================

    /** Returns true if the block has a simpleName field (is a Pollutant) */
    public static boolean isPollutant(Level level, BlockPos pos) {
        Block block = level.getBlockState(pos).getBlock();
        try {
            block.getClass().getMethod("getSimpleName");
            return true;
        } catch (NoSuchMethodException e) {
            return false;
        }
    }

    /** Returns true if the block has getPollutantType method (is AbstractGas) */
    public static boolean isGasPollutant(Level level, BlockPos pos) {
        Block block = level.getBlockState(pos).getBlock();
        try {
            block.getClass().getMethod("getPollutantType");
            return true;
        } catch (NoSuchMethodException e) {
            return false;
        }
    }

    /** Returns the carried pollution amount via reflection */
    public static int getPollutionAmount(Level level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        try {
            return (int) state.getBlock().getClass()
                .getMethod("getCarriedPollutionAmount", BlockState.class)
                .invoke(state.getBlock(), state);
        } catch (Exception e) {
            return 0;
        }
    }

    /** Returns the pollution capacity via reflection */
    public static int getPollutionCapacity(Level level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        try {
            return (int) state.getBlock().getClass()
                .getMethod("getPollutionCapacity")
                .invoke(state.getBlock());
        } catch (Exception e) {
            return 0;
        }
    }

    /** Returns the simple name string */
    public static String getPollutantName(Level level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        try {
            return (String) state.getBlock().getClass()
                .getMethod("getSimpleName")
                .invoke(state.getBlock());
        } catch (Exception e) {
            return null;
        }
    }

    /** Returns the ARGB color as int */
    public static int getPollutantColor(Level level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        try {
            Object color = state.getBlock().getClass().getMethod("getColor").invoke(state.getBlock());
            return (int) color.getClass().getMethod("getARGB").invoke(color);
        } catch (Exception e) {
            return 0;
        }
    }

    /** Returns the pollutant type name ("AIR" for gases) or null */
    public static String getPollutantType(Level level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        try {
            Object type = state.getBlock().getClass().getMethod("getPollutantType").invoke(state.getBlock());
            return type.toString();
        } catch (Exception e) {
            return null;
        }
    }

    /** Returns lower explosive limit or -1 */
    public static int getLowerExplosiveLimit(Level level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        try {
            return (int) state.getBlock().getClass()
                .getMethod("getLowerExplosiveLimit")
                .invoke(state.getBlock());
        } catch (Exception e) {
            return -1;
        }
    }

    /** Pump pollution into the block. Returns amount pumped. */
    public static int pumpPollution(LevelAccessor level, BlockPos pos, int amount) {
        BlockState state = level.getBlockState(pos);
        try {
            return (int) state.getBlock().getClass()
                .getMethod("pump", LevelAccessor.class, BlockPos.class, int.class)
                .invoke(state.getBlock(), level, pos, amount);
        } catch (Exception e) {
            return 0;
        }
    }

    /** Pump exactly 1 unit */
    public static boolean pumpPollutionOne(LevelAccessor level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        try {
            return (boolean) state.getBlock().getClass()
                .getMethod("pump", LevelAccessor.class, BlockPos.class)
                .invoke(state.getBlock(), level, pos);
        } catch (Exception e) {
            return false;
        }
    }

    /** Spend pollution. Returns amount spent. */
    public static int spendPollution(LevelAccessor level, BlockPos pos, int amount) {
        BlockState state = level.getBlockState(pos);
        try {
            return (int) state.getBlock().getClass()
                .getMethod("spend", LevelAccessor.class, BlockPos.class, int.class)
                .invoke(state.getBlock(), level, pos, amount);
        } catch (Exception e) {
            return 0;
        }
    }

    /** Spend exactly 1 unit */
    public static boolean spendPollutionOne(LevelAccessor level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        try {
            return (boolean) state.getBlock().getClass()
                .getMethod("spend", LevelAccessor.class, BlockPos.class)
                .invoke(state.getBlock(), level, pos);
        } catch (Exception e) {
            return false;
        }
    }

    /** Remove the pollutant block at pos */
    public static boolean removePollutant(Level level, BlockPos pos) {
        if (isPollutant(level, pos)) {
            level.removeBlock(pos, false);
            return true;
        }
        return false;
    }

    // ==================== FILTER INTERACTIONS ====================

    /** Returns true if the block is a FilterFrame (has getContent + fill methods) */
    public static boolean isFilter(Level level, BlockPos pos) {
        Block block = level.getBlockState(pos).getBlock();
        try {
            block.getClass().getMethod("getContent", BlockEntity.class);
            block.getClass().getMethod("fill", BlockEntity.class, Object.class, int.class);
            return true;
        } catch (NoSuchMethodException e) {
            return false;
        }
    }

    /** Returns true if filter is active */
    public static boolean isFilterActive(Level level, BlockPos pos) {
        BlockEntity be = level.getBlockEntity(pos);
        if (be == null) return false;
        try {
            return (boolean) be.getClass().getMethod("isActive").invoke(be);
        } catch (Exception e) {
            return false;
        }
    }

    /** Get free space in filter for a pollutant type */
    public static int getFilterFreeSpace(Level level, BlockPos filterPos, String type) {
        BlockEntity be = level.getBlockEntity(filterPos);
        BlockState state = level.getBlockState(filterPos);
        if (be == null) return 0;
        try {
            Object content = state.getBlock().getClass()
                .getMethod("getContent", BlockEntity.class)
                .invoke(state.getBlock(), be);
            Object pollutant = getPollutantByType(type);
            if (content != null && pollutant != null) {
                return (int) content.getClass()
                    .getMethod("getFreeSpaceFor", pollutant.getClass().getInterfaces()[0])
                    .invoke(content, pollutant);
            }
        } catch (Exception e) {}
        return 0;
    }

    /** Fill the filter. Returns amount filled. */
    public static int fillFilter(Level level, BlockPos filterPos, String type, int amount) {
        BlockEntity be = level.getBlockEntity(filterPos);
        BlockState state = level.getBlockState(filterPos);
        if (be == null) return 0;
        try {
            Object pollutant = getPollutantByType(type);
            if (pollutant != null) {
                return (int) state.getBlock().getClass()
                    .getMethod("fill", be.getClass(), Object.class, int.class)
                    .invoke(state.getBlock(), be, pollutant, amount);
            }
        } catch (Exception e) {}
        return 0;
    }

    /** Get filter fullness for a specific pollutant */
    public static int getFilterFullness(Level level, BlockPos filterPos, String type) {
        BlockEntity be = level.getBlockEntity(filterPos);
        BlockState state = level.getBlockState(filterPos);
        if (be == null) return 0;
        try {
            Object content = state.getBlock().getClass()
                .getMethod("getContent", BlockEntity.class)
                .invoke(state.getBlock(), be);
            Object pollutant = getPollutantByType(type);
            if (content != null && pollutant != null) {
                return (int) content.getClass()
                    .getMethod("getFullnessWith", pollutant.getClass().getInterfaces()[0])
                    .invoke(content, pollutant);
            }
        } catch (Exception e) {}
        return 0;
    }

    /** Get filter total capacity */
    public static int getFilterCapacity(Level level, BlockPos filterPos) {
        BlockEntity be = level.getBlockEntity(filterPos);
        BlockState state = level.getBlockState(filterPos);
        if (be == null) return 0;
        try {
            Object content = state.getBlock().getClass()
                .getMethod("getContent", BlockEntity.class)
                .invoke(state.getBlock(), be);
            if (content != null) {
                return (int) content.getClass().getMethod("getCapacity").invoke(content);
            }
        } catch (Exception e) {}
        return 0;
    }

    /** Get filter material ItemStack */
    public static ItemStack getFilterMaterial(Level level, BlockPos filterPos) {
        BlockEntity be = level.getBlockEntity(filterPos);
        if (be == null) return ItemStack.EMPTY;
        try {
            return (ItemStack) be.getClass().getMethod("getFilterMaterial").invoke(be);
        } catch (Exception e) {
            return ItemStack.EMPTY;
        }
    }

    /** Get filter byproduct ItemStack */
    public static ItemStack getFilterByproduct(Level level, BlockPos filterPos) {
        BlockEntity be = level.getBlockEntity(filterPos);
        if (be == null) return ItemStack.EMPTY;
        try {
            return (ItemStack) be.getClass().getMethod("getByproduct").invoke(be);
        } catch (Exception e) {
            return ItemStack.EMPTY;
        }
    }

    // ==================== POLLUTED WATER ====================

    /** Returns true if the block class is PollutedWater */
    public static boolean isPollutedWater(Level level, BlockPos pos) {
        return level.getBlockState(pos).getBlock().getClass().getSimpleName().equals("PollutedWater");
    }

    /** Returns true if water is a source block */
    public static boolean isPollutedWaterSource(Level level, BlockPos pos) {
        try {
            Class<?> pwClass = Class.forName("com.endertech.minecraft.mods.adpother.blocks.PollutedWater");
            return (boolean) pwClass.getMethod("isSource", Level.class, BlockPos.class)
                .invoke(null, level, pos);
        } catch (Exception e) {
            return false;
        }
    }

    /** Try to pollute water */
    public static boolean tryPolluteWater(Level level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        try {
            return (boolean) state.getBlock().getClass()
                .getMethod("tryPollute", net.minecraft.server.level.ServerLevel.class, BlockPos.class, BlockState.class)
                .invoke(state.getBlock(), level, pos, state);
        } catch (Exception e) {
            return false;
        }
    }

    // ==================== WORLD POLLUTION DATA ====================

    /** Get chunk pollution quantity */
    public static int getChunkPollution(Level level, BlockPos pos, String type) {
        try {
            Class<?> worldData = Class.forName("com.endertech.minecraft.mods.adpother.pollution.WorldData");
            Object chunkPol = worldData.getMethod("getChunkPollution", Level.class, BlockPos.class)
                .invoke(null, level, pos);
            Object pollutant = getPollutantByType(type);
            if (chunkPol != null && pollutant != null) {
                Object info = chunkPol.getClass()
                    .getMethod("getOrCreateInfoFor", pollutant.getClass().getInterfaces()[0])
                    .invoke(chunkPol, pollutant);
                if (info != null) {
                    return (int) info.getClass().getMethod("getQuantity").invoke(info);
                }
            }
        } catch (Exception e) {}
        return 0;
    }

    /** Get chunk pollution percentage */
    public static double getChunkPollutionPercentage(Level level, BlockPos pos, String type) {
        try {
            Class<?> worldData = Class.forName("com.endertech.minecraft.mods.adpother.pollution.WorldData");
            Class<?> biomeId = Class.forName("com.endertech.minecraft.forge.world.BiomeId");
            Object chunkPol = worldData.getMethod("getChunkPollution", Level.class, BlockPos.class)
                .invoke(null, level, pos);
            Object pollutant = getPollutantByType(type);
            Object biome = biomeId.getMethod("from", Level.class, BlockPos.class).invoke(null, level, pos);
            if (chunkPol != null && pollutant != null && biome != null) {
                Object info = chunkPol.getClass()
                    .getMethod("getOrCreateInfoFor", pollutant.getClass().getInterfaces()[0])
                    .invoke(chunkPol, pollutant);
                if (info != null) {
                    Object pct = info.getClass().getMethod("getPercentageIn", biomeId).invoke(info, biome);
                    return (double) pct.getClass().getMethod("getValue").invoke(pct);
                }
            }
        } catch (Exception e) {}
        return 0.0;
    }

    /** Returns true if position is an ignition source */
    public static boolean isIgnitionSource(Level level, BlockPos pos) {
        try {
            Class<?> worldData = Class.forName("com.endertech.minecraft.mods.adpother.pollution.WorldData");
            return (boolean) worldData.getMethod("isIgnitionSource", Level.class, BlockPos.class)
                .invoke(null, level, pos);
        } catch (Exception e) {
            return false;
        }
    }

    /** Check if entity has protective items (respirators) for a pollutant */
    public static boolean hasProtectiveItems(LivingEntity entity, String type) {
        if (!isGasPollutant(entity.level(), entity.blockPosition())) return false;
        try {
            Object pollutant = getPollutantByType(type);
            if (pollutant == null) return false;
            Map<?, ?> items = (Map<?, ?>) pollutant.getClass()
                .getMethod("getProtectiveItems", LivingEntity.class)
                .invoke(pollutant, entity);
            return items != null && !items.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    /** Get concentration altitude for a gas */
    public static int getConcentrationAltitude(Level level, BlockPos pos) {
        try {
            Class<?> biomeId = Class.forName("com.endertech.minecraft.forge.world.BiomeId");
            Object biome = biomeId.getMethod("from", Level.class, BlockPos.class).invoke(null, level, pos);
            Block block = level.getBlockState(pos).getBlock();
            return (int) block.getClass()
                .getMethod("getConcentrationAltitudeIn", biomeId)
                .invoke(block, biome);
        } catch (Exception e) {
            return 0;
        }
    }

    /** Check if gas is within cloud altitude */
    public static boolean isWithinCloudAltitude(Level level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        try {
            Class<?> biomeId = Class.forName("com.endertech.minecraft.forge.world.BiomeId");
            Object biome = biomeId.getMethod("from", Level.class, BlockPos.class).invoke(null, level, pos);
            return (boolean) state.getBlock().getClass()
                .getMethod("withinCloudAltitude", Level.class, BlockPos.class, biomeId)
                .invoke(state.getBlock(), level, pos, biome);
        } catch (Exception e) {
            return false;
        }
    }

    /** Check if gas is affected by wind */
    public static boolean isAffectedByWind(Level level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        try {
            Class<?> biomeId = Class.forName("com.endertech.minecraft.forge.world.BiomeId");
            Object biome = biomeId.getMethod("from", Level.class, BlockPos.class).invoke(null, level, pos);
            return (boolean) state.getBlock().getClass()
                .getMethod("canBeAffectedByWind", Level.class, BlockPos.class, biomeId)
                .invoke(state.getBlock(), level, pos, biome);
        } catch (Exception e) {
            return false;
        }
    }

    /** Check if excessive pollution should dissipate */
    public static boolean shouldDissipateExcessive(Level level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        try {
            Class<?> biomeId = Class.forName("com.endertech.minecraft.forge.world.BiomeId");
            Object biome = biomeId.getMethod("from", Level.class, BlockPos.class).invoke(null, level, pos);
            return (boolean) state.getBlock().getClass()
                .getMethod("shouldDissipateExcessive", net.minecraft.server.level.ServerLevel.class, BlockPos.class, biomeId)
                .invoke(state.getBlock(), level, pos, biome);
        } catch (Exception e) {
            return false;
        }
    }

    // ==================== BUILT-IN POLLUTANTS ====================

    /** Get a Pollutant instance by name using reflection */
    public static Object getPollutantByType(String name) {
        try {
            Class<?> pollutants = Class.forName("com.endertech.minecraft.mods.adpother.init.Pollutants$BuiltIn");
            Object builtIn = switch (name.toLowerCase()) {
                case "carbon" -> pollutants.getField("CARBON").get(null);
                case "sulfur" -> pollutants.getField("SULFUR").get(null);
                case "dust" -> pollutants.getField("DUST").get(null);
                default -> null;
            };
            if (builtIn != null) {
                return builtIn.getClass().getMethod("get").invoke(builtIn);
            }
        } catch (Exception e) {}
        return null;
    }

    /** Returns array of built-in pollutant names */
    public static String[] getBuiltInPollutantNames() {
        return new String[]{"carbon", "sulfur", "dust"};
    }
}