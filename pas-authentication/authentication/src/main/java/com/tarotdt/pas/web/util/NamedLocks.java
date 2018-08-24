//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.tarotdt.pas.web.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class NamedLocks {
    private static Map<String, NamedLocks.MonitorableReentrantLock> locks = new HashMap();
    private static Map<String, NamedLocks.MonitorableReentrantRWLock> rwLocks = new HashMap();

    public NamedLocks() {
    }

    public static synchronized NamedLocks.MonitorableReentrantLock getLock(String name) {
        NamedLocks.MonitorableReentrantLock r = (NamedLocks.MonitorableReentrantLock)locks.get(name);
        if(r == null) {
            r = new NamedLocks.MonitorableReentrantLock();
            locks.put(name, r);
        }

        return r;
    }

    public static synchronized NamedLocks.MonitorableReentrantRWLock getRWLock(String name) {
        NamedLocks.MonitorableReentrantRWLock r = (NamedLocks.MonitorableReentrantRWLock)rwLocks.get(name);
        if(r == null) {
            r = new NamedLocks.MonitorableReentrantRWLock();
            rwLocks.put(name, r);
        }

        return r;
    }

    public static Map<String, NamedLocks.MonitorableReentrantLock> getActiveLocks() {
        return Collections.unmodifiableMap(locks);
    }

    public static Map<String, NamedLocks.MonitorableReentrantRWLock> getActiveRWLocks() {
        return Collections.unmodifiableMap(rwLocks);
    }

    public static class MonitorableReentrantRWLock extends ReentrantReadWriteLock {
        private static final long serialVersionUID = 1L;

        public MonitorableReentrantRWLock() {
        }

        public Thread getOwner() {
            return super.getOwner();
        }
    }

    public static class MonitorableReentrantLock extends ReentrantLock {
        private static final long serialVersionUID = 1L;

        public MonitorableReentrantLock() {
        }

        public Thread getOwner() {
            return super.getOwner();
        }
    }
}
