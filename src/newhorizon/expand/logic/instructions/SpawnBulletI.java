package newhorizon.expand.logic.instructions;

import arc.math.Angles;
import arc.math.Mathf;
import arc.math.Rand;
import arc.util.Tmp;
import mindustry.entities.bullet.BulletType;
import mindustry.game.Team;
import mindustry.gen.Call;
import mindustry.logic.LExecutor;
import mindustry.logic.LVar;
import newhorizon.content.NHBullets;
import newhorizon.content.bullets.RaidBullets;

import static mindustry.Vars.content;

public class SpawnBulletI implements LExecutor.LInstruction {
    public LVar team, type, seed, count, sourceX, sourceY, targetX, targetY, inaccuracy;

    public SpawnBulletI(LVar team, LVar type, LVar seed, LVar count, LVar sourceX, LVar sourceY, LVar targetX, LVar targetY, LVar inaccuracy) {
        this.team = team;
        this.type = type;
        this.seed = seed;
        this.count = count;
        this.sourceX = sourceX;
        this.sourceY = sourceY;
        this.targetX = targetX;
        this.targetY = targetY;
        this.inaccuracy = inaccuracy;
    }

    @Override
    public void run(LExecutor exec) {
        if (!exec.privileged) return;
        Team t = team.team();
        if (t == null) return;
        int s = seed.numi();
        int tp = type.numi();

        int ct = count.numi();
        int sx = sourceX.numi();
        int sy = sourceY.numi();
        int tx = targetX.numi();
        int ty = targetY.numi();
        int inacc = inaccuracy.numi();

        Rand r = new Rand(s);
        for (int i = 0; i < ct; i++) {
            Tmp.v1.trns(r.random(360f), r.random(inacc));
            float dst = Mathf.dst(sx, sy, tx, ty);
            float ang = Angles.angle(sx, sy, tx, ty);
            float scl = Mathf.clamp(dst / (bulletType(tp).speed * bulletType(tp).lifetime), 0, 10f);
            if (bulletType(tp).speed < 0.01f) scl = 1f;
            Call.createBullet(bulletType(tp), t, sx + Tmp.v1.x, sy + Tmp.v1.y, ang, -1, 1f, scl);
        }
    }

    public BulletType bulletType(int type) {
        if (type < 10000) {
            return switch (type) {
                case 1 -> RaidBullets.raidBullet_1;
                case 2 -> RaidBullets.raidBullet_2;
                case 3 -> RaidBullets.raidBullet_3;
                case 4 -> RaidBullets.raidBullet_4;
                case 5 -> RaidBullets.raidBullet_5;
                case 6 -> RaidBullets.raidBullet_6;
                case 7 -> RaidBullets.raidBullet_7;
                case 8 -> RaidBullets.raidBullet_8;
                default -> NHBullets.railGun1;
            };
        }
        if (content.bullet(type - 10000) != null) return content.bullet(type - 10000);
        return content.bullet(0);
    }
}
