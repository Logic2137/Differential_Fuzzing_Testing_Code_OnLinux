

package custom;

import java.time.ZoneId;
import java.time.zone.ZoneRules;
import java.time.zone.ZoneRulesProvider;
import java.util.Set;
import java.util.NavigableMap;
import java.util.TreeMap;

public class CustomZoneRulesProvider extends ZoneRulesProvider {
    @Override
    protected Set<String> provideZoneIds() {
        return Set.of("Custom/Timezone");
    }

    @Override
    protected ZoneRules provideRules(String zoneId, boolean forCaching) {
        return ZoneId.of("UTC").getRules();
    }

    @Override
    protected NavigableMap<String, ZoneRules> provideVersions(String zoneId) {
        var map = new TreeMap<String, ZoneRules>();
        map.put("bogusVersion", getRules(zoneId, false));
        return map;
    }
}
