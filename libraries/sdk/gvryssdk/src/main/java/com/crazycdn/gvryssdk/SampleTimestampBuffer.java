package com.crazycdn.gvryssdk;

import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

/**
 * Created by yunshang on 18-1-20.
 */

public class SampleTimestampBuffer {
    private final NavigableMap<Long, Long> timeMap = new TreeMap();
    private long presentationTimeOffsetUs = 0L;

    public SampleTimestampBuffer() {
    }

    public synchronized void setPresentationTimeOffsetUs(long var1) {
        this.presentationTimeOffsetUs = var1;
    }

    public synchronized void addPresentationTimeUsForReleaseTimeUs(long var1, long var3) {
        this.timeMap.put(Long.valueOf(var3), Long.valueOf(this.presentationTimeOffsetUs + var1));
    }

    public synchronized long getSampleTimestampUsForReleaseTimeUs(long var1) {
        Map.Entry var3 = this.timeMap.floorEntry(Long.valueOf(var1));
        Map.Entry var4 = this.timeMap.ceilingEntry(Long.valueOf(var1));
        if(var3 == null && var4 == null) {
            return this.presentationTimeOffsetUs;
        } else if(var3 == null) {
            return ((Long)var4.getValue()).longValue();
        } else if(var4 == null) {
            return ((Long)var3.getValue()).longValue();
        } else {
            Long var5 = Long.valueOf(var1 - ((Long)var3.getKey()).longValue());
            Long var6 = Long.valueOf(((Long)var4.getKey()).longValue() - var1);
            Map.Entry var7 = var5.longValue() < var6.longValue()?var3:var4;
            this.timeMap.headMap((Long)var7.getKey()).clear();
            return ((Long)var7.getValue()).longValue();
        }
    }
}
