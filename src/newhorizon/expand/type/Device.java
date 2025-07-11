package newhorizon.expand.type;

import arc.Core;
import arc.func.Cons3;
import arc.struct.Seq;
import arc.util.Scaling;
import mindustry.ctype.ContentType;
import mindustry.ctype.UnlockableContent;
import mindustry.gen.Building;
import mindustry.ui.Styles;
import mindustry.world.Block;
import newhorizon.content.NHStats;

import static mindustry.ctype.ContentType.loadout_UNUSED;

public class Device extends UnlockableContent {
    public Seq<Block> installableBlocks = new Seq<>();
    public Seq<Block> compatibleBlocks = new Seq<>();

    public Cons3<Building, DeviceData, Float> modifier = (source, target, intensity) -> {
    };

    public Device(String name) {
        super(name);

        localizedName = Core.bundle.get("device." + name + ".name");
        description = Core.bundle.get("device." + name + ".description");
        details = Core.bundle.getOrNull("device." + name + ".details");

        alwaysUnlocked = true;
    }

    //known:
    //nh take mech_UNUSED for Module
    //eg take effect_UNUSED for DamageType
    //nh take loadout_UNUSED for Device
    @Override
    public ContentType getContentType() {
        return loadout_UNUSED;
    }

    @Override
    public void load() {
        super.load();
        fullIcon = uiIcon = Core.atlas.find(name);
    }

    @Override
    public void setStats() {
        super.setStats();

        stats.add(NHStats.installableBlocks, table -> {
            table.row();
            table.table(c -> {
                for (Block block : installableBlocks) {
                    c.table(Styles.grayPanel, b -> {
                        b.image(block.uiIcon).size(40).pad(10f).left().scaling(Scaling.fit);
                        b.table(info -> info.add(block.localizedName).right()).right().grow().pad(10f).padRight(15f);
                    }).growX().pad(5).row();
                }
            }).growX().colspan(table.getColumns());
            table.row();
        });

        stats.add(NHStats.compatibleBlocks, table -> {
            table.row();
            table.table(c -> {
                for (Block block : compatibleBlocks) {
                    c.table(Styles.grayPanel, b -> {
                        b.image(block.uiIcon).size(40).pad(10f).left().scaling(Scaling.fit);
                        b.table(info -> info.add(block.localizedName).right()).right().grow().pad(10f).padRight(15f);
                    }).growX().pad(5).row();
                }
            }).growX().colspan(table.getColumns());
            table.row();
        });
    }

    public void applyBuilding(Building source, DeviceData target, float intensity) {
        if (installableBlocks.contains(source.block) && compatibleBlocks.contains(target.building.block)) {
            modifier.get(source, target, intensity);
        }
    }
}

