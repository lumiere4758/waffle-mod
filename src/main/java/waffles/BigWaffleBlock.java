package waffles;

import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.CakeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class BigWaffleBlock extends CakeBlock {
	public static final MapCodec<BigWaffleBlock> CODEC = simpleCodec(BigWaffleBlock::new);

	// 600 full hunger bars = 1200 nutrition points
	public static final int NUTRITION = 1200;
	public static final float SATURATION = 600.0F;

	public BigWaffleBlock(Properties properties) {
		super(properties);
	}

	@Override
	public MapCodec<CakeBlock> codec() {
		return (MapCodec<CakeBlock>) (MapCodec) CODEC;
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
		if (level.isClientSide()) {
			if (tryEat(level, pos, state, player).consumesAction()) {
				return InteractionResult.SUCCESS;
			}
			if (player.getItemInHand(InteractionHand.MAIN_HAND).isEmpty()) {
				return InteractionResult.CONSUME;
			}
		}
		return tryEat(level, pos, state, player);
	}

	private static InteractionResult tryEat(LevelAccessor level, BlockPos pos, BlockState state, Player player) {
		if (!player.canEat(false)) {
			return InteractionResult.PASS;
		}

		player.awardStat(Stats.EAT_CAKE_SLICE);
		player.getFoodData().eat(NUTRITION, SATURATION);

		int bites = state.getValue(BITES);
		if (bites < 6) {
			level.setBlock(pos, state.setValue(BITES, bites + 1), 3);
		} else {
			level.removeBlock(pos, false);
		}

		return InteractionResult.SUCCESS;
	}
}
