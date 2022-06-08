package org.valkyrienskies.mod.common.item

import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionResult
import net.minecraft.world.item.Item
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import org.joml.Vector3i
import org.valkyrienskies.mod.common.shipObjectWorld
import org.valkyrienskies.mod.common.util.toBlockPos
import org.valkyrienskies.mod.common.util.toJOML

class ShipCreatorItem(properties: Properties, private val scale: Double) : Item(properties) {

    override fun useOn(ctx: UseOnContext): InteractionResult {
        println(ctx.level.isClientSide)
        val level = ctx.level as? ServerLevel ?: return super.useOn(ctx)
        val blockPos = ctx.clickedPos
        val blockState: BlockState = level.getBlockState(blockPos)

        println("Player right clicked on $blockPos")

        if (!level.isClientSide) {
            if (!blockState.isAir) {
                // Make a ship
                val dimensionId = 0 // TODO fix
                val shipData = level.shipObjectWorld.createNewShipAtBlock(blockPos.toJOML(), false, scale, dimensionId)

                val centerPos = shipData.chunkClaim.getCenterBlockCoordinates(Vector3i()).toBlockPos()

                // Move the block from the world to a ship
                level.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 11)
                level.setBlock(centerPos, blockState, 11)
            }
        }

        return super.useOn(ctx)
    }
}
