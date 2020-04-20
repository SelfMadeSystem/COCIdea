package uwu.smsgamer.cocidea.entity.entities.shulker;

import com.google.common.base.Predicate;
import net.minecraft.server.v1_12_R1.*;
import uwu.smsgamer.cocidea.entity.entities.shulkerbullet.CustomShulkerBullet;

import javax.annotation.Nullable;

public class CustomShulker extends EntityShulker {
    public CustomShulker(World world) {
        super(world);
    }

    @Override
    protected void r() {
        this.goalSelector.a(1, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
        this.goalSelector.a(4, new CustomShulker.a());
        this.goalSelector.a(7, new CustomShulker.e(null));
        this.goalSelector.a(8, new PathfinderGoalRandomLookaround(this));
        this.targetSelector.a(1, new PathfinderGoalHurtByTarget(this, true));
        this.targetSelector.a(2, new CustomShulker.d(this));
        this.targetSelector.a(3, new CustomShulker.c(this));
    }

    @Override
    public void B_() {
        super.B_();
    }

    @Override
    protected boolean p() {
        return true;
    }

    @Override
    public void move(EnumMoveType enummovetype, double d0, double d1, double d2) {
        if (enummovetype != EnumMoveType.SHULKER_BOX) {
            super.move(enummovetype, d0, d1, d2);
        }
    }

    class a extends PathfinderGoal {
        private int b;

        public a() {
            this.a(3);
        }

        public boolean a() {
            EntityLiving entityliving = CustomShulker.this.getGoalTarget();
            return (entityliving != null && entityliving.isAlive()) && CustomShulker.this.world.getDifficulty() != EnumDifficulty.PEACEFUL;
        }

        public void c() {
            this.b = 20;
            CustomShulker.this.a(100);
        }

        public void d() {
            CustomShulker.this.a(0);
        }

        public void e() {
            if (CustomShulker.this.world.getDifficulty() != EnumDifficulty.PEACEFUL) {
                --this.b;
                EntityLiving entityliving = CustomShulker.this.getGoalTarget();
                CustomShulker.this.getControllerLook().a(entityliving, 180.0F, 180.0F);
                double d0 = CustomShulker.this.h(entityliving);
                if (d0 < 400.0D) {
                    if (this.b <= 0) {
                        //Bukkit.broadcastMessage("Shot Bullet!");
                        this.b = 20 + CustomShulker.this.random.nextInt(10) * 20 / 2;
                        CustomShulkerBullet customShulkerBullet = new CustomShulkerBullet(CustomShulker.this.world, CustomShulker.this, entityliving, CustomShulker.this.dl().k());
                        customShulkerBullet.speed = 3;
                        CustomShulker.this.world.addEntity(customShulkerBullet);
                        CustomShulker.this.a(SoundEffects.gK, 2.0F, (CustomShulker.this.random.nextFloat() - CustomShulker.this.random.nextFloat()) * 0.2F + 1.0F);
                    }
                } else {
                    CustomShulker.this.setGoalTarget(null);
                }

                super.e();
            }

        }
    }

    static class c extends PathfinderGoalNearestAttackableTarget<EntityLiving> {
        public c(CustomShulker CustomShulker) {
            super(CustomShulker, EntityLiving.class, 10, true, false, new Predicate() {
                public boolean a(@Nullable EntityLiving entityliving) {
                    return entityliving instanceof IMonster;
                }

                public boolean apply(@Nullable Object object) {
                    return this.a((EntityLiving) object);
                }
            });
        }

        public boolean a() {
            return this.e.aY() != null && super.a();
        }

        protected AxisAlignedBB a(double d0) {
            EnumDirection enumdirection = ((CustomShulker) this.e).dl();
            return enumdirection.k() == EnumDirection.EnumAxis.X ? this.e.getBoundingBox().grow(4.0D, d0, d0) : (enumdirection.k() == EnumDirection.EnumAxis.Z ? this.e.getBoundingBox().grow(d0, d0, 4.0D) : this.e.getBoundingBox().grow(d0, 4.0D, d0));
        }
    }

    class d extends PathfinderGoalNearestAttackableTarget<EntityHuman> {
        public d(CustomShulker customShulker) {
            super(customShulker, EntityHuman.class, true);
        }

        public boolean a() {
            return CustomShulker.this.world.getDifficulty() != EnumDifficulty.PEACEFUL && super.a();
        }

        protected AxisAlignedBB a(double d0) {
            EnumDirection enumdirection = ((CustomShulker) this.e).dl();
            return enumdirection.k() == EnumDirection.EnumAxis.X ? this.e.getBoundingBox().grow(4.0D, d0, d0) : (enumdirection.k() == EnumDirection.EnumAxis.Z ? this.e.getBoundingBox().grow(d0, d0, 4.0D) : this.e.getBoundingBox().grow(d0, 4.0D, d0));
        }
    }

    class e extends PathfinderGoal {
        private int b;

        private e() {
        }

        public boolean a() {
            return CustomShulker.this.getGoalTarget() == null && CustomShulker.this.random.nextInt(40) == 0;
        }

        public boolean b() {
            return CustomShulker.this.getGoalTarget() == null && this.b > 0;
        }

        public void c() {
            this.b = 20 * (1 + CustomShulker.this.random.nextInt(3));
            CustomShulker.this.a(30);
        }

        public void d() {
            if (CustomShulker.this.getGoalTarget() == null) {
                CustomShulker.this.a(0);
            }

        }

        public void e() {
            --this.b;
        }

        e(Object object) {
            this();
        }
    }
}
