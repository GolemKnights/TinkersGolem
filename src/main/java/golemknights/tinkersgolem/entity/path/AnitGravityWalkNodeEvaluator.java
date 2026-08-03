package golemknights.tinkersgolem.entity.path;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class AnitGravityWalkNodeEvaluator extends WalkNodeEvaluator {
    @Override
    public int getNeighbors(Node[] p_77640_, Node p_77641_) {
        int i = 0;
        int j = 0;
        BlockPathTypes blockpathtypes = this.getCachedBlockType(this.mob, p_77641_.x, p_77641_.y - 1, p_77641_.z);
        BlockPathTypes blockpathtypes1 = this.getCachedBlockType(this.mob, p_77641_.x, p_77641_.y, p_77641_.z);
        if (this.mob.getPathfindingMalus(blockpathtypes) >= 0.0F && blockpathtypes1 != BlockPathTypes.STICKY_HONEY) {
            j = Mth.floor(Math.max(1.0F, this.mob.getStepHeight()));
        }

        double d0 = this.getFloorLevel(new BlockPos(p_77641_.x, p_77641_.y, p_77641_.z));
        Node node = this.findAcceptedNode(p_77641_.x, p_77641_.y, p_77641_.z + 1, j, d0, Direction.SOUTH, blockpathtypes1);
        if (this.isNeighborValid(node, p_77641_)) {
            p_77640_[i++] = node;
        }

        Node node1 = this.findAcceptedNode(p_77641_.x - 1, p_77641_.y, p_77641_.z, j, d0, Direction.WEST, blockpathtypes1);
        if (this.isNeighborValid(node1, p_77641_)) {
            p_77640_[i++] = node1;
        }

        Node node2 = this.findAcceptedNode(p_77641_.x + 1, p_77641_.y, p_77641_.z, j, d0, Direction.EAST, blockpathtypes1);
        if (this.isNeighborValid(node2, p_77641_)) {
            p_77640_[i++] = node2;
        }

        Node node3 = this.findAcceptedNode(p_77641_.x, p_77641_.y, p_77641_.z - 1, j, d0, Direction.NORTH, blockpathtypes1);
        if (this.isNeighborValid(node3, p_77641_)) {
            p_77640_[i++] = node3;
        }

        Node node4 = this.findAcceptedNode(p_77641_.x - 1, p_77641_.y, p_77641_.z - 1, j, d0, Direction.NORTH, blockpathtypes1);
        if (this.isDiagonalValid(p_77641_, node1, node3, node4)) {
            p_77640_[i++] = node4;
        }

        Node node5 = this.findAcceptedNode(p_77641_.x + 1, p_77641_.y, p_77641_.z - 1, j, d0, Direction.NORTH, blockpathtypes1);
        if (this.isDiagonalValid(p_77641_, node2, node3, node5)) {
            p_77640_[i++] = node5;
        }

        Node node6 = this.findAcceptedNode(p_77641_.x - 1, p_77641_.y, p_77641_.z + 1, j, d0, Direction.SOUTH, blockpathtypes1);
        if (this.isDiagonalValid(p_77641_, node1, node, node6)) {
            p_77640_[i++] = node6;
        }

        Node node7 = this.findAcceptedNode(p_77641_.x + 1, p_77641_.y, p_77641_.z + 1, j, d0, Direction.SOUTH, blockpathtypes1);
        if (this.isDiagonalValid(p_77641_, node2, node, node7)) {
            p_77640_[i++] = node7;
        }

        return i;
    }

    @Override
    protected double getFloorLevel(BlockPos p_164733_) {
        return (this.canFloat() || this.isAmphibious()) && this.level.getFluidState(p_164733_).is(FluidTags.WATER) ? (double)p_164733_.getY() - 0.5 : getCeilingLevel(this.level, p_164733_);
    }

    public static double getCeilingLevel(BlockGetter p_77612_, BlockPos p_77613_) {
        BlockPos blockpos = p_77613_.above();
        VoxelShape voxelshape = p_77612_.getBlockState(blockpos).getCollisionShape(p_77612_, blockpos);
        return (double)blockpos.getY() - (voxelshape.isEmpty() ? 0 : voxelshape.min(Direction.Axis.Y));
    }

    @Override
    public @NotNull Node getStart() {
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
        int i = this.mob.getBlockY();
        BlockState blockstate = this.level.getBlockState(blockpos$mutableblockpos.set(this.mob.getX(), i, this.mob.getZ()));
        BlockPos blockpos;
        if (!this.mob.canStandOnFluid(blockstate.getFluidState())) {
            if (this.canFloat() && this.mob.isInWater()) {
                while(true) {
                    if (!blockstate.is(Blocks.WATER) && blockstate.getFluidState() != Fluids.WATER.getSource(false)) {
                        ++i;
                        break;
                    }

                    --i;
                    blockstate = this.level.getBlockState(blockpos$mutableblockpos.set(this.mob.getX(), i, this.mob.getZ()));
                }
            } else if (this.mob.onGround()) {
                i = Mth.ceil(this.mob.getY() - 0.5);
            } else {
                for (blockpos = this.mob.blockPosition(); (this.level.getBlockState(blockpos).isAir() || this.level.getBlockState(blockpos).isPathfindable(this.level, blockpos, PathComputationType.LAND)) && blockpos.getY() < this.mob.level().getMaxBuildHeight(); blockpos = blockpos.above());

                i = blockpos.below().getY();
            }
        } else {
            while(true) {
                if (!this.mob.canStandOnFluid(blockstate.getFluidState())) {
                    ++i;
                    break;
                }

                --i;
                blockstate = this.level.getBlockState(blockpos$mutableblockpos.set(this.mob.getX(), i, this.mob.getZ()));
            }
        }

        blockpos = this.mob.blockPosition();
        if (!this.canStartAt(blockpos$mutableblockpos.set(blockpos.getX(), i, blockpos.getZ()))) {
            AABB aabb = this.mob.getBoundingBox();
            if (this.canStartAt(blockpos$mutableblockpos.set(aabb.minX, i, aabb.minZ)) || this.canStartAt(blockpos$mutableblockpos.set(aabb.minX, i, aabb.maxZ)) || this.canStartAt(blockpos$mutableblockpos.set(aabb.maxX, i, aabb.minZ)) || this.canStartAt(blockpos$mutableblockpos.set(aabb.maxX, i, aabb.maxZ))) {
                return this.getStartNode(blockpos$mutableblockpos);
            }
        }

        return this.getStartNode(new BlockPos(blockpos.getX(), i, blockpos.getZ()));
    }

    @Override
    public BlockPathTypes getBlockPathType(BlockGetter p_77576_, int p_77577_, int p_77578_, int p_77579_) {
        return getBlockPathTypeStatic(p_77576_, new BlockPos.MutableBlockPos(p_77577_, p_77578_, p_77579_));
    }

    public static BlockPathTypes getBlockPathTypeStatic(BlockGetter p_77605_, BlockPos.MutableBlockPos p_77606_) {
        int i = p_77606_.getX();
        int j = p_77606_.getY();
        int k = p_77606_.getZ();
        BlockPathTypes blockpathtypes = getBlockPathTypeRaw(p_77605_, p_77606_);
        if (blockpathtypes == BlockPathTypes.OPEN && j <= p_77605_.getMaxBuildHeight() - 1) {
            BlockPathTypes blockpathtypes1 = getBlockPathTypeRaw(p_77605_, p_77606_.set(i, j + 1, k));
            blockpathtypes = blockpathtypes1 != BlockPathTypes.WALKABLE && blockpathtypes1 != BlockPathTypes.OPEN && blockpathtypes1 != BlockPathTypes.WATER && blockpathtypes1 != BlockPathTypes.LAVA ? BlockPathTypes.WALKABLE : BlockPathTypes.OPEN;
            if (blockpathtypes1 == BlockPathTypes.DAMAGE_FIRE) {
                blockpathtypes = BlockPathTypes.DAMAGE_FIRE;
            }

            if (blockpathtypes1 == BlockPathTypes.DAMAGE_OTHER) {
                blockpathtypes = BlockPathTypes.DAMAGE_OTHER;
            }

            if (blockpathtypes1 == BlockPathTypes.STICKY_HONEY) {
                blockpathtypes = BlockPathTypes.STICKY_HONEY;
            }

            if (blockpathtypes1 == BlockPathTypes.POWDER_SNOW) {
                blockpathtypes = BlockPathTypes.DANGER_POWDER_SNOW;
            }

            if (blockpathtypes1 == BlockPathTypes.DAMAGE_CAUTIOUS) {
                blockpathtypes = BlockPathTypes.DAMAGE_CAUTIOUS;
            }
        }

        if (blockpathtypes == BlockPathTypes.WALKABLE) {
            blockpathtypes = checkNeighbourBlocks(p_77605_, p_77606_.set(i, j, k), blockpathtypes);
        }

        return blockpathtypes;
    }

}
