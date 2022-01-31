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
import dev.galacticraft.api.gas.Gas;
import dev.galacticraft.mod.Constant;
import dev.galacticraft.mod.lookup.filter.GasFilter;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record GasSlotSettings(int x, int y, @NotNull SlotType<Gas> type, @NotNull GasFilter filter,
                              boolean canInsertGases, boolean canTakeGases, long capacity,
                              @NotNull SlotChangeListener extractionListener,
                              @NotNull SlotChangeListener insertionListener) {
    public static class Builder {
        private final int x;
        private final int y;
        private final SlotType<Gas> type;
        private boolean canInsertGases = true;
        private boolean canExtractGases = true;
        private long capacity = 0;
        private @NotNull SlotChangeListener extractionListener = SlotChangeListener.DO_NOTHING;
        private @NotNull SlotChangeListener insertionListener = SlotChangeListener.DO_NOTHING;
        private @NotNull GasFilter filter = Constant.Filter.Gas.ALWAYS;

        private Builder(int x, int y, @NotNull SlotType<Gas> type) {
            this.x = x;
            this.y = y;
            this.type = type;
        }

        public static @NotNull Builder create(int x, int y, @NotNull SlotType<Gas> type) {
            Preconditions.checkNotNull(type);
            return new Builder(x, y, type);
        }

        public Builder filter(@NotNull GasFilter filter) {
            this.filter = filter;
            return this;
        }

        public Builder disableInput() {
            this.canInsertGases = false;
            return this;
        }

        public Builder disableOutput() {
            this.canExtractGases = false;
            return this;
        }

        public Builder capacity(long capacity) {
            this.capacity = capacity;
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

        public GasSlotSettings build() {
            return new GasSlotSettings(this.x, this.y, this.type, this.filter, this.canInsertGases, this.canExtractGases, this.capacity, this.extractionListener, this.insertionListener);
        }
    }

    @FunctionalInterface
    public interface SlotChangeListener {
        @NotNull SlotChangeListener DO_NOTHING = (player, stack) -> {
        };

        void onChange(@Nullable PlayerEntity player, dev.galacticraft.api.gas.GasStack stack);
    }
}
