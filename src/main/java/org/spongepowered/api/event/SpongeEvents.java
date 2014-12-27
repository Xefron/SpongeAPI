/*
 * This file is part of Sponge, licensed under the MIT License (MIT).
 *
 * Copyright (c) SpongePowered.org <http://www.spongepowered.org>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package org.spongepowered.api.event;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Maps;
import jdk.nashorn.internal.ir.Block;
import org.spongepowered.api.Game;
import org.spongepowered.api.entity.EntityInteractionType;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.player.PlayerInteractBlockEvent;
import org.spongepowered.api.util.event.callback.CallbackList;
import org.spongepowered.api.util.event.factory.EventFactory;
import org.spongepowered.api.util.event.factory.FactoryProvider;
import org.spongepowered.api.util.event.factory.ClassGeneratorProvider;
import org.spongepowered.api.util.event.factory.NullPolicy;

import java.util.Map;

/**
 * Generates Sponge event implementations.
 */
@SuppressWarnings("ConstantConditions")
public final class SpongeEvents {

    private static final FactoryProvider factoryProvider;
    private static final LoadingCache<Class<?>, EventFactory<?>> factories;

    static {
        factoryProvider = new ClassGeneratorProvider("org.spongepowered.api.event.impl");
        factoryProvider.setNullPolicy(NullPolicy.NON_NULL_BY_DEFAULT);

        factories = CacheBuilder.newBuilder()
                .build(
                        new CacheLoader<Class<?>, EventFactory<?>>() {
                            public EventFactory<?> load(Class<?> type) {
                                return factoryProvider.create(type, Object.class);
                            }
                        });
    }

    private SpongeEvents() {
    }

    public static PlayerInteractBlockEvent createPlayerInteractBlock(Game game, Cause cause, Block block, Player player, EntityInteractionType type) {
        Map<String, Object> values = Maps.newHashMap();
        values.put("block", block);
        values.put("interactionType", type);
        values.put("cause", cause);
        values.put("player", player);
        values.put("entity", player);
        values.put("game", game);
        values.put("callbacks", new CallbackList());
        return (PlayerInteractBlockEvent) factories.getUnchecked(PlayerInteractBlockEvent.class).apply(values);
    }

}
