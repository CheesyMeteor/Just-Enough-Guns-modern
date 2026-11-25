# Slice: events-misc (notes file: docs/notes/events-misc.md)
J = src/main/java/ttv/migami/jeg. You own exactly: J/event/*, J/effect/*, J/particles/*, J/modifier/*, J/enchantment/*, J/debug/*, J/animations/*, J/compat/*,
J/mixin/** (+MixinPlugin), src/main/resources/jeg.mixins.json, src/main/resources/META-INF/accesstransformer.cfg, and NEW src/main/resources/data/jeg/enchantment/** (+ data/jeg/tags/enchantment/**).
- Mixins: no refmap (removed already); Mojang/Parchment names in targets; check each target still exists in 1.21.1 with the same signature (DeltaTracker instead of float partialTick etc.).
  MixinExtras 0.4.x where used. Remove mixins that only served dropped compat. Unverifiable target -> PORT-VERIFY.
- accesstransformer.cfg is in SRG names (f_xxxx_/m_xxxx_). NeoForge ATs use Mojang names (`public net.minecraft.world.entity.animal.Bee hivePos`; methods name+descriptor e.g. `setHasStung(Z)V`).
  The trailing comments in the old file give Mojang names. FIRST grep the whole tree (/home/claude/jeg-port/src/main/java) and keep ONLY entries whose members are used; mark unsure ones `# PORT-VERIFY`.
- Enchantments: reimplement the class-based 1.20.1 enchantments the 1.21 way: one data JSON each in data/jeg/enchantment/ (supported_items, weight, max_level, min_cost/max_cost, anvil_cost, slots)
  and behaviour via enchantment effect components / custom effect types (see how /home/claude/jeg-ref/port does it) or, where simpler, event handlers using EnchantmentHelper.getItemEnchantmentLevel(holder, stack).
  `ModEnchantments` (init-net) exposes ResourceKey<Enchantment> constants with the OLD field names; write what you need in notes.
- Events -> NeoForge 21.1 (see guide); check how the compiling port declares EventBusSubscriber.
- MobEffect: attribute modifiers use ResourceLocation ids; `applyEffectTick(LivingEntity,int)` returns boolean. Particles need codec()/streamCodec().
- modifier package = global loot modifiers: MapCodec-based LootModifier registered in NeoForgeRegistries.GLOBAL_LOOT_MODIFIER_SERIALIZERS (own DeferredRegister; document in notes how JustEnoughGuns must register it).
- compat package: drop (PORT-DROPPED) unless it only touches vanilla.
