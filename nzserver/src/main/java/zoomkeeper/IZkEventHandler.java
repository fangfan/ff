package zoomkeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;

/**
 * Created by F.Fang on 2015/5/27.
 * Version :2015/5/27
 */
public interface IZkEventHandler {
    void handle(CuratorFramework curator, PathChildrenCacheEvent event);
}
