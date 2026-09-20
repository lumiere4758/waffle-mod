package waffles;

import java.util.function.Function;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.references.BlockItemId;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class Main implements ModInitializer {
	public static final String MOD_ID = "waffles";

	// 6 points = 3 hunger bars
	public static final FoodProperties WAFFLE_FOOD = new FoodProperties.Builder()
			.nutrition(6)
			.saturationModifier(0.6f)
			.build();

	public static final FoodProperties CHOCOLATE_WAFFLE_FOOD = new FoodProperties.Builder()
			.nutrition(7)
			.saturationModifier(0.7f)
			.build();

	public static final FoodProperties GLOW_BERRY_WAFFLE_FOOD = new FoodProperties.Builder()
			.nutrition(6)
			.saturationModifier(0.6f)
			.build();

	public static final FoodProperties GOLDEN_WAFFLE_FOOD = new FoodProperties.Builder()
			.nutrition(8)
			.saturationModifier(1.2f)
			.alwaysEdible()
			.build();

	public static final FoodProperties SWEET_BERRY_WAFFLE_FOOD = new FoodProperties.Builder()
			.nutrition(6)
			.saturationModifier(0.6f)
			.build();

	public static final Item BATTER_MIX = registerItem("batter_mix", Item::new, new Item.Properties());
	public static final Item WAFFLE = registerItem("waffle", Item::new, new Item.Properties().food(WAFFLE_FOOD));
	public static final Item CHOCOLATE_WAFFLE = registerItem("chocolate_waffle", Item::new, new Item.Properties().food(CHOCOLATE_WAFFLE_FOOD));
	public static final Item GLOW_BERRY_WAFFLE = registerItem("glow_berry_waffle", Item::new, new Item.Properties().food(GLOW_BERRY_WAFFLE_FOOD));
	public static final Item GOLDEN_WAFFLE = registerItem("golden_waffle", Item::new, new Item.Properties().food(GOLDEN_WAFFLE_FOOD));
	public static final Item SWEET_BERRY_WAFFLE = registerItem("sweet_berry_waffle", Item::new, new Item.Properties().food(SWEET_BERRY_WAFFLE_FOOD));

	public static final Block WAFFLE_BLOCK = registerBlock(
			"waffle_block",
			WaffleBlock::new,
			BlockBehaviour.Properties.of().strength(0.5f)
	);

	public static final Block BIG_WAFFLE = registerBlock(
			"big_waffle",
			BigWaffleBlock::new,
			BlockBehaviour.Properties.of().strength(0.5f)
	);

	@Override
	public void onInitialize() {
		CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.FOOD_AND_DRINKS).register(content -> {
			content.accept(BATTER_MIX);
			content.accept(WAFFLE);
			content.accept(CHOCOLATE_WAFFLE);
			content.accept(GLOW_BERRY_WAFFLE);
			content.accept(GOLDEN_WAFFLE);
			content.accept(SWEET_BERRY_WAFFLE);
			content.accept(WAFFLE_BLOCK.asItem());
			content.accept(BIG_WAFFLE.asItem());
		});
	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}

	private static Item registerItem(String name, Function<Item.Properties, Item> factory, Item.Properties properties) {
		ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, id(name));
		Item item = factory.apply(properties.setId(key));
		return Registry.register(BuiltInRegistries.ITEM, key, item);
	}

	private static Block registerBlock(String name, Function<BlockBehaviour.Properties, Block> factory, BlockBehaviour.Properties properties) {
		Identifier identifier = id(name);
		BlockItemId blockItemId = BlockItemId.create(identifier, identifier);
		Block block = factory.apply(properties.setId(blockItemId.block()));
		Registry.register(BuiltInRegistries.BLOCK, blockItemId.block(), block);
		BlockItem blockItem = new BlockItem(block, new Item.Properties().useBlockDescriptionPrefix().setId(blockItemId.item()));
		Registry.register(BuiltInRegistries.ITEM, blockItemId.item(), blockItem);
		return block;
	}
}
