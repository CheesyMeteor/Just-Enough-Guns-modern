#!/usr/bin/env python3
"""Static sanity checker for the JEG NeoForge 1.21.1 port (there is no compiler in the authoring sandbox).

Reports:
  1. BANNED  - leftover 1.20.1/Forge API patterns
  2. INTERNAL- imports of ttv.migami.jeg classes that do not exist in this tree
  3. UNSEEN  - external imports never used by the known-compiling reference port or Framework source
               (not necessarily wrong - means "verify this class exists in 1.21.1/NeoForge/GeckoLib")
Usage: python3 -I tools/check_port.py [path ...]   (default: src/main/java)
"""
import os, re, sys, collections

ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
REF_PORT = '/home/claude/jeg-ref/port/src'
REF_FW = '/home/claude/jeg-ref/framework'
SRC = os.path.join(ROOT, 'src/main/java')

BANNED = [
 (r'net\.minecraftforge', 'Forge package - use net.neoforged.* equivalent'),
 (r'new ResourceLocation\(', 'private in 1.21: ResourceLocation.fromNamespaceAndPath / parse / withDefaultNamespace'),
 (r'\.getTag\(\)', 'ItemStack NBT removed: use StackData'),
 (r'getOrCreateTag\(', 'ItemStack NBT removed: use StackData.get/edit'),
 (r'\.hasTag\(\)', 'ItemStack NBT removed: use StackData.has'),
 (r'\.setTag\(', 'ItemStack NBT removed: use StackData.set'),
 (r'getOrCreateTagElement|removeTagKey|getTagElement', 'ItemStack NBT removed: use StackData'),
 (r'\bRegistryObject\b', 'use DeferredHolder / Supplier'),
 (r'\bForgeRegistries\b', 'use BuiltInRegistries (or NeoForgeRegistries)'),
 (r'\bMessageDirection\b', 'Framework 1.21: PacketFlow.SERVERBOUND/CLIENTBOUND'),
 (r'extends PlayMessage<', 'Framework 1.21: plain class/record + STREAM_CODEC + static handle'),
 (r'\.endVertex\(\)', '1.21 vertex API: addVertex(...).setColor(...).setUv(...)'),
 (r'\bItemStack\.of\(', '1.21: ItemStack.parse(registries, tag)'),
 (r'MinecraftForge\.EVENT_BUS', 'NeoForge.EVENT_BUS'),
 (r'FMLJavaModLoadingContext', 'inject IEventBus/ModContainer in mod constructor'),
 (r'\bForgeConfigSpec\b', 'ModConfigSpec'),
 (r'\bLazyOptional\b', 'capabilities were reworked (IItemHandler via Capabilities.ItemHandler.BLOCK etc.)'),
 (r'\bIForgeRegistry\b', 'removed'),
 (r'(?<![A-Za-z])TickEvent\.', 'ClientTickEvent.Pre/Post, PlayerTickEvent.Pre/Post, ServerTickEvent, LevelTickEvent, RenderFrameEvent'),
 (r'RenderGuiOverlayEvent|RegisterGuiOverlaysEvent|VanillaGuiOverlay', 'RenderGuiLayerEvent / RegisterGuiLayersEvent / VanillaGuiLayers'),
 (r'getBuilder\(\)', 'Tesselator.getBuilder removed: Tesselator.getInstance().begin(mode, format)'),
 (r'\bNetworkEvent\b', 'removed with SimpleChannel'),
 (r'ClientRegistry|MinecraftForgeClient', 'removed'),
 (r'\bFriendlyByteBuf\b.*\bwriteItem\(|\.writeItem\(|\.readItem\(', 'use ItemStack.STREAM_CODEC / OPTIONAL_STREAM_CODEC'),
 (r'\bSavedData\b.*\bload\(CompoundTag\)', 'SavedData.Factory + load(tag, HolderLookup.Provider)'),
]

def java_files(paths):
    for p in paths:
        if os.path.isfile(p): yield p; continue
        for dp, _, fns in os.walk(p):
            for f in fns:
                if f.endswith('.java'): yield os.path.join(dp, f)

def collect_imports(path):
    s = set()
    pat = re.compile(r'^import\s+(?:static\s+)?([\w.]+?)(?:\.\*)?;', re.M)
    for f in java_files([path]):
        try: src = open(f, encoding='utf8', errors='ignore').read()
        except Exception: continue
        for m in pat.finditer(src): s.add(m.group(1))
    return s

def internal_classes():
    out = set()
    for dp, _, fns in os.walk(SRC):
        for f in fns:
            if f.endswith('.java'):
                rel = os.path.relpath(os.path.join(dp, f), SRC)[:-5]
                out.add(rel.replace(os.sep, '.'))
    return out

def fw_classes():
    out = set()
    for base in ('common/src/main/java', 'neoforge/src/main/java'):
        b = os.path.join(REF_FW, base)
        for dp, _, fns in os.walk(b):
            for f in fns:
                if f.endswith('.java'):
                    out.add(os.path.relpath(os.path.join(dp, f), b)[:-5].replace(os.sep, '.'))
    return out

def main():
    paths = sys.argv[1:] or [SRC]
    known = collect_imports(REF_PORT) | fw_classes()
    known_prefixes = {k.rsplit('.', 1)[0] for k in known}
    internal = internal_classes()
    internal_pk = {c.rsplit('.', 1)[0] for c in internal}
    imp = re.compile(r'^import\s+(static\s+)?([\w.]+?)(\.\*)?;', re.M)
    banned_c = [(re.compile(p), h) for p, h in BANNED]
    tot = collections.Counter()
    for f in sorted(java_files(paths)):
        rel = os.path.relpath(f, ROOT)
        src = open(f, encoding='utf8', errors='ignore').read()
        issues = []
        lines = src.split('\n')
        in_block = False
        for i, line in enumerate(lines, 1):
            # integration pass: skip block comments (fully commented-out files) and the comment part of // lines
            if in_block:
                if '*/' in line: in_block = False
                continue
            if line.lstrip().startswith('/*') and '*/' not in line:
                in_block = True
                continue
            if line.lstrip().startswith(('//', '*', '/*')):
                continue
            if '//' in line and '"' not in line:
                line = line.split('//', 1)[0]
            for rx, hint in banned_c:
                if rx.search(line):
                    issues.append(('BANNED', i, line.strip()[:100] + '   <- ' + hint)); tot['BANNED'] += 1
        for m in imp.finditer(src):
            static, name, star = m.group(1), m.group(2), m.group(3)
            ln = src[:m.start()].count('\n') + 1
            if name.startswith('ttv.migami.jeg'):
                parts = name.split('.')
                ok = False
                if star and not static:
                    ok = name in internal_pk or name in internal
                else:
                    for k in range(len(parts), 0, -1):
                        if '.'.join(parts[:k]) in internal: ok = True; break
                if not ok:
                    issues.append(('INTERNAL', ln, name)); tot['INTERNAL'] += 1
            elif name.startswith(('java.', 'javax.', 'jdk.')):
                continue
            else:
                n = name
                seen = n in known or any(n == k or n.startswith(k + '.') or k.startswith(n + '.') for k in ()) 
                if not seen and star: seen = n in known_prefixes
                if not seen:
                    # nested-class / static member of a known class
                    parts = n.split('.')
                    for k in range(len(parts) - 1, 0, -1):
                        if '.'.join(parts[:k]) in known: seen = True; break
                if not seen:
                    issues.append(('UNSEEN', ln, name)); tot['UNSEEN'] += 1
        if issues:
            print(f'== {rel}')
            for kind, ln, msg in issues: print(f'  {kind:8} {ln:5}  {msg}')
    print('\nSUMMARY', dict(tot))
    return 0

if __name__ == '__main__':
    sys.exit(main())
