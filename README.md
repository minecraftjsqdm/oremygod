# 挖嘞个雷 / End Meteorite & Change Jade

NeoForge 1.21.1 · MIT · mod id `oremygod` · v1.1.0

## English

Adds End Meteorite ore to the outer End islands and two gear lines above netherite — End Meteorite Iron and Change Jade — plus the Final Crystal material family, with a full fluid / melting set.

Highlights:

- **Ore & materials** — End Meteorite (debris → scrap → ingot → nugget → block) and Change Jade (ore in Ad Astra's `lunar_wastelands`, plus block and nugget).
- **Gear tiers** — a full tool/armor set in each of End Meteorite Iron and Change Jade. Smithing templates step gear up: Netherite → End Meteorite Iron → Change Jade.
- **Final Crystal family** — budding Final Crystal, crystal block and cluster, raw / ball / dust / clump / shard, Forged Crystal, and a Forging Additive Ingot.
- **Fluids** — Molten End Meteorite Iron, Molten Change Jade, Molten Final Crystal, Impure Molten Final Crystal, Molten Forging Additive and Molten Forged Crystal, with buckets and JEI melting/casting info.
- **Silent Gear** — Change Jade and Forged Crystal work as Silent Gear materials (cast on a Casting Table/Basin, gradable). Pouring 1000 mB of Molten Forged Crystal onto gear upgrades every Change Jade part to a Forged Crystal part while keeping enchantments and custom names.
- **Optional Ad Astra** — Change Jade spawns on the Moon.

## 中文

在末地外岛加入末地陨铁矿石，并在下界合金之上加入两条装备线——末地陨铁与嫦娥玉，另有末世水晶材料族，配套完整的流体 / 熔炼体系。

要点：

- 矿石与材料：末地陨铁（残骸 → 碎屑 → 锭 → 粒 → 块）与嫦娥玉（矿位于 Ad Astra 的 `lunar_wastelands`，另有块与粒）。
- 装备线：末地陨铁、嫦娥玉各一整套工具 / 盔甲。锻造模板可逐级升级：下界合金 → 末地陨铁 → 嫦娥玉。
- 末世水晶族：晶芽母岩、晶块、晶簇、粗晶 / 晶球 / 粉 / 团 / 碎片 / 锻晶，以及锻晶添加剂锭。
- 流体：熔融末地陨铁、熔融嫦娥玉、熔融末世水晶、不纯熔融末世水晶、熔融锻晶添加剂、熔融锻晶，含桶与 JEI 熔炼 / 铸造说明。
- Silent Gear：嫦娥玉与锻晶可作 Silent Gear 材料（铸造台 / 盆浇筑，可评级）。将 1000 mB 熔融锻晶浇在装备上，会把其中所有嫦娥玉部件升级为锻晶部件，并保留附魔与自定义名称。
- 可选 Ad Astra 联动：月球生成嫦娥玉。

## Dependencies / 依赖

- Required / 必需：NeoForge 21.1.x on Minecraft 1.21.1
- Optional / 可选：`irons_spellbooks`、`ars_nouveau`、`ad_astra`、`productivemetalworks >= 1.15.1`、`mekanism >= 10.7.19`

## Build / 构建

```sh
# Windows
$env:JAVA_HOME='<path-to-jdk21>'; .\gradlew.bat build --console=plain -q

# Linux / macOS
JAVA_HOME=<path-to-jdk21> ./gradlew build
```

Output jar: `build/libs/oremygod-1.1.0.jar`. 产物：`build/libs/oremygod-1.1.0.jar`。

## License / 许可

MIT. See `LICENSE`.
