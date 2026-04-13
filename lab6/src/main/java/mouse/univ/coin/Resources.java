package mouse.univ.coin;

import java.util.HashMap;

public class Resources {
    private final HashMap<String, HashMap<String, Integer>> resourceMap;

    public Resources(HashMap<String, HashMap<String, Integer>> resourceMap) {
        this.resourceMap = resourceMap;
    }

    public void add(String user, String coin, int money) {
        if (!resourceMap.containsKey(user)) {
            resourceMap.put(user, new HashMap<>());
        }
        HashMap<String, Integer> map = resourceMap.get(user);
        if (!map.containsKey(coin)) {
            map.put(coin, 0);
        }
        map.compute(coin, (k, prev) -> prev + money);
    }
}
