package core.types.pools;

import java.io.Serializable;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public interface LeafPool {
    ConcurrentHashMap<String, TreeNode> leaves = new ConcurrentHashMap<String, TreeNode>();
}
