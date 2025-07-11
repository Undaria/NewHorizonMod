package newhorizon.expand.block.consumer;

import arc.func.Func;
import arc.scene.ui.layout.Table;
import mindustry.Vars;
import mindustry.gen.Building;
import mindustry.type.ItemStack;
import mindustry.type.LiquidStack;
import mindustry.type.PayloadStack;
import mindustry.ui.ReqImage;
import mindustry.world.Block;
import mindustry.world.consumers.Consume;
import mindustry.world.meta.StatValues;
import newhorizon.expand.type.Recipe;

public class ConsumeRecipe extends Consume {
    public final Func<Building, Recipe> recipe;
    public final Func<Building, Recipe> display;

    @SuppressWarnings("unchecked")
    public <T extends Building> ConsumeRecipe(Func<T, Recipe> recipe, Func<T, Recipe> display) {
        this.recipe = (Func<Building, Recipe>) recipe;
        this.display = (Func<Building, Recipe>) display;
    }

    @Override
    public void apply(Block block) {
        block.hasItems = true;
        block.hasLiquids = true;

        block.acceptsItems = true;
        block.acceptsPayload = true;
    }

    @Override
    public void update(Building build) {
        for (LiquidStack stack : recipe.get(build).inputLiquid) {
            build.liquids.remove(stack.liquid, stack.amount * build.edelta() * multiplier.get(build));
        }
    }

    @Override
    public void trigger(Building build) {
        for (ItemStack stack : recipe.get(build).inputItem) {
            build.items.remove(stack.item, Math.round(stack.amount * multiplier.get(build)));
        }
        for(PayloadStack stack : recipe.get(build).inputPayload) {
            build.getPayloads().remove(stack.item, Math.round(stack.amount * multiplier.get(build)));
        }
    }

    @Override
    public float efficiency(Building build) {
        float ed = build.edelta();
        if (ed <= 0.00000001f) return 0f;
        float min = 1f;
        for (LiquidStack stack : recipe.get(build).inputLiquid) {
            min = Math.min(build.liquids.get(stack.liquid) / (stack.amount * ed * multiplier.get(build)), min);
        }
        for(PayloadStack stack : recipe.get(build).inputPayload){
            if(!build.getPayloads().contains(stack.item, Math.round(stack.amount * multiplier.get(build)))){
                min = Math.min(min, 0f);
            }
        }
        min = Math.min(build.consumeTriggerValid() || build.items.has(recipe.get(build).inputItem.toArray(ItemStack.class), multiplier.get(build)) ? 1f : 0f, min);

        return min;
    }

    @Override
    public void build(Building build, Table table) {
        table.update(() -> {
            table.clear();
            table.left();

            ItemStack[] currentItem = display.get(build).inputItem.toArray(ItemStack.class);
            LiquidStack[] currentLiquid = display.get(build).inputLiquid.toArray(LiquidStack.class);
            PayloadStack[] currentPayload = display.get(build).inputPayload.toArray(PayloadStack.class);
            table.table(cont -> {
                int i = 0;
                if (currentItem != null) {
                    for (ItemStack stack : currentItem) {
                        cont.add(new ReqImage(StatValues.stack(stack.item, Math.round(stack.amount * multiplier.get(build))),
                                () -> build.items != null && build.items.has(stack.item, Math.round(stack.amount * multiplier.get(build))))).padRight(8).left();
                        if (++i % 4 == 0) cont.row();
                    }
                }

                if (currentLiquid != null) {
                    for (LiquidStack stack : currentLiquid) {
                        cont.add(new ReqImage(stack.liquid.uiIcon,
                                () -> build.liquids != null && build.liquids.get(stack.liquid) > 0)).size(Vars.iconMed).padRight(8);
                        if (++i % 4 == 0) cont.row();
                    }
                }

                if (currentPayload != null) {
                    for (PayloadStack stack : currentPayload) {
                        cont.add(new ReqImage(StatValues.stack(stack.item, Math.round(stack.amount * multiplier.get(build))),
                                () -> build.getPayloads() != null && build.getPayloads().contains(stack.item, Math.round(stack.amount * multiplier.get(build))))).padRight(8);
                        if (++i % 4 == 0) cont.row();
                    }
                }

            });
        });
    }
}
