package uwu.smsgamer.cocidea.entity.entities.shulkerbullet;

import com.google.common.collect.Lists;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_12_R1.event.CraftEventFactory;
import org.bukkit.entity.LivingEntity;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class CustomShulkerBullet extends EntityShulkerBullet {
    private EntityLiving shooter;
    private Entity target;
    private EnumDirection c;
    private int d;
    private double e;
    private double f;
    private double g;
    private UUID h;
    private BlockPosition at;
    private UUID au;
    private BlockPosition av;
    public double speed = 1;
    public boolean dontMove = false;
    public boolean dontDie = false;
    public float damage = 4.0f;

    public CustomShulkerBullet(World world) {
        super(world);
        this.setSize(0.3125F, 0.3125F);
        this.noclip = true;
    }

    public SoundCategory bK() {
        return SoundCategory.HOSTILE;
    }

    public CustomShulkerBullet(World world, EntityLiving entityliving, Entity entity, EnumDirection.EnumAxis enumdirection_enumaxis) {
        this(world);
        this.shooter = entityliving;
        BlockPosition blockposition = new BlockPosition(entityliving);
        double d0 = (double) blockposition.getX() + 0.5D;
        double d1 = (double) blockposition.getY() + 0.5D;
        double d2 = (double) blockposition.getZ() + 0.5D;
        this.setPositionRotation(d0, d1, d2, this.yaw, this.pitch);
        this.target = entity;
        this.c = EnumDirection.UP;
        this.a(enumdirection_enumaxis);
        this.projectileSource = (LivingEntity) entityliving.getBukkitEntity();
    }

    public EntityLiving getShooter() {
        return this.shooter;
    }

    public void setShooter(EntityLiving e) {
        this.shooter = e;
    }

    public Entity getTarget() {
        return this.target;
    }

    public void setTarget(Entity e) {
        this.target = e;
        this.c = EnumDirection.UP;
        this.a(EnumDirection.EnumAxis.X);
    }

    protected void b(NBTTagCompound nbttagcompound) {
        BlockPosition blockposition;
        NBTTagCompound nbttagcompound1;
        if (this.shooter != null) {
            blockposition = new BlockPosition(this.shooter);
            nbttagcompound1 = GameProfileSerializer.a(this.shooter.getUniqueID());
            nbttagcompound1.setInt("X", blockposition.getX());
            nbttagcompound1.setInt("Y", blockposition.getY());
            nbttagcompound1.setInt("Z", blockposition.getZ());
            nbttagcompound.set("Owner", nbttagcompound1);
        }

        if (this.target != null) {
            blockposition = new BlockPosition(this.target);
            nbttagcompound1 = GameProfileSerializer.a(this.target.getUniqueID());
            nbttagcompound1.setInt("X", blockposition.getX());
            nbttagcompound1.setInt("Y", blockposition.getY());
            nbttagcompound1.setInt("Z", blockposition.getZ());
            nbttagcompound.set("Target", nbttagcompound1);
        }

        if (this.c != null) {
            nbttagcompound.setInt("Dir", this.c.a());
        }

        nbttagcompound.setInt("Steps", this.d);
        nbttagcompound.setDouble("TXD", this.e);
        nbttagcompound.setDouble("TYD", this.f);
        nbttagcompound.setDouble("TZD", this.g);
    }

    protected void a(NBTTagCompound nbttagcompound) {
        this.d = nbttagcompound.getInt("Steps");
        this.e = nbttagcompound.getDouble("TXD");
        this.f = nbttagcompound.getDouble("TYD");
        this.g = nbttagcompound.getDouble("TZD");
        if (nbttagcompound.hasKeyOfType("Dir", 99)) {
            this.c = EnumDirection.fromType1(nbttagcompound.getInt("Dir"));
        }

        NBTTagCompound nbttagcompound1;
        if (nbttagcompound.hasKeyOfType("Owner", 10)) {
            nbttagcompound1 = nbttagcompound.getCompound("Owner");
            this.h = GameProfileSerializer.b(nbttagcompound1);
            this.at = new BlockPosition(nbttagcompound1.getInt("X"), nbttagcompound1.getInt("Y"), nbttagcompound1.getInt("Z"));
        }

        if (nbttagcompound.hasKeyOfType("Target", 10)) {
            nbttagcompound1 = nbttagcompound.getCompound("Target");
            this.au = GameProfileSerializer.b(nbttagcompound1);
            this.av = new BlockPosition(nbttagcompound1.getInt("X"), nbttagcompound1.getInt("Y"), nbttagcompound1.getInt("Z"));
        }

    }

    protected void i() {
        super.i();
    }

    private void a(@Nullable EnumDirection enumdirection) {
        this.c = enumdirection;
    }

    private void a(@Nullable EnumDirection.EnumAxis enumdirection_enumaxis) {
        double d0 = 0.5D;
        BlockPosition blockposition;
        if (this.target == null) {
            blockposition = (new BlockPosition(this)).down();
        } else {
            d0 = (double) this.target.length * 0.5D;
            blockposition = new BlockPosition(this.target.locX, this.target.locY + d0, this.target.locZ);
        }

        double d1 = (double) blockposition.getX() + 0.5D;
        double d2 = (double) blockposition.getY() + d0;
        double d3 = (double) blockposition.getZ() + 0.5D;
        EnumDirection enumdirection = null;
        if (blockposition.g(this.locX, this.locY, this.locZ) >= 4.0D) {
            BlockPosition blockposition1 = new BlockPosition(this);
            ArrayList arraylist = Lists.newArrayList();
            if (enumdirection_enumaxis != EnumDirection.EnumAxis.X) {
                if (blockposition1.getX() < blockposition.getX() && this.world.isEmpty(blockposition1.east())) {
                    arraylist.add(EnumDirection.EAST);
                } else if (blockposition1.getX() > blockposition.getX() && this.world.isEmpty(blockposition1.west())) {
                    arraylist.add(EnumDirection.WEST);
                }
            }

            if (enumdirection_enumaxis != EnumDirection.EnumAxis.Y) {
                if (blockposition1.getY() < blockposition.getY() && this.world.isEmpty(blockposition1.up())) {
                    arraylist.add(EnumDirection.UP);
                } else if (blockposition1.getY() > blockposition.getY() && this.world.isEmpty(blockposition1.down())) {
                    arraylist.add(EnumDirection.DOWN);
                }
            }

            if (enumdirection_enumaxis != EnumDirection.EnumAxis.Z) {
                if (blockposition1.getZ() < blockposition.getZ() && this.world.isEmpty(blockposition1.south())) {
                    arraylist.add(EnumDirection.SOUTH);
                } else if (blockposition1.getZ() > blockposition.getZ() && this.world.isEmpty(blockposition1.north())) {
                    arraylist.add(EnumDirection.NORTH);
                }
            }

            enumdirection = EnumDirection.a(this.random);
            if (arraylist.isEmpty()) {
                for (int i = 5; !this.world.isEmpty(blockposition1.shift(enumdirection)) && i > 0; --i) {
                    enumdirection = EnumDirection.a(this.random);
                }
            } else {
                enumdirection = (EnumDirection) arraylist.get(this.random.nextInt(arraylist.size()));
            }

            d1 = this.locX + (double) enumdirection.getAdjacentX();
            d2 = this.locY + (double) enumdirection.getAdjacentY();
            d3 = this.locZ + (double) enumdirection.getAdjacentZ();
        }

        this.a(enumdirection);
        double d4 = d1 - this.locX;
        double d5 = d2 - this.locY;
        double d6 = d3 - this.locZ;
        double d7 = (double) MathHelper.sqrt(d4 * d4 + d5 * d5 + d6 * d6);
        if (d7 == 0.0D) {
            this.e = 0.0D;
            this.f = 0.0D;
            this.g = 0.0D;
        } else {
            this.e = d4 / d7 * 0.15D;
            this.f = d5 / d7 * 0.15D;
            this.g = d6 / d7 * 0.15D;
        }

        this.impulse = true;
        this.d = 10 + this.random.nextInt(5) * 10;
    }

    public void B_() {
        if (!this.world.isClientSide && this.world.getDifficulty() == EnumDifficulty.PEACEFUL) {
            if (!dontDie)
                this.die();
        } else {
            { //super
                if (!this.world.isClientSide) {
                    this.setFlag(6, this.aW());
                }

                this.Y();
            }
            if (!this.world.isClientSide) {
                List list;
                Iterator iterator;
                EntityLiving entityliving;
                if (this.target == null && this.au != null) {
                    list = this.world.a(EntityLiving.class, new AxisAlignedBB(this.av.a(-2, -2, -2), this.av.a(2, 2, 2)));
                    iterator = list.iterator();

                    while (iterator.hasNext()) {
                        entityliving = (EntityLiving) iterator.next();
                        if (entityliving.getUniqueID().equals(this.au)) {
                            this.target = entityliving;
                            break;
                        }
                    }

                    this.au = null;
                }

                if (this.shooter == null && this.h != null) {
                    list = this.world.a(EntityLiving.class, new AxisAlignedBB(this.at.a(-2, -2, -2), this.at.a(2, 2, 2)));
                    iterator = list.iterator();

                    while (iterator.hasNext()) {
                        entityliving = (EntityLiving) iterator.next();
                        if (entityliving.getUniqueID().equals(this.h)) {
                            this.shooter = entityliving;
                            break;
                        }
                    }

                    this.h = null;
                }

                if (this.target == null || !this.target.isAlive() || this.target instanceof EntityHuman && ((EntityHuman) this.target).isSpectator()) {
                    if (!this.isNoGravity()) {
                        this.motY -= 0.04D;
                    }
                } else {
                    this.e = MathHelper.a(this.e * 1.025D, -1.0D, 1.0D);
                    this.f = MathHelper.a(this.f * 1.025D, -1.0D, 1.0D);
                    this.g = MathHelper.a(this.g * 1.025D, -1.0D, 1.0D);
                    this.motX += (this.e - this.motX) * 0.2D;
                    this.motY += (this.f - this.motY) * 0.2D;
                    this.motZ += (this.g - this.motZ) * 0.2D;
                }

                if (this.dontMove) {
                    AxisAlignedBB thisB = this.getBoundingBox();
                    for (Entity entity: world.entityList) {
                        /* a - x min
                         * b - y min
                         * c - z min
                         * d - x max
                         * e - y max
                         * f - z max
                         */
                        if(entity instanceof EntityShulkerBullet || entity.s(target))
                            continue;
                        AxisAlignedBB entityB = entity.getBoundingBox();
                        if(thisB.d > entityB.a && thisB.a < entityB.d &&
                          thisB.e > entityB.b && thisB.b < entityB.e &&
                          thisB.f > entityB.c && thisB.c < entityB.f) {
                            MovingObjectPosition mop = new MovingObjectPosition(entity);
                            this.a(mop);
                        }
                    }
                    /*
                    MovingObjectPosition movingobjectposition = null;

                    for (double b = 10; b < 1000; b *= 10) {
                        for (int x = -1; x <= 1; x++) {
                            for (int y = -1; y <= 1; y++) {
                                for (int z = -1; z <= 1; z++) {
                                    this.motX = x / b;
                                    this.motY = y / b;
                                    this.motZ = z / b;
                                    movingobjectposition = ProjectileHelper.a(this, true, false, this.shooter);
                                    if (movingobjectposition != null) {
                                        this.a(movingobjectposition);
                                        break;
                                    }
                                }
                                if (movingobjectposition != null) {
                                    break;
                                }
                            }
                            if (movingobjectposition != null) {
                                break;
                            }
                        }
                    }*/
                } else {
                    MovingObjectPosition movingobjectposition = ProjectileHelper.a(this, true, false, this.shooter);
                    if (movingobjectposition != null) {
                        this.a(movingobjectposition);
                    }
                }
            }

            if (this.dontMove) {
                this.motX = 0;
                this.motY = 0;
                this.motZ = 0;
            }

            this.setPosition(this.locX + this.motX * speed, this.locY + this.motY * speed, this.locZ + this.motZ * speed);

            ProjectileHelper.a(this, 0.5F);
            if (this.world.isClientSide) {
                this.world.addParticle(EnumParticle.END_ROD, this.locX - this.motX, this.locY - this.motY + 0.15D, this.locZ - this.motZ, 0.0D, 0.0D, 0.0D, new int[0]);
            } else if (this.target != null && !this.target.dead) {
                if (this.d > 0) {
                    --this.d;
                    if (this.d == 0) {
                        this.a(this.c == null ? null : this.c.k());
                    }
                }

                if (this.c != null) {
                    BlockPosition blockposition = new BlockPosition(this);
                    EnumDirection.EnumAxis enumdirection_enumaxis = this.c.k();
                    if (this.world.d(blockposition.shift(this.c), false)) {
                        this.a(enumdirection_enumaxis);
                    } else {
                        BlockPosition blockposition1 = new BlockPosition(this.target);
                        if (enumdirection_enumaxis == EnumDirection.EnumAxis.X && blockposition.getX() == blockposition1.getX() || enumdirection_enumaxis == EnumDirection.EnumAxis.Z && blockposition.getZ() == blockposition1.getZ() || enumdirection_enumaxis == EnumDirection.EnumAxis.Y && blockposition.getY() == blockposition1.getY()) {
                            this.a(enumdirection_enumaxis);
                        }
                    }
                }
            }
        }

    }

    public boolean isBurning() {
        return false;
    }

    public float aw() {
        return 1.0F;
    }

    protected void a(MovingObjectPosition movingobjectposition) {
        CraftEventFactory.callProjectileHitEvent(this, movingobjectposition);
        if (movingobjectposition.entity == null) {
            ((WorldServer) this.world).a(EnumParticle.EXPLOSION_LARGE, this.locX, this.locY, this.locZ, 2, 0.2D, 0.2D, 0.2D, 0.0D, new int[0]);
            this.a(SoundEffects.gD, 1.0F, 1.0F);
        } else {
            boolean flag = movingobjectposition.entity.damageEntity(DamageSource.a(this, this.shooter).b(), damage);
            if (flag) {
                this.a(this.shooter, movingobjectposition.entity);
            }
        }
        if (!dontDie)
            this.die();
    }

    public boolean damageEntity(DamageSource damagesource, float f) {
        if (!this.world.isClientSide && !dontDie) {
            this.a(SoundEffects.gE, 1.0F, 1.0F);
            ((WorldServer) this.world).a(EnumParticle.CRIT, this.locX, this.locY, this.locZ, 15, 0.2D, 0.2D, 0.2D, 0.0D, new int[0]);
            this.die();
        }
        return true;
    }
}

