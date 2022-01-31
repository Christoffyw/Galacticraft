/*
 * Copyright (c) 2019-2022 Team Galacticraft
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package dev.galacticraft.mod.screen.slot;

import com.google.common.base.Preconditions;
import com.mojang.datafixers.util.Pair;
import dev.galacticraft.mod.Constant;
import dev.galacticraft.mod.lookup.filter.ItemFilter;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record SlotSettings(int x, int y, @NotNull SlotType<ItemVariant> type, @NotNull ItemFilter filter, boolean canInsertItems,
                           boolean canTakeItems,
                           int maxCount, @Nullable Pair<Identifier, Identifier> icon,
                           @NotNull SlotChangeListener extractionListener,
                           @NotNull SlotChangeListener insertionListener) {
    public static class Builder {
        private final int x;
        private final int y;
        private final SlotType<ItemVariant> type;
        private boolean canInsertItems = true;
        private boolean canTakeItems = true;
        private int maxCount = 64;
        private @NotNull SlotChangeListener extractionListener = SlotChangeListener.DO_NOTHING;
        private @NotNull SlotChangeListener insertionListener = SlotChangeListener.DO_NOTHING;
        private @NotNull ItemFilter filter = Constant.Filter.Item.ALWAYS;
        private @Nullable Pair<Identifier, Identifier> icon = null;

        private Builder(int x, int y, @NotNull SlotType<ItemVariant> type) {
            this.x = x;
            this.y = y;
            this.type = type;
        }

        @Contract("_, _, _ -> new")
        public static @NotNull Builder create(int x, int y, @NotNull SlotType<ItemVariant> type) {
            Preconditions.checkNotNull(type);
            return new Builder(x, y, type);
        }

        public Builder filter(@NotNull ItemFilter filter) {
            this.filter = filter;
            return this;
        }

        public Builder disableInput() {
            this.canInsertItems = false;
            return this;
        }

        public Builder disableOutput() {
            this.canTakeItems = false;
            return this;
        }

        public Builder maxCount(int maxCount) {
            this.maxCount = maxCount;
            return this;
        }

        public Builder icon(@NotNull Pair<Identifier, Identifier> icon) {
            this.icon = icon;
            return this;
        }

        public Builder onExtract(@NotNull SlotChangeListener listener) {
            this.extractionListener = listener;
            return this;
        }

        public Builder onInsert(@NotNull SlotChangeListener listener) {
            this.insertionListener = listener;
            return this;
        }

        public SlotSettings build() {
            return new SlotSettings(this.x, this.y, this.type, this.filter, this.canInsertItems, this.canTakeItems, this.maxCount, this.icon, this.extractionListener, this.insertionListener);
        }
    }

    @FunctionalInterface
    public interface SlotChangeListener {
        @NotNull SlotChangeListener DO_NOTHING = (player, stack) -> {
        };

        void onChange(@Nullable PlayerEntity player, ItemStack stack);
    }
}
