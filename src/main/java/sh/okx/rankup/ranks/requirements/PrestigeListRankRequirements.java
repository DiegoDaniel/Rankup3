package sh.okx.rankup.ranks.requirements;

import org.bukkit.entity.Player;
import sh.okx.rankup.RankupPlugin;
import sh.okx.rankup.prestige.Prestige;
import sh.okx.rankup.prestige.Prestiges;
import sh.okx.rankup.requirements.Requirement;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class PrestigeListRankRequirements implements RankRequirements {
  private final RankupPlugin plugin;
  private final RankRequirements defaultRequirements;
  private final Map<String, RankRequirements> requirements;

  public PrestigeListRankRequirements(RankupPlugin plugin, RankRequirements defaultRequirements, Map<String, RankRequirements> requirements) {
    Objects.requireNonNull(plugin);
    Objects.requireNonNull(defaultRequirements);

    this.plugin = plugin;
    this.defaultRequirements = defaultRequirements;
    this.requirements = requirements;
  }

  @Override
  public Set<Requirement> getRequirements(Player player) {
    return getRankRequirements(player).getRequirements(player);
  }

  @Override
  public boolean hasRequirements(Player player) {
    return getRankRequirements(player).hasRequirements(player);
  }

  @Override
  public Requirement getRequirement(Player player, String name) {
    return getRankRequirements(player).getRequirement(player, name);
  }

  @Override
  public void applyRequirements(Player player) {
    getRankRequirements(player).applyRequirements(player);
  }

  private RankRequirements getRankRequirements(Player player) {
    Prestiges prestiges = plugin.getPrestiges();
    if (player == null || prestiges == null) {
      return defaultRequirements;
    }

    for (Prestige prestige : prestiges.getTree()) {
      String next = prestige.getNext();
      if(plugin.getPermissions().inGroup(player.getUniqueId(), next)) {
        RankRequirements rankRequirements = this.requirements.get(next.toLowerCase());
        if (rankRequirements != null) {
          return rankRequirements;
        }
      }
    }
    return defaultRequirements;
  }
}
